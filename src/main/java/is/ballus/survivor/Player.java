package is.ballus.survivor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Player {
    public int playerIndex;
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
    private int[] differenceOfOpinionFromFavorite;
    private int[] differenceOfOpinionFromChosenPlayer;
    public int inRelationSum;
    public int outRelationSum;
    private int influence;
    private int influenceRemaining;
    private int[] influenceForMe;
    private int[] influenceForMeRemaining;
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
    private boolean[] hasBeenPickedAsNumber = new boolean[9];



    public Player(int index, int numPlayers, int roundNum, Settings settings, ArrayList<Player> playerList, Player[] playerArr) {
        this.playerIndex = index;
        this.roundBorn = roundNum;
        this.playerList = playerList;
        this.playerArr = playerArr;
        this.numPlayers = numPlayers;
        this.placement = numPlayers;
        this.outRelationships = new int[numPlayers];
        this.inRelationships = new int[numPlayers];
        this.outRelationshipsChange = new int[numPlayers];
        this.inRelationshipsChange = new int[numPlayers];
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
        this.outRelationshipsChange =  player.outRelationshipsChange.clone();
        this.inRelationshipsChange = player.inRelationshipsChange.clone();
        this.differenceOfOpinionFromFavorite = player.differenceOfOpinionFromFavorite.clone();
        this.differenceOfOpinionFromChosenPlayer = player.differenceOfOpinionFromChosenPlayer.clone();
        this.influenceForMe =  player.influenceForMe.clone();
        this.influenceForMeRemaining = player.influenceForMeRemaining.clone();
        this.inRelationSum = player.inRelationSum;
        this.outRelationSum = player.outRelationSum;
        this.playerName = player.playerName;
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

    public int getOutRelationshipsChange(Player player) {
        return outRelationshipsChange[player.getPlayerIndex()];
    }

    public int getInRelationshipsChange(Player player) {
        return inRelationshipsChange[player.getPlayerIndex()];
    }

    public void resetRelationshipsChange() {
        this.inRelationshipsChange = new int[numPlayers];
        this.outRelationshipsChange = new int[numPlayers];
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

    public int findFavorite(ArrayList<Player> players) {
        int max = -1000;
        int maxPlayerIndex = 0;
        for (int i = 0; i < players.size(); i++) {
            int playeridx = players.get(i).getPlayerIndex();
            if (playeridx != this.getPlayerIndex()) {
                if (outRelationships[playeridx] == max) {
                    System.out.println("First tiebreaker between " + playerArr[maxPlayerIndex].getName() + " and " + playerArr[playeridx].getName());
                    if (inRelationships[playeridx] == inRelationships[maxPlayerIndex]) {
                        System.out.println("second tiebreaker");
                    }
                    if (inRelationships[playeridx] > inRelationships[maxPlayerIndex]) {
                        maxPlayerIndex = playeridx;
                    }
                }
                if (outRelationships[playeridx] > max) {
                    max = outRelationships[playeridx];
                    maxPlayerIndex = playeridx;
                }
            }
        }
        System.out.println(this.getName() + " chooses: " + playerArr[maxPlayerIndex].getName());
        return maxPlayerIndex;
    }


    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public void selectNextPlace(ArrayList<Player> players) {
        System.out.println("players.size(): " + players.size());
        if (players.size() == 1) {
            players.get(0).setPlacement(this.getPlacement() + 1);
            return;
        }
        if (players.size() != 2) {
            this.chosenPlayer = playerArr[findFavorite(players)];
            players.remove(chosenPlayer);
            this.closestFromBeingChosen = playerArr[findFavorite(players)];
            chosenPlayer.setPlacement(this.getPlacement() + 1);
            changeOpinionOfMe(chosenPlayer, (players.size()) * 5);
            for (Player player : players) {
                if (player != chosenPlayer) {
                    this.changeOpinionOfMe(player, -5);
                }
            }
            chosenPlayer.selectNextPlace(players);
        } else {
            this.chosenPlayer = playerArr[findFavorite(players)];
            chosenPlayer.setPlacement(this.getPlacement() + 1);
            this.changeOpinionOfMe(chosenPlayer, 50);
            players.remove(chosenPlayer);
            this.closestFromBeingChosen = players.get(0);
            this.changeOpinionOfMe(closestFromBeingChosen, -50);
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

    public void changeOpinionOfMe(Player player, int value) {
        this.changeInRelationships(player, value);
        this.changeInRelationshipsChange(player, value);
        player.changeOutRelationships(this, value);
        player.changeOutRelationshipsChange(this, value);
        System.out.println(player.getName() + " Changes Opinion of " + this.getName() + " by " + value);
    }

    public void changeOpinionOf(Player player, int value) {
        this.changeOutRelationships(player, value);
        this.changeOutRelationshipsChange(player, value);
        player.changeInRelationships(this, value);
        player.changeInRelationshipsChange(this, value);
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
                } else if (this.inRelationships[i] >= 50) {
                    this.incomingRelationshipStatus.get(1).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(1).add(this);
                    updateResults(results, otherPlayer, 15);
                } else if (this.inRelationships[i] >= 25) {
                    this.incomingRelationshipStatus.get(2).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(2).add(this);
                    updateResults(results, otherPlayer, 10);
                }  else if (this.inRelationships[i] >= 10) {
                    this.incomingRelationshipStatus.get(3).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(3).add(this);
                    updateResults(results, otherPlayer, 5);
                } else if (this.inRelationships[i] <= -100) {
                    this.incomingRelationshipStatus.get(8).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(8).add(this);
                    updateResults(results, otherPlayer, -20);
                } else if (this.inRelationships[i] <= -50) {
                    this.incomingRelationshipStatus.get(7).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(7).add(this);
                    updateResults(results, otherPlayer, -15);
                } else if (this.inRelationships[i] <= -25) {
                    this.incomingRelationshipStatus.get(6).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(6).add(this);
                    updateResults(results, otherPlayer, -10);
                } else if (this.inRelationships[i] <= -10) {
                    this.incomingRelationshipStatus.get(5).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(5).add(this);
                    updateResults(results, otherPlayer, -5);
                } else {
                    this.incomingRelationshipStatus.get(4).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(4).add(this);

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
        System.out.println("clearRelationshipStatus()");
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

    public void praisePlayer (Player player) {
        System.out.println(this.getName() + this.getRoundBorn() + " praises " + player.getName()+ player.getRoundBorn());
        this.errorCheckRelationshipStatus();
        player.errorCheckRelationshipStatus();
        player.printIncomingRelationshipStatus();
        this.printIncomingRelationshipStatus();
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
                    p.changeOpinionOf(player, base);
                }
            }
            base -= 5;
        }

        base = 20;
        for (int i = 0; i < 4; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base);
                }
            }
            base -= 5;
        }

        base = -5;
        for (int i = 5; i < 9; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base);
                }
            }
            base -= 5;
        }

        base = 20;
        for (int i = 0; i < 4; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base);
                }
            }
            base -= 5;
        }

        player.changeOpinionOf(this, 10);
    }

    public void criticizePlayer (Player player) {
        System.out.println(this.getName() + this.getRoundBorn() + " criticizes " + player.getName() + player.getRoundBorn());
        this.errorCheckRelationshipStatus();
        player.errorCheckRelationshipStatus();
        System.out.println("playerArr[player.getPlayerIndex()] == player");
        System.out.println(playerArr[player.getPlayerIndex()] == player);
        player.printIncomingRelationshipStatus();

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
                    p.changeOpinionOf(player, base);
                }
            }
            base += 5;
        }

        base = -20;
        for (int i = 0; i < 4; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base);
                }
            }
            base += 5;
        }

        base = 5;
        for (int i = 5; i < 9; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base);
                }
            }
            base += 5;
        }

        base = -20;
        for (int i = 0; i < 4; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base);
                }
            }
            base += 5;
        }

        player.changeOpinionOf(this, -10);
    }

    public boolean canPraise(Player player) {
        if (this.isEliminated()) {
            return false;
        }
        return this.outRelationships[player.getPlayerIndex()] >= 0;
    }

    public boolean canCriticize(Player player) {
        if (this.isEliminated()) {
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
            int myInfluence = player.influence + influenceChange;
            int myInfluenceRemaining = player.influenceRemaining + influenceChange;
            player.setInfluenceForMe(this, myInfluence);
            player.setInfluenceForMeRemaining(this, myInfluenceRemaining);
        }
    }

    public void updateDifferenceOfOpinionFromChosenPlayer() {
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

    public int getDifferenceOfOpinionFromChosen(Player player) {
        return this.differenceOfOpinionFromChosenPlayer[player.getPlayerIndex()];
    }

    public void updateFavoriteRemainingPlayer(ArrayList<Player> players) {
        this.favoriteRemainingPlayer = playerArr[this.findFavorite(players)];
        ArrayList<Player> tempList = new ArrayList<>(players);
        tempList.remove(favoriteRemainingPlayer);
        this.secondFavoriteRemainingPlayer = playerArr[this.findFavorite(tempList)];
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

    public boolean getHasBeenPickedAsNumber(int index) {
        return hasBeenPickedAsNumber[index];
    }

    public void setHasBeenPickedAsNumber(int index) {
        this.hasBeenPickedAsNumber[index] = true;
    }
}




