import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadSynchronizationSemaphore {

    private static final Lock lock = new ReentrantLock();
    private static final int sliderInitValue = 50;
    private static volatile int semaphore = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Thread Example with Semaphore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, sliderInitValue);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JButton startButton1 = new JButton("Start 1");
        JButton stopButton1 = new JButton("Stop 1");
        JButton startButton2 = new JButton("Start 2");
        JButton stopButton2 = new JButton("Stop 2");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton1);
        buttonPanel.add(stopButton1);
        buttonPanel.add(startButton2);
        buttonPanel.add(stopButton2);

        frame.add(slider, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 150);
        frame.setVisible(true);

        Thread t1 = new TThread(10, slider);
        Thread t2 = new TThread(90, slider);

        startButton1.addActionListener(e -> {
            if (semaphore == 0) {
                semaphore = 1;
                t1.setPriority(Thread.MIN_PRIORITY);
                t1.start();
            } else {
                showMessage("Busy with a thread");
            }
        });

        stopButton1.addActionListener(e -> {
            if (semaphore == 1) {
                semaphore = 0;
                t1.interrupt();
            }
        });

        startButton2.addActionListener(e -> {
            if (semaphore == 0) {
                semaphore = 2;
                t2.setPriority(Thread.MAX_PRIORITY);
                t2.start();
            } else {
                showMessage("Busy with a thread");
            }
        });

        stopButton2.addActionListener(e -> {
            if (semaphore == 2) {
                semaphore = 0;
                t2.interrupt();
            }
        });
    }

    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    static class TThread extends Thread {
        private int targetValue;
        private JSlider slider;

        TThread(int targetValue, JSlider slider) {
            this.targetValue = targetValue;
            this.slider = slider;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    lock.lock();
                    slider.setValue(targetValue);
                    lock.unlock();
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                
            } finally {
                lock.unlock();
                semaphore = 0;
            }
        }
    }
}
