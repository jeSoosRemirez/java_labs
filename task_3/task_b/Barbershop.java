import java.util.LinkedList;
import java.util.Queue;

class Barbershop {
    private int maxChairs = 1; // Chairs qnt
    private Queue<Customer> waitingCustomers = new LinkedList<>();
    private boolean barberSleeping = true;

    public synchronized void addCustomer(Customer customer) {
        if (waitingCustomers.size() < maxChairs) {
            waitingCustomers.add(customer);
            if (barberSleeping) {
                barberSleeping = false;
                customer.wakeBarber();
            }
        } else {
            System.out.println("Відвідувач " + customer.getName() + " заходить і чекає.");
        }
    }

    public synchronized Customer getNextCustomer() {
        if (!waitingCustomers.isEmpty()) {
            return waitingCustomers.poll();
        }
        return null;
    }

    public synchronized void releaseBarber() {
        barberSleeping = true;
    }
}