import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;
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
    
    public void updateEventDate(String id, LocalDateTime newDateTime){
        Event event = getEvent(id);
        event.setDateTime(newDateTime);
    }   

    public void updateEventLocation(String id, String newLocation){
        Event event = getEvent(id);
        event.setLocation(newLocation);
    }

    public void updateEventCapacity(String id, int newCapacity){
        Event event = getEvent(id);
        event.setCapacity(newCapacity);
    }
    public void updateWorkshopTopic(String id, String newTopic) {
        Event event = getEvent(id);
        if (event instanceof Workshop workshop) {
            workshop.setTopic(newTopic);
        } else {
            throw new IllegalArgumentException("Event is not a Workshop");
        }
    }

    // Update Seminar speaker
    public void updateSeminarSpeaker(String id, String newSpeaker) {
        Event event = getEvent(id);
        if (event instanceof Seminar seminar) {
            seminar.setSpeakerName(newSpeaker);
        } else {
            throw new IllegalArgumentException("Event is not a Seminar");
        }
    }

    // Update Concert age restriction
    public void updateConcertAgeRestriction(String id, String newAgeRestriction) {
        Event event = getEvent(id);
        if (event instanceof Concert concert) {
            concert.setAgeRestriction(newAgeRestriction);
        } else {
            throw new IllegalArgumentException("Event is not a Concert");
        }
    }
}

