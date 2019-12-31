package main

import (
	"io/ioutil"
	"log"
	"net/http"
	"os"
)

func main() {

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
}
