package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;


public class ConcurrentThreadsExample {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static JFrame jframe = new JFrame("TEST");
    private static Webcam webcam = Webcam.getDefault();

    private static final class Capture extends Thread {

        private static final AtomicInteger number = new AtomicInteger(0);

        public Capture() {
            super("capture-" + number.incrementAndGet());
        }

        @Override
        public void run() {

            if (!webcam.isOpen()) {
                webcam.open();
            }


                int n = counter.incrementAndGet();
                /*if (n != 0 && n % 100 == 0) {
                    System.out.println(Thread.currentThread().getName() + ": Frames captured: " + n);
                }*/

                ImageIcon icon = new ImageIcon(webcam.getImage());
                JLabel jlabel = new JLabel(icon);
                jframe.getContentPane().removeAll();
                jframe.getContentPane().add(jlabel);
                jframe.repaint();
            webcam.close();
        }
    }

    public static void main(String[] args) throws Throwable {

        /*
         * This example will start several concurrent threads which use single
         * webcam instance.
         */
        jframe.setVisible(true);
        jframe.setResizable(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();

        int n = Runtime.getRuntime().availableProcessors() * 4;
        for (int i = 0; i < n; i++) {
            System.out.println("Thread: " + i);
            new Capture().start();
        }

        Thread.sleep(5 * 60 * 1000); // 5 minutes

        System.exit(1);
    }
}
