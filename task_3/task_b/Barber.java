class Barber extends Thread {
    private Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    public void run() {
        while (true) {
            if (barbershop.getNextCustomer() != null) {
                System.out.println("Перукар починає стрижку.");
                try {
                    sleep(2000); // Час стрижки
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Перукар закінчив стрижку.");
                barbershop.releaseBarber();
            } else {
                System.out.println("Перукар спить в кріслі.");
                try {
                    sleep(5000); // Час спання
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}