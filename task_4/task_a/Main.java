public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        database.loadFromDatabase();

        // Writer threads
        Thread writerThread1 = new Thread(() -> {
            database.addRecord("John Doe", "123-456-7890");
        });

        // Reader threads
        Thread readerThread1 = new Thread(() -> {
            String phone = database.findPhoneByName("John Doe");
            System.out.println("Phone for John Doe: " + phone);
        });

        Thread readerThread2 = new Thread(() -> {
            String name = database.findNameByPhone("123-456-7890");
            System.out.println("Name for 123-456-7890: " + name);
        });

        writerThread1.start();
        readerThread1.start();
        readerThread2.start();

        try {
            writerThread1.join();
            readerThread1.join();
            readerThread2.join();
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
