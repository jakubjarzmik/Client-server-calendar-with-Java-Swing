package frames.calendar;

import frames.calendar.threads.ClockThread;
import frames.calendar.threads.NameDayThread;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa tworząca górny panel programu
 */
public class UpperPanel extends JPanel{
    /**
     * Konstruktor ustawiający tło panelu i uruchamiający metodę budującą panel
     */
    public UpperPanel() {
        setBackground(Color.WHITE);
        this.add(createGUI());
    }
    /**
     * Kreuje główny zarys panelu
     * @return zwraca zbudowany panel
     */
    private JPanel createGUI() {
        JPanel upperPanel = new JPanel(new FlowLayout());
        upperPanel.setBackground(Color.WHITE);
        upperPanel.add(createClockGUI());
        upperPanel.add(createNameDayGUI());
        return upperPanel;
    }

    /**
     * Kreuje panel z zegarem
     * @return zwraca zbudowany panel
     */
    private JPanel createClockGUI(){
        JPanel clockPanel = new JPanel(new FlowLayout());
        JLabel clockLabel = new JLabel(createClock());
        clockPanel.setPreferredSize(new Dimension(72,34));
        clockPanel.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE,2));
        clockPanel.setBackground(CalendarFrame.NAVY_BLUE);
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setFont(CalendarFrame.BIG_DEFAULT_BOLD_FONT);

        clockPanel.add(clockLabel);
        ClockThread clockThread = new ClockThread(clockLabel);
        clockThread.start();
        return clockPanel;
    }

    /**
     * Kreuje panel z imieninami oraz nietypowymi świętami
     * @return zwraca zbudowany panel
     */
    private JPanel createNameDayGUI(){
        JPanel nameDayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameDayLabel = new JLabel();
        nameDayPanel.setPreferredSize(new Dimension(249,34));
        nameDayPanel.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE,2));
        nameDayPanel.setBackground(CalendarFrame.NAVY_BLUE);
        nameDayLabel.setForeground(Color.WHITE);
        nameDayLabel.setFont(CalendarFrame.BIG_DEFAULT_BOLD_FONT);


        nameDayPanel.add(nameDayLabel);
        NameDayThread nameDayThread = new NameDayThread(nameDayLabel);
        nameDayThread.start();
        return nameDayPanel;
    }

    /**
     * Ustawia aktualny czas oraz ustawia odpowiedni format
     * @return zwraca aktualny czas
     */
    public static String createClock(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }

}
