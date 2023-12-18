class Customer extends Thread {
    private String name;
    private Barbershop barbershop;

    public Customer(String name, Barbershop barbershop) {
        this.name = name;
        this.barbershop = barbershop;
    }

    public void run() {
        System.out.println("Відвідувач " + name + " заходить в перукарню.");
        barbershop.addCustomer(this);
        while (!isInterrupted()) {
            try {
                sleep(2000); // Час стрижки
                System.out.println("Відвідувач " + name + " закінчив стрижку і виходить з перукарні.");
                break;
            } catch (InterruptedException e) {
                // Якщо відвідувач буде прерваний (наприклад, чергу видалять), він вийде.
                break;
            }
        }
    }

    public void wakeBarber() {
        System.out.println("Відвідувач " + name + " будить перукаря.");
        interrupt();
    }
}
