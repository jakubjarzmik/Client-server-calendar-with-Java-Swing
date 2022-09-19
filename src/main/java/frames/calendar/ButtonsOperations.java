package frames.calendar;

import frames.newevent.NewEventFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;

public class ButtonsOperations {
    private static ButtonsOperations instance;
    private final CalendarFrame calendarFrame;
    private final JPanel menuPanel, eventsPanel, centerPanel;
    private JPanel monthPanel;
    private final JButton calendarButton, eventListButton, changeUserButton;
    private final JButton[] dayButtons;
    private static int actualSelectedDay = -1,
            actualSelectedMonth = LocalDate.now().getMonthValue()-1,
            actualSelectedYear = LocalDate.now().getYear();
    private ButtonsOperations() {
        calendarFrame = CalendarFrame.getInstance();

        menuPanel = calendarFrame.menuPanel;
        monthPanel = calendarFrame.monthPanel;
        eventsPanel = calendarFrame.eventsPanel;
        centerPanel = calendarFrame.centerPanel;

        calendarButton = calendarFrame.menuPanel.calendarButton;
        eventListButton = calendarFrame.menuPanel.eventListButton;
        changeUserButton =  calendarFrame.menuPanel.changeUserButton;
        dayButtons= calendarFrame.monthPanel.dayButtons;

        addActionListenersToButtons();
    }
    public static ButtonsOperations getInstance(){
        if(instance == null) {
            instance = new ButtonsOperations();
        }
        return instance;
    }

    public static int getActualSelectedDay() {
        return actualSelectedDay;
    }

    public static int getActualSelectedMonth() {
        return actualSelectedMonth;
    }

    public static int getActualSelectedYear() {
        return actualSelectedYear;
    }

    /**
     * Pobiera z paneli przyciski i dodająca do nich ActionListenery
     */
    private void addActionListenersToButtons(){
        calendarButton.addActionListener(calendarFrame);
        eventListButton.addActionListener(calendarFrame);
        changeUserButton.addActionListener(calendarFrame);
        for(int i =0; i<31;i++){
            if(dayButtons[i] == null) {
                break;
            }
            dayButtons[i].addActionListener(calendarFrame);
        }
    }
    protected void refreshButtonsOperations(){
        addActionListenersToButtons();
        refreshPanel();
    }

    protected void checkClickedButton(Object source){
        if(source == calendarButton){
            changePanel(eventsPanel,monthPanel);
        }
        else if(source == eventListButton) {
            changePanel(monthPanel, eventsPanel);
        }
        else if(source == changeUserButton){
            deleteUserDetailsFile();
        }
        else{
            for(int i=0;i<31;i++){
                if(source==dayButtons[i]){
                    openNewEventFrameWindow(i);
                    break;
                }
            }
        }
    }
    private void changePanel(JPanel panelToRemove, JPanel panelToSet){
        centerPanel.remove(panelToRemove);
        centerPanel.add(panelToSet);
        refreshPanel();
    }
    private void deleteUserDetailsFile(){
        File file = new File("localfiles/userDetails.dat");
        file.delete();
        calendarFrame.dispose();
    }
    private void openNewEventFrameWindow(int selectedDay){
        actualSelectedDay=selectedDay;
        NewEventFrame newEventFrame = new NewEventFrame();
        newEventFrame.setVisible(true);
    }

    /**
     * Zmienia miesiąc na poprzedni
     */
    protected void setPreviousMonth(){
        actualSelectedMonth--;
        if(actualSelectedMonth<0){
            actualSelectedYear--;
            actualSelectedMonth = 11;
        }
        setMonth();
    }
    /**
     * Zmienia miesiąc na następny
     */
    protected void setNextMonth(){
        actualSelectedMonth++;
        if(actualSelectedMonth>11){
            actualSelectedYear++;
            actualSelectedMonth = 0;
        }
        setMonth();
    }

    /**
     * Obsługuje zmiane paneli miesięcy
     */
    private void setMonth(){
        MonthPanel newMonthPanel = new MonthPanel(actualSelectedMonth, actualSelectedYear);
        centerPanel.remove(monthPanel);
        monthPanel = newMonthPanel;
        centerPanel.add(monthPanel);
        refreshButtonsOperations();
    }

    /**
     * Odświeża widok okna
     */
    protected void refreshPanel(){
        JPanel newViewPanel = new JPanel(new BorderLayout());
        newViewPanel.add(menuPanel,BorderLayout.WEST);
        newViewPanel.add(centerPanel);
        calendarFrame.setContentPane(newViewPanel);
        calendarFrame.revalidate();
        calendarFrame.pack();
    }
}
