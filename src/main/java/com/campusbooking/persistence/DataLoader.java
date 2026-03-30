package com.campusbooking.persistence;

import com.campusbooking.models.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class DataLoader {

    public static List<User> loadUsers(String fileName) {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split(",");
                if (p.length < 4) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                String name = p[0].trim();
                String email = p[1].trim();

                int userId;
                try {
                    userId = Integer.parseInt(p[2].trim());
                } catch (NumberFormatException nfe) {
                    System.out.println("Skipping invalid userId: " + p[2]);
                    continue;
                }

                String type = p[3].trim();

                switch (type) {
                    case "Student":
                    case "student": users.add(new Student(name, email, userId));
                        break;
                    case "Staff":
                    case "staff": users.add(new Staff(name, email, userId));
                        break;
                    case "Guest":
                    case "guest": users.add(new Guest(name, email, userId));
                        break;
                    default: System.out.println("Skipping unknown type: " + type);

                }
            }

            System.out.println("Loaded " + users.size() + " users");

        } catch (Exception e) {
            System.out.println("Error loading " + fileName + ": " + e.getMessage());
        }

        return users;
    }


    public static List<Event> loadEvents(String fileName) {
        List<Event> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                String eventId = p[0].trim();
                String title = p[1].trim();
                LocalDateTime dateTime = LocalDateTime.parse(p[2].trim());
                String location = p[3].trim();
                int capacity = Integer.parseInt(p[4].trim());
                String status = p[5].trim();
                String type = p[6].trim();

                switch (type) {
                    case "Workshop" ->
                            events.add(new Workshop(eventId, title, dateTime, location, capacity, p[7].trim()));

                    case "Seminar" ->
                            events.add(new Seminar(eventId, title, dateTime, location, capacity, p[8].trim()));

                    case "Concert" ->
                            events.add(new Concert(eventId, title, dateTime, location, capacity, p[9].trim()));
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading " + fileName);
        }

        System.out.println("Loaded " + events.size() + " events");

        return events;
    }


    public static List<Booking> loadBookings(String fileName) {
        List<Booking> bookings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] tokens = line.split(",");

                if (tokens.length < 5) continue;

                String bookingId = tokens[0].trim();
                String userId = tokens[1].trim();
                String eventId = tokens[2].trim();
                LocalDateTime createdAt = LocalDateTime.parse(tokens[3].trim());
                String status = tokens[4].trim();

                Booking booking = new Booking(bookingId, userId, eventId, createdAt);
                bookings.add(booking);
                System.out.println("Loaded booking: " + bookingId + " status: " + status);

            }

        } catch (Exception e) {
            System.err.println("Error loading " + fileName);
        }

        System.out.println("Loaded " + bookings.size() + " bookings");

        return bookings;
    }
}
