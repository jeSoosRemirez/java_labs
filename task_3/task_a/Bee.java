class Bee implements Runnable {
    private int beeId;
    private Forest forest;

    public Bee(int beeId, Forest forest) {
        this.beeId = beeId;
        this.forest = forest;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Collecting honey takes time
                forest.bee(beeId);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
