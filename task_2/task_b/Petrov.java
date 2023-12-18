class Petrov implements Runnable {
    private Warehouse warehouse;

    public Petrov(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                int item = warehouse.get();
                System.out.println("Петров вантажить товар " + item + " в вантажівку");
                Thread.sleep(100); // Імітує час на вантажування
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}