package server;

import storageclasses.AllDayEvent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa - serwer programu
 * @author Jakub Jarzmik
 */
class Server {
    private static final int serverPort = 2020;
    ServerSocket s;
    protected static ArrayList<UserData> usersData;
    protected static ArrayList<AllDayEvent> defaultEvents;

    /**
     * Konstruktor inicjalizujący konieczne zmienne i kontenery danych oraz uruchamiający serwer
     */
    Server() {
        try {
            usersData = new ArrayList<>();
            defaultEvents = new ArrayList<>();
            setDefaultEvents();
            getDefaultEvents();
            s = new ServerSocket(serverPort);
            System.out.println("Serwer uruchomiony");
        } catch (Exception e) {
            System.out.println("Nie można utworzyć gniazda");
            System.exit(1);
        }
    }

    /**
     * Obsługuje łączących się klientów
     */
    void run() {
        Socket socket;
        try {
            while(true) {
                /* Czekaj, aż klient się połączy */
                socket = s.accept();
                System.out.println("Klient połączony");
                new Session(socket).start();
            }            
        } catch (Exception e) {
            System.out.println("Problem połączenia z klientem");
        } finally {
            try {/* ubicie serwera */                
                s.close();
            } catch (Exception e) {
                System.out.println("Problem z zamykaniem połączenia");
            }
        }
    }//koniec funkcji dzialaj()

    /**
     * Służy do uruchomienia aplikacji
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    /**
     * Zapisuje wydarzenia użytkowników do pliku
     */
    protected static void saveEvents(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("serverfiles/usersData.dat"))) {
            for(UserData userData: usersData) {
                out.writeObject(userData);
            }
        } catch(IOException e) { e.printStackTrace(); }
    }

    /**
     * Pobiera wydarzenia użytkowników z pliku
     */
    protected static void getEvents()
    {
        usersData.clear();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("serverfiles/usersData.dat"))) {
            while(true) {
                UserData userData = (UserData) in.readObject();
                usersData.add(userData);
            }
        }
        catch(EOFException e) { System.out.println("Koniec odczytu eventów użytkownika na serwer"); }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * pobiera domyślne wydarzenia z pliku
     */
    private static void getDefaultEvents()
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("serverfiles/defaultEvents.dat"))) {
            while(true) {
                AllDayEvent event = (AllDayEvent) in.readObject();
                defaultEvents.add(event);
            }
        }
        catch(EOFException e) { System.out.println("Koniec odczytu domyślnych eventów na serwer"); }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * służy do ustawienia domyślnych wydarzeń
     */
    private static void setDefaultEvents() {
        ArrayList<AllDayEvent> defaultEventsToSet = new ArrayList<>();
        defaultEventsToSet.add(new AllDayEvent("Święto Konstytucji", "", LocalDate.of(2022, 5, 3)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Matki", "", LocalDate.of(2022, 5, 26)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Dziecka", "", LocalDate.of(2022, 6, 1)));
        defaultEventsToSet.add(new AllDayEvent("Zielone Świątki", "", LocalDate.of(2022, 6, 5)));
        defaultEventsToSet.add(new AllDayEvent("Boże Ciało", "", LocalDate.of(2022, 6, 16)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Ojca", "", LocalDate.of(2022, 6, 23)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Pamięci Powstania Warszawskiego", "", LocalDate.of(2022, 8, 1)));
        defaultEventsToSet.add(new AllDayEvent("Wniebowzięcie Najświętszej Maryi Panny", "", LocalDate.of(2022, 8, 15)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Solidarności i Wolności", "", LocalDate.of(2022, 8, 31)));
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("serverfiles/defaultEvents.dat"))) {
            for (AllDayEvent allDayEvent : defaultEventsToSet) {
                out.writeObject(allDayEvent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
