import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Warehouse {
    private BlockingQueue<Integer> storage = new ArrayBlockingQueue<>(10); // Max qnt

    public void put(int item) throws InterruptedException {
        storage.put(item);
    }

    public int get() throws InterruptedException {
        return storage.take();
    }
}