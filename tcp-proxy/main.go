package main

import (
	"log"

	"github.com/google/tcpproxy"
)

func main() {

	var p tcpproxy.Proxy

	p.AddRoute(":80", tcpproxy.To("1wikipedia.pl:80"))

	log.Fatal(p.Run())
}
