package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"time"
)

// TimeResponse returns current time
type TimeResponse struct {
	TimeValue string `json:"time"`
	HostName  string `json:"host"`
}

func handler(w http.ResponseWriter, r *http.Request) {
	hostName, _ := os.Hostname()
	currTime := time.Now().Format(time.RFC3339)
	content, _ := json.Marshal(&TimeResponse{
		TimeValue: currTime,
		HostName:  hostName,
	})
	w.Header().Add("Content-Type", "application/json")
	fmt.Fprintf(w, "%s\n", content)
}

func main() {
	log.Println("Starting")
	http.HandleFunc("/", handler)
	http.ListenAndServe(":80", nil)
}
