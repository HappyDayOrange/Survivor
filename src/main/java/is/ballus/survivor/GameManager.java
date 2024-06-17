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
    int generationNum = 0;
    int roundNum = 0;
    private List<List<Player>> playerActions = new ArrayList<>();

    public GameManager(int numPlayers) {
        this.setup(numPlayers);
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
            this.remainingPlayers.add(this.playerArr[i]);
        }
        this.humanPlayer = manager.humanPlayer;
        this.playerActions = manager.playerActions;
        this.clearPlayerActions();
        this.relationshipManager = new RelationshipManager(numPlayers, playerArr);
    }

    public void setup(int numPlayers) {
        generationNum++;
        System.out.println("Generation number: " + generationNum);
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

    public void simulateRound() {
        for (int i = 0; i < numPlayers; i++) {
            playerArr[i].setNominations(0);
            playerArr[i].setVotes(0);
        }
        this.updateRelationshipStatuses();
        if (this.roundNum == 0) {
            Player winner = this.selectFirstRoundLeader();
            winner.setPlacement(1);
            ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
            winner.selectNextPlace(temp);
            this.roundNum++;
        } else if (this.remainingPlayers.size() == 2) {
            System.out.println("Test");
            this.performPlayerActions();
            Player winner = election(this.playerList, this.remainingPlayers);
            winner.setPlacement(1);
            ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
            winner.selectNextPlace(temp);
            Player loser = temp.get(0);
            loser.eliminatePlayer();
            System.out.println(loser.getName() + " is eliminated");
            this.remainingPlayers.remove(loser);
            this.eliminatedPlayers.add(loser);
            this.roundNum++;
        } else {
            for (Player player : playerList) {
                System.out.println(player.getName() + " was born round number " + player.getRoundBorn());
            }
            for (Player player : playerArr) {
                System.out.println(player.getName() + " was born round number " + player.getRoundBorn());
            }


            this.performPlayerActions();
            int[] nominees = voteForNominees(this.remainingPlayers);
            ArrayList<Player> nomineesAsList = new ArrayList<Player>();
            nomineesAsList.add(this.playerArr[nominees[0]]);
            nomineesAsList.add(this.playerArr[nominees[1]]);
            Player winner = election(this.remainingPlayers, nomineesAsList);
            winner.setPlacement(1);
            ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
            winner.selectNextPlace(temp);
            Player loser = temp.get(0);
            loser.eliminatePlayer();
            System.out.println(loser.getName() + " is eliminated");
            this.remainingPlayers.remove(loser);
            this.eliminatedPlayers.add(loser);
            this.roundNum++;
        }
        this.clearPlayerActions();
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

            System.out.println("Test!!!!");
            System.out.println(player == playerArr[player.getPlayerIndex()]);
            System.out.println(praisedPlayer == playerArr[praisedPlayer.getPlayerIndex()]);
            System.out.println(criticizedPlayer == playerArr[criticizedPlayer.getPlayerIndex()]);

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
        Player p = playerList.get(player.getPlayerIndex());
        Player pp = playerList.get(praisedPlayer.getPlayerIndex());
        Player cp = playerList.get(criticizedPlayer.getPlayerIndex());

        System.out.println("TEST!!!!!!!!!!!!!!!!!!!!!");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println(playerArr[i] == playerList.get(i));
        }


        playerActions.get(actingNum).add(p);
        playerActions.get(actingNum).add(pp);
        playerActions.get(actingNum).add(cp);
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void updateRelationshipStatuses() {
        for (Player player : this.playerList) {
            player.clearRelationshipStatus();
        }
        for (Player player : this.playerList) {
            player.updateRelationSums();
            //System.out.println(player.getName() + " Relationsum = " + player.getInRelationSum());
            player.updateInfluence();
            player.printIncomingRelationshipStatus();
            //System.out.println(player.getName() + " Influence= " + player.getInfluence());
            //System.out.println(player.getName() + " InfluenceRemaining= " + player.getInfluenceRemaining());
        }
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
