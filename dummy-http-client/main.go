package main

import (
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"time"
)

func main() {

	for {
		serverAddress := os.Getenv("SERVER_ADDRESS")
		if serverAddress == "" {
			log.Fatalf("Missing SERVER_ADDRESS")
		}
		log.Println("Calling => ", serverAddress)
		resp, err := http.Get(serverAddress)

		if err != nil {
			log.Fatalf(err.Error())
		}
		body, err := ioutil.ReadAll(resp.Body)
		defer resp.Body.Close()
		log.Printf(" HTTP: %d | %s", resp.StatusCode, body)
		<-time.After(1 * time.Second)
	}

}
