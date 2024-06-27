package is.ballus.survivor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SurvivorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Settings settings = new Settings();
        GameController gameController = new GameController(stage, settings);
    }

    public static void main(String[] args) {
        launch();
    }
}