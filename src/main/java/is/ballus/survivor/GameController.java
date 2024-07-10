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
    GameManager partialTurn;
    MainMenuController mainMenuController;





    public GameController(Stage stage, Settings settings) throws IOException {
        this.settings = settings;
        FXMLLoader fxmlLoader = new FXMLLoader(SurvivorApplication.class.getResource("mainMenu-view.fxml"));
        Parent root = fxmlLoader.load();
        mainMenuController = fxmlLoader.getController();
        gameManager = new GameManager(settings);
        gameManagerPrediction = new GameManager(gameManager);
        gameManagerPreview = new GameManager(gameManager);
        partialTurn = new GameManager(gameManager);
        assignStrategies();
        gameManagerPrediction.simulateRound(0, true);
        firstRoundPlacements();
        System.out.println("búbb");
        mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers(), partialTurn.getPlayers());
        mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview, partialTurn);
        mainMenuController.setGameController(this);
        Scene scene = new Scene(root, 1820, 980);
        mainMenuController.saveScene(scene);
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
            this.gameManagerPreview.simulateRound(0, true);
        }
        mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers(), partialTurn.getPlayers());
        mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview, partialTurn);
        mainMenuController.reloadData();
    }

    public void endTurn(Player humanPlayer, Player praisedPlayer, Player criticizedPlayer, boolean simulateAll, int numOfActions) {
        if (!simulateAll) {
            partialTurn = new GameManager(gameManager);
            partialTurn.simulateRound(numOfActions, false);
            mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers(), partialTurn.getPlayers());
            mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview, partialTurn);
            mainMenuController.reloadData();
        } else {
            if (gameManager.remainingPlayers.size() > 1) {
                if (praisedPlayer != null && criticizedPlayer != null) {
                    gameManager.setPlayerActions(humanPlayer, praisedPlayer, criticizedPlayer);
                }
                partialTurn = new GameManager(gameManager);
                gameManager.simulateRound(0, true);
            }
            if (gameManagerPrediction.remainingPlayers.size() > 1) {
                this.gameManagerPrediction = new GameManager(gameManager);
                gameManagerPrediction.simulateRound(0, true);
            }
            mainMenuController.setPlayers(gameManager.getPlayers(), gameManagerPrediction.getPlayers(), gameManagerPreview.getPlayers(), partialTurn.getPlayers());
            mainMenuController.setGameManagers(gameManager, gameManagerPrediction, gameManagerPreview, partialTurn);
            mainMenuController.reloadData();
        }
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
