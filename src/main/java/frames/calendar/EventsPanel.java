package frames.calendar;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.*;

import storageclasses.AllDayEvent;
import storageclasses.Event;

/**
 * Klasa służąca do wyświetlenia panelu z listą wydarzeń
 * @author Jakub Jarzmik
 */
public class EventsPanel extends JPanel{
    protected ArrayList<Event> userEvents;
    protected ArrayList<AllDayEvent> defaultEvents;
    private JPanel guiPanel;
    private final CalendarFrame calendarFrame;

    /**
     * Konstruktor służący do inicjalizacji komponentów, koniecznych zmiennych i kontenerów
     */
    public EventsPanel() {
        calendarFrame = CalendarFrame.getInstance();
        userEvents = new ArrayList<>();
        defaultEvents = new ArrayList<>();
        setBackground(Color.WHITE);
        guiPanel = createGUI();
        this.add(guiPanel);
    }

    /**
     * Kreuje główny zarys panelu
     * @return zwraca zbudowany panel
     */
    private JPanel createGUI() {
        JPanel eventsPanel = new JPanel();
        eventsPanel.setBorder(BorderFactory.createLineBorder(CalendarFrame.NAVY_BLUE,2));
        eventsPanel.setLayout(new BorderLayout());
        eventsPanel.setBackground(Color.WHITE);
        eventsPanel.setForeground(CalendarFrame.NAVY_BLUE);
        eventsPanel.add(createTitleGUI(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(createEventsListGUI());
        scrollPane.setHorizontalScrollBar(null);
        eventsPanel.add(scrollPane, BorderLayout.CENTER);
        eventsPanel.setPreferredSize(new Dimension(326,220));
        return eventsPanel;
    }

    /** Kreuje widok panelu tytułowego listy wydarzeń
     * @return zwraca zbudowany panel
     */
    private JPanel createTitleGUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.setBackground(CalendarFrame.NAVY_BLUE);
        JLabel label = new JLabel("Twoje wydarzenia", SwingConstants.CENTER);
        label.setFont(CalendarFrame.HEADER_DEFAULT_FONT);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(150,25));
        titlePanel.add(label, BorderLayout.CENTER);

        return titlePanel;
    }

    /**
     * Buduje panel z listą wydarzeń
     * @return zwraca zbudowany panel
     */
    private JPanel createEventsListGUI() {
        ArrayList<Event> allEvents = new ArrayList<>(userEvents);
        allEvents.addAll(defaultEvents);

        allEvents.sort(Comparator.comparing(Event::eventStartTime));
        JPanel eventsListPanel = new JPanel(new FlowLayout());
        eventsListPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        eventsListPanel.add(addTodayPanel());
        int howMany18Rows = 2;
        int howMany16Rows = 0;
        for (Event e : allEvents){
            if(checkEventAvailability(e.eventEndTime())) continue;
            JPanel eventPanel = new JPanel(new BorderLayout(2,2));
            JPanel eventNamePanel = new JPanel(new BorderLayout());
            String eventName = e.getName();
            JLabel eventNameLabel = new JLabel("<html>"+eventName+"</html>");
            if(eventName.length() > 0){
                int nameHeight = (eventName.length()/40 + 1)*18;
                howMany18Rows+=nameHeight/18;
                eventNameLabel.setPreferredSize(new Dimension(300,nameHeight));
            }
            JLabel eventDurationLabel = new JLabel(e.toString());
            JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel eventOngoingMessageLabel = new JLabel("TRWA");

            String eventDescription = e.getDescription();
            JLabel eventDescriptionLabel = new JLabel("<html>"+eventDescription+"</html>");
            boolean checkAddingDescriptionPanel = false;
            if(checkEventAvailability(e.eventStartTime())){ descriptionPanel.add(eventOngoingMessageLabel); checkAddingDescriptionPanel = true;}
            if(eventDescription.length() > 0){
                int descriptionHeight = (eventDescription.length()/45 + 1)*16;
                howMany16Rows += descriptionHeight/16;
                eventDescriptionLabel.setPreferredSize(new Dimension(290,descriptionHeight));
                descriptionPanel.add(eventDescriptionLabel); checkAddingDescriptionPanel = true;
            }
            JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

            eventNameLabel.setForeground(CalendarFrame.NAVY_BLUE);
            eventNameLabel.setFont(CalendarFrame.BIG_DEFAULT_BOLD_FONT);
            eventDurationLabel.setForeground(CalendarFrame.LIGHT_RED);
            eventOngoingMessageLabel.setForeground(CalendarFrame.LIGHT_RED);
            eventOngoingMessageLabel.setFont(CalendarFrame.DEFAULT_BOLD_FONT);
            eventDurationLabel.setFont(CalendarFrame.DEFAULT_FONT);
            eventDescriptionLabel.setForeground(CalendarFrame.NAVY_BLUE);
            eventDescriptionLabel.setFont(CalendarFrame.DEFAULT_FONT);
            separator.setForeground(CalendarFrame.LIGHT_BLUE);
            separator.setBackground(new Color(0,23,48,100));

            eventNamePanel.add(eventNameLabel,BorderLayout.NORTH);
            eventNamePanel.add(eventDurationLabel, BorderLayout.CENTER);
            howMany18Rows++;

            eventPanel.add(eventNamePanel,BorderLayout.NORTH);

            if(checkAddingDescriptionPanel) {
                eventPanel.add(descriptionPanel,BorderLayout.CENTER);
            }

            eventPanel.add(separator,BorderLayout.SOUTH);
            howMany18Rows++;
            eventsListPanel.add(eventPanel);
        }
        eventsListPanel.setPreferredSize(new Dimension(300,howMany18Rows*18+ howMany16Rows*16));
        return eventsListPanel;
    }

    /**
     * Buduje panel wyświetlający dzisiejszą datę na liście
     * @return zwraca zbudowany panel
     */
    private JPanel addTodayPanel(){
        JPanel todayPanel = new JPanel(new BorderLayout(5,5));
        todayPanel.setPreferredSize(new Dimension(300,32));

        String dayOfMonth = Event.checkLength(LocalDate.now().getDayOfMonth()+"");
        String month = Event.checkLength(LocalDate.now().getMonthValue()+"");

        JLabel eventNameLabel = new JLabel("DZISIAJ "+ dayOfMonth+"."+month+"."+LocalDate.now().getYear());

        eventNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        eventNameLabel.setForeground(CalendarFrame.LIGHT_RED);
        eventNameLabel.setFont(CalendarFrame.BIG_DEFAULT_BOLD_FONT);

        JSeparator firstSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        firstSeparator.setForeground(CalendarFrame.LIGHT_RED);
        firstSeparator.setBackground(CalendarFrame.LIGHT_RED);

        todayPanel.add(eventNameLabel,BorderLayout.NORTH);
        todayPanel.add(firstSeparator,BorderLayout.CENTER);

        return todayPanel;
    }

    /**
     * Sprawdza czy wydarzenie już się nie odbyło lub czy aktualnie trwa
     * @param localDateTime czas startu lub zakonczenia wydarzenia
     * @return zwraca prawdę moment z parametru jest z przeszłości, w przeciwnym wypadku zwraca fałsz
     */
    private boolean checkEventAvailability(LocalDateTime localDateTime){
        if(localDateTime.getYear()<LocalDateTime.now().getYear()) return true;
        else if(localDateTime.getYear()==LocalDateTime.now().getYear()) {
            if(localDateTime.getDayOfYear()<LocalDateTime.now().getDayOfYear()) return true;
            else if(localDateTime.getDayOfYear()==LocalDateTime.now().getDayOfYear()){
                if(localDateTime.getHour()<LocalDateTime.now().getHour()) return true;
                else if(localDateTime.getHour()==LocalDateTime.now().getHour()){
                    return localDateTime.getMinute() < LocalDateTime.now().getMinute();
                }
            }
        }
        return false;
    }

    /**
     * Dodaje wydarzenie użytkownika
     * @param event wydarzenie użytkownika
     */
    public void addEvent(Event event){
        userEvents.add(event);
        refreshEvents();
    }

    /**
     * Odświeża panel z listą wydarzeń
     */
    protected void refreshEvents(){
        this.remove(guiPanel);
        guiPanel = createGUI();
        this.add(guiPanel);
        calendarFrame.centerPanel.remove(calendarFrame.monthPanel);
        calendarFrame.centerPanel.add(calendarFrame.eventsPanel);
        calendarFrame.buttonsOperations.refreshPanel();
    }

}

