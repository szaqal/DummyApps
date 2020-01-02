package main

import (
	"log"

	"github.com/google/tcpproxy"
)

func main() {

	var p tcpproxy.Proxy

	p.AddRoute(":80", tcpproxy.To("wikipedia.pl:80"))

	log.Fatal(p.Run())
}
