class Nechiporchuk implements Runnable {
    private Warehouse warehouse;

    public Nechiporchuk(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            int totalValue = 0;
            for (int i = 1; i <= 10; i++) {
                int item = warehouse.get();
                totalValue += item;
                System.out.println("Нечипорчук підраховує вартість: " + totalValue);
                Thread.sleep(100); // Імітує час на підрахунок
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}