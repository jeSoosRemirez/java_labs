package demo.src.main.java.com.example;

import mpi.MPI;

public class Matrix {

    private static final int M = 1000;
    private static final int N = 1000;
    public static int rank = MPI.COMM_WORLD.Rank();
    public static int size = MPI.COMM_WORLD.Size();

    public static void main(String[] args) {
        MPI.Init(args);

        // Створюємо матриці
        double[][] A = new double[M][N];
        double[][] B = new double[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = Math.random();
                B[i][j] = Math.random();
            }
        }

        // Створюємо результатну матрицю
        double[][] C = new double[M][N];

        // Виконуємо паралельне множення матриць
        switch (rank) {
            case 0:
                sequentialMultiplication(A, B, C);
                break;
            case 1:
                firstStripMultiplication(A, B, C);
                break;
            case 2:
                foxMultiplication(A, B, C);
                break;
            case 3:
                cannonMultiplication(A, B, C);
                break;
        }

        // Виводимо результат
        if (rank == 0) {
            System.out.println("C:");
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(C[i][j] + " ");
                }
                System.out.println();
            }
        }

        MPI.Finalize();
    }

    // Послідовний алгоритм множення матриць
    private static void sequentialMultiplication(double[][] A, double[][] B, double[][] C) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }

    // Стрічкова схема
    private static void firstStripMultiplication(double[][] A, double[][] B, double[][] C) {
        int n = N / size;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < N; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }

    // Метод Фокса
    private static void foxMultiplication(double[][] A, double[][] B, double[][] C) {
        int n = N / size;

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = 0;
                for (int k = 0; k < N; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        for (int k = 1; k < size; k++) {
            for (int i = 0; i < M; i++) {
                for (int j = k * n; j < (k + 1) * n; j++) {
                    C[i][j] = C[i][j] + A[i][k * n] * B[k * n][j];
                }
            }
        }
    }

    // Метод Кеннона
private static void cannonMultiplication(double[][] A, double[][] B, double[][] C) {
    int n = N / size;

    // Розподіл стовпців матриці B між процесорами
    double[][] B_per_proc = new double[M][n];
    for (int i = 0; i < M; i++) {
        for (int k = 0; k < size; k++) {
            B_per_proc[i][k * n] = B[i][k * n];
        }
    }

    // Виконуємо множення матриць
    for (int k = 0; k < size; k++) {
        // Повертаємо фазу k
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = C[i][j] * B_per_proc[i][k * n];
            }
        }

        // Виконуємо зрушення фази
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < n; j++) {
                C[i * n + j][j] = B_per_proc[i * n + j][j];
            }
        }
    }
}
}