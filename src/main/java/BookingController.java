//console based
import java.util.List;
import java.util.Scanner;
public class BookingController {
    private BookingManager bookingManager;
    private EventManager eventManager;

    public BookingController(BookingManager bookingManager, EventManager eventManager) {
        this.bookingManager = bookingManager;
        this.eventManager = eventManager;
    }

    public void bookingMenu(Scanner sc, List<User> users){
        boolean done = false;

        while(!done){
            System.out.println("\n ===Booking Menu ===");
            System.out.println("1. Book Event");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Booking History");
            System.out.println("4. View Event Options");
            System.out.println("5. Back");
            System.out.print("Please enter your choice: ");

            String choice = sc.nextLine();

            if (choice.equals("1")){
                bookEventFlow(sc, users);
            } else if(choice.equals("2")){
                cancelBookingFlow(sc);
            } else if(choice.equals("3")){
                viewUserBookingsFlow(sc);
            } else if(choice.equals("4")){
                viewEventRosterFlow(sc);
            } else if(choice.equals("5")){
                done = true;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    private void bookEventFlow(Scanner sc, List<User> users){
        System.out.println("Enter userId (int): ");
        int userId = readInt(sc);

        System.out.println("Enter eventId (int): ");
        String eventId = sc.nextLine().trim();

        User user = findUser(users, userId);
        if(user == null){
            System.out.println("User not found" + userId);
            return;
        }

        Event event;
        try{
            event = eventManager.getEvent(eventId);
        } catch(Exception e){
            System.out.println("Event not found" + eventId);
            return;
        }
        try{
            Booking booking = bookingManager.bookEvent(user, event);
            System.out.println("Booked" + booking);
        } catch(Exception e){
            System.out.println("Booking failed" + e.getMessage());
        }
    }

    private void cancelBookingFlow(Scanner sc){
        System.out.println("Enter event Id: ");
        String eventId = sc.nextLine().trim();

        Event event;
        try{
            event = eventManager.getEvent(eventId);
        } catch(Exception e){
            System.out.println("Event not found" + eventId);
            return;
        }

        System.out.println("Enter booking Id: ");
        String bookingId = sc.nextLine().trim();

        try{
            bookingManager.cancelBooking(event, bookingId);
            System.out.println("Booking has been cancelled" + bookingId);
        } catch(Exception e){
            System.out.println("Cancel failed" + e.getMessage());
        }
    }

    private void viewUserBookingsFlow(Scanner sc) {
        System.out.println("Enter userId(int): ");
        int userId = readInt(sc);

        List<Booking> bookings = bookingManager.getBookingsForUser(userId);

        if (bookings.size() == 0) {
            System.out.println("No bookings found for user " + userId);
            return;
        }

        System.out.println("Bookings for user" + userId + ":");
        for (int i = 0; i < bookings.size(); i++) {
            System.out.println(bookings.get(i));
        }
    }

    private void viewEventRosterFlow(Scanner sc) {
        System.out.println("Enter event Id: ");
        String eventId = sc.nextLine().trim();

        Event event;
        try{
            event = eventManager.getEvent(eventId);
        } catch(Exception e){
            System.out.println("Event not found" + eventId);
            return;
        }
        System.out.println("\nEvent: " + event.getTitle() + "(" + event.getEventId() + ")");
        System.out.println("Status: " + event.getStatus() + "Capacity: " + event.getCapacity());

        List<Booking> confirmed = bookingManager.getConfirmedRoster(event);
        List<Booking> waitlist = bookingManager.getWaitlistRoster(event);

        System.out.println("\nConfirmed (" + confirmed.size() + "):");
        for (int i = 0; i < confirmed.size(); i++) {
            Booking b = confirmed.get(i);
            System.out.println(" - " + b.getBookingId() + "user =" + b.getUserId() + "|" + b.getStatus());
        }

        System.out.println("\nWaitlist (" + waitlist.size() + "):");
        for (int i = 0; i < waitlist.size(); i++) {
            Booking b = waitlist.get(i);
            System.out.println(" " + (i + 1) + ")" + b.getBookingId() + "user =" + b.getUserId() + "|" + b.getStatus() + "user=" + b.getUserId() + "|" + b.getStatus());
        }
    }

    private User findUser(List<User> users, int userId){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getuserID() == userId){
                return users.get(i);
            }
        }
        return null;
    }

    private int readInt(Scanner sc){
        while (true){
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            }  catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }
    }
}
