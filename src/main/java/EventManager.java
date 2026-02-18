import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

    private Map<String, Event> events = new HashMap<>();

    public void addEvent(Event event) {
        if(events.containsKey(event.getEventId())) {
            throw new IllegalArgumentException("Duplicate event ID");
        }

        events.put(event.getEventId(), event);
    }

    public Event getEvent(String id) {
        Event event = events.get(id);
        if (event == null) {
        throw new IllegalArgumentException("No event found with ID: " + id);
        }
        return event;
        }


    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }
    
    public void cancelEvent(String id) {
        Event event = getEvent(id);
        if (event == null) {
            throw new IllegalArgumentException("No Event found with id: " + id);
        }
        getEvent(id).cancelEvent();
    }
}