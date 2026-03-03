import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;

public class EventLoader {


    public static void loadEvents(String filePath, EventManager manager) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                String eventId = data[0];
                String title = data[1];
                LocalDateTime dateTime = LocalDateTime.parse(data[2]);
                String location = data[3];
                int capacity = Integer.parseInt(data[4]);
                String statusStr = data[5];
                String eventType = data[6];
                String topic = data[7];
                String speakerName = data[8];
                String ageRestriction = data[9];

                Event event = null;

                switch (eventType) {

                    case "Workshop":
                        if (topic == null || topic.isBlank()) {
                            throw new IllegalArgumentException("Workshop requires topic.");
                        }
                        event = new Workshop(eventId, title, dateTime, location, capacity, topic);
                        break;

                    case "Seminar":
                        if (speakerName == null || speakerName.isBlank()) {
                            throw new IllegalArgumentException("Seminar requires speakerName.");
                        }
                        event = new Seminar(eventId, title, dateTime, location, capacity, speakerName);
                        break;

                    case "Concert":
                        if (ageRestriction == null || ageRestriction.isBlank()) {
                            throw new IllegalArgumentException("Concert requires ageRestriction.");
                        }
                        event = new Concert(eventId, title, dateTime, location, capacity, ageRestriction);
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown event type: " + eventType);
                }

                // Set status from CSV
                if (statusStr.equalsIgnoreCase("Cancelled")) {
                    event.setStatus(EventStatus.CANCELLED);
                } else {
                    event.setStatus(EventStatus.ACTIVE);
                }

                manager.addEvent(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}