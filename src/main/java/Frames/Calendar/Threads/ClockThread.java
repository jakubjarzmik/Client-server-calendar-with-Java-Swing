package Frames.Calendar.Threads;

import Frames.Calendar.UpperPanel;

import javax.swing.*;

/**
 * Klasa - wątek zegara
 * @author Jakub Jarzmik
 */
public class ClockThread extends Thread{
    protected JLabel clock;

    public ClockThread(JLabel clock) {
        this.clock = clock;
    }

    /**
     * Odpowiada za działanie wątku
     */
    @Override
    public void run() {
        while(true){
            try{ Thread.sleep(1000);} catch (Exception e) { e.printStackTrace(); }
            clock.setText(UpperPanel.createClock());
        }
    }
}
