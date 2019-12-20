package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

// TimeResponse returns current time
type TimeResponse struct {
	TimeValue string `json:"time"`
}

func handler(w http.ResponseWriter, r *http.Request) {
	currTime := time.Now().Format(time.RFC3339)
	content, _ := json.Marshal(&TimeResponse{
		TimeValue: currTime,
	})
	w.Header().Add("Content-Type", "application/json")
	fmt.Fprintf(w, "%s", content)
}

func main() {
	log.Println("Starting")
	http.HandleFunc("/", handler)
	http.ListenAndServe(":80", nil)
}
