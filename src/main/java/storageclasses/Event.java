package storageclasses;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Klasa abstrakcyjna przechowująca wydarzenia
 */
public abstract class Event implements Serializable {
    protected String name;
    protected String description;

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Metoda służąca do formatowania wyświetlanego dnia lub miesiąca, gdy np dzien jest liczba pojedyncza to ta metoda doda 0 przed liczba
     * @param s dzień lub miesiąc
     * @return sformatowany string
     */
    public static String checkLength (String s){
        if(s.length()==1) s = "0"+s;
        return s;
    }

    /**
     * @return moment rozpoczęcia wydarzenia
     */
    abstract public LocalDateTime eventStartTime();

    /**
     * @return moment zakończenia wydarzenia
     */
    abstract public LocalDateTime eventEndTime();

    /**
     * @return sformatowany tekst daty i czasu wydarzenia
     */
    abstract public String toString();
}