import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ThreadSynchronization {
    
    private static volatile boolean isRunning = false;
    private static volatile int t1Priority = Thread.NORM_PRIORITY;
    private static volatile int t2Priority = Thread.NORM_PRIORITY;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Thread Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton increaseT1Priority = new JButton("Increase T1 Priority");
        JButton decreaseT1Priority = new JButton("Decrease T1 Priority");
        JButton increaseT2Priority = new JButton("Increase T2 Priority");
        JButton decreaseT2Priority = new JButton("Decrease T2 Priority");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(increaseT1Priority);
        buttonPanel.add(decreaseT1Priority);
        buttonPanel.add(increaseT2Priority);
        buttonPanel.add(decreaseT2Priority);

        frame.add(slider, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 150);
        frame.setVisible(true);

        Thread t1 = new TThread1(slider);
        Thread t2 = new TThread2(slider);

        startButton.addActionListener(e -> {
            if (!isRunning) {
                isRunning = true;
                t1.setPriority(t1Priority);
                t2.setPriority(t2Priority);
                t1.start();
                t2.start();
            }
        });

        stopButton.addActionListener(e -> {
            isRunning = false;
            t1.interrupt();
            t2.interrupt();
        });

        increaseT1Priority.addActionListener(e -> {
            if (t1Priority < Thread.MAX_PRIORITY) {
                t1Priority++;
                t1.setPriority(t1Priority);
            }
        });

        decreaseT1Priority.addActionListener(e -> {
            if (t1Priority > Thread.MIN_PRIORITY) {
                t1Priority--;
                t1.setPriority(t1Priority);
            }
        });

        increaseT2Priority.addActionListener(e -> {
            if (t2Priority < Thread.MAX_PRIORITY) {
                t2Priority++;
                t2.setPriority(t2Priority);
            }
        });

        decreaseT2Priority.addActionListener(e -> {
            if (t2Priority > Thread.MIN_PRIORITY) {
                t2Priority--;
                t2.setPriority(t2Priority);
            }
        });
    }

    static class TThread1 extends Thread {
        private JSlider slider;

        TThread1(JSlider slider) {
            this.slider = slider;
        }

        @Override
        public void run() {
            while (isRunning) {
                slider.setValue(slider.getValue() + 1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    static class TThread2 extends Thread {
        private JSlider slider;

        TThread2(JSlider slider) {
            this.slider = slider;
        }

        @Override
        public void run() {
            while (isRunning) {
                slider.setValue(slider.getValue() - 1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
