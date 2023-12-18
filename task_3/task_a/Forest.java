import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Forest {
    private int honeyPotCapacity;
    private int honeyPot;
    private Lock lock;
    private Condition bearSleeping;
    private Condition beesCollecting;

    public Forest(int honeyPotCapacity) {
        this.honeyPotCapacity = honeyPotCapacity;
        this.honeyPot = 0;
        this.lock = new ReentrantLock();
        this.bearSleeping = lock.newCondition();
        this.beesCollecting = lock.newCondition();
    }

    public void bear() throws InterruptedException {
        lock.lock();
        try {
            while (honeyPot < honeyPotCapacity) {
                bearSleeping.await();
            }
            System.out.println("Bear is awake and eating honey.");
            honeyPot = 0;
            beesCollecting.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    public void bee(int beeId) throws InterruptedException {
        lock.lock();
        try {
            while (honeyPot == honeyPotCapacity) {
                beesCollecting.await();
            }
            honeyPot++;
            System.out.println("Bee " + beeId + " collected honey. Honey pot: " + honeyPot);

            if (honeyPot == honeyPotCapacity) {
                System.out.println("Bee " + beeId + " woke up the bear!");
                bearSleeping.signal();
            }
        } 
        finally {
            lock.unlock();
        }
    }
}