package frames.newevent;

import frames.calendar.CalendarFrame;
import storageclasses.AllDayEvent;
import storageclasses.TemporaryEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Klasa służąca do wyświetlenia okna dodającego wydarzenie
 * @author Jakub Jarzmik
 */
public class NewEventFrame extends JDialog implements ActionListener {
    private final CalendarFrame calendarFrame;
    private NewEventFramePanel newEventFramePanel;
    private JCheckBox temporaryEventCheckBox, allDayEventCheckBox;
    //private JTextField name;
    private JButton saveButton, cancelButton;

    /**
     * Konstruktor uruchamiający metodę inicjalizacji okna
     */
    public NewEventFrame() {
        calendarFrame = CalendarFrame.getInstance();
        init();
    }

    /**
     * Inicjalizuje główny widok okna
     */
    public void init(){
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 280);
        setModal(true);
        setIcon();
        setTitle("Nowe wydarzenie");

        initializeComponents();

        add(newEventFramePanel);
        pack();
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
    private void initializeComponents(){
        newEventFramePanel = new NewEventFramePanel(this);

        temporaryEventCheckBox = newEventFramePanel.temporaryEventCheckBox;
        allDayEventCheckBox = newEventFramePanel.allDayEventCheckBox;
        temporaryEventCheckBox.addActionListener(this);
        allDayEventCheckBox.addActionListener(this);

        cancelButton = newEventFramePanel.cancelButton;
        saveButton = newEventFramePanel.saveButton;
        cancelButton.addActionListener(this);
        saveButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == temporaryEventCheckBox) {
            temporaryEventCheckBox.setSelected(true);
            allDayEventCheckBox.setSelected(false);
            newEventFramePanel.setComboBoxesEnable(true);
        } else if (source == allDayEventCheckBox) {
            temporaryEventCheckBox.setSelected(false);
            allDayEventCheckBox.setSelected(true);
            newEventFramePanel.setComboBoxesEnable(false);
        }
        else if(source == cancelButton){
            dispose();
        }
    }

    /**
     * Zapisuje wydarzenie
     */
    protected void saveChanges(){
        if(newEventFramePanel.name.getText().length()!=0) {
            try {
                calendarFrame.getCenterPanel().remove(calendarFrame.getEventsPanel());
                if (temporaryEventCheckBox.isSelected()) {
                    TemporaryEvent temporaryEvent = new TemporaryEvent(newEventFramePanel.name.getText(), newEventFramePanel.description.getText(),
                            LocalDateTime.of(newEventFramePanel.yearStartTime.getSelectedIndex() + calendarFrame.getActualSelectedYear(), newEventFramePanel.monthStartTime.getSelectedIndex() + 1,
                                    newEventFramePanel.dayStartTime.getSelectedIndex() + 1, newEventFramePanel.hourStartTime.getSelectedIndex(),
                                    newEventFramePanel.minuteStartTime.getSelectedIndex()),
                            LocalDateTime.of(newEventFramePanel.yearEndTime.getSelectedIndex() + calendarFrame.getActualSelectedYear(), newEventFramePanel.monthEndTime.getSelectedIndex() + 1,
                                    newEventFramePanel.dayEndTime.getSelectedIndex() + 1, newEventFramePanel.hourEndTime.getSelectedIndex(),
                                    newEventFramePanel.minuteEndTime.getSelectedIndex()));
                    calendarFrame.getEventsPanel().addEvent(temporaryEvent);
                } else if (allDayEventCheckBox.isSelected()) {
                    AllDayEvent allDayEvent = new AllDayEvent(newEventFramePanel.name.getText(), newEventFramePanel.description.getText(), LocalDate.of(newEventFramePanel.yearStartTime.getSelectedIndex() + calendarFrame.getActualSelectedYear(), newEventFramePanel.monthStartTime.getSelectedIndex() + 1,
                            newEventFramePanel.dayStartTime.getSelectedIndex() + 1));
                    calendarFrame.getEventsPanel().addEvent(allDayEvent);
                }
                dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Moment zakończenia wydarzenia jest wcześniej niż moment rozpoczęcia");
            }
        }else JOptionPane.showMessageDialog(this, "Podaj nazwę wydarzenia");
    }
}
