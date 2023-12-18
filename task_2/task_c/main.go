package main

import (
	"fmt"
	"sync"
)

func main() {
	energyYin := []int{10, 20, 10, 12, 20}
	// energyYin := []int{2, 20, 8, 1, 1}
	energyYang := []int{7, 15, 9, 4, 11}

	numMonks := len(energyYin)
	if numMonks != len(energyYang) {
		fmt.Println("Кількість монахів в різних монастирях не однакова")
		return
	}

	// Wins counter
	winsYin := 0
	winsYang := 0

	resultChannel := make(chan string, numMonks)

	var wg sync.WaitGroup
	for i := 0; i < numMonks; i++ {
		wg.Add(1)
		go func(index int) {
			defer wg.Done()

			if energyYin[index] > energyYang[index] {
				resultChannel <- "Інь"
			} else if energyYang[index] > energyYin[index] {
				resultChannel <- "Янь"
			} else {
				resultChannel <- "Нічия"
			}
		}(i)
	}

	go func() {
		wg.Wait()
		close(resultChannel)
	}()

	// Count wins
	for result := range resultChannel {
		if result == "Інь" {
			winsYin++
		} else if result == "Янь" {
			winsYang++
		}
	}

	if winsYin > winsYang {
		fmt.Println("Монастир Інь переміг у змаганнях")
	} else if winsYang > winsYin {
		fmt.Println("Монастир Янь переміг у змаганнях")
	} else {
		fmt.Println("Змагання завершилися в нічию, обидва монастирі рівні за силою Ци")
	}
}
