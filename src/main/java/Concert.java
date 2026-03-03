import java.time.LocalDateTime;

public class Concert extends Event {

    private String ageRestriction;

    public Concert(String eventId, String title, LocalDateTime dateTime,
                   String location, int capacity, String ageRestriction) {

        super(eventId, title, dateTime, location, capacity);

        if (ageRestriction == null || ageRestriction.isBlank()) {
            throw new IllegalArgumentException("Age restriction cannot be empty");
        }

        this.ageRestriction = ageRestriction;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(String ageRestriction) {
        if (ageRestriction == null || ageRestriction.isBlank()) {
            throw new IllegalArgumentException("Age restriction cannot be empty");
        }
        this.ageRestriction = ageRestriction;
    }

    @Override
    public String toString() {
        return super.toString() + " | Type: Concert | Age Restriction: " + ageRestriction;
    }
}
