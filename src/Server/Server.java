package Server;

import StorageClasses.AllDayEvent;
import StorageClasses.UnusualHolidayAndNameDay;

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
    protected static ArrayList<UnusualHolidayAndNameDay> unusualHolidayAndNameDays;

    /**
     * Konstruktor inicjalizujący konieczne zmienne i kontenery danych oraz uruchamiający serwer
     */
    Server() {
        try {
            usersData = new ArrayList<>();
            defaultEvents = new ArrayList<>();
            unusualHolidayAndNameDays = new ArrayList<>();
            //setDefaultEvents();
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
        Socket socket = null;
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
        catch(EOFException e) { System.out.println("Koniec odczytu eventow uzytkownika na serwer"); }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * pobiera domyślne wydarzenia oraz nietypowe święta i imieniny z pliku
     */
    private static void getDefaultEvents()
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("serverfiles/defaultEvents.dat"))) {
            while(true) {
                AllDayEvent event = (AllDayEvent) in.readObject();
                defaultEvents.add(event);
            }
        }
        catch(EOFException e) { System.out.println("Koniec odczytu domyslnych eventow na serwer"); }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("serverfiles/unusualHolidaysAndNameDays.dat"))) {
            while(true) {
                UnusualHolidayAndNameDay unusualHolidayAndNameDay = (UnusualHolidayAndNameDay) in.readObject();
                unusualHolidayAndNameDays.add(unusualHolidayAndNameDay);
            }
        }
        catch(EOFException e) { System.out.println("Koniec odczytu nietypowych świąt i imienin na serwer"); }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * służy do ustawienia domyślnych wydarzeń, świąt nietypowych oraz imienin (domyślnie nieuruchamiana)
     */
    private static void setDefaultEvents(){
        ArrayList<AllDayEvent> defaultEventsToSet = new ArrayList<>();
        defaultEventsToSet.add(new AllDayEvent("Święto Konstytucji", "", LocalDate.of(2022,5,3)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Matki", "", LocalDate.of(2022,5,26)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Dziecka", "", LocalDate.of(2022,6,1)));
        defaultEventsToSet.add(new AllDayEvent("Zielone Świątki", "", LocalDate.of(2022,6,5)));
        defaultEventsToSet.add(new AllDayEvent("Boże Ciało", "", LocalDate.of(2022,6,16)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Ojca", "", LocalDate.of(2022,6,23)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Pamięci Powstania Warszawskiego", "", LocalDate.of(2022,8,1)));
        defaultEventsToSet.add(new AllDayEvent("Wniebowzięcie Najświętszej Maryi Panny", "", LocalDate.of(2022,8,15)));
        defaultEventsToSet.add(new AllDayEvent("Dzień Solidarności i Wolności", "", LocalDate.of(2022,8,31)));
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("serverfiles/defaultEvents.dat"))) {
            for(AllDayEvent allDayEvent: defaultEventsToSet) {
                out.writeObject(allDayEvent);
            }
        } catch(IOException e) { e.printStackTrace(); }

        ArrayList<UnusualHolidayAndNameDay> unusualHolidayAndNameDayToSet = new ArrayList<>();
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,5,30),"Światowy Dzień Soku","Feliks, Ferdynand"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,5,31),"Światowy Dzień Cytrynówki","Aniela, Petronela"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,1),"Dzień dziecka","Jakub, Konrad"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,2),"Dzień Leśnika","Erazm, Marianna"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,3),"Światowy Dzień Roweru","Leszek, Kłotylda"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,4),"Dzień Drukarza","Karol, Franciszek"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,5),"Dzień Dziękczynienia","Waleria, Bonifacy"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,6),"Dzień Chemika","Paulina, Laura"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,7),"Święto Flagi Peru","Robert, Wiesław"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,8),"Dzień Informatyka","Maksym, Medard"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,9),"Dzień Przyjaciela","Palagia, Felicjan"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,10),"Międzynarodowy Dzień Elektryka", "Bogumiła, Małgorzata"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,11),"Międzynarodowy Dzień Rysia","Barnaba, Feliks"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,12),"Dzień Stylisty Paznokci","Jan, Onufry"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,13),"Dzień Dobrych Rad","Lucjan, Antoni"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,14),"Światowy Dzień Żonglerki","Walery, Bazyli"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,15),"Światowy Dzień Wiatru","Wit, Justyna"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,16),"Boże Ciało","Alina, Justyna"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,17),"Dzień Czołgisty","Laura, Adolf"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,18),"Międzynarodowy Dzień Sushi","Marek, Elżbieta"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,19),"Dzień Kota Garfielda","Gerwazy, Protazy"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,20),"Światowy Dzień Uchodźcy","Bogna, Florentyna"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,21),"Najdłuższy Dzień Roku","Alicja, Alojzy"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,22),"Światowy Dzień Garbusa","Paulina, Flawiusz"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,23),"Dzień Ojca","Wanda, Zenon"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,24),"Dzień Przytulania","Jan, Danuta"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,25),"Dzień Stoczniowca", "Lucja, Wilhelm"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,26),"Dzień Szwagra","Jan, Paweł"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,27),"Dzień bez płacenia gotówką","Maria, Władysław"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,28),"Dzień grania w Butelkę","Leon, Ireneusz"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,29),"Dzień Rybaka","Piotr, Paweł"));
        unusualHolidayAndNameDayToSet.add(new UnusualHolidayAndNameDay(LocalDate.of(2022,6,30),"Dzień Asteroid","Emilia, Lucyna"));

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("serverfiles/unusualHolidaysAndNameDays.dat"))) {
            for(UnusualHolidayAndNameDay unusualHolidayAndNameDay: unusualHolidayAndNameDayToSet) {
                out.writeObject(unusualHolidayAndNameDay);
            }
        } catch(IOException e) { e.printStackTrace(); }
    }
}
