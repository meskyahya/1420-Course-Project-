public class EventBookingSystem {
    private EventManager eventManager;
    private BookingManager bookingManager;
    private WaitlistManager waitlistManager;

    public EventBookingSystem() {
        this.eventManager = new EventManager();
        this.bookingManager = new BookingManager();
        this.waitlistManager = new WaitlistManager();
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public BookingManager getBookingManager() {
        return bookingManager;
    }

    public WaitlistManager getWaitlistManager() {
        return waitlistManager;
    }
}
//contentArea.getChildren().setAll(new EventView(system.getEventManager()));