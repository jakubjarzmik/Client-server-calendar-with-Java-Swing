package frames.calendar;

import frames.newevent.NewEventFrame;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

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
            changePanel(calendarFrame.eventsPanel,calendarFrame.monthPanel);
        }
        else if(source == eventListButton) {
            changePanel(calendarFrame.monthPanel, calendarFrame.eventsPanel);
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
        calendarFrame.centerPanel.remove(panelToRemove);
        calendarFrame.centerPanel.add(panelToSet);
        calendarFrame.refreshPanel();
    }
    private void deleteUserDetailsFile(){
        File file = new File("localfiles/userDetails.dat");
        file.delete();
        calendarFrame.dispose();
    }
    private void openNewEventFrameWindow(int selectedDay){
        calendarFrame.actualSelectedDay=selectedDay;
        NewEventFrame newEventFrame = new NewEventFrame();
        newEventFrame.setVisible(true);
    }
}
