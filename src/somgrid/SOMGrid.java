package somgrid;
import javafx.application.Application;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class SOMGrid extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            ScrollPane root =
                    (ScrollPane)loader.load(getClass().getResource("SOMGrid.fxml").openStream());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            root.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
