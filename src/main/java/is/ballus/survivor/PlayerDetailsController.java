package is.ballus.survivor;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayerDetailsController {

    @FXML
    private TableView<Player> fxTable;
    @FXML
    private Label fxNameLabel;
    @FXML
    private TableColumn<Player, String> fxNameColumn;
    @FXML
    private TableColumn<Player, Integer> fxIncomingOpinion;
    @FXML
    private TableColumn<Player, Integer> fxInOpinionChange;
    @FXML
    private TableColumn<Player, Integer> fxMyActions;
    @FXML
    private TableColumn<Player, Integer> fxOtherActions;
    @FXML
    private TableColumn<Player, Integer> fxPassive;
    @FXML
    private TableColumn<Player, Integer> fxOutgoingOpinion;
    @FXML
    private TableColumn<Player, Integer> fxOutOpinionChange;
    @FXML
    private TableColumn<Player, Player> fxFavoritePlayer;
    @FXML
    private TableColumn<Player, Integer> fxDifferenceFromFavorite;
    @FXML
    private TableColumn<Player, Player> fxChosenPlayer;
    @FXML
    private TableColumn<Player, Integer> fxDifferenceFromChosen;
    @FXML
    private TableColumn<Player, Integer> fxPlacement;
    @FXML
    private TableColumn<Player, Integer> fxPraiseSumPreview;
    @FXML
    private TableColumn<Player, Integer> fxCriticizeSumPreview;
    @FXML
    private TableColumn<Player, Integer> fxPraiseSumRemainingPreview;
    @FXML
    private TableColumn<Player, Integer> fxCriticizeSumRemainingPreview;
    @FXML
    private TableColumn<Player, Integer> fxInfluenceForMe;
    @FXML
    private TableColumn<Player, Integer> fxInfluenceForMeRemaining;
    @FXML
    private TableColumn<Player, Integer> fxHeadToHeadScore;
    @FXML
    private TableColumn<Player, Integer> fxHeadToHeadRemaining;


    private ObservableList<Player> otherPlayers;
    private ObservableList<Player> allPlayers;
    private Player selectedPlayer;
    private MainMenuController mainMenuController;
    private Scene mainMenuScene;

    @FXML
    public void initialize() {
        fxNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxFavoritePlayer.setCellValueFactory(new PropertyValueFactory<>("favoriteRemainingPlayerAsString"));
        fxChosenPlayer.setCellValueFactory(new PropertyValueFactory<>("chosenPlayerAsString"));
        fxPlacement.setCellValueFactory(new PropertyValueFactory<>("placement"));

        fxIncomingOpinion.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = getIncomingOpinion(selectedPlayer, otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxOutOpinionChange.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = otherPlayer.getInRelationshipsChange(selectedPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxMyActions.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getInRelationshipsChangeMyActions(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxOtherActions.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getInRelationshipsChangeOtherPlayerActions(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxPassive.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getInRelationshipsChangePassive(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxOutgoingOpinion.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = getOutgoingOpinion(selectedPlayer, otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxInOpinionChange.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = otherPlayer.getOutRelationshipsChange(selectedPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxDifferenceFromFavorite.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = getDifferenceFromFavorite(selectedPlayer, otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxDifferenceFromChosen.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = getDifferenceFromChosen(selectedPlayer, otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxPraiseSumPreview.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getPraiseSumPreview(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxCriticizeSumPreview.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getCriticizeSumPreview(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxPraiseSumRemainingPreview.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getPraiseRemainingSumPreview(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxCriticizeSumRemainingPreview.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = selectedPlayer.getCriticizeRemainingSumPreview(otherPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxInfluenceForMe.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = otherPlayer.getInfluenceForMe(selectedPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxInfluenceForMeRemaining.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int relationshipValue = otherPlayer.getInfluenceForMeRemaining(selectedPlayer);
            return new ReadOnlyIntegerWrapper(relationshipValue).asObject();
        });

        fxHeadToHeadScore.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int headToHeadValue = selectedPlayer.getHeadToHeadScoreWithPlayer(otherPlayer);
            return new ReadOnlyIntegerWrapper(headToHeadValue).asObject();
        });

        fxHeadToHeadRemaining.setCellValueFactory(cellData -> {
            Player otherPlayer = cellData.getValue();
            int headToHeadValue = selectedPlayer.getHeadToHeadScoreRemainingWithPlayer(otherPlayer);
            return new ReadOnlyIntegerWrapper(headToHeadValue).asObject();
        });


        fxTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getPlacement() == 1) {
                        setStyle("-fx-background-color: rgba(0, 255, 0, 0.2)");
                    } else if (item.isEliminated()){
                        setStyle("-fx-background-color: rgba(255, 0, 0, 0.2)");
                    } else {
                        setStyle("");
                    }
                }

                setOnMouseClicked(event -> {
                    setStyle("-fx-background-color: -fx-selection-bar");
                    if (event.getClickCount() == 2 && !isEmpty()) {
                        Player rowData = getItem();
                        showPlayerDetails(rowData);
                    }
                });
            }
        });
    }
    private void showPlayerDetails(Player selectedPlayer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playerMenu-view.fxml"));
            Parent playerDetailsRoot = loader.load();

            PlayerDetailsController controller = loader.getController();
            controller.setPlayerAndOthers(selectedPlayer, allPlayers);
            controller.setMainMenuController(mainMenuController);
            controller.saveManeMenuScene(mainMenuScene);

            Stage stage = (Stage) fxTable.getScene().getWindow();
            Scene scene = new Scene(playerDetailsRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerAndOthers(Player player, ObservableList<Player> allPlayers) {
        fxNameLabel.setText("Name: " + player.getName());
        this.selectedPlayer = player;
        this.allPlayers = allPlayers;
        List<Player> otherPlayers = allPlayers.stream()
               .filter(p -> !p.equals(player))
               .toList();
        this.otherPlayers = FXCollections.observableArrayList(otherPlayers);
        fxTable.setItems(this.otherPlayers);
    }

    public void saveManeMenuScene(Scene s) {
        this.mainMenuScene = s;
    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    private int getIncomingOpinion(Player selectedPlayer, Player otherPlayer)
    {
        return selectedPlayer.getOpinionOfMe(otherPlayer);
    }

    private int getOutgoingOpinion(Player selectedPlayer, Player otherPlayer)
    {
        return selectedPlayer.getOpinionOf(otherPlayer);
    }

    private int getDifferenceFromFavorite(Player selectedPlayer, Player otherPlayer)
    {
        return selectedPlayer.getDifferenceOfOpinionFromFavorite(otherPlayer);
    }

    private int getDifferenceFromChosen(Player selectedPlayer, Player otherPlayer)
    {
        return selectedPlayer.getDifferenceOfOpinionFromChosen(otherPlayer);
    }

    @FXML
    private void goBackToMainMenu(ActionEvent event) {
        try {
            // Reload data using the existing instance
            mainMenuController.reloadData();

            // Close the player details view and switch back to the main menu view
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene); // Close the player details view without creating a new instance of the main menu
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
