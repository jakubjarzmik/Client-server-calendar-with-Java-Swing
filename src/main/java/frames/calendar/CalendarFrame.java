package frames.calendar;

import frames.newevent.NewEventFrame;
import storageclasses.UnusualHolidayAndNameDay;
import storageclasses.AllDayEvent;
import storageclasses.Event;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Główna klasa służąca do wyświetlania okna
 * @author Jakub Jarzmik
 * @version 0.1
 */
public class CalendarFrame extends JFrame implements ActionListener {
    public static final Color NAVY_BLUE = new Color(0,23,48);
    public static final Color LIGHT_BLUE = new Color(74,215,209);
    public static final Color LIGHT_RED = new Color(254,74,73);
    public static final Font DEFAULT_FONT = new Font("Open Sans", Font.PLAIN, 12);
    public static final Font DEFAULT_BOLD_FONT = new Font("Open Sans", Font.BOLD, 12);
    public static final Font BIG_DEFAULT_FONT = new Font("Open Sans", Font.PLAIN, 14);
    public static final Font BIG_DEFAULT_BOLD_FONT = new Font("Open Sans", Font.BOLD, 14);
    public static final Font HEADER_DEFAULT_FONT = new Font("Open Sans", Font.BOLD, 16);
    public static final String[] MONTH_NAMES = { "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};
    public static String[] unusualHolidayAndNameDay;
    private final WindowCloser windowCloser = new WindowCloser();
    protected MonthPanel monthPanel;
    protected EventsPanel eventsPanel;
    protected UpperPanel upperPanel;
    protected MenuPanel menuPanel;
    protected JPanel centerPanel;
    private JButton calendarButton, eventListButton,changeUserButton;
    private JButton[] dayButtons;
    protected int actualSelectedDay, actualSelectedMonth, actualSelectedYear;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String nick;
    private boolean changeNickname;


    /**
     * Konstruktor inicjalizuje komponenty, łączy się z serwerem i wyświetla okno logowania
     */
    public CalendarFrame() {
        super("Kalendarz"); // tytuł okienka
        unusualHolidayAndNameDay = new String[2];
        actualSelectedMonth = LocalDate.now().getMonthValue()-1;
        actualSelectedYear = LocalDate.now().getYear();
        actualSelectedDay = -1;
        monthPanel = new MonthPanel(this,actualSelectedMonth, actualSelectedYear);
        eventsPanel = new EventsPanel(this);
        menuPanel = new MenuPanel();
        centerPanel = new JPanel(new BorderLayout());
        try{
            Socket socket = new Socket("127.0.0.1", 2020);
            out = new ObjectOutputStream(
                            socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e){
            JOptionPane.showMessageDialog(this, "Nie można połączyć się z serwerem");
            System.exit(0);
        }
        nick = "";
        changeNickname = false;
        readNick();
        showLoginWindow();
        upperPanel = new UpperPanel();
        init();
    }

    /**
     * Służy do uruchomienia aplikacji
     */
    public static void main(String[] args) {
        new CalendarFrame();
    }

    public EventsPanel getEventsPanel() {
        return eventsPanel;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public int getActualSelectedDay() {
        return actualSelectedDay;
    }

    public int getActualSelectedMonth() {
        return actualSelectedMonth;
    }

    public int getActualSelectedYear() {
        return actualSelectedYear;
    }

    /**
     * Służy do wyświetlenia okna aplikacji z wszystkimi komponentami
     */
    public void init() {
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setIcon();
        setResizable(false);


        centerPanel.setBorder(BorderFactory.createLineBorder(NAVY_BLUE));
        centerPanel.add(upperPanel,BorderLayout.NORTH);
        centerPanel.add(monthPanel);
        add(menuPanel,BorderLayout.WEST);
        add(centerPanel);
        buttonsOperations();
        eventsPanel.refreshEvents();

        this.addWindowListener(windowCloser);
        pack();
        setVisible(true);

    }

    /**
     * Ustawia ikonę okienka
     */
    private void setIcon(){
        try {
            Image imageIcon = ImageIO.read(new File("icons/calendarIcon.png"));
            setIconImage(imageIcon);
        }catch (IOException e){e.printStackTrace();}
    }

    /**
     * Pobiera z paneli przyciski i dodająca do nich ActionListenery
     */
    private void buttonsOperations(){
        calendarButton = menuPanel.calendarButton;
        eventListButton = menuPanel.eventListButton;
        changeUserButton = menuPanel.changeUserButton;
        calendarButton.addActionListener(this);
        eventListButton.addActionListener(this);
        changeUserButton.addActionListener(this);

        dayButtons= monthPanel.dayButtons;
        for(int i =0; i<31;i++){
            if(dayButtons[i] == null) {
                break;
            }
            dayButtons[i].addActionListener(this);
        }
    }

    /**
     * Zapisuje nick użytkownika do pliku by nie musiał się kolejny raz logować
     */
    private void saveNick(){
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("localfiles/userDetails.dat"))) {
            out.writeUTF(nick);
        } catch(IOException e) { e.printStackTrace(); }
    }

    /**
     * Wyświetla okienko logowania
     */
    private void showLoginWindow(){
        if(nick.isBlank() || changeNickname) {
            nick = JOptionPane.showInputDialog("Podaj swój nickname:");
            String[] options = new String[]{"TAK","NIE"};
            int result = JOptionPane.showOptionDialog(this,"Zapamiętać nazwę użytkownika?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
                    null,options,null);
            if(result == 0)
                saveNick();
        }
        changeNickname = false;
        showEvents();
    }

    /**
     * Wczystuje nick użytkownika z pliku jeśli był zapisany
     */
    private void readNick(){
        try (DataInputStream in = new DataInputStream(new FileInputStream("localfiles/userDetails.dat"))) {
            nick = in.readUTF();
        }
        catch (FileNotFoundException ex) { System.out.println("Brak pliku z nickiem użytkownika"); }
        catch(IOException e) { e.printStackTrace(); }
    }

    /**
     * Pobiera wydarzenia z serwera
     */
    private void showEvents(){
        try {
            out.writeObject(nick);

            UnusualHolidayAndNameDay u = (UnusualHolidayAndNameDay) in.readObject();
            unusualHolidayAndNameDay[0] = u.getHolidayName();
            unusualHolidayAndNameDay[1] = u.getNameDay();

            eventsPanel.userEvents =(ArrayList<Event>) in.readObject();
            eventsPanel.defaultEvents =(ArrayList<AllDayEvent>) in.readObject();
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * Obsługuje przyciski w aplikacji
     * @param e <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/event/ActionEvent.html">ActionEvent</a>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == calendarButton){
            centerPanel.remove(eventsPanel);
            centerPanel.add(monthPanel);
            refreshPanel();
        }
        else if(source == eventListButton) {
            centerPanel.remove(monthPanel);
            centerPanel.add(eventsPanel);
            refreshPanel();
        }
        else if(source == changeUserButton){
            File file = new File("localfiles/userDetails.dat");
            file.delete();
            dispose();
        }
        else{
            for(int i=0;i<31;i++){
                if(source==dayButtons[i]){
                    actualSelectedDay=i;
                    NewEventFrame newEventFrame = new NewEventFrame(this);
                    newEventFrame.setVisible(true);
                    break;
                }
            }
        }
    }

    /**
     * Zmienia miesiąc na poprzedni
     */
    protected void setPreviousMonth(){
        actualSelectedMonth--;
        if(actualSelectedMonth<0){
            actualSelectedYear--;
            actualSelectedMonth = 11;
        }
        setMonth();
    }
    /**
     * Zmienia miesiąc na następny
     */
    protected void setNextMonth(){
        actualSelectedMonth++;
        if(actualSelectedMonth>11){
            actualSelectedYear++;
            actualSelectedMonth = 0;
        }
        setMonth();
    }

    /**
     * Obsługuje zmiane paneli miesięcy
     */
    private void setMonth(){
        MonthPanel newMonthPanel = new MonthPanel(this,actualSelectedMonth, actualSelectedYear);
        centerPanel.remove(monthPanel);
        monthPanel = newMonthPanel;
        centerPanel.add(monthPanel);
        buttonsOperations();
        refreshPanel();
    }

    /**
     * Odświeża widok okna
     */
    protected void refreshPanel(){
        JPanel newViewPanel = new JPanel(new BorderLayout());
        newViewPanel.add(menuPanel,BorderLayout.WEST);
        newViewPanel.add(centerPanel);
        setContentPane(newViewPanel);
        revalidate();
        pack();
    }

    /**
     * Klasa zagnieżdżona służąca do zamykania okna
     */
    class WindowCloser extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            close();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            close();
        }
        private void close(){
            try {
                out.writeObject(eventsPanel.userEvents);
            }catch(IOException exception) {exception.printStackTrace();}
            System.exit(0);
        }
    }
}

