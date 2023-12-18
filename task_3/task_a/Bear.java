class Bear implements Runnable {
    private Forest forest;

    public Bear(Forest forest) {
        this.forest = forest;
    }

    @Override
    public void run() {
        while (true) {
            try {
                forest.bear();
                Thread.sleep(2000); // Bear is eating honey
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}