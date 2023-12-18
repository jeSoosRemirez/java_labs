import java.util.concurrent.locks.*;

public class Threads {
    public int[] states;
    public int n;
    public Lock lock;
    public Condition condition;

    public Threads(int n) {
        this.n = n;
        states = new int[n];
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void acceptState(int threadId, int state) throws InterruptedException {
        lock.lock();
        try {
            int prevThreadId = (threadId - 1 + n) % n;
            int nextThreadId = (threadId + 1) % n;
            
            System.out.println("Thread " + threadId + " is in state " + state);

            states[threadId] = state;

            if (states[threadId] != states[nextThreadId] && states[threadId] != states[prevThreadId]) {
                // Swap states
                states[threadId] = 1 - states[threadId];
                condition.signalAll();
                System.out.println("Thread " + threadId + " changed state due to neighbor state change.");
            } else {
                // Wait for other threads to reach the same state
                while (states[threadId] != states[nextThreadId] || states[threadId] != states[prevThreadId]) {
                    System.out.println("Thread " + threadId + " is waiting for neighbors to reach the same state.");
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}