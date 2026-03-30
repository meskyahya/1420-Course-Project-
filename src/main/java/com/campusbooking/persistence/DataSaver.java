package com.campusbooking.persistence;

import com.campusbooking.models.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DataSaver {

    // Save Users to CSV File
    public static void saveUsers(List<User> users, String filePath) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("userId,name,email,userType");
            
            for (User u : users) {

                pw.println(
                u.getuserID() + "," +
                u.getName() + "," +
                u.getEmail() + "," +
                u.getrole()
    );
            }

        } catch (IOException e) {
            System.out.println("Error saving users.csv");
        }
    }

    // Save Events to Csv File
    public static void saveEvents(List<Event> events, String filePath) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");

            for (Event e : events) {

                String topic = "";
                String speaker = "";
                String age = "";

                if (e instanceof Workshop)
                    topic = ((Workshop) e).getTopic();

                if (e instanceof Seminar)
                    speaker = ((Seminar) e).getSpeakerName();

                if (e instanceof Concert)
                    age = ((Concert) e).getAgeRestriction();

                String eventType = "Event";

                if (e instanceof Workshop) eventType = "Workshop";
                else if (e instanceof Seminar) eventType = "Seminar";
                else if (e instanceof Concert) eventType = "Concert";

                pw.println(
                        e.getEventId() + "," +
                                e.getTitle() + "," +
                                e.getDateTime() + "," +
                                e.getLocation() + "," +
                                e.getCapacity() + "," +
                                e.getStatus() + "," +
                                eventType + "," +
                                topic + "," +
                                speaker + "," +
                                age
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving events.csv");
        }
    }

    // Save Bookings to CSV File
    public static void saveBookings(List<Booking> bookings, String filePath) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("bookingId,userId,eventId,createdAt,bookingStatus");

            for (Booking b : bookings) {
                System.out.println("Saving booking: " + b.getBookingId() + " status: " + b.getStatus());
                pw.println(
                        b.getBookingId() + "," +
                                b.getUserId() + "," +
                                b.getEventId() + "," +
                                b.getCreatedAt() + "," +
                                b.getStatus()
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving bookings.csv");
        }
    }
}
