import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataSaver {

    // Save all users to users.csv
    public static void saveUsers(List<User> users, String filePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("userId,name,email,userType");
            bw.newLine();

            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);

                String type;
                if (u instanceof Student) {
                    type = "Student";
                } else if (u instanceof Staff) {
                    type = "Staff";
                } else {
                    type = "Guest";
                }

                bw.write(u.getuserID() + "," + u.getname() + "," + u.getemail() + "," + type);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Save all events to events.csv
    public static void saveEvents(List<Event> events, String filePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");
            bw.newLine();

            for (int i = 0; i < events.size(); i++) {
                Event e = events.get(i);

                String type;
                String topic = "";
                String speaker = "";
                String age = "";

                if (e instanceof Workshop) {
                    type = "Workshop";
                    topic = ((Workshop) e).getTopic();
                } else if (e instanceof Seminar) {
                    type = "Seminar";
                    speaker = ((Seminar) e).getSpeakerName();
                } else {
                    type = "Concert";
                    age = ((Concert) e).getAgeRestriction();
                }

                String status;
                if (e.getStatus() == EventStatus.CANCELLED) {
                    status = "Cancelled";
                } else {
                    status = "Active";
                }

                bw.write(e.getEventId() + "," + e.getTitle() + "," +
                         e.getDateTime() + "," + e.getLocation() + "," +
                         e.getCapacity() + "," + status + "," +
                         type + "," + topic + "," + speaker + "," + age);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    // Save all bookings to bookings.csv
    public static void saveBookings(List<Booking> bookings, String filePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("bookingId,userId,eventId,createdAt,bookingStatus");
            bw.newLine();

            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);

                bw.write(b.getBookingId() + "," + b.getUserId() + "," +
                         b.getEventId() + "," + b.getCreatedAt() + "," +
                         b.getStatus());
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }
}
