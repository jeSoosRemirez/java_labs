import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        String[] strings = {"AABC", "ABBC", "CDCA", "BBBA"};
        AtomicInteger[] aCount = new AtomicInteger[strings.length];
        AtomicInteger[] bCount = new AtomicInteger[strings.length];

        for (int i = 0; i < strings.length; i++) {
            aCount[i] = new AtomicInteger(countLetter(strings[i], 'A'));
            bCount[i] = new AtomicInteger(countLetter(strings[i], 'B'));
        }

        while (!areCountsEqual(aCount) || !areCountsEqual(bCount)) {
            for (int i = 0; i < strings.length; i++) {
                for (int j = 0; j < strings.length; j++) {
                    if (i != j) {
                        balanceCounts(aCount, i, j);
                        balanceCounts(bCount, i, j);
                    }
                }
            }
        }

        for (String str : strings) {
            System.out.println(str);
        }
    }

    private static int countLetter(String str, char letter) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == letter) {
                count++;
            }
        }
        return count;
    }

    private static void balanceCounts(AtomicInteger[] counts, int index1, int index2) {
        int diff = counts[index1].get() - counts[index2].get();
        if (diff > 0) {
            // Too many of the letter in index1, replace it in index2
            if (counts[index1].compareAndSet(counts[index1].get(), counts[index1].get() - 1)) {
                if (counts[index2].compareAndSet(counts[index2].get(), counts[index2].get() + 1)) {
                    swapLetters(index1, index2);
                }
            }
        } else if (diff < 0) {
            // Too few of the letter in index1, replace it in index2
            if (counts[index1].compareAndSet(counts[index1].get(), counts[index1].get() + 1)) {
                if (counts[index2].compareAndSet(counts[index2].get(), counts[index2].get() - 1)) {
                    swapLetters(index1, index2);
                }
            }
        }
    }

    private static void swapLetters(int index1, int index2) {
        // Swap letters A and C, B and D in the strings
        System.out.println("Swapping letters in strings " + index1 + " and " + index2);
    }

    private static boolean areCountsEqual(AtomicInteger[] counts) {
        for (int i = 1; i < counts.length; i++) {
            if (counts[i].get() != counts[0].get()) {
                return false;
            }
        }
        return true;
    }
}
