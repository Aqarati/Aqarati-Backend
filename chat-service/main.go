package main

import (
	"encoding/json"
	"fmt"
	"net/http"

	eureka "github.com/xuanbo/eureka-client"
)

func main() {

	fmt.Println("tsest")
	registerWithEureka()
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

	// start http server
	if err := http.ListenAndServe(":10000", nil); err != nil {
		fmt.Println(err)
	}
}
