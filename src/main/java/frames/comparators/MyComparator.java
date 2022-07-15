package frames.comparators;

import storageclasses.Event;

import java.util.Comparator;

/**
 * Klasa utworzona w celu sortowania wydarzeń chronologicznie na liście
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html">Comparator</a>
 * @author Jakub Jarzmik
 */
public class MyComparator implements Comparator<Event> {
    @Override
    public int compare(Event e1, Event e2){
        if(e2==null) return -1;
        return e1.eventStartTime().compareTo(e2.eventStartTime());
    }
}
