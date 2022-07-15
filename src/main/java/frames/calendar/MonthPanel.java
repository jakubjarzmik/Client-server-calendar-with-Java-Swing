package frames.calendar;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Klasa służąca do wyświetlenia panelu z kalendarzem
 * @author Jakub Jarzmik
 */
class MonthPanel extends JPanel {
    CalendarFrame calendarFrame;
    private final int month;
    private final int year;
    protected JButton previousMonthButton, nextMonthButton;
    protected JButton[] dayButtons;

    private final String[] dayNames = {"Nd", "Pon", "Wt", "Śr", "Czw", "Pt", "Sob"};

    /**
     * Konstruktor służący do inicjalizacji komponentów, koniecznych zmiennych i kontenerów
     * @param calendarFrame Główne okno programu
     * @param month Miesiąc który ma być wyświetlony
     * @param year Rok który ma być wyświetlony
     */
    public MonthPanel(CalendarFrame calendarFrame,int month, int year) {
        this.calendarFrame = calendarFrame;
        dayButtons = new JButton[31];
        this.month = month;
        this.year = year;
        setBackground(Color.WHITE);
        this.add(createGUI());
    }
    /**
     * Kreuje główny zarys panelu
     * @return zwraca zbudowany panel
     */
    private JPanel createGUI() {
        JPanel monthPanel = new JPanel();
        monthPanel.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE,2));
        monthPanel.setLayout(new BorderLayout());
        monthPanel.setBackground(Color.WHITE);
        monthPanel.setForeground(CalendarFrame.NAVY_BLUE);
        monthPanel.add(createTitleGUI(), BorderLayout.NORTH);
        monthPanel.add(createDaysGUI(), BorderLayout.SOUTH);

        return monthPanel;
    }

    /**
     * Kreuje widok panelu z informacją o wyświetlanym miesiącu i roku oraz przycikami do zmiany miesiąca
     * @return zwraca zbudowany panel
     */
    private JPanel createTitleGUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.setBackground(CalendarFrame.NAVY_BLUE);
        JLabel label = new JLabel(CalendarFrame.monthNames[month] + " " + year, SwingConstants.CENTER);

        Action nextMonthAction = new AbstractAction("") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calendarFrame.setNextMonth();
                }
            };
            nextMonthAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke("RIGHT"));
            nextMonthButton = new JButton(nextMonthAction);
            nextMonthButton.getActionMap().put("nextMonthAction", nextMonthAction);
            nextMonthButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    (KeyStroke) nextMonthAction.getValue(Action.ACCELERATOR_KEY), "nextMonthAction");

            Action previousMonthAction = new AbstractAction("") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calendarFrame.setPreviousMonth();
                }
            };
            previousMonthAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke("LEFT"));
            previousMonthButton = new JButton(previousMonthAction);
            previousMonthButton.getActionMap().put("previousMonthAction", previousMonthAction);
            previousMonthButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    (KeyStroke) previousMonthAction.getValue(Action.ACCELERATOR_KEY), "previousMonthAction");



        try {
            Image previousMonthIcon = ImageIO.read(new File("icons/previousMonth.png"));
            Image nextMonthIcon = ImageIO.read(new File("icons/nextMonth.png"));
            previousMonthButton.setIcon(new ImageIcon(previousMonthIcon));
            nextMonthButton.setIcon(new ImageIcon(nextMonthIcon));
        }catch (IOException e){e.printStackTrace();}
        previousMonthButton.setBackground(CalendarFrame.NAVY_BLUE);
        nextMonthButton.setBackground(CalendarFrame.NAVY_BLUE);
        previousMonthButton.setBorder(null);
        nextMonthButton.setBorder(null);

        label.setForeground(Color.WHITE);
        label.setFont(CalendarFrame.headerDefaultFont);
        label.setPreferredSize(new Dimension(150,25));

        titlePanel.add(previousMonthButton);
        titlePanel.add(label);
        titlePanel.add(nextMonthButton);
        return titlePanel;
    }

    /**
     * Kreuje widok panelu szczegółów kalendarza
     * @return zwraca zbudowany panel
     */
    private JPanel createDaysGUI() {
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new GridLayout(0, dayNames.length));

        Calendar today = Calendar.getInstance();
        int tMonth = today.get(Calendar.MONTH);
        int tYear = today.get(Calendar.YEAR);
        int tDay = today.get(Calendar.DAY_OF_MONTH);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Calendar iterator = (Calendar) calendar.clone();
        iterator.add(Calendar.DAY_OF_MONTH,
                -(iterator.get(Calendar.DAY_OF_WEEK) - 1));

        Calendar maximum = (Calendar) calendar.clone();
        maximum.add(Calendar.MONTH, +1);

        for (int i = 0; i < dayNames.length; i++) {
            JPanel dPanel = new JPanel();
            dPanel.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE));
            if(i==0)
                dPanel.setBackground(CalendarFrame.LIGHT_RED);
            else
                dPanel.setBackground(new Color(0,23,48,100));
            JLabel dLabel = new JLabel(dayNames[i]);
            dLabel.setFont(CalendarFrame.bigDefaultBoldFont);
            dLabel.setPreferredSize(new Dimension(34,20));
            dLabel.setVerticalAlignment(SwingConstants.CENTER);
            dLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dPanel.add(dLabel);
            dayPanel.add(dPanel);
        }

        int count = 0;
        int limit = dayNames.length * 6;

        while (iterator.getTimeInMillis() < maximum.getTimeInMillis()) {
            int lMonth = iterator.get(Calendar.MONTH);
            int lYear = iterator.get(Calendar.YEAR);

            JButton dButton = new JButton();
            dayButtons[iterator.get(Calendar.DAY_OF_MONTH)-1] = dButton;
            dButton.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE));
            dButton.setFont(CalendarFrame.bigDefaultFont);

            if ((lMonth == month) && (lYear == year)) {
                int lDay = iterator.get(Calendar.DAY_OF_MONTH);
                dButton.setText(Integer.toString(lDay));
                if ((tMonth == month) && (tYear == year) && (tDay == lDay)) {
                    dButton.setBackground(CalendarFrame.LIGHT_BLUE);
                } else {
                    dButton.setBackground(Color.WHITE);
                }
            } else {
                dButton.setText(" ");
                dButton.setBackground(Color.WHITE);
                dButton.setEnabled(false);
            }
            dayPanel.add(dButton);
            iterator.add(Calendar.DAY_OF_YEAR, +1);
            count++;
        }

        if(limit-7>=count)
            limit-=7;
        for (int i = count; i < limit; i++) {
            JPanel dPanel = new JPanel();
            dPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel dayLabel = new JLabel();
            dayLabel.setText(" ");
            dPanel.setBackground(Color.WHITE);
            dPanel.add(dayLabel);
            dayPanel.add(dPanel);
        }
        return dayPanel;
    }
}

