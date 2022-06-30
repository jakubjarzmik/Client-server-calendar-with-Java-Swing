package Frames.Calendar;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa służąca do wyświetlenia menu z lewej strony
 * @author Jakub Jarzmik
 */
class MenuPanel extends JPanel {
    protected JButton calendarButton;
    protected JButton eventListButton;
    protected JButton changeUserButton;

    /**
     * Konstruktor ustawiający tło i dodający główny panel
     */
    public MenuPanel() {
        setBackground(CalendarFrame.NAVY_BLUE);
        this.add(createGUI());
    }
    /**
     * Kreuje główny zarys panelu
     * @return zwraca zbudowany panel
     */
    private JPanel createGUI() {
        JPanel menuPanel = new JPanel(new GridLayout(3,1));
        menuPanel.add(calendarButtonPanel());
        menuPanel.add(eventListButtonPanel());
        menuPanel.add(changeUserButtonPanel());

        return menuPanel;
    }

    /**
     * Kreuje panel z przycikiem do przełączenia się na panel kalendarza
     * @return zwraca zbudowany panel
     */
    private JPanel calendarButtonPanel(){
        JPanel calendarButtonPanel = new JPanel();
        calendarButton = new JButton("Kalendarz");

        setButtonPanelStyle(calendarButtonPanel);
        setButtonStyle(calendarButton, CalendarFrame.NAVY_BLUE,CalendarFrame.LIGHT_BLUE);
        calendarButtonPanel.add(calendarButton);

        return  calendarButtonPanel;
    }
    /**
     * Kreuje panel z przycikiem do przełączenia się na panel z listą wydarzeń
     * @return zwraca zbudowany panel
     */
    private JPanel eventListButtonPanel(){
        JPanel eventListButtonPanel = new JPanel();
        eventListButton = new JButton("Lista wydarzeń");

        setButtonPanelStyle(eventListButtonPanel);
        setButtonStyle(eventListButton, CalendarFrame.NAVY_BLUE,CalendarFrame.LIGHT_BLUE);
        eventListButtonPanel.add(eventListButton);

        return  eventListButtonPanel;
    }

    /**
     * Kreuje panel z przycikiem do wylogowania użytkownika i zamknięcia programu
     * @return zwraca zbudowany panel
     */
    private JPanel changeUserButtonPanel(){
        JPanel changeUserButtonPanel = new JPanel();
        changeUserButton = new JButton("Wyloguj");

        setButtonPanelStyle(changeUserButtonPanel);
        setButtonStyle(changeUserButton, Color.WHITE,CalendarFrame.LIGHT_RED);
        changeUserButtonPanel.add(changeUserButton);

        return  changeUserButtonPanel;
    }

    /**
     * Ustawia wygląd przycisku w menu
     * @param c komponent do zmiany wyglądu
     * @param foreground zmienna z kolorem tekstu komponentu
     * @param background zmienna z kolorem tła komponentu
     */
    private void setButtonStyle(Component c, Color foreground, Color background){
        c.setForeground(foreground);
        c.setBackground(background);
        c.setFont(CalendarFrame.bigDefaultBoldFont);
        c.setPreferredSize(new Dimension(150,35));
    }

    /**
     * Ustawia wygląd panelu z przyciskiem
     * @param p panel, którego wygląd będzie ustawiony
     */
    private void setButtonPanelStyle(JPanel p){
        p.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        p.setBackground(CalendarFrame.NAVY_BLUE);
    }
}
