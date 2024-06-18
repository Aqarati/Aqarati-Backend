package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/gorilla/websocket"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	eureka "github.com/xuanbo/eureka-client"
)

var db *sql.DB

func main() {
	fmt.Println("Starting server...")
	err := godotenv.Load()
	if err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	// Initialize the database connection
	db, err = connectToPostgres()
	if err != nil {
		log.Fatalf("Error connecting to the database: %v", err)
	}
	defer db.Close()

	registerWithEureka()

	http.HandleFunc("/v1/channels", createChannel)
	http.HandleFunc("/v1/messages", sendMessage)
	http.HandleFunc("/ws", wsEndpoint)

	fmt.Println("Server started at :8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

func registerWithEureka() {
	client := eureka.NewClient(&eureka.Config{
		DefaultZone:           "http://localhost:8761/eureka/",
		App:                   "chat-service",
		Port:                  8080,
		RenewalIntervalInSecs: 30,
		DurationInSecs:        90,
	})

	client.Start()
	http.HandleFunc("/v1/services", func(writer http.ResponseWriter, request *http.Request) {
		// full applications from eureka server
		apps := client.Applications

		b, _ := json.Marshal(apps)
		_, _ = writer.Write(b)
	})

	// Start the HTTP server
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
		Name   string `json:"name"`
		UserID int    `json:"user_id"`
	}

	var body RequestBody
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Insert channel into database
	var channelID int
	err := db.QueryRow("INSERT INTO channels (name) VALUES ($1) RETURNING id", body.Name).Scan(&channelID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Add user to the channel
	_, err = db.Exec("INSERT INTO channel_members (user_id, channel_id) VALUES ($1, $2)", body.UserID, channelID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
}

func sendMessage(w http.ResponseWriter, r *http.Request) {
	type RequestBody struct {
		ChannelID int    `json:"channel_id"`
		UserID    int    `json:"user_id"`
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
	upgrader := websocket.Upgrader{
		CheckOrigin: func(r *http.Request) bool {
			return true
		},
	}

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
		var msg Message
		err := conn.ReadJSON(&msg)
		if err != nil {
			log.Println("Error reading json.", err)
			break
		}
		broadcastMessage(msg)
	}
}

type Message struct {
	ChannelID int    `json:"channel_id"`
	UserID    int    `json:"user_id"`
	Content   string `json:"content"`
}

var clients = make(map[*websocket.Conn]bool)
var broadcast = make(chan Message)

func broadcastMessage(msg Message) {
	for client := range clients {
		err := client.WriteJSON(msg)
		if err != nil {
			log.Printf("websocket error: %v", err)
			client.Close()
			delete(clients, client)
		}
	}
}
