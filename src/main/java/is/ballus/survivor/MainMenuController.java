package is.ballus.survivor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private TableView<Player> fxTable;
    @FXML
    private TableColumn<Player, String> fxNameColumn;
    @FXML
    private TableColumn<Player, Integer> fxOverallOpinionColumn;
    @FXML
    private TableColumn<Player, Integer> fxInfluence;
    @FXML
    private TableColumn<Player, Integer> fxInfluenceRemaining;
    @FXML
    private TableColumn<Player, Integer> fxPlacement;
    @FXML
    private TableColumn<Player, Integer> fxFinalVotes;
    @FXML
    private TableColumn<Player, Integer> fxHeadToHead;
    @FXML
    private TableColumn<Player, Integer> fxNominations;
    @FXML
    private TableColumn<Player, Integer> fxVotes;
    @FXML
    private TableColumn<Player, Boolean> fxEliminatedColumn;
    @FXML
    private TableColumn<Player, String> fxPraisedPlayer;
    @FXML
    private TableColumn<Player, String> fxCriticizedPlayer;


    @FXML
    private TableView<Player> fxTablePrediction;
    @FXML
    private TableColumn<Player, String> fxNameColumnPrediction;
    @FXML
    private TableColumn<Player, Integer> fxOverallOpinionColumnPrediction;
    @FXML
    private TableColumn<Player, Integer> fxInfluencePrediction;
    @FXML
    private TableColumn<Player, Integer> fxInfluenceRemainingPrediction;
    @FXML
    private TableColumn<Player, Integer> fxPlacementPrediction;
    @FXML
    private TableColumn<Player, Integer> fxFinalVotesPrediction;
    @FXML
    private TableColumn<Player, Integer> fxHeadToHeadPrediction;
    @FXML
    private TableColumn<Player, Integer> fxNominationsPrediction;
    @FXML
    private TableColumn<Player, Integer> fxVotesPrediction;
    @FXML
    private TableColumn<Player, Boolean> fxEliminatedColumnPrediction;
    @FXML
    private TableColumn<Player, String> fxPraisedPlayerPrediction;
    @FXML
    private TableColumn<Player, String> fxCriticizedPlayerPrediction;

    @FXML
    private TableView<Player> fxTablePreview;
    @FXML
    private TableColumn<Player, String> fxNameColumnPreview;
    @FXML
    private TableColumn<Player, Integer> fxOverallOpinionColumnPreview;
    @FXML
    private TableColumn<Player, Integer> fxInfluencePreview;
    @FXML
    private TableColumn<Player, Integer> fxInfluenceRemainingPreview;
    @FXML
    private TableColumn<Player, Integer> fxPlacementPreview;
    @FXML
    private TableColumn<Player, Integer> fxFinalVotesPreview;;
    @FXML
    private TableColumn<Player, Integer> fxHeadToHeadPreview;
    @FXML
    private TableColumn<Player, Integer> fxNominationsPreview;
    @FXML
    private TableColumn<Player, Integer> fxVotesPreview;
    @FXML
    private TableColumn<Player, Boolean> fxEliminatedColumnPreview;
    @FXML
    private TableColumn<Player, String> fxPraisedPlayerPreview;
    @FXML
    private TableColumn<Player, String> fxCriticizedPlayerPreview;

    @FXML
    private Label fxPraiseLabel;
    @FXML
    private Label fxCriticizeLabel;

    private ObservableList<Player> playerList;
    private ObservableList<Player> playerListPrediction;
    private ObservableList<Player> playerListPreview;
    private Scene thisScene;
    private GameManager gameManager;
    private GameManager gameManagerPrediction;
    private GameManager gameManagerPreview;
    private Player humanPlayer;
    private Player praisedPlayer;
    private Player criticizedPlayer;
    private GameController gameController;

    public void setPlayers(Player[] players, Player[] playersPrediction, Player[] playersPreview) {
        this.playerList = FXCollections.observableArrayList(players);
        fxTable.setItems(this.playerList);
        this.playerListPrediction = FXCollections.observableArrayList(playersPrediction);
        fxTablePrediction.setItems(this.playerListPrediction);
        this.playerListPreview = FXCollections.observableArrayList(playersPreview);
        fxTablePreview.setItems(this.playerListPreview);
    }

    public void setGameManagers(GameManager manager, GameManager managerPrediction, GameManager previewManager) {
        this.gameManager = manager;
        this.gameManagerPrediction = managerPrediction;
        this.gameManagerPreview = previewManager;
        this.humanPlayer = manager.getHumanPlayer();

    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void saveScene(Scene s) {
        this.thisScene = s;
    }

    @FXML
    public void initialize() {
        fxNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxOverallOpinionColumn.setCellValueFactory(new PropertyValueFactory<>("inRelationSum"));
        fxInfluence.setCellValueFactory(new PropertyValueFactory<>("influence"));
        fxInfluenceRemaining.setCellValueFactory(new PropertyValueFactory<>("influenceRemaining"));
        fxPlacement.setCellValueFactory(new PropertyValueFactory<>("placement"));
        fxFinalVotes.setCellValueFactory(new PropertyValueFactory<>("finalVotes"));
        fxHeadToHead.setCellValueFactory(new PropertyValueFactory<>("headToHeadWins"));
        fxNominations.setCellValueFactory(new PropertyValueFactory<>("nominations"));
        fxVotes.setCellValueFactory(new PropertyValueFactory<>("votes"));
        fxEliminatedColumn.setCellValueFactory(new PropertyValueFactory<>("eliminated"));
        fxPraisedPlayer.setCellValueFactory(new PropertyValueFactory<>("praisedPlayerAsString"));
        fxCriticizedPlayer.setCellValueFactory(new PropertyValueFactory<>("criticizedPlayerAsString"));

        // Sort by placement by default
        fxPlacement.setSortType(TableColumn.SortType.ASCENDING); // or DESCENDING
        fxTable.getSortOrder().add(fxPlacement);

        fxTable.setRowFactory(tv -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Player rowData = row.getItem();
                    showPlayerDetails(rowData, playerList, fxTable);
                }
            });
            return row;
        });

        fxNameColumnPrediction.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxOverallOpinionColumnPrediction.setCellValueFactory(new PropertyValueFactory<>("inRelationSum"));
        fxInfluencePrediction.setCellValueFactory(new PropertyValueFactory<>("influence"));
        fxInfluenceRemainingPrediction.setCellValueFactory(new PropertyValueFactory<>("influenceRemaining"));
        fxPlacementPrediction.setCellValueFactory(new PropertyValueFactory<>("placement"));
        fxFinalVotesPrediction.setCellValueFactory(new PropertyValueFactory<>("finalVotes"));
        fxHeadToHeadPrediction.setCellValueFactory(new PropertyValueFactory<>("headToHeadWins"));
        fxNominationsPrediction.setCellValueFactory(new PropertyValueFactory<>("nominations"));
        fxVotesPrediction.setCellValueFactory(new PropertyValueFactory<>("votes"));
        fxEliminatedColumnPrediction.setCellValueFactory(new PropertyValueFactory<>("eliminated"));
        fxPraisedPlayerPrediction.setCellValueFactory(new PropertyValueFactory<>("praisedPlayerAsString"));
        fxCriticizedPlayerPrediction.setCellValueFactory(new PropertyValueFactory<>("criticizedPlayerAsString"));

        // Sort by placement by default
        fxPlacementPrediction.setSortType(TableColumn.SortType.ASCENDING); // or DESCENDING
        fxTablePrediction.getSortOrder().add(fxPlacementPrediction);

        fxTablePrediction.setRowFactory(tv -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Player rowData = row.getItem();
                    showPlayerDetails(rowData, playerListPrediction, fxTablePrediction);
                }
            });
            return row;
        });

        fxNameColumnPreview.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxOverallOpinionColumnPreview.setCellValueFactory(new PropertyValueFactory<>("inRelationSum"));
        fxInfluencePreview.setCellValueFactory(new PropertyValueFactory<>("influence"));
        fxInfluenceRemainingPreview.setCellValueFactory(new PropertyValueFactory<>("influenceRemaining"));
        fxPlacementPreview.setCellValueFactory(new PropertyValueFactory<>("placement"));
        fxFinalVotesPreview.setCellValueFactory(new PropertyValueFactory<>("finalVotes"));
        fxHeadToHeadPreview.setCellValueFactory(new PropertyValueFactory<>("headToHeadWins"));
        fxNominationsPreview.setCellValueFactory(new PropertyValueFactory<>("nominations"));
        fxVotesPreview.setCellValueFactory(new PropertyValueFactory<>("votes"));
        fxEliminatedColumnPreview.setCellValueFactory(new PropertyValueFactory<>("eliminated"));
        fxPraisedPlayerPreview.setCellValueFactory(new PropertyValueFactory<>("praisedPlayerAsString"));
        fxCriticizedPlayerPreview.setCellValueFactory(new PropertyValueFactory<>("criticizedPlayerAsString"));

        // Sort by placement by default
        fxPlacementPreview.setSortType(TableColumn.SortType.ASCENDING); // or DESCENDING
        fxTablePreview.getSortOrder().add(fxPlacementPreview);

        fxTablePreview.setRowFactory(tv -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Player rowData = row.getItem();
                    showPlayerDetails(rowData, playerListPreview, fxTablePreview);
                }
            });
            return row;
        });
    }
    private void showPlayerDetails(Player selectedPlayer, ObservableList<Player> otherPlayers, TableView<Player> table) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playerMenu-view.fxml"));
            Parent playerDetailsRoot = loader.load();

            PlayerDetailsController controller = loader.getController();
            //List<Player> otherPlayers = playerList.stream()
            //        .filter(player -> !player.equals(selectedPlayer))
            //        .toList();
            controller.setPlayerAndOthers(selectedPlayer, otherPlayers);
            controller.setMainMenuController(this);
            controller.saveManeMenuScene(thisScene);

            Stage stage = (Stage) table.getScene().getWindow();
            Scene scene = new Scene(playerDetailsRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void simulateRound() {
        gameController.simulateRound(humanPlayer, praisedPlayer, criticizedPlayer);
    }

    @FXML
    public void endTurn() {
        gameController.endTurn(humanPlayer, praisedPlayer, criticizedPlayer);
        fxPraiseLabel.setText("No one");
        this.praisedPlayer = null;
        fxCriticizeLabel.setText("No one");
        this.criticizedPlayer = null;
    }

    public void reloadData() {
        System.out.println("initialize()" + playerList.size());
        initialize();
    }


    @FXML
    public void selectPraise(ActionEvent event) {
        Player selectedPlayer = fxTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null && humanPlayer.canPraise(selectedPlayer)) {
            fxPraiseLabel.setText(selectedPlayer.getName());
            this.praisedPlayer = selectedPlayer;
        }
    }

    @FXML
    public void selectCriticize(ActionEvent event) {
        Player selectedPlayer = fxTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null && humanPlayer.canCriticize(selectedPlayer)) {
            fxCriticizeLabel.setText(selectedPlayer.getName());
            this.criticizedPlayer = selectedPlayer;
        }
    }



}
