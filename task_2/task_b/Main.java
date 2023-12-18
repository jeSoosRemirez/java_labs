

public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        Thread ivanovThread = new Thread(new Ivanov(warehouse));
        Thread petrovThread = new Thread(new Petrov(warehouse));
        Thread nechiporchukThread = new Thread(new Nechiporchuk(warehouse));

        ivanovThread.start();
        petrovThread.start();
        nechiporchukThread.start();
    }
}