package frames.calendar;

import frames.newevent.NewEventFrame;

import javax.swing.*;
import java.io.File;
class ButtonsOperations {
    private final CalendarFrame calendarFrame;
    private final JButton calendarButton, eventListButton, changeUserButton;
    private final JButton[] dayButtons;
    ButtonsOperations() {
        calendarFrame = CalendarFrame.getInstance();
        calendarButton = calendarFrame.menuPanel.calendarButton;
        eventListButton = calendarFrame.menuPanel.eventListButton;
        changeUserButton =  calendarFrame.menuPanel.changeUserButton;
        dayButtons= calendarFrame.monthPanel.dayButtons;
        addActionListenersToButtons();
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
        calendarFrame.refreshPanel();
    }

    protected void checkClickedButton(Object source){
        if(source == calendarButton){
            calendarFrame.centerPanel.remove(calendarFrame.eventsPanel);
            calendarFrame.centerPanel.add(calendarFrame.monthPanel);
            calendarFrame.refreshPanel();
        }
        else if(source == eventListButton) {
            calendarFrame.centerPanel.remove(calendarFrame.monthPanel);
            calendarFrame.centerPanel.add(calendarFrame.eventsPanel);
            calendarFrame.refreshPanel();
        }
        else if(source == changeUserButton){
            File file = new File("localfiles/userDetails.dat");
            file.delete();
            calendarFrame.dispose();
        }
        else{
            for(int i=0;i<31;i++){
                if(source==dayButtons[i]){
                    calendarFrame.actualSelectedDay=i;
                    NewEventFrame newEventFrame = new NewEventFrame();
                    newEventFrame.setVisible(true);
                    break;
                }
            }
        }
    }
}
