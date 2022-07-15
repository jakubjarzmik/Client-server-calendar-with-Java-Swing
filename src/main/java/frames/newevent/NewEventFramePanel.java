package frames.newevent;

import frames.calendar.CalendarFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Month;

/**
 * Klasa służąca do zbudowania głównego panelu w oknie NewEventFrame
 * @author Jakub Jarzmik
 */
class NewEventFramePanel extends JPanel {
    private final CalendarFrame calendarFrame;
    private final NewEventFrame newEventFrame;
    protected JCheckBox temporaryEventCheckBox, allDayEventCheckBox;
    protected JTextField name;
    protected JTextArea description;
    protected JComboBox<String> monthStartTime, monthEndTime;
    protected JComboBox<Integer> dayStartTime, yearStartTime, yearEndTime,dayEndTime, hourStartTime, minuteStartTime,hourEndTime, minuteEndTime;
    private Integer[] days, years, hours, minutes;
    protected JButton saveButton, cancelButton;

    /**
     * Konstruktor ustawiający komponenty panelu
     * @param calendarFrame główne okno programu
     * @param newEventFrame okno dodawania nowego wydarzenia
     */
    public NewEventFramePanel(CalendarFrame calendarFrame, NewEventFrame newEventFrame) {
        this.calendarFrame = calendarFrame;
        this.newEventFrame = newEventFrame;
        setDatesArrays();
        this.add(createGUI());
    }
    /**
     * Kreuje główny zarys panelu
     * @return zwraca zbudowany panel
     */
    private JPanel createGUI(){
        JPanel windowPanel = new JPanel(new BorderLayout());
        windowPanel.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE,2));
        windowPanel.add(createCenterPanel(),BorderLayout.NORTH);
        windowPanel.add(createDescriptionPanel(), BorderLayout.CENTER);
        windowPanel.add(createButtonsPanel(),BorderLayout.SOUTH);
        return windowPanel;
    }

    /**
     * Kreuje środkową część okna
     * @return zwraca zbudowany panel
     */
    private JPanel createCenterPanel(){
        JPanel centerPanel = new JPanel(new GridLayout(4,1));

        centerPanel.add(createCheckBoxesPanel());
        centerPanel.add(createNamePanel());
        centerPanel.add(createStartTimePanel());
        centerPanel.add(createEndTimePanel());

        return centerPanel;
    }

    /**
     * Kreuje panel z checkboxami
     * @return zwraca zbudowany panel
     */
    private JPanel createCheckBoxesPanel(){
        JPanel checkBoxesPanel = new JPanel(new FlowLayout());
        checkBoxesPanel.setBackground(Color.WHITE);
        temporaryEventCheckBox = new JCheckBox("Wydarzenie czasowe");
        temporaryEventCheckBox.setSelected(true);
        allDayEventCheckBox = new JCheckBox("Wydarzenie całodniowe");
        temporaryEventCheckBox.setForeground(CalendarFrame.NAVY_BLUE);
        allDayEventCheckBox.setForeground(CalendarFrame.NAVY_BLUE);
        temporaryEventCheckBox.setBackground(Color.WHITE);
        allDayEventCheckBox.setBackground(Color.WHITE);
        checkBoxesPanel.add(temporaryEventCheckBox);
        checkBoxesPanel.add(allDayEventCheckBox);
        return checkBoxesPanel;
    }

    /**
     * Kreuje panel z polem do wprowadzenia nazwy wydarzenia
     * @return zwraca zbudowany panel
     */
    private JPanel createNamePanel(){
        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Nazwa:");
        setLabel(nameLabel);
        namePanel.add(nameLabel);
        name = new JTextField(null,25);
        namePanel.add(name);
        return namePanel;
    }

    /**
     * Kreuje panel służący do ustawienia czasu rozpoczęcia wydarzenia
     * @return zwraca zbudowany panel
     */
    private JPanel createStartTimePanel(){
        JPanel startTimePanel = new JPanel(new FlowLayout());
        startTimePanel.setBackground(Color.WHITE);
        JLabel startTimeLabel = new JLabel("Czas rozpoczęcia:");
        setLabel(startTimeLabel);
        startTimePanel.add(startTimeLabel);
        dayStartTime = new JComboBox<>(days);
        dayStartTime.setSelectedItem(days[calendarFrame.getActualSelectedDay()]);
        monthStartTime = new JComboBox<>(CalendarFrame.monthNames);
        monthStartTime.setSelectedItem(CalendarFrame.monthNames[calendarFrame.getActualSelectedMonth()]);
        yearStartTime = new JComboBox<>(years);
        hourStartTime = new JComboBox<>(hours);
        minuteStartTime = new JComboBox<>(minutes);
        setComboBoxesAppearance(dayStartTime,monthStartTime,yearStartTime,hourStartTime,minuteStartTime);
        dayStartTime.setEnabled(false);
        monthStartTime.setEnabled(false);
        yearStartTime.setEnabled(false);
        startTimePanel.add(dayStartTime);
        startTimePanel.add(monthStartTime);
        startTimePanel.add(yearStartTime);
        startTimePanel.add(new JLabel(" "));
        startTimePanel.add(hourStartTime);
        startTimePanel.add(new JLabel(":"));
        startTimePanel.add(minuteStartTime);
        return startTimePanel;
    }

    /**
     * Kreuje panel służący do ustawienia czasu zakończenia wydarzenia
     * @return zwraca zbudowany panel
     */
    private JPanel createEndTimePanel(){
        JPanel endTimePanel = new JPanel(new FlowLayout());
        endTimePanel.setBackground(Color.WHITE);
        JLabel endTimeLabel = new JLabel("Czas zakończenia:");
        setLabel(endTimeLabel);
        endTimePanel.add(endTimeLabel);
        dayEndTime = new JComboBox<>(days);
        dayEndTime.setSelectedItem(days[calendarFrame.getActualSelectedDay()]);
        monthEndTime = new JComboBox<>(CalendarFrame.monthNames);
        monthEndTime.setSelectedItem(CalendarFrame.monthNames[calendarFrame.getActualSelectedMonth()]);
        yearEndTime = new JComboBox<>(years);
        hourEndTime = new JComboBox<>(hours);
        minuteEndTime = new JComboBox<>(minutes);
        setComboBoxesAppearance(dayEndTime,monthEndTime,yearEndTime,hourEndTime,minuteEndTime);
        endTimePanel.add(dayEndTime);
        endTimePanel.add(monthEndTime);
        endTimePanel.add(yearEndTime);
        endTimePanel.add(new JLabel(" "));
        endTimePanel.add(hourEndTime);
        endTimePanel.add(new JLabel(":"));
        endTimePanel.add(minuteEndTime);
        return endTimePanel;
    }

    /**
     * Ustawia wygląd pól wyboru w oknie
     * @param days pole wyboru dni
     * @param months pole wyboru miesięcy
     * @param years pole wyboru lat
     * @param hours pole wyboru godzin
     * @param minutes pole wyboru minut
     */
    private void setComboBoxesAppearance(JComboBox<Integer> days,JComboBox<String> months, JComboBox<Integer> years,JComboBox<Integer> hours, JComboBox<Integer> minutes){
        Dimension dimension4020 = new Dimension(40,20);
        days.setPreferredSize(dimension4020);
        months.setPreferredSize(new Dimension(92,20));
        years.setPreferredSize(new Dimension(53,20));
        hours.setPreferredSize(dimension4020);
        minutes.setPreferredSize(dimension4020);
    }

    /**
     * Ustawia wygląd labeli
     * @param label label, któremu ustawiamy wygląd
     */
    private void setLabel(JLabel label){
        label.setPreferredSize(new Dimension(120,30));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setForeground(CalendarFrame.NAVY_BLUE);
    }

    /**
     * Służy do blokowania pól wyboru
     * @param b typ logiczny służący do tego czy okna mają być zablokowane
     */
    protected void setComboBoxesEnable(boolean b){
        hourStartTime.setEnabled(b);
        minuteStartTime.setEnabled(b);
        dayEndTime.setEnabled(b);
        monthEndTime.setEnabled(b);
        yearEndTime.setEnabled(b);
        hourEndTime.setEnabled(b);
        minuteEndTime.setEnabled(b);
        if(b) {
            hourEndTime.setSelectedItem(hours[0]);
            minuteEndTime.setSelectedItem(minutes[0]);
        }
        else {
            hourEndTime.setSelectedItem(hours[23]);
            minuteEndTime.setSelectedItem(minutes[59]);
        }
    }

    /**
     * Ustawia wartości w tablicach potrzebnych w polach wyboru
     */
    private void setDatesArrays(){
        int year = calendarFrame.getActualSelectedYear();
        years = new Integer[10];
        for(int i=0;i<10;i++)
            years[i] = year+i;

        setDays();

        hours = new Integer[24];
        for(int i=0;i<24;i++)
            hours[i] = i;

        minutes = new Integer[60];
        for(int i=0;i<60;i++)
            minutes[i] = i;
    }

    /**
     * Służy do ustawienia ilości dni w danym miesiącu
     */
    private void setDays(){
        Month month = Month.of(calendarFrame.getActualSelectedMonth()+1);
        int daysInMonth = month.length(true);
        if(month==Month.FEBRUARY)
            if(calendarFrame.getActualSelectedYear()%4!=0)
                daysInMonth--;
        Integer[] days = new Integer[daysInMonth];
        for(int i=0;i<days.length;i++)
            days[i] = i+1;
        this.days = days;
    }

    /**
     * Kreuje panel z polem do wpisania opisu wydarzenia
     * @return zwraca zbudowany panel
     */
    private JPanel createDescriptionPanel(){
        JPanel descriptionPanel = new JPanel(new FlowLayout());
        descriptionPanel.setBackground(Color.WHITE);
        descriptionPanel.setPreferredSize(new Dimension(400,100));
        JLabel descriptionLabel = new JLabel("Opis:");
        descriptionPanel.add(descriptionLabel);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptionLabel.setPreferredSize(new Dimension(120,65));
        descriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        descriptionLabel.setForeground(CalendarFrame.NAVY_BLUE);
        description = new JTextArea(null,4,25);
        description.setLineWrap(true);
        descriptionPanel.add(new JScrollPane(description));
        return descriptionPanel;
    }

    /**
     * kreuje panel z przyciskami
     * @return zwraca zbudowany panel
     */
    private JPanel createButtonsPanel(){
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,40,5));
        buttonsPanel.setBackground(Color.WHITE);


        Action saveAction = new AbstractAction("Zapisz") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newEventFrame.saveChanges();
            }
        };
        saveAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke("control S"));
        saveButton = new JButton(saveAction);
        saveButton.getActionMap().put("saveAction", saveAction);
        saveButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) saveAction.getValue(Action.ACCELERATOR_KEY), "saveAction");

        //saveButton = new JButton("Zapisz");
        cancelButton = new JButton("Anuluj");
        saveButton.setBackground(CalendarFrame.NAVY_BLUE);
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(CalendarFrame.NAVY_BLUE);
        cancelButton.setForeground(Color.WHITE);

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        return buttonsPanel;
    }
}
