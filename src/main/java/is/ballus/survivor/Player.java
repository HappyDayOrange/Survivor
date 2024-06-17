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
    public int inRelationSum;
    public int outRelationSum;
    private int influence;
    private int influenceRemaining;
    private int nominations = 0;
    private int votes = 0;
    public String playerName;
    private List<Player> generateHelper = new ArrayList<Player>();
    private ArrayList<Player> errorListPlayers = new ArrayList<Player>();
    private ArrayList<Integer> errorListValues = new ArrayList<Integer>();
    private int numErrors = 0;
    private List<List<Player>> incomingRelationshipStatus = new ArrayList<>();
    private List<List<Player>> outcomingRelationshipStatus = new ArrayList<>();


    public Player(int index, int numPlayers, int roundNum, Settings settings, ArrayList<Player> playerList, Player[] playerArr) {
        this.playerIndex = index;
        this.roundBorn = roundNum;
        this.playerList = playerList;
        this.playerArr = playerArr;
        this.numPlayers = numPlayers;
        this.placement = numPlayers;
        this.outRelationships = new int[numPlayers];
        this.inRelationships = new int[numPlayers];
        this.inRelationSum = 0;
        this.outRelationSum = 0;
        this.playerName = settings.playerNames[index];
        List<Player> friends = new ArrayList<>();
        incomingRelationshipStatus.add(friends);
        outcomingRelationshipStatus.add(friends);
        List<Player> allies = new ArrayList<>();
        incomingRelationshipStatus.add(allies);
        outcomingRelationshipStatus.add(allies);
        List<Player> supporters = new ArrayList<>();
        incomingRelationshipStatus.add(supporters);
        outcomingRelationshipStatus.add(supporters);
        List<Player> neutral = new ArrayList<>();
        incomingRelationshipStatus.add(neutral);
        outcomingRelationshipStatus.add(neutral);
        List<Player> opponents = new ArrayList<>();
        incomingRelationshipStatus.add(opponents);
        outcomingRelationshipStatus.add(opponents);
        List<Player> rivals = new ArrayList<>();
        incomingRelationshipStatus.add(rivals);
        outcomingRelationshipStatus.add(rivals);
        List<Player> enemies = new ArrayList<>();
        incomingRelationshipStatus.add(enemies);
        outcomingRelationshipStatus.add(enemies);
        generateHelper.add(this);
        this.errorCheckIncomingRelationshipStatus();
    }

    public Player(Player player,int roundNum, ArrayList<Player> playerList, Player[] playerArr) {
        this.playerIndex = player.playerIndex;
        this.roundBorn = roundNum;
        this.playerList = playerList;
        this.playerArr = playerArr;
        this.numPlayers = player.numPlayers;
        this.placement = player.placement;
        this.outRelationships =  player.outRelationships.clone();
        this.inRelationships = player.inRelationships.clone();
        this.inRelationSum = player.inRelationSum;
        this.outRelationSum = player.outRelationSum;
        this.playerName = player.playerName;
        List<Player> friends = new ArrayList<>();
        incomingRelationshipStatus.add(friends);
        outcomingRelationshipStatus.add(friends);
        List<Player> allies = new ArrayList<>();
        incomingRelationshipStatus.add(allies);
        outcomingRelationshipStatus.add(allies);
        List<Player> supporters = new ArrayList<>();
        incomingRelationshipStatus.add(supporters);
        outcomingRelationshipStatus.add(supporters);
        List<Player> neutral = new ArrayList<>();
        incomingRelationshipStatus.add(neutral);
        outcomingRelationshipStatus.add(neutral);
        List<Player> opponents = new ArrayList<>();
        incomingRelationshipStatus.add(opponents);
        outcomingRelationshipStatus.add(opponents);
        List<Player> rivals = new ArrayList<>();
        incomingRelationshipStatus.add(rivals);
        outcomingRelationshipStatus.add(rivals);
        List<Player> enemies = new ArrayList<>();
        incomingRelationshipStatus.add(enemies);
        outcomingRelationshipStatus.add(enemies);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        this.errorCheckIncomingRelationshipStatus();
    }

    public boolean setInitialRelations(List<Player> players, int v, int l) {
        List<Player> tempList = new ArrayList<>(players);
        tempList.removeAll(generateHelper);
        if (tempList.isEmpty()) {
            System.out.println("Error!");
            addToErrorList(v);
            numErrors++;
            players.remove(this);
            return false;
        }
        Player pick = tempList.get((int) (Math.random() * tempList.size()));
        System.out.println(this.getName() + " picks: " + pick.getName());
        players.remove(this);
        players.remove(pick);
        generateHelper.add(pick);
        pick.generateHelper.add(this);
        this.setOutRelationships(pick, v);
        this.setInRelationships(pick, v);
        pick.setOutRelationships(this, v);
        pick.setInRelationships(this, v);
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


    public int getInRelationSum() {
        return inRelationSum;
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
        int max = -100;
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
        players.remove(this);
        System.out.println("players.size(): " + players.size());
        if (players.size() == 1) {
            players.get(0).setPlacement(this.getPlacement() + 1);
            return;
        }
        if (players.size() != 2) {
            Player chosenPlayer = playerArr[findFavorite(players)];
            chosenPlayer.setPlacement(this.getPlacement() + 1);
            changeOpinionOfMe(chosenPlayer, (players.size() - 1) * 5);
            for (Player player : players) {
                if (player != chosenPlayer) {
                    this.changeOpinionOfMe(player, -5);
                }
            }
            chosenPlayer.selectNextPlace(players);
        } else {
            Player chosenPlayer = playerArr[findFavorite(players)];
            chosenPlayer.setPlacement(this.getPlacement() + 1);
            changeOpinionOfMe(chosenPlayer, 50);
            players.remove(chosenPlayer);
            for (Player player : players) {
                if (player != chosenPlayer) {
                    this.changeOpinionOfMe(player, -50);
                    player.setPlacement(this.getPlacement() + 2);
                }
            }
        }
    }

    public void changeOpinionOfMe(Player player, int value) {
        this.changeInRelationships(player, value);
        player.changeOutRelationships(this, value);
        System.out.println(player.getName() + " Changes Opinion of " + this.getName() + " by " + value);
    }

    public void changeOpinionOf(Player player, int value) {
        this.changeOutRelationships(player, value);
        player.changeInRelationships(this, value);
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
        this.clearRelationshipStatus();

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
                } else if (this.inRelationships[i] <= -100) {
                    this.incomingRelationshipStatus.get(6).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(6).add(this);
                    updateResults(results, otherPlayer, -20);
                } else if (this.inRelationships[i] <= -50) {
                    this.incomingRelationshipStatus.get(5).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(5).add(this);
                    updateResults(results, otherPlayer, -15);
                } else if (this.inRelationships[i] <= -25) {
                    this.incomingRelationshipStatus.get(4).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(4).add(this);
                    updateResults(results, otherPlayer, -10);
                } else {
                    this.incomingRelationshipStatus.get(3).add(otherPlayer);
                    otherPlayer.outcomingRelationshipStatus.get(3).add(this);
                }
            }

        }
        this.errorCheckIncomingRelationshipStatus();
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

            incomingRelationshipStatus.get(i).clear();
            outcomingRelationshipStatus.get(i).clear();

        }
    }

    public void printIncomingRelationshipStatus() {
        System.out.println(this.getName() + " incoming relationship status");
        System.out.println("this.incomingRelationshipStatus.get(3).size: " + this.incomingRelationshipStatus.get(3).size());
        for (int i = 0; i < incomingRelationshipStatus.size(); i++) {
            if (!incomingRelationshipStatus.get(i).isEmpty()) {
                System.out.println("incomingRelationshipStatus.get() " + i );
            }
            for (int k = 0; k < incomingRelationshipStatus.get(i).size(); k++) {
                System.out.print(incomingRelationshipStatus.get(i).get(k).getName() + " ");
            }
            if (!incomingRelationshipStatus.get(i).isEmpty()) {
                System.out.println(" ");
            }
        }
    }

    public void praisePlayer (Player player) {
        System.out.println(this.getName() + this.getRoundBorn() + " praises " + player.getName()+ player.getRoundBorn());
        this.errorCheckIncomingRelationshipStatus();
        player.printIncomingRelationshipStatus();
        this.printIncomingRelationshipStatus();
        for (Player p: player.incomingRelationshipStatus.get(3)) {
            if (p != this) {
                p.changeOpinionOf(player, 5);
            }
        }

        int base = -10;
        for (int i = 4; i < 7; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base);
                }
            }
            base -= 5;
        }

        base = 20;
        for (int i = 0; i < 3; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base);
                }
            }
            base -= 5;
        }

        base = -10;
        for (int i = 4; i < 7; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base);
                }
            }
            base -= 5;
        }

        base = 20;
        for (int i = 0; i < 3; i++) {
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
        this.errorCheckIncomingRelationshipStatus();
        System.out.println("playerArr[player.getPlayerIndex()] == player");
        System.out.println(playerArr[player.getPlayerIndex()] == player);
        player.printIncomingRelationshipStatus();

        for (Player p: player.incomingRelationshipStatus.get(3)) {
            if (p != this) {
                p.changeOpinionOf(player, -5);
            }
        }

        int base = 10;
        for (int i = 4; i < 7; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base);
                }
            }
            base += 5;
        }

        base = -20;
        for (int i = 0; i < 3; i++) {
            for (Player p: this.incomingRelationshipStatus.get(i)) {
                if (p != player) {
                    p.changeOpinionOf(player, base);
                }
            }
            base += 5;
        }

        base = 10;
        for (int i = 4; i < 7; i++) {
            for (Player p: player.incomingRelationshipStatus.get(i)) {
                if (p != this) {
                    p.changeOpinionOf(this, base);
                }
            }
            base += 5;
        }

        base = -20;
        for (int i = 0; i < 3; i++) {
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
        return this.outRelationships[player.getPlayerIndex()] >= 0;
    }

    public boolean canCriticize(Player player) {
        return this.outRelationships[player.getPlayerIndex()] <= 0;
    }

    public int getRoundBorn() {
        return roundBorn;
    }

    public boolean errorCheckIncomingRelationshipStatus() {
        int sum = 0;
        for (List<Player> relationshipStatus : incomingRelationshipStatus) {
            sum += relationshipStatus.size();
        }
        if (sum != 9 && sum != 0) {
            System.out.println("ERROR!!!");
            System.out.println("sum = " + sum);
            return  false;
        }
        return true;
    }
}


