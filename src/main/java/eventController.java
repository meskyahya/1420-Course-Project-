public class MainApp extends Application {

    private EventBookingSystem system = new EventBookingSystem();
    private StackPane contentArea = new StackPane();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        VBox navigation = new VBox(10);
        navigation.setPrefWidth(200);

        Button userBtn = new Button("User Management");
        Button eventBtn = new Button("Event Management");
        Button bookingBtn = new Button("Booking Management");
        Button waitlistBtn = new Button("Waitlist Management");

        navigation.getChildren().addAll(userBtn, eventBtn, bookingBtn, waitlistBtn);
        root.setLeft(navigation);
        root.setCenter(contentArea);

        // Connect buttons to views
        userBtn.setOnAction(e -> contentArea.getChildren().setAll(new UserView(system)));
        eventBtn.setOnAction(e -> contentArea.getChildren().setAll(new EventView(system)));
        bookingBtn.setOnAction(e -> contentArea.getChildren().setAll(new BookingView(system)));
        waitlistBtn.setOnAction(e -> contentArea.getChildren().setAll(new WaitlistView(system)));

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Campus Event Booking System");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}