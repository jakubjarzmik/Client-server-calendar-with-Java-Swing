package storageclasses;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Klasa nietypowego święta i imienin
 * @author Jakub Jarzmik
 */
public class UnusualHolidayAndNameDay implements Serializable {
    protected LocalDate localDate;
    protected String holidayName;
    protected String nameDay;

    public UnusualHolidayAndNameDay(LocalDate localDate, String holidayName, String nameDay) {
        this.localDate = localDate;
        this.holidayName = holidayName;
        this.nameDay = nameDay;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public String getNameDay() {
        return nameDay;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}
