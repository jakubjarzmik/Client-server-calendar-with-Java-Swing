package server;

import storageclasses.Event;
import storageclasses.UnusualHolidayAndNameDay;
import adapters.ExternalWebsitesAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Klasa obsługująca klienta na serwerze
 * @author Jakub Jarzmik
 */
class Session extends Thread {
    private final Socket socket;
    private String nick;
    protected ArrayList<Event> userEvents;
    protected ObjectOutputStream out = null;


    /**
     * Konstruktor inicjalizujący zmienne oraz pobierający wydarzenia z serwera
     * @param socket
     */
    Session(Socket socket) {
        this.socket = socket;
        Server.getEvents();
        userEvents = new ArrayList<>();
    }

    /**
     * Odpowiada za działanie sesji klienta
     */
    public void run() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            nick =(String) in.readObject();
            System.out.println("Nick: " + nick);

            ExternalWebsitesAdapter adapter = new ExternalWebsitesAdapter();
            String holidayName, nameDay;
            try {
                holidayName = adapter.getTodayHolidayName();
            }catch (IOException exception){ holidayName = "* błąd połączenia *";}

            try {
                nameDay = adapter.getTodayNameDay();
            }catch (IOException exception){nameDay = "* błąd połączenia *";}


            UnusualHolidayAndNameDay unusualHolidayAndNameDay = new UnusualHolidayAndNameDay(LocalDate.now(), holidayName, nameDay);
            out.writeObject(unusualHolidayAndNameDay);
            getMyEvents();
            out.writeObject(userEvents);
            out.writeObject(Server.defaultEvents);


            while (true) {
                userEvents =(ArrayList<Event>) in.readObject();
            }
        } catch (Exception e) {
            System.out.println("Problem w komunikacji z klientem");
        } finally {
            try {/* zamykanie połączenia */
                saveMyEvents();
                Server.saveEvents();
                in.close();
                out.close();
                socket.close();
                System.out.println("Koniec sesji z klientem");
            } catch (Exception e) {
                System.out.println("Problem z zamykaniem połączenia");
            }
        }
    }

    /**
     * pobiera wydarzenia użytkownika sesji z serwera
     */
    protected void getMyEvents(){
        boolean isFinded = false;
        for(UserData ud: Server.usersData)
        {
            if(ud.getNick().equals(nick)) {
                userEvents = ud.getEvents();
                isFinded = true;
                break;
            }
        }
        if(!isFinded){
            Server.usersData.add(new UserData(nick));
            System.out.println("Brak użytkownika w bazie danych, tworzymy nowy obiekt");
        }
    }

    /**
     * zapisuje wydarzenia użytkownika na serwer
     */
    protected void saveMyEvents(){
        for(UserData ud: Server.usersData)
        {
            if(ud.getNick().equals(nick)) {
                ud.setEvents(userEvents);
                break;
            }
        }
    }
}
