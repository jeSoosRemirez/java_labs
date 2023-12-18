import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GardenApp {
    private static final int GARDEN_SIZE = 5;
    private static final int MAX_WATER_LEVEL = 10;
    private static int[][] garden = new int[GARDEN_SIZE][GARDEN_SIZE];
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        Thread gardenerThread = new Thread(new Gardener());
        Thread natureThread = new Thread(new Nature());
        Thread monitor1Thread = new Thread(new Monitor1());
        Thread monitor2Thread = new Thread(new Monitor2());

        gardenerThread.start();
        natureThread.start();
        monitor1Thread.start();
        monitor2Thread.start();
    }

    static class Gardener implements Runnable {
        @Override
        public void run() {
            Random random = new Random();

            while (true) {
                int row = random.nextInt(GARDEN_SIZE);
                int col = random.nextInt(GARDEN_SIZE);
                int water = random.nextInt(MAX_WATER_LEVEL);

                rwLock.writeLock().lock();
                try {
                    garden[row][col] += water;
                    System.out.println("Gardener watered the plant at (" + row + "," + col + ") with " + water + " units of water.");
                } 
                finally {
                    rwLock.writeLock().unlock();
                }

                try {
                    Thread.sleep(1000); // Sleep for a while before the next action
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Nature implements Runnable {
        @Override
        public void run() {
            Random random = new Random();

            while (true) {
                int row = random.nextInt(GARDEN_SIZE);
                int col = random.nextInt(GARDEN_SIZE);
                int damage = random.nextInt(5);

                rwLock.writeLock().lock();
                try {
                    garden[row][col] -= damage;
                    System.out.println("Nature damaged the plant at (" + row + "," + col + ") by " + damage + " units.");
                } 
                finally {
                    rwLock.writeLock().unlock();
                }

                try {
                    Thread.sleep(1500); // Sleep for a while before the next action
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Monitor1 implements Runnable {
        @Override
        public void run() {
            while (true) {
                rwLock.readLock().lock();
                try {
                    saveGardenStateToFile();
                }
                finally {
                    rwLock.readLock().unlock();
                }

                try {
                    Thread.sleep(5000); // Sleep for a while before the next monitor1 action
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void saveGardenStateToFile() {
            try (FileWriter fileWriter = new FileWriter("garden_state.txt", true)) {
                for (int i = 0; i < GARDEN_SIZE; i++) {
                    for (int j = 0; j < GARDEN_SIZE; j++) {
                        fileWriter.write(garden[i][j] + " ");
                    }
                    fileWriter.write("\n");
                }
                fileWriter.write("\n");
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Monitor2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                rwLock.readLock().lock();
                try {
                    printGardenState();
                } 
                finally {
                    rwLock.readLock().unlock();
                }

                try {
                    Thread.sleep(3000); // Sleep for a while before the next monitor2 action
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void printGardenState() {
            System.out.println("Garden State:");
            for (int i = 0; i < GARDEN_SIZE; i++) {
                for (int j = 0; j < GARDEN_SIZE; j++) {
                    System.out.print(garden[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
