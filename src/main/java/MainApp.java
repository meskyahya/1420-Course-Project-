import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Shared backend
        EventManager eventManager = new EventManager();
        BookingManager bookingManager = new BookingManager();

        // User Management tab
        UserController userController = new UserController();
        Tab userTab = new Tab("User Management");
        userTab.setContent(userController.getView());

        // Event Management tab
        EventController eventController = new EventController(eventManager);
        Tab eventTab = new Tab("Event Management");
        eventTab.setContent(eventController.getView());

        // Booking Management tab
        BookingView bookingView = new BookingView();
        BookingGUIController bookingController = new BookingGUIController(
            bookingManager,
            bookingView,
            userController.getUsers(),
            eventManager.getAllEvents()
        );
        Tab bookingTab = new Tab("Booking Management");
        bookingTab.setContent(bookingController.getView());

        // Waitlist Management tab
        WaitlistController waitlistController = new WaitlistController();
        waitlistController.setEvents(eventManager.getAllEvents());
        Tab waitlistTab = new Tab("Waitlist Management");
        waitlistTab.setContent(waitlistController.getView());

        tabPane.getTabs().addAll(userTab, eventTab, bookingTab, waitlistTab);

        Scene scene = new Scene(tabPane, 850, 600);
        primaryStage.setTitle("Campus Event Booking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
