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
    protected MonthPanel monthPanel;
    protected EventsPanel eventsPanel;
    protected UpperPanel upperPanel;
    protected MenuPanel menuPanel;
    protected JPanel centerPanel;

    protected String nick;
    private final WindowCloser windowCloser = new WindowCloser();
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    protected ButtonsOperations buttonsOperations;
    private UserServices userServices;


    /**
     * Konstruktor inicjalizuje komponenty, łączy się z serwerem i wyświetla okno logowania
     */
    protected CalendarFrame() {
        super("Kalendarz"); // tytuł okienka
        unusualHolidayAndNameDay = new String[2];
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
    }
    public static CalendarFrame getInstance(){
        if(instance == null) {
            instance = new CalendarFrame();
        }
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


    /**
     * Służy do wyświetlenia okna aplikacji z wszystkimi komponentami
     */
    public void init() {
        menuPanel = new MenuPanel();
        monthPanel = new MonthPanel(ButtonsOperations.getActualSelectedMonth(), (ButtonsOperations.getActualSelectedYear()));
        eventsPanel = new EventsPanel();
        centerPanel = new JPanel(new BorderLayout());


        buttonsOperations = ButtonsOperations.getInstance();
        userServices = new UserServices();

        upperPanel = new UpperPanel();

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
     * Obsługuje przyciski w aplikacji
     * @param e <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/event/ActionEvent.html">ActionEvent</a>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        buttonsOperations.checkClickedButton(e.getSource());
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

