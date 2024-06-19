package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"strconv"
	"time"

	"github.com/gorilla/mux"
	"github.com/gorilla/websocket"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	eureka "github.com/xuanbo/eureka-client"
)

var db *sql.DB
var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func main() {
	fmt.Println("Starting server...")

	err := godotenv.Load()
	if err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	// Initialize database connection
	db, err = connectToPostgres()
	if err != nil {
		log.Fatalf("Error connecting to the database: %v", err)
	}
	defer db.Close()

	// Register with Eureka
	registerWithEureka()

	// Create router
	r := mux.NewRouter()

	// Define API routes
	r.HandleFunc("/v1/channels/{channel_id}/messages", getMessagesByChannel).Methods("GET")
	r.HandleFunc("/v1/channels", createChannel).Methods("POST")
	r.HandleFunc("/v1/channels/{channel_id}/users", addUserToChannel).Methods("POST")
	r.HandleFunc("/v1/channels/{channel_id}/users/{user_id}", removeUserFromChannel).Methods("DELETE")
	r.HandleFunc("/v1/messages", sendMessage).Methods("POST")
	r.HandleFunc("/ws", wsEndpoint)
	r.HandleFunc("/v1/users/{user_id}/channels", getUserChannels).Methods("GET")

	// Start HTTP server
	port := ":8080"
	fmt.Printf("Server started at %s\n", port)
	log.Fatal(http.ListenAndServe(port, r))
}

func registerWithEureka() {
	client := eureka.NewClient(&eureka.Config{
		DefaultZone:           "http://eureka-server:8761/eureka",
		App:                   "chat-service",
		Port:                  8080,
		RenewalIntervalInSecs: 30,
		DurationInSecs:        90,
	})

	client.Start()

	// Endpoint to retrieve registered services (optional)
	http.HandleFunc("/v1/services", func(writer http.ResponseWriter, request *http.Request) {
		apps := client.Applications
		b, _ := json.Marshal(apps)
		_, _ = writer.Write(b)
	})

	// Start the HTTP server for service registry
	go func() {
		if err := http.ListenAndServe(":10000", nil); err != nil {
			fmt.Println(err)
		}
	}()
}

func connectToPostgres() (*sql.DB, error) {
	host := os.Getenv("DB_HOST")
	port := os.Getenv("DB_PORT")
	user := os.Getenv("DB_USER")
	password := os.Getenv("DB_PASSWORD")
	dbname := os.Getenv("DB_NAME")

	psqlInfo := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)
	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		return nil, err
	}

	err = db.Ping()
	if err != nil {
		return nil, err
	}

	fmt.Println("Successfully connected to the database!")
	return db, nil
}

func createChannel(w http.ResponseWriter, r *http.Request) {
	type RequestBody struct {
		Name    string   `json:"name"`
		UserIDs []string `json:"user_ids"`
	}

	var body RequestBody
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Start a transaction
	tx, err := db.Begin()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer func() {
		if err != nil {
			tx.Rollback()
			return
		}
		err = tx.Commit()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	}()

	// Insert channel into database
	var channelID int
	err = tx.QueryRow("INSERT INTO channels (name) VALUES ($1) RETURNING id", body.Name).Scan(&channelID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Add the users to the channel
	for _, userID := range body.UserIDs {
		// Check if user exists
		var userExists bool
		err = tx.QueryRow("SELECT EXISTS(SELECT 1 FROM users WHERE id=$1)", userID).Scan(&userExists)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		// Create user if they do not exist
		if !userExists {
			if err := createUser(tx, userID); err != nil {
				http.Error(w, err.Error(), http.StatusInternalServerError)
				return
			}
		}

		// Add user to channel
		_, err = tx.Exec("INSERT INTO channel_members (user_id, channel_id) VALUES ($1, $2)", userID, channelID)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
	}

	// Return the channel ID or some identifier
	response := struct {
		ChannelID int `json:"channel_id"`
	}{
		ChannelID: channelID,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(response)
}

func addUserToChannel(w http.ResponseWriter, r *http.Request) {
	type RequestBody struct {
		UserID    string `json:"user_id"`
		ChannelID int    `json:"channel_id"`
	}

	var body RequestBody
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Start a transaction
	tx, err := db.Begin()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer func() {
		if err != nil {
			tx.Rollback()
			return
		}
		err = tx.Commit()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	}()

	// Check if user exists
	var userExists bool
	err = tx.QueryRow("SELECT EXISTS(SELECT 1 FROM users WHERE id=$1)", body.UserID).Scan(&userExists)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Create user if they do not exist
	if !userExists {
		err = createUser(tx, body.UserID)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
	}

	// Now userID should be valid, proceed with adding user to channel_members
	_, err = tx.Exec("INSERT INTO channel_members (user_id, channel_id) VALUES ($1, $2)", body.UserID, body.ChannelID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
}

func removeUserFromChannel(w http.ResponseWriter, r *http.Request) {
	// Parse channel_id and user_id from URL parameters
	vars := mux.Vars(r)
	channelID := vars["channel_id"]
	userID := vars["user_id"]

	// Convert channelID to integer
	channelIDInt, err := strconv.Atoi(channelID)
	if err != nil {
		http.Error(w, "Invalid channel ID", http.StatusBadRequest)
		return
	}

	// Start a transaction
	tx, err := db.Begin()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer func() {
		if err != nil {
			tx.Rollback()
			return
		}
		err = tx.Commit()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	}()

	// Delete the user from channel_members
	_, err = tx.Exec("DELETE FROM channel_members WHERE user_id = $1 AND channel_id = $2", userID, channelIDInt)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent) // StatusNoContent (204) indicates successful deletion
}

func createUser(tx *sql.Tx, userID string) error {
	// You may have additional fields to create the user, adapt as needed
	_, err := tx.Exec("INSERT INTO users (id) VALUES ($1)", userID)
	if err != nil {
		return err
	}
	return nil
}

func sendMessage(w http.ResponseWriter, r *http.Request) {
	type RequestBody struct {
		ChannelID int    `json:"channel_id"`
		UserID    string `json:"user_id"`
		Content   string `json:"content"`
	}

	var body RequestBody
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Insert message into database
	_, err := db.Exec("INSERT INTO messages (channel_id, user_id, content) VALUES ($1, $2, $3)",
		body.ChannelID, body.UserID, body.Content)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
}

func wsEndpoint(w http.ResponseWriter, r *http.Request) {
	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println(err)
		return
	}

	go handleMessages(conn)
}

func handleMessages(conn *websocket.Conn) {
	defer conn.Close()
	for {
		var msg struct {
			ChannelID int    `json:"channel_id"`
			UserID    string `json:"user_id"`
			Content   string `json:"content"`
		}
		err := conn.ReadJSON(&msg)
		if err != nil {
			log.Println("Error reading json.", err)
			break
		}
		broadcastMessage(msg)
	}
}

func broadcastMessage(msg struct {
	ChannelID int    `json:"channel_id"`
	UserID    string `json:"user_id"`
	Content   string `json:"content"`
}) {

}

func getMessagesByChannel(w http.ResponseWriter, r *http.Request) {
	log.Println("getting messages")
	// Extract channel_id from URL parameters
	vars := mux.Vars(r)
	channelID := vars["channel_id"]

	// Convert channelID to integer
	channelIDInt, err := strconv.Atoi(channelID)
	if err != nil {
		http.Error(w, "Invalid channel ID", http.StatusBadRequest)
		return
	}

	// Query messages from the database for the specified channel
	rows, err := db.Query("SELECT id, user_id, content, created_at FROM messages WHERE channel_id = $1", channelIDInt)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	// Prepare a slice to hold the messages
	var messages []struct {
		ID        int       `json:"id"`
		UserID    string    `json:"user_id"`
		Content   string    `json:"content"`
		CreatedAt time.Time `json:"created_at"`
	}

	// Iterate through the query results and populate the messages slice
	for rows.Next() {
		var msg struct {
			ID        int
			UserID    string
			Content   string
			CreatedAt time.Time
		}
		if err := rows.Scan(&msg.ID, &msg.UserID, &msg.Content, &msg.CreatedAt); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		// Append msg to messages slice
		messages = append(messages, struct {
			ID        int       `json:"id"`
			UserID    string    `json:"user_id"`
			Content   string    `json:"content"`
			CreatedAt time.Time `json:"created_at"`
		}{
			ID:        msg.ID,
			UserID:    msg.UserID,
			Content:   msg.Content,
			CreatedAt: msg.CreatedAt,
		})
	}

	// Check for any errors encountered during iteration
	if err := rows.Err(); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Respond with the JSON array of messages
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(messages)
}

func getUserChannels(w http.ResponseWriter, r *http.Request) {
	// Extract user_id from URL parameters
	vars := mux.Vars(r)
	userID := vars["user_id"]

	// Query channels from the database for the specified user
	rows, err := db.Query(`
		SELECT c.id, c.name, c
		FROM channels c
		INNER JOIN channel_members cm ON c.id = cm.channel_id
		WHERE cm.user_id = $1`, userID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	// Prepare a slice to hold the channels
	var channels []struct {
		ID   int    `json:"id"`
		Name string `json:"name"`
	}

	// Iterate through the query results and populate the channels slice
	for rows.Next() {
		var channel struct {
			ID   int
			Name string
		}
		if err := rows.Scan(&channel.ID, &channel.Name); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		// Append channel to channels slice
		channels = append(channels, struct {
			ID   int    `json:"id"`
			Name string `json:"name"`
		}{
			ID:   channel.ID,
			Name: channel.Name,
		})
	}

	// Check for any errors encountered during iteration
	if err := rows.Err(); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Respond with the JSON array of channels
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(channels)
}
