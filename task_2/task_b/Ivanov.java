class Ivanov implements Runnable {
    private Warehouse warehouse;

    public Ivanov(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Іванов вивозить товар " + i);
                warehouse.put(i);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
