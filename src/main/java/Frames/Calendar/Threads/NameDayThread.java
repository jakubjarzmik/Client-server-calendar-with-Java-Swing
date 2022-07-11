package Frames.Calendar.Threads;

import Frames.Calendar.CalendarFrame;

import javax.swing.*;

/**
 * Klasa - wątek, odpowiedzialny za płynną zmianę imienin na nietypowe święto
 * @author Jakub Jarzmik
 */
public class NameDayThread extends Thread {
    protected JLabel nameDay;
    protected String[] today;
    double x;
    double y;
    public NameDayThread(JLabel nameDay) {
        today = CalendarFrame.unusualHolidayAndNameDay.clone();
        this.nameDay = nameDay;
        x = nameDay.getLocation().getX()+6;
        y = nameDay.getLocation().getY()+7;

    }

    /**
     * Odpowiada za działanie wątku
     */
    @Override
    public void run(){
        nameDay.setText("Imieniny: "+today[1]);
        int count=0;
        int numberOfText=0;
        while(true){
            if(count == 200) {
                y -= 40;
                count=-200;
                if(numberOfText==0) {
                    nameDay.setText(today[0]);
                    numberOfText=1;
                }
                else{
                    nameDay.setText("Imieniny: "+today[1]);
                    numberOfText=0;
                }
            }
            if(count==0)
                try{ for(int i=0;i<1000;i++){nameDay.setLocation((int)x,(int)y);Thread.sleep(1);}}catch(Exception e){e.printStackTrace();}
            y+=0.1;
            nameDay.setLocation((int)x,(int)y);
            count+=1;
            try{Thread.sleep(1);}catch(Exception e){e.printStackTrace();}
        }
    }
}
