package is.ballus.survivor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameManager {
    public Player[] playerArr;
    public ArrayList<Player> remainingPlayers;
    public ArrayList<Player> eliminatedPlayers;
    public ArrayList<Player> playerList;
    public int numPlayers;
    public RelationshipManager relationshipManager;
    public int[] winners;
    public Settings settings;
    private Player humanPlayer;
    private Player firstRoundWinner;
    int generationNum = 0;
    int roundNum = 0;
    private List<List<Player>> playerActions = new ArrayList<>();

    public GameManager(int numPlayers) {
        this.setup(numPlayers);
        this.createPlayerActionsList();
    }

    public GameManager(GameManager manager) {
        this.generationNum = manager.generationNum;
        this.roundNum = manager.roundNum;
        this.winners = manager.winners;
        System.out.println("Generation number: " + generationNum);
        this.numPlayers = manager.numPlayers;
        this.settings = manager.settings;
        this.playerArr = new Player[numPlayers];
        this.playerList = new ArrayList<Player>();
        this.remainingPlayers = new ArrayList<Player>();
        this.eliminatedPlayers = new ArrayList<Player>();

        for (int i = 0; i < numPlayers; i++) {
            this.playerArr[i] = new Player(manager.playerArr[i], roundNum, this.playerList, this.playerArr);
            this.playerList.add(this.playerArr[i]);
            if (this.playerArr[i].isEliminated()) {
                this.eliminatedPlayers.add(this.playerArr[i]);
            } else {
                this.remainingPlayers.add(this.playerArr[i]);
            }
        }
        this.humanPlayer = manager.humanPlayer;
        createPlayerActionsList();
        System.out.println("##############");
        System.out.println("playerActions.size() " + playerActions.size());
        this.clearPlayerActions();
        System.out.println("playerActions.size() " + playerActions.size());
        this.relationshipManager = new RelationshipManager(numPlayers, playerArr);
        this.firstRoundWinner = playerArr[manager.firstRoundWinner.getPlayerIndex()];
    }

    public void setup(int numPlayers) {
        generationNum++;
        System.out.println("Generation number: " + generationNum);
        if (generationNum == 1000) {
            return;
        }
        this.settings = new Settings();
        this.playerArr = new Player[numPlayers];
        this.numPlayers = numPlayers;

        for (int i = 0; i < numPlayers; i++) {
            this.playerArr[i] = new Player(i, numPlayers, roundNum, settings, playerList, playerArr);
        }
        this.humanPlayer = playerArr[0];
        this.playerList = new ArrayList<>(Arrays.asList(playerArr));
        this.remainingPlayers = new ArrayList<>(Arrays.asList(playerArr));
        this.eliminatedPlayers = new ArrayList<Player>();
        this.relationshipManager = new RelationshipManager(numPlayers, playerArr);
        if (!relationshipManager.generateRelationships(playerList)) {
            setup(numPlayers);
        }
        this.firstRoundWinner = this.selectFirstRoundLeader();
    }

    public void simulateRound() {
        resetNominationsAndVotes();
        resetRelationshipChange();

        if (this.roundNum == 0) {
            handleFirstRound();
        } else if (this.remainingPlayers.size() == 2) {
            handleFinalRound();
        } else {
            handleRegularRound();
        }

        this.updateRelationshipStatuses();
        this.clearPlayerActions();
    }

    private void resetRelationshipChange() {
        for (Player player : playerArr) {
            player.resetRelationshipsChange();
        }
    }

    private void resetNominationsAndVotes() {
        for (Player player : playerArr) {
            player.setNominations(0);
            player.setVotes(0);
        }
    }

    private void handleFirstRound() {
        firstRoundWinner.setPlacement(1);
        ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
        temp.remove(firstRoundWinner);
        firstRoundWinner.selectNextPlace(temp);
        this.roundNum++;
    }

    private void handleFinalRound() {
        System.out.println("Test");
        this.performPlayerActions();
        Player winner = election(this.playerList, this.remainingPlayers);
        winner.setPlacement(1);
        ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
        temp.remove(winner);
        winner.selectNextPlace(temp);
        eliminatePlayer(temp.get(0));
        this.roundNum++;
    }

    private void handleRegularRound() {
        logPlayerBirthRounds();
        this.updateRelationshipStatuses();
        errorCheckAndPrintStatus();

        this.performPlayerActions();
        int[] nominees = voteForNominees(this.remainingPlayers);
        ArrayList<Player> nomineesAsList = new ArrayList<>();
        nomineesAsList.add(this.playerArr[nominees[0]]);
        nomineesAsList.add(this.playerArr[nominees[1]]);
        Player winner = election(this.remainingPlayers, nomineesAsList);
        winner.setPlacement(1);
        ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
        temp.remove(winner);
        winner.selectNextPlace(temp);
        eliminatePlayer(temp.get(0));
        this.roundNum++;
    }

    private void logPlayerBirthRounds() {
        for (Player player : playerList) {
            System.out.println(player.getName() + " was born round number " + player.getRoundBorn());
        }
        for (Player player : playerArr) {
            System.out.println(player.getName() + " was born round number " + player.getRoundBorn());
        }
    }

    private void errorCheckAndPrintStatus() {
        for (Player player : playerArr) {
            player.errorCheckIncomingRelationshipStatus();
            player.printIncomingRelationshipStatus();
        }
    }

    private void eliminatePlayer(Player loser) {
        loser.eliminatePlayer();
        System.out.println(loser.getName() + " is eliminated");
        this.remainingPlayers.remove(loser);
        this.eliminatedPlayers.add(loser);
    }

    public Player election(ArrayList<Player> players, ArrayList<Player> nominees) {
        System.out.println("Voting for leader");
        int[] results = new int[numPlayers];
        ArrayList<Player> temp = new ArrayList<>(players);
        temp.removeAll(nominees);
        System.out.println("temp.size() " + temp.size());
        for (int i = 0; i < players.size() - 2; i++ ) {
            int chosenPlayerIndex = temp.get(i).findFavorite(nominees);
            results[chosenPlayerIndex]++;
            if (playerArr[chosenPlayerIndex] == nominees.get(0)) {
                nominees.get(0).changeOpinionOf(temp.get(i), + 5);
                nominees.get(1).changeOpinionOf(temp.get(i), - 5);
            }
            else {
                nominees.get(1).changeOpinionOf(temp.get(i), + 5);
                nominees.get(0).changeOpinionOf(temp.get(i), - 5);
            }
        }
        for (int i = 0; i < numPlayers; i++) {
            playerArr[i].setVotes(results[i]);
        }
        int[] winner = findNominateWinners(results, true);
        return playerArr[winner[0]];
    }

    public int[] voteForNominees(ArrayList<Player> players) {
        System.out.println("Nominating leader");
        int[] results = new int[numPlayers];
        for (int i = 0; i < players.size(); i++ ) {
            results[players.get(i).findFavorite(players)]++;
        }
        for (int i = 0; i < numPlayers; i++) {
            playerArr[i].setNominations(results[i]);
        }
        System.out.println("Nominee Votes: " + Arrays.toString(results));

        results = findNominateWinners(results, true);
        return results;
    }

    private int[] countVotes(int[] nom) {
        System.out.println("countVotes()");
        int[] votes = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            int count = 0;
            for (int k = 0; k < numPlayers; k++) {
                if (i == nom[k]) {
                    count++;
                }
            }
            votes[i] = count;
        }
        return votes;
    }

    private int[] findNominateWinners(int[] votes, boolean verbose) {
        System.out.println("findNominateWinners()");
        int[] result = new int[2];
        int countFirst = 0;
        int firstIdx = 0;
        int countSecond = 0;
        int secondIdx = -1;
        for (int i = 0; i < numPlayers; i++) {
            if (votes[i] == 0) {
                continue;
            }
            if (votes[i] > countFirst) {
                if (verbose) {
                    System.out.println("New winner:" + i + " votes:" + votes[i]);
                    System.out.println("New runner-up:" + firstIdx + " votes:" + countFirst);
                    System.out.println("Old runner-up:" + secondIdx + " votes:" + countSecond);
                }
                countSecond = countFirst;
                secondIdx = firstIdx;
                countFirst = votes[i];
                firstIdx = i;
                continue;
            } else if (votes[i] == countFirst) {
                System.out.println("Tie first place");
                if (playerArr[i].getPlacement() < playerArr[firstIdx].getPlacement()) {
                    System.out.println("firstIdx placement: " + playerArr[firstIdx].getPlacement() + " challenger placement: "  + playerArr[i].getPlacement());
                    System.out.println("New winner:" + playerArr[i].getName() + " votes:" + votes[i] + " old winner:" + playerArr[firstIdx].getName() + " votes:" + countFirst);
                    countSecond = countFirst;
                    secondIdx = firstIdx;
                    firstIdx = i;
                    continue;
                }
            }
            if (votes[i] > countSecond) {
                if (verbose) {
                    System.out.println("New runner-up:" + playerArr[i].getName() + " votes:" + votes[i] + " old runner up:" + playerArr[secondIdx].getName() + " votes:" + countSecond);
                }
                countSecond = votes[i];
                secondIdx = i;
            } else if (votes[i] == countSecond) {
                System.out.println("Tie second place");
                if (playerArr[i].getPlacement() < playerArr[secondIdx].getPlacement()) {
                    if(verbose) {
                        System.out.println("Old runner up placement: " + playerArr[secondIdx].getPlacement() + " challenger placement: "  + playerArr[i].getPlacement());
                        System.out.println("New runner-up:" + playerArr[i].getName() + " votes:" + votes[i] + " old runner-up:" + playerArr[secondIdx].getName() + " votes:" + countSecond);
                        countSecond = votes[i];
                        secondIdx = i;
                    }
                }
            }
        }
        result[0] = firstIdx;
        result[1] = secondIdx;
        System.out.println("Votes:" + Arrays.toString(votes));
        System.out.println("winner:" +  playerArr[firstIdx].getName() + " votes:" + countFirst + " runner-up:" + playerArr[secondIdx].getName() + " votes:" + countSecond);
        return result;
    }

    public Player selectFirstRoundLeader() {
        return this.playerArr[(int)(Math.random() * playerArr.length)];
    }

    public Player[] getPlayers() {
        return this.playerArr;
    }

    public void praisePlayer (Player PraisingPlayer, Player PraisedPlayer) {
        PraisingPlayer.praisePlayer(PraisedPlayer);
    }

    public void performPlayerActions() {
        for (int i = 0; i < playerActions.size(); i++) {
            if (playerActions.get(i).isEmpty()) {
                continue;
            }
            Player player = playerActions.get(i).get(0);
            Player praisedPlayer = playerActions.get(i).get(1);
            Player criticizedPlayer = playerActions.get(i).get(2);

            System.out.println("Performing actions for player: " + player.getName());
            System.out.println("Praising player: " + praisedPlayer.getName());
            System.out.println("Criticizing player: " + criticizedPlayer.getName());

            // Debugging: Check relationship status before influence update
            System.out.println("Before updateInfluence: ");
            player.printIncomingRelationshipStatus();
            praisedPlayer.printIncomingRelationshipStatus();
            criticizedPlayer.printIncomingRelationshipStatus();

            player.updateInfluence();

            // Debugging: Check relationship status after influence update
            System.out.println("After updateInfluence: ");
            player.printIncomingRelationshipStatus();
            praisedPlayer.printIncomingRelationshipStatus();
            criticizedPlayer.printIncomingRelationshipStatus();

            player.updateInfluence();
            praisedPlayer.updateInfluence();
            criticizedPlayer.updateInfluence();

            player.praisePlayer(praisedPlayer);
            player.criticizePlayer(criticizedPlayer);
        }
    }

    public void clearPlayerActions() {
        for (List<Player> playerAction : playerActions) {
            playerAction.clear();
        }
    }

    public void setPlayerActions(Player player, Player praisedPlayer, Player criticizedPlayer) {
        int actingNum = player.getPlacement() - 1;
        Player p = playerArr[player.getPlayerIndex()];
        Player pp = playerArr[praisedPlayer.getPlayerIndex()];
        Player cp = playerArr[criticizedPlayer.getPlayerIndex()];

        System.out.println("TEST!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(player == playerArr[player.getPlayerIndex()]);
        System.out.println(praisedPlayer == playerArr[praisedPlayer.getPlayerIndex()]);
        System.out.println(criticizedPlayer == playerArr[criticizedPlayer.getPlayerIndex()]);

        System.out.println(p == playerArr[player.getPlayerIndex()]);
        System.out.println(pp == playerArr[praisedPlayer.getPlayerIndex()]);
        System.out.println(cp == playerArr[criticizedPlayer.getPlayerIndex()]);


        playerActions.get(actingNum).add(p);
        playerActions.get(actingNum).add(pp);
        playerActions.get(actingNum).add(cp);
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void updateRelationshipStatuses() {
        System.out.println("playerArr.length: " + playerArr.length);
        for (Player player : this.playerArr) {
            player.clearRelationshipStatus();
            player.setFinalVotes(0);
        }
        for (Player player : this.playerArr) {
            player.updateFavoriteRemainingPlayer(this.remainingPlayers);
        }
        for (Player player : this.playerArr) {
            player.updateRelationSums();
            //System.out.println(player.getName() + " Relationsum = " + player.getInRelationSum());
            player.updateInfluence();
            player.updateInfluenceForMe();
            player.updateDifferenceOfOpinionFromChosenPlayer();
            player.updateDifferenceOfOpinionFromFavorite();
            player.printIncomingRelationshipStatus();
            //System.out.println(player.getName() + " Influence= " + player.getInfluence());
            //System.out.println(player.getName() + " InfluenceRemaining= " + player.getInfluenceRemaining());
        }
    }

    public void createPlayerActionsList() {
        System.out.println("CREATING PLAYER ACTIONS");
        List<Player> action1 = new ArrayList<>();
        playerActions.add(action1);
        List<Player> action2 = new ArrayList<>();
        playerActions.add(action2);
        List<Player> action3 = new ArrayList<>();
        playerActions.add(action3);
        List<Player> action4 = new ArrayList<>();
        playerActions.add(action4);
        List<Player> action5 = new ArrayList<>();
        playerActions.add(action5);
        List<Player> action6 = new ArrayList<>();
        playerActions.add(action6);
        List<Player> action7 = new ArrayList<>();
        playerActions.add(action7);
        List<Player> action8 = new ArrayList<>();
        playerActions.add(action8);
        List<Player> action9 = new ArrayList<>();
        playerActions.add(action9);
        List<Player> action10 = new ArrayList<>();
        playerActions.add(action10);
        List<Player> action11 = new ArrayList<>();
        playerActions.add(action11);
    }


    public static void main(String[] args) {
        int[] numbeOfElectionWinners = new int[11];

        for (int i = 0; i < 1; i++) {
            int count = 0;
            GameManager gamemanager = new GameManager(10);
            System.out.println("Successful generation number: " + gamemanager.generationNum);
            /*
            System.out.println(Arrays.toString(gamemanager.winners));
            for (int k = 0; k < 10; k++) {
                if (gamemanager.winners[k] >= 0) {
                    count++;
                }
            }
            numbeOfElectionWinners[count]++;
             */
        }
        //System.out.println(Arrays.toString(numbeOfElectionWinners));
        /*
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("/////////////////////////////////////////////////////////////////////");
        System.out.println("/////////////////////////////////////////////////////////////////////");

        int inSum[] = new int[gamemanager.playerArr.length];
        int outSum[] = new int[gamemanager.playerArr.length];
        for (int i = 0; i < gamemanager.playerArr.length; i++) {
            System.out.print(Arrays.toString(gamemanager.playerArr[i].inRelationships) + " ");
            inSum[i] = IntStream.of(gamemanager.playerArr[i].inRelationships).sum();
            System.out.print("sum:" + inSum[i] + "\n");
        }
        int inSumSum = IntStream.of(inSum).sum();
        System.out.println("inSumSum:" + inSumSum + Arrays.toString(inSum));

        System.out.println("/////////////////////////////////////////////////////////////////////");
        for (int i = 0; i < gamemanager.playerArr.length; i++) {
            System.out.print(Arrays.toString(gamemanager.playerArr[i].outRelationships) + " ");
            outSum[i] = IntStream.of(gamemanager.playerArr[i].outRelationships).sum();
            System.out.print("sum:" + outSum[i] + "\n");
        }
        int outSumSum = IntStream.of(outSum).sum();
        System.out.println("outSumSum:" + outSumSum);
        */
    }
}
