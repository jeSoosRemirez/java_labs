import java.util.concurrent.Semaphore;


public class Main {
    private static final int TOTAL_BEES = 5;
    private static Semaphore semaphore = new Semaphore(1);
    private static boolean winnieFound = false;

    public static void main(String[] args) {
        Thread[] bees = new Thread[TOTAL_BEES];

        for (int i = 0; i < TOTAL_BEES; i++) {
            bees[i] = new Bee("Bee #" + (i + 1));
            bees[i].start();
        }

        for (int i = 0; i < TOTAL_BEES; i++) {
            try {
                bees[i].join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All bees have returned to the hive.");
        if (winnieFound) {
            System.out.println("Winnie the Pooh has been found and punished!");
        } else {
            System.out.println("Winnie the Pooh was not found in the forest.");
        }
    }

    static class Bee extends Thread {
        public Bee(String name) {
            super(name);
        }

        @Override
        public void run() {
            searchForest();
        }

        private void searchForest() {
            try {
                System.out.println(getName() + " is searching the forest.");
                Thread.sleep((int) (Math.random() * 5000));

                // Acquire the semaphore to check the forest
                semaphore.acquire();

                if (!winnieFound && !winnieFoundInForest()) {
                    System.out.println(getName() + " did not find Winnie the Pooh.");
                }

                semaphore.release();
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private boolean winnieFoundInForest() {
            boolean found = Math.random() < 0.2; // 20% chance of finding Winnie
            if (found) {
                winnieFound = true;
                System.out.println(getName() + " found Winnie the Pooh and is punishing him!");
            }
            return found;
        }
    }
}
