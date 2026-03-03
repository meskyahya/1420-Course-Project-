import java.time.LocalDateTime;

public class Workshop extends Event {

    private String topic;

    public Workshop(String eventId, String title, LocalDateTime dateTime,
                    String location, int capacity, String topic) {

        super(eventId, title, dateTime, location, capacity);

        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Workshop topic cannot be empty");
        }

        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Workshop topic cannot be empty");
        }
        this.topic = topic;
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Workshop | Topic: " + topic;
    }
}
