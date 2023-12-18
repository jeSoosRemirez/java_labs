public class Main {
    public static void main(String[] args) {
        Forest forest = new Forest(5); // Honey pot capacity

        Thread bearThread = new Thread(new Bear(forest));
        bearThread.start();

        for (int i = 1; i <= 10; i++) {
            Thread beeThread = new Thread(new Bee(i, forest));
            beeThread.start();
        }
    }
}