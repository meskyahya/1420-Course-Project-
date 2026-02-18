import java.time.LocalDateTime;

public class Seminar extends Event {

    private String speakerName;

    public Seminar(String eventId,
                   String title,
                   LocalDateTime dateTime,
                   String location,
                   int capacity,
                   String speakerName) {

        super(eventId, title, dateTime, location, capacity);

        if (speakerName == null || speakerName.isBlank()) {
            throw new IllegalArgumentException("Speaker name cannot be empty");
        }

        this.speakerName = speakerName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        if (speakerName == null || speakerName.isBlank()) {
            throw new IllegalArgumentException("Speaker name cannot be empty");
        }
        this.speakerName = speakerName;
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Seminar | Speaker: " + speakerName;
    }
}
