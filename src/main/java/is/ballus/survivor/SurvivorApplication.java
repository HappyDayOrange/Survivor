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
        FXMLLoader fxmlLoader = new FXMLLoader(SurvivorApplication.class.getResource("mainMenu-view.fxml"));
        Parent root = fxmlLoader.load();
        MainMenuController controller = fxmlLoader.getController();
        GameManager gameManager = new GameManager(11);
        GameManager gameManagerPrediction = new GameManager(gameManager);
        GameManager gameManagerPreview = new GameManager(gameManager);
        gameManagerPrediction.simulateRound();
        System.out.println("búbb");
        controller.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers());
        controller.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview);
        Scene scene = new Scene(root, 1820, 980);
        controller.saveScene(scene);
        stage.setTitle("Survivor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}