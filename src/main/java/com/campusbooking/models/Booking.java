package com.campusbooking.models;

import java.time.LocalDateTime;

/* Represents a reservation linking a User to an Event.
 Status values: "Confirmed", "Waitlisted", "Cancelled"
 */
public class Booking {

    private String bookingId;
    private String userId;
    private String eventId;
    private LocalDateTime createdAt;
    private String status; // "Confirmed", "Waitlisted", "Cancelled"

    public Booking(String bookingId, String userId, String eventId, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.userId    = userId;
        this.eventId   = eventId;
        this.createdAt = createdAt;
        this.status    = "Waitlisted";
    }

    public Booking(String bookingId, String userId, String eventId, LocalDateTime createdAt, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String        getBookingId() { return bookingId; }
    public String        getUserId()    { return userId; }
    public String        getEventId()   { return eventId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String        getStatus()    { return status; }

    public void setStatus(String status) { this.status = status; }

    public boolean isConfirmed()  { return "Confirmed".equals(status); }
    public boolean isWaitlisted() { return "Waitlisted".equals(status); }
    public boolean isCancelled()  { return "Cancelled".equals(status); }

    @Override
    public String toString() {
        return "Booking[" + bookingId + "] User:" + userId +
               " Event:" + eventId + " Status:" + status;
    }
}
