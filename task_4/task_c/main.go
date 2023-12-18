package main

import (
	"fmt"
	"sync"
)

// Route struct
type Route struct {
	From  string
	To    string
	Price int
}

// Graph struct
type BusGraph struct {
	routes    []Route
	cities    map[string]bool
	graphLock sync.RWMutex
}

func NewBusGraph() *BusGraph {
	return &BusGraph{
		routes: make([]Route, 0),
		cities: make(map[string]bool),
	}
}

func (bg *BusGraph) AddRoute(route Route) {
	bg.graphLock.Lock()
	defer bg.graphLock.Unlock()
	bg.routes = append(bg.routes, route)
	bg.cities[route.From] = true
	bg.cities[route.To] = true
}

func (bg *BusGraph) RemoveRoute(route Route) {
	bg.graphLock.Lock()
	defer bg.graphLock.Unlock()
	for i, r := range bg.routes {
		if r == route {
			bg.routes = append(bg.routes[:i], bg.routes[i+1:]...)
			return
		}
	}
}

func (bg *BusGraph) AddCity(city string) {
	bg.graphLock.Lock()
	defer bg.graphLock.Unlock()
	bg.cities[city] = true
}

func (bg *BusGraph) RemoveCity(city string) {
	bg.graphLock.Lock()
	defer bg.graphLock.Unlock()
	delete(bg.cities, city)
}

func (bg *BusGraph) FindRoutePrice(from, to string) (int, bool) {
	bg.graphLock.RLock()
	defer bg.graphLock.RUnlock()

	if from == to {
		return 0, true
	}

	visited := make(map[string]bool)
	queue := []Route{{From: from, Price: 0}}
	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]
		visited[current.From] = true

		for _, route := range bg.routes {
			if route.From == current.From && !visited[route.To] {
				newRoute := Route{
					From: route.To,
					To: current.To,
					Price: current.Price + route.Price,
				}
				queue = append(queue, newRoute)

				if route.To == to {
					return newRoute.Price, true
				}
			}
		}
	}

	return 0, false
}

func main() {
	graph := NewBusGraph()

	graph.AddRoute(Route{From: "A", To: "B", Price: 10})
	graph.AddRoute(Route{From: "B", To: "A", Price: 10})
	graph.AddRoute(Route{From: "B", To: "C", Price: 15})
	graph.AddCity("D")

	routeToUpdate := Route{From: "A", To: "B", Price: 12}
	graph.RemoveRoute(Route{From: "A", To: "B", Price: 10})
	graph.AddRoute(routeToUpdate)

	price, found := graph.FindRoutePrice("A", "D")
	if found {
		fmt.Printf("Race price from A to C: %d\n", price)
	} else {
		fmt.Println("Route was not found")
	}
}
