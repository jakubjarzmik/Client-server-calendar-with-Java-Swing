package frames.calendar;
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
    private static CalendarFrame instance;
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
    protected int actualSelectedDay, actualSelectedMonth, actualSelectedYear;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String nick;
    private boolean changeNickname;
    private final ButtonsOperations buttonsOperations;


    /**
     * Konstruktor inicjalizuje komponenty, łączy się z serwerem i wyświetla okno logowania
     */
    private CalendarFrame() {
        super("Kalendarz"); // tytuł okienka
        unusualHolidayAndNameDay = new String[2];
        actualSelectedMonth = LocalDate.now().getMonthValue()-1;
        actualSelectedYear = LocalDate.now().getYear();
        actualSelectedDay = -1;
        monthPanel = new MonthPanel(actualSelectedMonth, actualSelectedYear);
        eventsPanel = new EventsPanel();
        menuPanel = new MenuPanel();
        centerPanel = new JPanel(new BorderLayout());
        buttonsOperations = new ButtonsOperations();
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
    public static CalendarFrame getInstance(){
        if(instance == null) {
            instance = new CalendarFrame();
            System.out.println("nullem jest");
        }
        System.out.println("asda");
        return instance;
    }

    /**
     * Służy do uruchomienia aplikacji
     */

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
        buttonsOperations.checkClickedButton(e.getSource());
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
        MonthPanel newMonthPanel = new MonthPanel(actualSelectedMonth, actualSelectedYear);
        centerPanel.remove(monthPanel);
        monthPanel = newMonthPanel;
        centerPanel.add(monthPanel);
        buttonsOperations.refreshButtonsOperations();
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

