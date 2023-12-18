public class Main {
    public static void main(String[] args) {
        Barbershop barbershop = new Barbershop();
        Barber barber = new Barber(barbershop);
        barber.start();

        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer("Відвідувач " + i, barbershop);
            customer.start();
        }
    }
}