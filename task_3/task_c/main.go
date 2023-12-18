package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	agentSleepTime = 100 * time.Millisecond
	smokeTime = 50 * time.Millisecond
)

var (
	table = make([]string, 2)
	agentSem = make(chan struct{}, 1)
	smokerSem = make(chan struct{}, 0)
	mutex sync.Mutex
)

func main() {
	rand.Seed(time.Now().UnixNano())

	go agent()

	for i := 0; i < 3; i++ {
		go smoker(i)
	}

	var wg sync.WaitGroup
	wg.Add(4)
	go func() {
		wg.Wait()
		close(smokerSem)
	}()

	wg.Wait()
}

func agent() {
	for {
		ing1, ing2 := prepareIngredients()
		fmt.Printf("Агент поклав на стіл %s та %s.\n", ing1, ing2)

		mutex.Lock()
		table[0] = ing1
		table[1] = ing2
		mutex.Unlock()

		smokerSem <- struct{}{}
		<-smokerSem

		time.Sleep(agentSleepTime)
	}
}

func smoker(id int) {
	for {
		<-smokerSem
		if canSmoke(id) {
			smoke(id)
			time.Sleep(smokeTime)
		}
		smokerSem <- struct{}{}
	}
}

func prepareIngredients() (string, string) {
	ingredients := []string{"тютюн", "папірець", "сірники"}
	ing1, ing2 := "", ""
	for {
		ing1 = ingredients[rand.Intn(3)]
		ing2 = ingredients[rand.Intn(3)]
		if ing1 != ing2 {
			return ing1, ing2
		}
	}
}

func canSmoke(id int) bool {
	ingredients := []string{"тютюн", "папірець", "сірники"}
	for _, ingredient := range ingredients {
		if ingredient != table[0] && ingredient != table[1] {
			return ingredient == ingredients[id]
		}
	}
	return false
}

func smoke(id int) {
	ingredients := []string{"тютюн", "папірець", "сірники"}
	fmt.Printf("Курець %d (із %s) почав курити.\n", id, ingredients[id])
}
