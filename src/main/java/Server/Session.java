package Server;

import StorageClasses.Event;
import StorageClasses.UnusualHolidayAndNameDay;
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

            Document unusualHolidayDocument = Jsoup.connect("https://bimkal.pl/kalendarz-swiat").get();
            Elements unusualHolidayElements = unusualHolidayDocument.getElementsByClass("nietypowe");
            String holidayName = unusualHolidayElements.get(0).text().split(",")[0];

            Document nameDayDocument = Jsoup.connect("https://www.google.com/search?q=imieniny+dzisiaj&oq=imie&aqs=chrome.0.69i59j69i57j0i512l3j69i60l3.3538j0j4&sourceid=chrome&ie=UTF-8").get();
            Elements nameDayElements = nameDayDocument.getElementsByClass("hgKElc");
            String nameDay = nameDayElements.get(0).text().split(":")[1].split(" i ")[0];


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
