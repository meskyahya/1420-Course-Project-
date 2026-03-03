import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

//Launches a tabbed window with all four modules.
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // User Management tab (teammate fills in)
        Tab userTab = new Tab("User Management");
        userTab.setContent(placeholder("User Management - Coming Soon"));

        // Event Management tab (teammate fills in)
        Tab eventTab = new Tab("Event Management");
        eventTab.setContent(placeholder("Event Management - Coming Soon"));

        // Booking Management tab (teammate fills in)
        Tab bookingTab = new Tab("Booking Management");
        bookingTab.setContent(placeholder("Booking Management - Coming Soon"));

        // Waitlist Management tab (fully implemented)
        List<Event> events = new ArrayList<>();
        // TODO: replace with real events from EventManager once integrated
        // Example: events = eventManager.getAllEvents();

        WaitlistController waitlistController = new WaitlistController();
        waitlistController.setEvents(events);

        Tab waitlistTab = new Tab("Waitlist Management");
        waitlistTab.setContent(waitlistController.getView());

        tabPane.getTabs().addAll(userTab, eventTab, bookingTab, waitlistTab);

        Scene scene = new Scene(tabPane, 850, 600);
        primaryStage.setTitle("Campus Event Booking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Simple placeholder for unfinished tabs. */
    private StackPane placeholder(String message) {
        StackPane pane = new StackPane();
        Label label = new Label(message);
        label.setFont(Font.font("System", 16));
        label.setStyle("-fx-text-fill: grey;");
        pane.getChildren().add(label);
        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
