package com.example;

import mpi.MPI;

public class ParallelMatrixMultiplication {

    public static void main(String[] args) {
        // Ініціалізація MPI
        MPI.Init(args);

        // Отримання розміру матриць
        int n = Integer.parseInt(args[0]);

        // Отримання алгоритму множення
        int algorithm = Integer.parseInt(args[1]);

        // Отримання кількості потоків
        int p = Integer.parseInt(args[2]);

        // Створення матриць
        double[][] A = new double[n][n];
        double[][] B = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = Math.random();
                B[i][j] = Math.random();
            }
        }

        // Розподіл матриць між процесами
        MPI.COMM_WORLD.Bcast(A, 0, n * n, MPI.DOUBLE);
        MPI.COMM_WORLD.Bcast(B, 0, n * n, MPI.DOUBLE);

        // Виконання множення матриць
        long startTime = System.currentTimeMillis();
        switch (algorithm) {
            case 1:
                // Стрічкова схема
                sequentialMultiply(A, B);
                break;
            case 2:
                // Метод Фокса
                foxMultiply(A, B, p);
                break;
            case 3:
                // Метод Кеннона
                cannonMultiply(A, B, p);
                break;
        }
        long endTime = System.currentTimeMillis();

        // Виведення результатів
        double time = (endTime - startTime) / 1000.0;
        int operations = n * n * n;
        System.out.println("Розмір матриць: " + n);
        System.out.println("Алгоритм: " + algorithm);
        System.out.println("Кількість потоків: " + p);
        System.out.println("Час виконання: " + time + " секунд");
        System.out.println("Кількість операцій: " + operations);

        // Завершення MPI
        MPI.Finalize();
    }

    private static void sequentialMultiply(double[][] A, double[][] B) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    A[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }

    private static void foxMultiply(double[][] A, double[][] B, int p) {
        int chunkSize = A.length / p;
        for (int i = 0; i < p; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize;
            for (int j = 0; j < B[0].length; j++) {
                for (int k = start; k < end; k++) {
                    A[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }

    private static void cannonMultiply(double[][] A, double[][] B, int p) {
        int chunkSize = A.length / p;
        int halfSize = chunkSize / 2;

        // Розподіл матриць між процесами
        for (int i = 0; i < p; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize;
            MPI.COMM_WORLD.Bcast(A[start:end], i, chunkSize, MPI.DOUBLE);
            MPI.COMM_WORLD.Bcast(B[start:end], i, chunkSize, MPI.DOUBLE);
        }

        // Розподіл роботи між процесами
        for (int stage = 0; stage < n; stage += 2 * halfSize) {
            for (int i = 0; i < p; i++) {
                int start = i * chunkSize;
                int end = (i + 1) * chunkSize;

                // Обчислення першої половини результатів
                for (int j = 0; j < halfSize; j++) {
                    for (int k = j; k < end; k++) {
                        A[start + j][stage + k] += A[start + j][stage + i] * B[i][k];
                    }
                }

                // Обчислення другої половини результатів
                for (int j = halfSize; j < end; j++) {
                    for (int k = j; k < end; k++) {
                        A[start + j][stage + k] += A[start + i][stage + k] * B[i][j];
                    }
                }
            }
        }
    }

