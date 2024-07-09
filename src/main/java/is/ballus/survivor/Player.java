package is.ballus.survivor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Player {
    public int playerIndex;
    public boolean isReal = false;
    public boolean isHuman = false;
    public AIOverallStrategy strategy;
    private int roundBorn;
    private BooleanProperty eliminated = new SimpleBooleanProperty(false);
    private int numPlayers;
    public ArrayList<Player> playerList;
    private Player[] playerArr;
    private int placement = -1;
    public int[] outRelationships;
    public int[] inRelationships;
    public int[] outRelationshipsChange;
    public int[] inRelationshipsChange;
    public int[] inRelationshipsChangePassive;
    public int[] inRelationshipsChangeMyActions;
    public int[] inRelationshipsChangeOtherPlayerActions;
    public int[] outPraisePreview;
    public int[] inPraisePreview;
    public int[] outCriticizePreview;
    public int[] inCriticizePreview;
    public int[] inPraiseRemainingPreview;
    public int[] inCriticizeRemainingPreview;
    private int[] praisePreviewInSum;
    private int[] criticizePreviewInSum;
    private int[] praisePreviewInRemainingSum;
    private int[] criticizePreviewInRemainingSum;
    private int[] differenceOfOpinionFromFavorite;
    private int[] differenceOfOpinionFromChosenPlayer;
    private int[] headToHeadScores;
    private int[] headToHeadScoresRemaining;
    private int headToHeadWins;
    private int headToHeadWinDifferenceRemaining;
    private int headToHeadWinSum;
    private int headToHeadWinSumRemaining;
    public int inRelationSum;
    public int outRelationSum;
    private int influence;
    private int influenceRemaining;
    private int[] influenceForMe;
    private int[] influenceForMeRemaining;
    private List<Player> supports = new ArrayList<Player>();
    private List<Player> opposes = new ArrayList<Player>();
    private List<Player> supporters = new ArrayList<Player>();
    private List<Player> opponents = new ArrayList<Player>();
    private List<Player> allies = new ArrayList<Player>();
    private List<Player> enemies = new ArrayList<Player>();
    private List<Player> neutrals = new ArrayList<Player>();
    private int nominations = 0;
    private int votes = 0;
    private int finalVotes = 0;
    public String playerName;
    private List<Player> generateHelper = new ArrayList<Player>();
    private ArrayList<Player> errorListPlayers = new ArrayList<Player>();
    private ArrayList<Integer> errorListValues = new ArrayList<Integer>();
    private int numErrors = 0;
    private List<List<Player>> incomingRelationshipStatus = new ArrayList<>();
    private List<List<Player>> outcomingRelationshipStatus = new ArrayList<>();
    private Player favoriteRemainingPlayer;
    private Player secondFavoriteRemainingPlayer;
    private Player chosenPlayer;
    private Player closestFromBeingChosen;
    private Player praisedPlayer;
    private Player criticizedPlayer;
    private boolean[] hasBeenPickedAsNumber = new boolean[9];



    public Player(int index, int numPlayers, int roundNum, Settings settings, ArrayList<Player> playerList, Player[] playerArr) {
        this.playerIndex = index;
        this.isReal = true;
        this.roundBorn = roundNum;
        this.playerList = playerList;
        this.playerArr = playerArr;
        this.numPlayers = numPlayers;
        this.placement = numPlayers;
        this.outRelationships = new int[numPlayers];
        this.inRelationships = new int[numPlayers];
        this.outPraisePreview = new int[numPlayers];
        this.inPraisePreview = new int[numPlayers];
        this.inPraiseRemainingPreview = new int[numPlayers];
        this.inCriticizeRemainingPreview = new int[numPlayers];
        this.outCriticizePreview = new int[numPlayers];
        this.inCriticizePreview = new int[numPlayers];
        this.praisePreviewInSum = new int[numPlayers];
        this.criticizePreviewInSum = new int[numPlayers];
        this.praisePreviewInRemainingSum = new int[numPlayers];
        this.criticizePreviewInRemainingSum = new int[numPlayers];
        this.outRelationshipsChange = new int[numPlayers];
        this.inRelationshipsChange = new int[numPlayers];
        this.inRelationshipsChangePassive = new int[this.numPlayers];
        this.inRelationshipsChangeMyActions = new int[this.numPlayers];
        this.inRelationshipsChangeOtherPlayerActions = new int[this.numPlayers];
        this.headToHeadScores = new int[numPlayers];
        this.headToHeadScoresRemaining = new int[numPlayers];
        this.influenceForMe = new int[numPlayers];
        this.influenceForMeRemaining = new int[numPlayers];
        this.differenceOfOpinionFromFavorite = new int[numPlayers];
        this.differenceOfOpinionFromChosenPlayer = new int[numPlayers];
        this.inRelationSum = 0;
        this.outRelationSum = 0;
        this.playerName = settings.playerNames[index];
        List<Player> friends = new ArrayList<>();
        List<Player> friendsOut = new ArrayList<>();
        incomingRelationshipStatus.add(friends);
        outcomingRelationshipStatus.add(friendsOut);
        List<Player> allies = new ArrayList<>();
        List<Player> alliesOut = new ArrayList<>();
        incomingRelationshipStatus.add(allies);
        outcomingRelationshipStatus.add(alliesOut);
        List<Player> supporters = new ArrayList<>();
        List<Player> supportersOut = new ArrayList<>();
        incomingRelationshipStatus.add(supporters);
        outcomingRelationshipStatus.add(supportersOut);
        List<Player> backers = new ArrayList<>();
        List<Player> backersOut = new ArrayList<>();
        incomingRelationshipStatus.add(backers);
        outcomingRelationshipStatus.add(backersOut);
        List<Player> neutral = new ArrayList<>();
        List<Player> neutralOut = new ArrayList<>();
        incomingRelationshipStatus.add(neutral);
        outcomingRelationshipStatus.add(neutralOut);
        List<Player> skeptics = new ArrayList<>();
        List<Player> skepticsOut = new ArrayList<>();
        incomingRelationshipStatus.add(skeptics);
        outcomingRelationshipStatus.add(skepticsOut);
        List<Player> opponents = new ArrayList<>();
        List<Player> opponentsOut = new ArrayList<>();
        incomingRelationshipStatus.add(opponents);
        outcomingRelationshipStatus.add(opponentsOut);
        List<Player> rivals = new ArrayList<>();
        List<Player> rivalsOut = new ArrayList<>();
        incomingRelationshipStatus.add(rivals);
        outcomingRelationshipStatus.add(rivalsOut);
        List<Player> enemies = new ArrayList<>();
        List<Player> enemiesOut = new ArrayList<>();
        incomingRelationshipStatus.add(enemies);
        outcomingRelationshipStatus.add(enemiesOut);
        generateHelper.add(this);
    }

    public Player(Player player,int roundNum, ArrayList<Player> playerList, Player[] playerArr) {
        this.playerIndex = player.playerIndex;
        this.roundBorn = roundNum;
        if (player.isEliminated()) {
            this.eliminatePlayer();
        }
        this.playerList = playerList;
        this.playerArr = playerArr;
        this.numPlayers = player.numPlayers;
        this.placement = player.placement;
        this.outRelationships =  player.outRelationships.clone();
        this.inRelationships = player.inRelationships.clone();
        this.outPraisePreview = player.outPraisePreview.clone();
        this.inPraisePreview = player.inPraisePreview.clone();
        this.outCriticizePreview = player.outCriticizePreview.clone();
        this.inCriticizePreview = player.inCriticizePreview.clone();
        this.inPraiseRemainingPreview = player.inPraiseRemainingPreview.clone();
        this.inCriticizeRemainingPreview = player.inCriticizeRemainingPreview.clone();
        this.praisePreviewInSum = player.praisePreviewInSum.clone();
        this.criticizePreviewInSum = player.criticizePreviewInSum.clone();
        this.praisePreviewInRemainingSum = player.praisePreviewInRemainingSum.clone();
        this.criticizePreviewInRemainingSum = player.criticizePreviewInRemainingSum.clone();
        this.outRelationshipsChange =  player.outRelationshipsChange.clone();
        this.inRelationshipsChange = player.inRelationshipsChange.clone();
        this.inRelationshipsChangePassive = player.inRelationshipsChangePassive.clone();
        this.inRelationshipsChangeMyActions = player.inRelationshipsChangeMyActions.clone();
        this.inRelationshipsChangeOtherPlayerActions = player.inRelationshipsChangeOtherPlayerActions.clone();
        this.differenceOfOpinionFromFavorite = player.differenceOfOpinionFromFavorite.clone();
        this.differenceOfOpinionFromChosenPlayer = player.differenceOfOpinionFromChosenPlayer.clone();
        this.headToHeadScores = player.headToHeadScores.clone();
        this.headToHeadScoresRemaining = player.headToHeadScoresRemaining.clone();
        this.influenceForMe =  player.influenceForMe.clone();
        this.influenceForMeRemaining = player.influenceForMeRemaining.clone();
        this.inRelationSum = player.inRelationSum;
        this.outRelationSum = player.outRelationSum;
        this.playerName = player.playerName;
        this.strategy = player.strategy;
        List<Player> friends = new ArrayList<>();
        List<Player> friendsOut = new ArrayList<>();
        incomingRelationshipStatus.add(friends);
        outcomingRelationshipStatus.add(friendsOut);
        List<Player> allies = new ArrayList<>();
        List<Player> alliesOut = new ArrayList<>();
        incomingRelationshipStatus.add(allies);
        outcomingRelationshipStatus.add(alliesOut);
        List<Player> supporters = new ArrayList<>();
        List<Player> supportersOut = new ArrayList<>();
        incomingRelationshipStatus.add(supporters);
        outcomingRelationshipStatus.add(supportersOut);
        List<Player> backers = new ArrayList<>();
        List<Player> backersOut = new ArrayList<>();
        incomingRelationshipStatus.add(backers);
        outcomingRelationshipStatus.add(backersOut);
        List<Player> neutral = new ArrayList<>();
        List<Player> neutralOut = new ArrayList<>();
        incomingRelationshipStatus.add(neutral);
        outcomingRelationshipStatus.add(neutralOut);
        List<Player> skeptics = new ArrayList<>();
        List<Player> skepticsOut = new ArrayList<>();
        incomingRelationshipStatus.add(skeptics);
        outcomingRelationshipStatus.add(skepticsOut);
        List<Player> opponents = new ArrayList<>();
        List<Player> opponentsOut = new ArrayList<>();
        incomingRelationshipStatus.add(opponents);
        outcomingRelationshipStatus.add(opponentsOut);
        List<Player> rivals = new ArrayList<>();
        List<Player> rivalsOut = new ArrayList<>();
        incomingRelationshipStatus.add(rivals);
        outcomingRelationshipStatus.add(rivalsOut);
        List<Player> enemies = new ArrayList<>();
        List<Player> enemiesOut = new ArrayList<>();
        incomingRelationshipStatus.add(enemies);
        outcomingRelationshipStatus.add(enemiesOut);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        inRelationshipsChangePassive = new int[numPlayers];
        inRelationshipsChangeMyActions = new int[numPlayers];
        inRelationshipsChangeOtherPlayerActions = new int[numPlayers];
    }

    public boolean setInitialRelations(List<Player> players) {
        List<Player> tempList = new ArrayList<>(players);
        tempList.remove(this);
        if (tempList.isEmpty()) {
            System.out.println("Error!");
            numErrors++;
            return false;
        }

        int value = -20;
        for (int i = 0; i < 9; i++) {
            Player pick = tempList.get((int) (Math.random() * tempList.size()));
            System.out.println(this.getName() + " picks: " + pick.getName());
            pick.setInRelationships(this, value);
            this.setOutRelationships(pick, value);
            tempList.remove(pick);
            value += 5;
            pick.calculateInRelationSum();
        }
        return true;
    }

    public void setInRelationships(Player player, int value) {
        this.inRelationships[player.getPlayerIndex()] = value;
    }

    public void setOutRelationships(Player player, int value) {
        if (this.outRelationships[player.getPlayerIndex()] != 0) {
            addToErrorList(this.outRelationships[player.getPlayerIndex()]);
            generateHelper.remove(player);
            incrementNumErrors();
            this.outRelationships[player.getPlayerIndex()] = value;

        } else if (errorListValues.contains(value) && value != 0) {
            errorListValues.remove((Integer) value);
        }
        this.outRelationships[player.getPlayerIndex()] = value;
    }

    public int[] getOutRelationships() {
        return outRelationships;
    }

    public void changeOutRelationships(Player player, int value) {
        this.outRelationships[player.getPlayerIndex()] += value;
    }

    public void changeInRelationships(Player player, int value) {
        this.inRelationships[player.getPlayerIndex()] += value;
    }

    public void changeOutRelationshipsChange(Player player, int value) {
        this.outRelationshipsChange[player.getPlayerIndex()] += value;
    }

    public void changeInRelationshipsChange(Player player, int value) {
        this.inRelationshipsChange[player.getPlayerIndex()] += value;
    }

    public void changeInRelationshipsChangeMyActions(Player player, int value) {
        this.inRelationshipsChangeMyActions[player.getPlayerIndex()] += value;
    }

    public void changeInRelationshipsChangeOtherPlayerActions(Player player, int value) {
        this.inRelationshipsChangeOtherPlayerActions[player.getPlayerIndex()] += value;
    }

    public void changeInRelationshipsChangePassive(Player player, int value) {
        this.inRelationshipsChangePassive[player.getPlayerIndex()] += value;
    }

    public void changeOutPraisePreview(Player player, int value) {
        this.outPraisePreview[player.getPlayerIndex()] += value;
    }

    public void changeInPraisePreview(Player player, int value) {
        this.inPraisePreview[player.getPlayerIndex()] += value;
    }

    public void changeOutCriticizePreview(Player player, int value) {
        this.outCriticizePreview[player.getPlayerIndex()] += value;
    }

    public void changeInCriticizePreview(Player player, int value) {
        this.inCriticizePreview[player.getPlayerIndex()] += value;
    }

    public void changeInCriticizeRemainingPreview(Player player, int value) {
        this.inCriticizeRemainingPreview[player.getPlayerIndex()] += value;
    }

    public void changeInPraiseRemainingPreview(Player player, int value) {
        this.inPraiseRemainingPreview[player.getPlayerIndex()] += value;
    }

    public void resetPreviews() {
        this.outPraisePreview = new int[numPlayers];
        this.inPraisePreview = new int[numPlayers];
        this.outCriticizePreview = new int[numPlayers];
        this.inCriticizePreview = new int[numPlayers];
        this.inPraiseRemainingPreview = new int[numPlayers];
        this.inCriticizeRemainingPreview = new int[numPlayers];
    }
    public void resetPreviewSums() {
        this.praisePreviewInSum = new int[numPlayers];
        this.criticizePreviewInSum = new int[numPlayers];
        this.praisePreviewInRemainingSum = new int[numPlayers];
        this.criticizePreviewInRemainingSum = new int[numPlayers];
    }

    public int getOutRelationshipsChange(Player player) {
        return outRelationshipsChange[player.getPlayerIndex()];
    }

    public int getInRelationshipsChange(Player player) {
        return inRelationshipsChange[player.getPlayerIndex()];
    }

    public int getInRelationshipsChangePassive(Player player) {
        return inRelationshipsChangePassive[player.getPlayerIndex()];
    }

    public int getInRelationshipsChangeOtherPlayerActions(Player player) {
        return inRelationshipsChangeOtherPlayerActions[player.getPlayerIndex()];
    }

    public int getInRelationshipsChangeMyActions(Player player) {
        return inRelationshipsChangeMyActions[player.getPlayerIndex()];
    }

    public void resetRelationshipsChange() {
        Arrays.fill(this.inRelationshipsChange, 0);
        Arrays.fill(this.outRelationshipsChange, 0);
        Arrays.fill(this.inRelationshipsChangePassive, 0);
        Arrays.fill(this.inRelationshipsChangeOtherPlayerActions, 0);
        Arrays.fill(this.inRelationshipsChangeMyActions, 0);
    }


    public int getInRelationSum() {
        return inRelationSum;
    }

    public int getOutRelationSum() {
        return outRelationSum;
    }

    public String getName() {
        return playerName;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getNumErrors() {
        return numErrors;
    }

    public void setNumErrors(int value) {
        numErrors = value;
    }

    public ArrayList<Integer> getErrorListValues() {
        return errorListValues;
    }

    public void incrementNumErrors() {
        numErrors++;
    }

    public void calculateInRelationSum() {
        inRelationSum = IntStream.of(inRelationships).sum();
    }

    public void calculateOutRelationSum() {
        outRelationSum = IntStream.of(outRelationships).sum();
    }

    public void addToErrorList(int v) {
        errorListValues.add(v);
    }

    public void updateErrorListPlayers(List<Player> playerList) {
        errorListPlayers.clear();
        for (int i = 0; i < playerList.size(); i++) {
            if (outRelationships[i] == -99 && i != playerIndex && !errorListPlayers.contains(playerList.get(i))) {
                errorListPlayers.add(playerList.get(i));
            }
        }
        setNumErrors(errorListPlayers.size());
    }

    public ArrayList<Player> getErrorListPlayers() {
        return errorListPlayers;
    }

    public List<String> getErrorListPlayersAsString() {
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < errorListPlayers.size(); i++) {
            tempList.add(errorListPlayers.get(i).getName());
        }
        return tempList;
    }

    public int getOpinionOf(Player player) {
        return outRelationships[player.getPlayerIndex()];
    }

    public int getOpinionOfMe(Player player) {
        return inRelationships[player.getPlayerIndex()];
    }

    public int getHeadToHeadScoreWithPlayer(Player player) {
        return headToHeadScores[player.getPlayerIndex()];
    }

    public int getHeadToHeadScoreRemainingWithPlayer(Player player) {
        return headToHeadScoresRemaining[player.getPlayerIndex()];
    }



    public int findFavorite(ArrayList<Player> players, boolean verbose) {
        int max = -10000;
        int favoritePlayerIndex = 0;
        for (Player player : players) {
            int playeridx = player.getPlayerIndex();
            if (playeridx != this.getPlayerIndex()) {
                if (outRelationships[playeridx] == max) {
                    if (verbose) {
                        System.out.println("First tiebreaker between " + playerArr[favoritePlayerIndex].getName() + " and " + playerArr[playeridx].getName());
                    }

                    if (inRelationships[playeridx] == inRelationships[favoritePlayerIndex]) {
                        if (verbose) {
                            System.out.println("second tiebreaker");
                        }
                    }
                    if (inRelationships[playeridx] > inRelationships[favoritePlayerIndex]) {
                        favoritePlayerIndex = playeridx;
                    }
                }
                if (outRelationships[playeridx] > max) {
                    max = outRelationships[playeridx];
                    favoritePlayerIndex = playeridx;
                }
            }
        }
        if (verbose) {
            System.out.println(this.getName() + " chooses: " + playerArr[favoritePlayerIndex].getName());
        }
        return favoritePlayerIndex;
    }

    public int findLeastFavorite(ArrayList<Player> players) {
        int min = 10000;
        int LeastFavoritePlayerIndex = 0;
        for (Player player : players) {
            int playeridx = player.getPlayerIndex();
            if (playeridx != this.getPlayerIndex()) {
                if (outRelationships[playeridx] == min) {
                    System.out.println("First tiebreaker between " + playerArr[LeastFavoritePlayerIndex].getName() + " and " + playerArr[playeridx].getName());
                    if (inRelationships[playeridx] == inRelationships[LeastFavoritePlayerIndex]) {
                        System.out.println("second tiebreaker");
                    }
                    if (inRelationships[playeridx] < inRelationships[LeastFavoritePlayerIndex]) {
                        LeastFavoritePlayerIndex = playeridx;
                    }
                }
                if (outRelationships[playeridx] < min) {
                    min = outRelationships[playeridx];
                    LeastFavoritePlayerIndex = playeridx;
                }
            }
        }
        System.out.println(this.getName() + " Least favorite player is: " + playerArr[LeastFavoritePlayerIndex].getName());
        return LeastFavoritePlayerIndex;
    }


    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public void selectNextPlace(ArrayList<Player> players) {
        if (players.size() == 1) {
            players.get(0).setPlacement(this.getPlacement() + 1);
            return;
        }
        if (players.size() != 2) {
            this.chosenPlayer = playerArr[findFavorite(players, true)];
            players.remove(chosenPlayer);
            this.closestFromBeingChosen = playerArr[findFavorite(players, false)];
            chosenPlayer.setPlacement(this.getPlacement() + 1);
            changeOpinionOfMe(chosenPlayer, (players.size()) * 5, false, true, false, false);
            for (Player player : players) {
                if (player != chosenPlayer) {
                    this.changeOpinionOfMe(player, -5, false, true, false, false);
                }
            }
            chosenPlayer.selectNextPlace(players);
        } else {
            this.chosenPlayer = playerArr[findFavorite(players, true)];
            this.chosenPlayer.clearChosenPlayer();
            chosenPlayer.setPlacement(this.getPlacement() + 1);
            this.changeOpinionOfMe(chosenPlayer, 50, false, true, false, false);
            players.remove(chosenPlayer);
            this.closestFromBeingChosen = players.get(0);
            this.closestFromBeingChosen.clearChosenPlayer();
            this.changeOpinionOfMe(closestFromBeingChosen, -50, false, true, false, false);
            closestFromBeingChosen.setPlacement(this.getPlacement() + 2);

            /*
            for (Player player : playerArr) {
                player.clearRelationshipStatus();
            }
            for (Player player : playerArr) {
                player.updateInfluence();
            }
            chosenPlayer.praisePlayer(this);
            closestFromBeingChosen.criticizePlayer(this);

             */
        }
    }

    public void changeOpinionOfMe(Player player, int value, boolean preview, boolean passive, boolean otherPlayerAction, boolean myAction) {

        if (this.allies.contains(player)) {
            value -= 5;
        }
        if (this.enemies.contains(player)) {
            value += 5;
        }

        if (value > 0 && this.incomingRelationshipStatus.get(8).contains(player)) {
            value *= 2;
            if (!preview) {
                System.out.println("Redemption!");
            }
        }
        if (value < 0 && this.incomingRelationshipStatus.get(0).contains(player)) {
            value *= 2;
            if (!preview) {
                System.out.println("Betrayal");
            }
        }

        this.changeInRelationships(player, value);
        this.changeInRelationshipsChange(player, value);
        if (passive) {
            this.changeInRelationshipsChangePassive(player, value);
        }
        if (otherPlayerAction) {
            player.changeInRelationshipsChangeOtherPlayerActions(this, value);
        }
        if (myAction) {
            player.changeInRelationshipsChangeMyActions(this, value);
        }
        player.changeOutRelationships(this, value);
        player.changeOutRelationshipsChange(this, value);
        System.out.println(player.getName() + " Changes Opinion of " + this.getName() + " by " + value);
    }

    public void changeOpinionOf(Player player, int value, boolean preview, boolean praise, boolean passive, boolean otherPlayerAction, boolean myAction) {

        if (player.allies.contains(this)) {
            value -= 5;
        }
        if (player.enemies.contains(this)) {
            value += 5;
        }
        if (value > 0 && player.incomingRelationshipStatus.get(8).contains(this)) {
            value *= 2;
            if (!preview) {
                System.out.println("Redemption!");
            }
        }
        if (value < 0 && player.incomingRelationshipStatus.get(0).contains(this)) {
            value *= 2;
            if (!preview) {
                System.out.println("Betrayal!");
            }
        }
        if (preview) {
            if (praise) {
                this.changeOutPraisePreview(player, value);
                player.changeInPraisePreview(this, value);
                if (!this.isEliminated()) {
                    player.changeInPraiseRemainingPreview(this, value);
                }
            } else {
                this.changeOutCriticizePreview(player, value);
                player.changeInCriticizePreview(this, value);
                if (!this.isEliminated()) {
                    player.changeInCriticizeRemainingPreview(this, value);
                }
            }
            return;
        }
        this.changeOutRelationships(player, value);
        this.changeOutRelationshipsChange(player, value);
        player.changeInRelationships(this, value);
        player.changeInRelationshipsChange(this, value);
        if (passive) {
            player.changeInRelationshipsChangePassive(this, value);
        }
        if (otherPlayerAction) {
            player.changeInRelationshipsChangeOtherPlayerActions(this, value);
        }
        if (myAction) {
            player.changeInRelationshipsChangeMyActions(this, value);
        }
        System.out.println(this.getName() + " Changes Opinion of " + player.getName() + " by " + value);
    }

    public boolean isEliminated() {
        return eliminated.get();
    }

    public void eliminatePlayer() {
        this.eliminated.set(true);
    }

    public BooleanProperty eliminatedProperty() {
        return eliminated;
    }

    public int getNominations() {
        return nominations;
    }

    public void setNominations(int nominations) {
        this.nominations = nominations;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void updateRelationSums() {
        inRelationSum = IntStream.of(inRelationships).sum();
        outRelationSum = IntStream.of(outRelationships).sum();
    }

    public int getInfluence() {
        return influence;
    }

    public void setInfluence(int influence) {
        this.influence = influence;
    }

    public int getInfluenceRemaining() {
        return influenceRemaining;
    }

    public void setInfluenceRemaining(int influenceRemaining) {
        this.influenceRemaining = influenceRemaining;
    }


    public void updateInfluence() {
        int[] results = new int[2];

        // Clear current relationship statuses

        for (int i = 0; i < numPlayers; i++) {
            if (this.getPlayerIndex() != i) {
                Player otherPlayer = this.playerArr[i];
                if (this.inRelationships[i] >= 100) {
                    this.incomingRelationshipStatus.get(0).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(0).add(this);
                    updateResults(results, otherPlayer, 20);
                    this.allies.add(otherPlayer);
                    this.supporters.add(otherPlayer);
                    otherPlayer.supports.add(this);
                } else if (this.inRelationships[i] >= 50) {
                    this.incomingRelationshipStatus.get(1).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(1).add(this);
                    updateResults(results, otherPlayer, 15);
                    this.allies.add(otherPlayer);
                    this.supporters.add(otherPlayer);
                    otherPlayer.supports.add(this);
                } else if (this.inRelationships[i] >= 25) {
                    this.incomingRelationshipStatus.get(2).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(2).add(this);
                    updateResults(results, otherPlayer, 10);
                    this.supporters.add(otherPlayer);
                    otherPlayer.supports.add(this);
                }  else if (this.inRelationships[i] >= 10) {
                    this.incomingRelationshipStatus.get(3).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(3).add(this);
                    updateResults(results, otherPlayer, 5);
                } else if (this.inRelationships[i] <= -100) {
                    this.incomingRelationshipStatus.get(8).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(8).add(this);
                    updateResults(results, otherPlayer, -20);
                    this.enemies.add(otherPlayer);
                    this.opponents.add(otherPlayer);
                    otherPlayer.opposes.add(this);
                } else if (this.inRelationships[i] <= -50) {
                    this.incomingRelationshipStatus.get(7).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(7).add(this);
                    updateResults(results, otherPlayer, -15);
                    this.enemies.add(otherPlayer);
                    this.opponents.add(otherPlayer);
                    otherPlayer.opposes.add(this);
                } else if (this.inRelationships[i] <= -25) {
                    this.incomingRelationshipStatus.get(6).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(6).add(this);
                    updateResults(results, otherPlayer, -10);
                    this.opponents.add(otherPlayer);
                    otherPlayer.opposes.add(this);
                } else if (this.inRelationships[i] <= -10) {
                    this.incomingRelationshipStatus.get(5).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(5).add(this);
                    updateResults(results, otherPlayer, -5);
                } else {
                    this.incomingRelationshipStatus.get(4).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(4).add(this);
                    this.neutrals.add(otherPlayer);

                }
            }

        }
        setInfluence(results[0]);
        setInfluenceRemaining(results[1]);
    }


    private void updateResults(int[] results, Player otherPlayer, int value) {
        if (otherPlayer.isEliminated()) {
            results[0] += value;
        } else {
            results[0] += value;
            results[1] += value;
        }
    }

    public void clearRelationshipStatus() {
        this.allies.clear();
        this.supporters.clear();
        this.neutrals.clear();
        this.opponents.clear();
        this.enemies.clear();
        this.supports.clear();
        this.opposes.clear();
        for (int i = 0; i < incomingRelationshipStatus.size(); i++) {
            this.incomingRelationshipStatus.get(i).clear();
            this.outcomingRelationshipStatus.get(i).clear();

        }
    }

    public void printIncomingRelationshipStatus() {
        System.out.println(this.getName() + " incoming relationship status");
        for (int i = 0; i < incomingRelationshipStatus.size(); i++) {
            if (!incomingRelationshipStatus.get(i).isEmpty()) {
                System.out.println("incomingRelationshipStatus.get(" + i + ")");
            }
            for (int k = 0; k < incomingRelationshipStatus.get(i).size(); k++) {
                System.out.print(incomingRelationshipStatus.get(i).get(k).getName() + " ");
            }
            if (!incomingRelationshipStatus.get(i).isEmpty()) {
                System.out.println(" ");
            }
        }
    }

    public void printOutcomingRelationshipStatus() {
        System.out.println(this.getName() + " outcoming relationship status");
        for (int i = 0; i < outcomingRelationshipStatus.size(); i++) {
            if (!outcomingRelationshipStatus.get(i).isEmpty()) {
                System.out.println("outcomingRelationshipStatus.get(" + i + ")");
            }
            for (int k = 0; k < outcomingRelationshipStatus.get(i).size(); k++) {
                System.out.print(outcomingRelationshipStatus.get(i).get(k).getName() + " ");
            }
            if (!outcomingRelationshipStatus.get(i).isEmpty()) {
                System.out.println(" ");
            }
        }
    }

    public void praisePlayer (Player player, boolean preview) {
        if (!preview) {
            System.out.println(this.getName() + this.getRoundBorn() + " praises " + player.getName()+ player.getRoundBorn());
            this.praisedPlayer = player;
        }

        /*
        for (Player p: player.incomingRelationshipStatus.get(3)) {
            if (p != this) {
                p.changeOpinionOf(player, 5);
            }
        }

         */

        int base = -5;
        for (int i = 5; i < 9; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base, preview, true, false, true, false);
                }
            }
            base -= 5;
        }

        base = 20;
        for (int i = 0; i < 4; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base, preview, true, false, true, false);
                }
            }
            base -= 5;
        }

        base = -5;
        for (int i = 5; i < 9; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base, preview, true, false, false, true);
                }
            }
            base -= 5;
        }

        base = 20;
        for (int i = 0; i < 4; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base, preview, true, false, false, true);
                }
            }
            base -= 5;
        }

        player.changeOpinionOf(this, 10, preview, true, false, false, true);
    }

    public void criticizePlayer (Player player, boolean preview) {

        if (!preview) {
            System.out.println(this.getName() + this.getRoundBorn() + " criticizes " + player.getName() + player.getRoundBorn());
            this.criticizedPlayer = player;
            this.errorCheckRelationshipStatus();
        }


        /*
        for (Player p: player.incomingRelationshipStatus.get(3)) {
            if (p != this) {
                p.changeOpinionOf(player, -5);
            }
        }
         */

        int base = 5;
        for (int i = 5; i < 9; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base, preview, false, false, true, false);
                }
            }
            base += 5;
        }

        base = -20;
        for (int i = 0; i < 4; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base, preview, false, false, true, false);
                }
            }
            base += 5;
        }

        base = 5;
        for (int i = 5; i < 9; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base, preview, false, false, false, true);
                }
            }
            base += 5;
        }

        base = -20;
        for (int i = 0; i < 4; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base, preview, false, false, false, true);
                }
            }
            base += 5;
        }

        player.changeOpinionOf(this, -10, preview, false, false, false, true);
    }

    public void praisePreview(Player player) {
        this.resetPreviews();
        player.resetPreviews();
        this.praisePlayer(player, true);
        this.praisePreviewInSum[player.getPlayerIndex()] = IntStream.of(inPraisePreview).sum();
        this.praisePreviewInRemainingSum[player.getPlayerIndex()] = IntStream.of(inPraiseRemainingPreview).sum();
    }

    public void criticizePreview(Player player) {
        this.resetPreviews();
        player.resetPreviews();
        this.criticizePlayer(player, true);
        this.criticizePreviewInSum[player.getPlayerIndex()] = IntStream.of(inCriticizePreview).sum();
        this.criticizePreviewInRemainingSum[player.getPlayerIndex()] = IntStream.of(inCriticizeRemainingPreview).sum();
    }

    public boolean canPraise(Player player) {
        if (this.isEliminated() || player == this || !this.isReal) {
            return false;
        }
        return this.outRelationships[player.getPlayerIndex()] >= 0;
    }

    public boolean canCriticize(Player player) {
        if (this.isEliminated() || player == this || !this.isReal) {
            return false;
        }
        return this.outRelationships[player.getPlayerIndex()] <= 0;
    }

    public int getRoundBorn() {
        return roundBorn;
    }

    public boolean errorCheckRelationshipStatus() {
        int sum = 0;
        for (List<Player> relationshipStatus : incomingRelationshipStatus) {
            sum += relationshipStatus.size();
        }
        if (sum != 10) {
            System.out.println("ERROR!!! incomingRelationshipStatus");
            System.out.println("sum = " + sum);
            System.out.println("Player: " + this.getName() + " born round: " + this.getRoundBorn());
            return  false;
        }

        sum = 0;
        for (List<Player> relationshipStatus : outcomingRelationshipStatus) {
            sum += relationshipStatus.size();
        }
        if (sum != 10) {
            System.out.println("ERROR!!! outcomingRelationshipStatus");
            System.out.println("sum = " + sum);
            System.out.println("Player: " + this.getName() + " born round: " + this.getRoundBorn());
            return  false;
        }
        return true;
    }

    public void updateInfluenceForMe() {
        System.out.println("updateInfluenceForStatus for: " + this.getName() + " " + this.getRoundBorn());
        updateInfluenceForStatus(0, -20);
        updateInfluenceForStatus(1, -15);
        updateInfluenceForStatus(2, -10);
        updateInfluenceForStatus(3, -5);
        updateInfluenceForStatus(4, 0);
        updateInfluenceForStatus(5, 5);
        updateInfluenceForStatus(6, 10);
        updateInfluenceForStatus(7, 15);
        updateInfluenceForStatus(8, 20);
    }

    private void updateInfluenceForStatus(int statusIndex, int influenceChange) {

        for (Player player : outcomingRelationshipStatus.get(statusIndex)) {
            int temp = influenceChange;
            int tempremaining = influenceChange;
            for (Player playerToCheck : player.playerArr) {
                if (!player.neutrals.contains(playerToCheck)) {
                    if (this.allies.contains(playerToCheck)) {
                        System.out.println(playerToCheck.getName() + " influenceChange - 5 for: " + player.getName());
                        temp -= 5;
                        if(!playerToCheck.isEliminated()) {
                            tempremaining -= 5;
                        }
                    } else if (this.enemies.contains(playerToCheck)) {
                        System.out.println(playerToCheck.getName() + " influenceChange + 5 for: " + player.getName());
                        temp += 5;
                        if(!playerToCheck.isEliminated()) {
                            tempremaining += 5;
                        }
                    }
                }
                int myInfluence = player.influence + temp;
                int myInfluenceRemaining = player.influenceRemaining + tempremaining;
                player.setInfluenceForMe(this, myInfluence);
                player.setInfluenceForMeRemaining(this, myInfluenceRemaining);
            }
        }
    }

    public void updateDifferenceOfOpinionFromChosenPlayer() {
        Arrays.fill(this.differenceOfOpinionFromChosenPlayer, 0);
        for (int i = 0; i < numPlayers; i++) {
            if (i != this.getPlayerIndex() && playerArr[i].getChosenPlayer() != null) {
                if (playerArr[i].getChosenPlayer() == this) {
                    this.differenceOfOpinionFromChosenPlayer[i] = playerArr[i].getOpinionOf(playerArr[i].getClosestFromBeingChosen()) - playerArr[i].getOpinionOf(this);
                } else {
                    this.differenceOfOpinionFromChosenPlayer[i] = playerArr[i].getOpinionOf(playerArr[i].getChosenPlayer()) - playerArr[i].getOpinionOf(this);
                }
            }
        }
    }

    public void updateDifferenceOfOpinionFromFavorite() {
        for (int i = 0; i < numPlayers; i++) {
            if (i != this.getPlayerIndex()) {
                if (playerArr[i].getFavoriteRemainingPlayer() == this) {
                    this.differenceOfOpinionFromFavorite[i] = playerArr[i].getOpinionOf(playerArr[i].getSecondFavoriteRemainingPlayer()) - playerArr[i].getOpinionOf(this);
                } else {
                    this.differenceOfOpinionFromFavorite[i] = playerArr[i].getOpinionOf(playerArr[i].getFavoriteRemainingPlayer()) - playerArr[i].getOpinionOf(this);
                }
            }
        }
    }

    public int getDifferenceOfOpinionFromFavorite(Player player) {
        return this.differenceOfOpinionFromFavorite[player.getPlayerIndex()];
    }

    public int[] getDifferenceOfOpinionFromFavoriteArray() {
        return this.differenceOfOpinionFromFavorite;
    }

    public int getDifferenceOfOpinionFromChosen(Player player) {
        return this.differenceOfOpinionFromChosenPlayer[player.getPlayerIndex()];
    }

    public void updateFavoriteRemainingPlayer(ArrayList<Player> players) {
        this.favoriteRemainingPlayer = playerArr[this.findFavorite(players, false)];
        ArrayList<Player> tempList = new ArrayList<>(players);
        tempList.remove(favoriteRemainingPlayer);
        this.secondFavoriteRemainingPlayer = playerArr[this.findFavorite(tempList, false)];
        this.favoriteRemainingPlayer.incrementFinalVotes();
    }

    public Player getFavoriteRemainingPlayer() {
        return this.favoriteRemainingPlayer;
    }

    public String getFavoriteRemainingPlayerAsString() {
        return this.favoriteRemainingPlayer.getName();
    }

    public int getFinalVotes() {
        return finalVotes;
    }

    public void setFinalVotes(int votes) {
        this.finalVotes = votes;
    }

    public void incrementFinalVotes() {
        this.finalVotes++;
    }

    public Player getSecondFavoriteRemainingPlayer() {
        return secondFavoriteRemainingPlayer;
    }

    public Player getChosenPlayer() {
        return this.chosenPlayer;
    }

    public void clearChosenPlayer() {
        this.chosenPlayer = null;
        this.closestFromBeingChosen = null;
    }

    public String getChosenPlayerAsString() {
        if (this.chosenPlayer != null) {
            return this.chosenPlayer.getName();
        }
        return "";
    }

    public Player getClosestFromBeingChosen() {
        return closestFromBeingChosen;
    }

    public int getInfluenceForMe(Player player) {
        return influenceForMe[player.getPlayerIndex()];
    }

    public void setInfluenceForMe(Player player, int value) {
        this.influenceForMe[player.getPlayerIndex()] = value;
    }

    public int getInfluenceForMeRemaining(Player player) {
        return influenceForMeRemaining[player.getPlayerIndex()];
    }

    public void setInfluenceForMeRemaining(Player player, int value) {
        this.influenceForMeRemaining[player.getPlayerIndex()] = value;
    }

    public int[] getPraiseInPreviewSum() {
        return this.praisePreviewInSum;
    }

    public int[] getCriticizePreviewSum() {
        return this.criticizePreviewInSum;
    }

    public int getPraiseSumPreview(Player player) {
        return this.praisePreviewInSum[player.getPlayerIndex()];
    }

    public int getCriticizeSumPreview(Player player) {
        return this.criticizePreviewInSum[player.getPlayerIndex()];
    }

    public int getCriticizeRemainingSumPreview(Player player) {
        return this.criticizePreviewInRemainingSum[player.getPlayerIndex()];
    }

    public int getPraiseRemainingSumPreview(Player player) {
        return this.praisePreviewInRemainingSum[player.getPlayerIndex()];
    }
    public int[] getCriticizePreviewInRemainingSum() {
        return this.criticizePreviewInRemainingSum;
    }

    public int[] getPraisePreviewInRemainingSum() {
        return this.praisePreviewInRemainingSum;
    }

    public boolean getHasBeenPickedAsNumber(int index) {
        return hasBeenPickedAsNumber[index];
    }

    public void setHasBeenPickedAsNumber(int index) {
        this.hasBeenPickedAsNumber[index] = true;
    }

    public Player[] pickPlayers() {
        if (isReal && !isHuman) {
            return this.strategy.pickPlayers();
        }
        return null;
    }

    public void setStrategy(AIOverallStrategy strategy) {
        this.strategy = strategy;
    }

    public Player getPraisedPlayer() {
        return praisedPlayer;
    }

    public String getPraisedPlayerAsString() {
        if (this.praisedPlayer != null) {
            return praisedPlayer.getName();
        }
        return "";
    }

    public void setPraisedPlayer(Player praisedPlayer) {
        this.praisedPlayer = praisedPlayer;
    }

    public Player getCriticizedPlayer() {
        return criticizedPlayer;
    }

    public String getCriticizedPlayerAsString() {
        if (this.criticizedPlayer != null) {
            return criticizedPlayer.getName();
        }
        return "";
    }

    public void setCriticizedPlayer(Player criticizedPlayer) {
        this.criticizedPlayer = criticizedPlayer;
    }

    public void clearPicks() {
        this.setPraisedPlayer(null);
        this.setCriticizedPlayer(null);
    }

    public void calculateHeadToHead(ArrayList<Player> players) {
        ArrayList<Player> otherPlayers = new ArrayList<>(players);
        otherPlayers.remove(this);
        for (Player player : otherPlayers) {
            int score = 0;
            int scoreRemaining = 0;
            for (Player playerToCompare : playerArr) {
                if (playerToCompare != player && playerToCompare != this) {
                    ArrayList<Player> playersToCompare = new ArrayList<>(Arrays.asList(this, player)) ;
                    Player chosenPlayer = playerArr[playerToCompare.findFavorite(playersToCompare, false)];
                    if (chosenPlayer == this) {
                        score += 1;
                        if (!playerToCompare.isEliminated()) {
                            scoreRemaining += 1;
                        }
                    } else {
                        score -= 1;
                        if (!playerToCompare.isEliminated()) {
                            scoreRemaining -= 1;
                        }
                    }
                }
            }
            this.headToHeadScores[player.getPlayerIndex()] = score;
            player.headToHeadScores[this.getPlayerIndex()] = score * -1;
            this.headToHeadScoresRemaining[player.getPlayerIndex()] = scoreRemaining;
            player.headToHeadScoresRemaining[this.getPlayerIndex()] = scoreRemaining * -1;
        }
        if (otherPlayers.isEmpty()) {
            return;
        }
        otherPlayers.get(0).calculateHeadToHead(otherPlayers);
    }

    public void calculateHeadToHeadWinsAndSum() {
        int score = 0;
        int scoreRemaining = 0;
        int sum = 0;
        int sumRemaining = 0;
        for (int i = 0; i < numPlayers; i++) {
            if (!playerArr[i].isEliminated()) {
                sum += headToHeadScores[i];
                sumRemaining += headToHeadScoresRemaining[i];
                if (headToHeadScores[i] > 0) {
                    score += 1;
                    if (headToHeadScoresRemaining[i] > 0) {
                        scoreRemaining += 1;
                    }
                }
            }
            this.headToHeadWins = score;
            this.headToHeadWinDifferenceRemaining = scoreRemaining;
            this.headToHeadWinSum = sum;
            this.headToHeadWinSumRemaining = sumRemaining;
        }
    }

    public void clearHeadToHeadStats() {
        Arrays.fill(this.headToHeadScores, 0);
        Arrays.fill(this.headToHeadScoresRemaining, 0);
        this.headToHeadWins = 0;
        this.headToHeadWinDifferenceRemaining = 0;
        this.headToHeadWinSum = 0;
        this.headToHeadWinSumRemaining= 0;
    }

    public int getHeadToHeadWins() {
        return headToHeadWins;
    }

    public List<Player> getSupporters() {
        return this.supporters;
    }

    public List<Player> getSupports() {
        return this.supports;
    }

    public List<Player> getOpponents() {
        return this.opponents;
    }

    public List<Player> getOpposes() {
        return this.opposes;
    }

    public List<Player> getIncomingRelationshipStatus(int index) {
        return incomingRelationshipStatus.get(index);
    }

    public List<Player> getOutcomingRelationshipStatus(int index) {
        return outcomingRelationshipStatus.get(index);
    }

}




