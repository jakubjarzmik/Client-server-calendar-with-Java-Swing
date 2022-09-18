package frames.calendar;

import storageclasses.AllDayEvent;
import storageclasses.Event;
import storageclasses.UnusualHolidayAndNameDay;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import static frames.calendar.CalendarFrame.unusualHolidayAndNameDay;

class UserServices {
    CalendarFrame calendarFrame;
    private boolean changeNickname;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private String nick;
    UserServices(){
        calendarFrame = CalendarFrame.getInstance();
        nick = calendarFrame.nick;
        out = calendarFrame.out;
        in = calendarFrame.in;
        readNick();
        showLoginWindow();
        showEvents();
    }
    /**
     * Wczystuje nick użytkownika z pliku jeśli był zapisany
     */
    private void readNick(){
        try (DataInputStream in = new DataInputStream(new FileInputStream("localfiles/userDetails.dat"))) {
            nick = in.readUTF();
        }
        catch (FileNotFoundException ex) { System.out.println("Brak pliku z nickiem użytkownika"); }
        catch(IOException e) { e.printStackTrace(); }
    }
    /**
     * Wyświetla okienko logowania
     */
    private void showLoginWindow(){
        if(nick.isBlank() || changeNickname) {
            nick = JOptionPane.showInputDialog("Podaj swój nickname:");
            String[] options = new String[]{"TAK","NIE"};
            int result = JOptionPane.showOptionDialog(calendarFrame,"Zapamiętać nazwę użytkownika?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
                    null,options,null);
            if(result == 0)
                saveNick();
        }
        changeNickname = false;
    }
    /**
     * Zapisuje nick użytkownika do pliku by nie musiał się kolejny raz logować
     */
    private void saveNick(){
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("localfiles/userDetails.dat"))) {
            out.writeUTF(nick);
        } catch(IOException e) { e.printStackTrace(); }
    }

    /**
     * Pobiera wydarzenia z serwera
     */
    private void showEvents(){
        try {
            calendarFrame.out.writeObject(nick);

            UnusualHolidayAndNameDay u = (UnusualHolidayAndNameDay) in.readObject();
            unusualHolidayAndNameDay[0] = u.getHolidayName();
            unusualHolidayAndNameDay[1] = u.getNameDay();

            calendarFrame.eventsPanel.userEvents =(ArrayList<Event>) in.readObject();
            calendarFrame.eventsPanel.defaultEvents =(ArrayList<AllDayEvent>) in.readObject();
        }catch (Exception e){e.printStackTrace();}
    }

}
