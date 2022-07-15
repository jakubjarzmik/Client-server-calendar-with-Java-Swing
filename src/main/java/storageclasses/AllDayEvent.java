package storageclasses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Klasa wydarzenia całodniowego
 * @author Jakub Jarzmik
 */
public class AllDayEvent extends Event{
    protected LocalDate eventDay;

    public AllDayEvent(String name, String description, LocalDate eventDay) {
        super(name, description);
        this.eventDay = eventDay;
    }

    /**
     * @return moment rozpoczęcia wydarzenia
     */
    @Override
    public LocalDateTime eventStartTime() {
        return LocalDateTime.of(eventDay, LocalTime.of(0,0));
    }

    /**
     * @return moment zakończenia wydarzenia
     */
    @Override
    public LocalDateTime eventEndTime() {
        return LocalDateTime.of(eventDay, LocalTime.of(23,59,59));
    }
    /**
     * @return sformatowany tekst daty wydarzenia
     */
    @Override
    public String toString() {
        String dayOfMonth = checkLength(eventDay.getDayOfMonth()+"");
        String month = checkLength(eventDay.getMonthValue()+"");
        return "All-day "+ dayOfMonth+"."+month+"."+eventDay.getYear();
    }
}
