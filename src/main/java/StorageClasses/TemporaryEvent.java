package StorageClasses;

import java.time.LocalDateTime;

/**
 * Klasa wydarzenia czasowego
 */
public class TemporaryEvent extends Event{
    LocalDateTime eventStartTime;
    LocalDateTime eventEndTime;

    public TemporaryEvent(String name, String description, LocalDateTime eventStartTime, LocalDateTime eventEndTime) throws Exception {
        super(name, description);
            checkEventValidity(eventStartTime, eventEndTime);
            this.eventStartTime = eventStartTime;
            this.eventEndTime = eventEndTime;
    }

    /**
     * @return moment rozpoczęcia wydarzenia
     */
    @Override
    public LocalDateTime eventStartTime() {
        return eventStartTime;
    }

    /**
     * @return moment zakończenia wydarzenia
     */
    @Override
    public LocalDateTime eventEndTime() {
        return eventEndTime;
    }


    /**
     * @return sformatowany tekst daty wydarzenia
     */
    @Override
    public String toString() {
        String startTimeDayOfMonth = checkLength(eventStartTime.getDayOfMonth()+"");
        String startTimeMonth = checkLength(eventStartTime.getMonthValue()+"");
        String startTimeHour = checkLength(eventStartTime.getHour()+"");
        String startTimeMinute = checkLength(eventStartTime.getMinute()+"");

        String endTimeDayOfMonth = checkLength(eventEndTime.getDayOfMonth()+"");
        String endTimeMonth = checkLength(eventEndTime.getMonthValue()+"");
        String endTimeHour = checkLength(eventEndTime.getHour()+"");
        String endTimeMinute = checkLength(eventEndTime.getMinute()+"");
        return startTimeDayOfMonth+"."+startTimeMonth+"."+eventStartTime.getYear()+" "+ startTimeHour+":"+startTimeMinute+" - "+
                endTimeDayOfMonth+"."+endTimeMonth+"."+eventEndTime.getYear()+" "+ endTimeHour +":"+endTimeMinute;
    }

    /**
     * rzuca wyjątek gdy moment zakończenia wydarzenia ma się zdarzyć wczesniej niż moment rozpoczęcia
     * @param eventStartTime początek wydarzenia
     * @param eventEndTime koniec wydarzenia
     * @throws Exception informacja o niepoprawnych danych wydarzenia
     */
    private void checkEventValidity(LocalDateTime eventStartTime, LocalDateTime eventEndTime) throws Exception {
        Exception exception = new Exception("Event end time is earlier than event start time");
        if(eventEndTime.getYear()<eventStartTime.getYear()) throw exception;
        else if(eventEndTime.getYear()==eventStartTime.getYear()) {
            if(eventEndTime.getDayOfYear()<eventStartTime.getDayOfYear()) throw exception;
            else if(eventEndTime.getDayOfYear()==eventStartTime.getDayOfYear()){
                if(eventEndTime.getHour()<eventStartTime.getHour()) throw exception;
                else if(eventEndTime.getHour()==eventStartTime.getHour()){
                    if(eventEndTime.getMinute()<eventStartTime.getMinute()) throw exception;
                }
            }
        }
    }
}
