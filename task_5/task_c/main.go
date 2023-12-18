package main

import (
	"fmt"
	"sync"
)

func main() {
	// Ініціалізуємо масиви і синхронізуючі об'єкти
	array1 := []int{1, 2, 3}
	array2 := []int{4, 5, 6}
	array3 := []int{7, 8, 9}
	targetSum := (sumArray(array1) + sumArray(array2) + sumArray(array3)) / 3
	var wg sync.WaitGroup

	// Створюємо і запускаємо три потоки
	for i, arr := range []*[]int{&array1, &array2, &array3} {
		wg.Add(1)
		go func(index int, array *[]int) {
			defer wg.Done()
			for {
				currentSum := sumArray(*array)
				if currentSum == targetSum {
					break
				}

				if currentSum < targetSum {
					(*array)[index]++
				} else {
					(*array)[index]--
				}
			}
		}(i, arr)
	}

	wg.Wait()

	// Виводимо результати
	fmt.Println("Масив 1:", array1)
	fmt.Println("Масив 2:", array2)
	fmt.Println("Масив 3:", array3)
}

func sumArray(array []int) int {
	sum := 0
	for _, v := range array {
		sum += v
	}
	return sum
}
