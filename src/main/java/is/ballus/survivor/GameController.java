package is.ballus.survivor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    Settings settings;
    GameManager gameManager;
    GameManager gameManagerPrediction;
    GameManager gameManagerPreview;
    MainMenuController mainMenuController;





    public GameController(Stage stage, Settings settings) throws IOException {
        this.settings = settings;
        FXMLLoader fxmlLoader = new FXMLLoader(SurvivorApplication.class.getResource("mainMenu-view.fxml"));
        Parent root = fxmlLoader.load();
        mainMenuController = fxmlLoader.getController();
        gameManager = new GameManager(settings);
        gameManagerPrediction = new GameManager(gameManager);
        gameManagerPreview = new GameManager(gameManager);
        assignStrategies();
        gameManagerPrediction.simulateRound();
        firstRoundPlacements();
        System.out.println("búbb");
        mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers());
        mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview);
        mainMenuController.setGameController(this);
        mainMenuController.initialize();
        Scene scene = new Scene(root, 1820, 980);
        mainMenuController.saveScene(scene);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Survivor");
        stage.setScene(scene);
        stage.show();
    }

    public void simulateRound(Player humanPlayer, Player praisedPlayer, Player criticizedPlayer) {
        if (gameManagerPreview.remainingPlayers.size() > 1) {
            this.gameManagerPreview = new GameManager(gameManager);
            if (praisedPlayer != null && criticizedPlayer != null) {
                this.gameManagerPreview.setPlayerActions(humanPlayer, praisedPlayer, criticizedPlayer);
            }
            this.gameManagerPreview.simulateRound();
        }
        mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers());
        mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview);
        mainMenuController.reloadData();
    }

    public void endTurn(Player humanPlayer, Player praisedPlayer, Player criticizedPlayer) {
        if (gameManager.remainingPlayers.size() > 1) {
            if (praisedPlayer != null && criticizedPlayer != null) {
                gameManager.setPlayerActions(humanPlayer, praisedPlayer, criticizedPlayer);
            }
            gameManager.simulateRound();
        }
        if (gameManagerPrediction.remainingPlayers.size() > 1) {
            this.gameManagerPrediction = new GameManager(gameManager);
            gameManagerPrediction.simulateRound();
        }
        this.gameManagerPreview = new GameManager(gameManager);
        mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers());
        mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview);
        mainMenuController.reloadData();
    }

    public void assignStrategies() {
        AIRoundStrategy roundStrategy = new AIRoundStrategy(gameManager, gameManagerPrediction);
        for (Player player : gameManager.playerArr) {
            if (player.isReal && !player.isHuman) {
                AIOverallStrategy strategy = new AIOverallStrategy(gameManager, gameManagerPrediction, roundStrategy, player, settings.getPlayerStrategy(player.getPlayerIndex()));
                player.setStrategy(strategy);
            }
        }
    }

    public void firstRoundPlacements() {
        for (Player player : gameManager.playerArr) {
            player.setPlacement(gameManagerPrediction.playerArr[player.getPlayerIndex()].getPlacement());
        }
        gameManager.playersPickingActions();
    }


}
