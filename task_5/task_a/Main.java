public class Main {
    public static void main(String[] args) {
        int numThreads = 5;
        Threads synchronizer = new Threads(numThreads);
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    // Simulate accepting random states 0 or 1
                    int state = (int) (Math.random() * 2);
                    for (int j = 0; j < 3; j++) {
                        synchronizer.acceptState(threadId, state);
                        Thread.sleep(1000);
                        state = 1 - state; // Simulate changing state
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Виведення стану усіх потоків
        System.out.println("Final thread states:");
        for (int i = 0; i < numThreads; i++) {
            System.out.println("Thread " + i + " is in state " + synchronizer.states[i]);
        }
    }
}