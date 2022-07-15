package server;

import storageclasses.Event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa przechowująca wydarzenia danego użytkownika
 * @author Jakub Jarzmik
 */
class UserData implements Serializable {
    private String nick;
    private ArrayList<Event> events;

    /**
     * Konstruktor tworzący dla użytkownika listę wydarzeń
     * @param nick nick użytkownika
     */
    public UserData(String nick) {
        this.nick = nick;
        events = new ArrayList<>();
    }

    /**
     * dodaje wydarzenia do listy
     * @param event wydarzenie
     */
    protected void addEvent(Event event){
        events.add(event);
    }

    public String getNick() {
        return nick;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
