package is.ballus.survivor;

import java.util.*;

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
    private Player latestLoser;
    int generationNum = 0;
    private int roundNum = 0;
    private List<List<Player>> playerActions = new ArrayList<>();

    public GameManager(Settings settings) {
        this.setup(settings);
        this.updateRelationshipStatuses();
        this.calculatePreviewSums();
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
        this.relationshipManager = new RelationshipManager(numPlayers, playerArr);
        this.firstRoundWinner = playerArr[manager.firstRoundWinner.getPlayerIndex()];
        if (manager.getLatestLoser() != null) {
            this.latestLoser = playerArr[manager.getLatestLoser().getPlayerIndex()];
        }
        this.updateRelationshipStatuses();
        this.playerActions = new ArrayList<>();
        for (List<Player> list : manager.playerActions) {
            playerActions.add(new ArrayList<Player>(list));
        }
        for (int i = 0; i < playerArr.length; i++) {
            Player player = playerArr[i];
            player.setPraisedPlayer(manager.playerArr[i].getPraisedPlayer());
            player.setCriticizedPlayer(manager.playerArr[i].getCriticizedPlayer());
        }
    }

    public void setup(Settings settings) {
        generationNum++;
        System.out.println("Generation number: " + generationNum);
        if (generationNum == 10000) {
            return;
        }
        this.settings = new Settings();
        this.numPlayers = settings.getNumPLayers();
        this.playerArr = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            this.playerArr[i] = new Player(i, numPlayers, roundNum, settings, playerList, playerArr);
        }
        this.humanPlayer = playerArr[0];
        this.playerList = new ArrayList<>(Arrays.asList(playerArr));
        this.remainingPlayers = new ArrayList<>(Arrays.asList(playerArr));
        this.eliminatedPlayers = new ArrayList<Player>();
        this.relationshipManager = new RelationshipManager(numPlayers, playerArr);
        if (!relationshipManager.generateRelationships(playerList)) {
            setup(settings);
        }
        this.firstRoundWinner = this.selectFirstRoundLeader();
    }

    public void simulateRound(int numberOfActions, boolean simulateAllActions) {
        if (this.roundNum == 0) {
            playerList.get(0).calculateHeadToHead(playerList);
        }
        this.resetNominationsAndVotes();
        this.resetPreviewStats();
        this.resetRelationshipChange();
        this.clearPlayerPicks();

        /*
        if (this.latestLoser != null) {
            Player[] result = this.latestLoser.strategy.praiseFavoriteRemainingAndCriticizeLeastFavoriteRemaining();
            this.setPlayerActions(latestLoser, result[0], result[1]);
        }
         */
        this.performPlayerActions(numberOfActions, simulateAllActions);

        if (this.roundNum == 0) {
            handleFirstRound();
        } else if (this.remainingPlayers.size() == 2) {
            handleFinalRound();
        } else {
            handleRegularRound();
        }

        this.updateRelationshipStatuses();
        this.calculatePreviewSums();
        if (simulateAllActions) {
            this.clearPlayerActions();
            this.playersPickingActions();
        }

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
        Player winner = election(this.playerList, this.remainingPlayers);
        winner.setPlacement(1);
        ArrayList<Player> temp = new ArrayList<>(this.remainingPlayers);
        temp.remove(winner);
        winner.selectNextPlace(temp);
        eliminatePlayer(temp.get(0));
        this.roundNum++;
    }

    private void handleRegularRound() {
        /*
        logPlayerBirthRounds();
        this.updateRelationshipStatuses();
        errorCheckAndPrintStatus();
         */
        ArrayList<Player> nomineesAsList = voteForNominees(this.remainingPlayers);
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
            player.errorCheckRelationshipStatus();
            player.printIncomingRelationshipStatus();
        }
    }

    private void eliminatePlayer(Player loser) {
        loser.eliminatePlayer();
        System.out.println(loser.getName() + " is eliminated");
        this.remainingPlayers.remove(loser);
        this.eliminatedPlayers.add(loser);
        this.latestLoser = loser;
    }

    public Player election(ArrayList<Player> players, ArrayList<Player> nominees) {
        System.out.println("Voting for leader");
        int[] results = new int[numPlayers];
        ArrayList<Player> temp = new ArrayList<>(players);
        temp.removeAll(nominees);
        for (int i = 0; i < players.size() - 2; i++ ) {
            int chosenPlayerIndex = temp.get(i).findFavorite(nominees, true);
            results[chosenPlayerIndex]++;
            if (playerArr[chosenPlayerIndex] == nominees.get(0)) {
                nominees.get(0).changeOpinionOf(temp.get(i), + 10, false, false, true, false, false);
                nominees.get(1).changeOpinionOf(temp.get(i), - 10, false, false, true, false, false);
            }
            else {
                nominees.get(1).changeOpinionOf(temp.get(i), + 10, false, false, true, false, false);
                nominees.get(0).changeOpinionOf(temp.get(i), - 10, false,false, true, false, false);
            }
        }
        for (int i = 0; i < numPlayers; i++) {
            playerArr[i].setVotes(results[i]);
        }
        ArrayList<Player> winner = findNominateWinners(results, true);
        return winner.get(0);
    }

    public ArrayList<Player> voteForNominees(ArrayList<Player> players) {
        System.out.println("Nominating leader");
        int[] results = new int[numPlayers];
        for (int i = 0; i < players.size(); i++ ) {
            results[players.get(i).findFavorite(players, true)]++;
        }
        for (int i = 0; i < numPlayers; i++) {
            playerArr[i].setNominations(results[i]);
        }
        System.out.println("Nominee Votes: " + Arrays.toString(results));

        return findNominateWinners(results, true);

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

    public ArrayList<Player> findNominateWinners(int[] votes, boolean verbose) {
        int max = Arrays.stream(votes).max().getAsInt();
        ArrayList<Player> result = new ArrayList<>();
        ArrayList<Player> temp = new ArrayList<>();

        for (int i = 0; i < votes.length; i++) {
            if (votes[i] == max) {
                result.add(playerArr[i]);
            }
        }

        if (result.size() == 1) {
            votes[result.get(0).getPlayerIndex()] = 0;
            max = Arrays.stream(votes).max().getAsInt();
            for (int i = 0; i < votes.length; i++) {
                if (votes[i] == max) {
                    temp.add(playerArr[i]);
                }
            }
            if (temp.size() == 1) {
                result.addAll(temp);
                return result;
            }
            temp = latestLoserTieBreaker(temp);
            result.add(temp.get(0));
            return result;
        }
        return latestLoserTieBreaker(result);
    }

    public ArrayList<Player> latestLoserTieBreaker(ArrayList<Player> playersToChooseFrom) {
        if (latestLoser != null) {
            ArrayList<Player> result = new ArrayList<>();
            result.add(playerArr[latestLoser.findFavorite(playersToChooseFrom, false)]);
            playersToChooseFrom.removeAll(result);
            result.add(playerArr[latestLoser.findFavorite(playersToChooseFrom, false)]);
            return result;
        }
        return placementTieBreaker(playersToChooseFrom);
    }

    private ArrayList<Player>  placementTieBreaker(ArrayList<Player> playersToChooseFrom) {
        Collections.sort(playersToChooseFrom, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getPlacement(), p2.getPlacement());
            }
        });
        if (playersToChooseFrom.size() == 2) {
            return playersToChooseFrom;
        }
        return new ArrayList<>(playersToChooseFrom.subList(0, 2));
    }
/*
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

 */

    public Player selectFirstRoundLeader() {
        return this.playerArr[(int)(Math.random() * playerArr.length)];
    }

    public Player[] getPlayers() {
        return this.playerArr;
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public ArrayList<Player> getRemainingPlayers() {
        return this.remainingPlayers;
    }

    public void praisePlayer (Player PraisingPlayer, Player PraisedPlayer) {
        PraisingPlayer.praisePlayer(PraisedPlayer, false);
    }

    public void performPlayerActions(int numberOfActionsToSimulate, boolean simulateAll) {
        if (simulateAll) {
            numberOfActionsToSimulate = playerActions.size() - 1;
        }
        System.out.println("Round number " + roundNum);
        System.out.println("playerActions.size() " + playerActions.size());
        System.out.println("numberOfActionsToSimulate " + numberOfActionsToSimulate);
        for (int i = 0; i <= numberOfActionsToSimulate; i++) {
            List<Player> playerAction = playerActions.get(i);
            if (!playerAction.isEmpty()) {
                Player player = playerArr[playerAction.get(0).getPlayerIndex()];
                Player praisedPlayer = playerArr[playerAction.get(1).getPlayerIndex()];
                Player criticizedPlayer = playerArr[playerAction.get(2).getPlayerIndex()];

                System.out.println("Performing actions for player: " + player.getName());
                System.out.println("Praises player: " + praisedPlayer.getName());
                System.out.println("Criticizes player: " + criticizedPlayer.getName());

                clearRelationshipStatusesOfAllPlayers();
                updateRelationshipStatuses();

                player.praisePlayer(praisedPlayer, false);
                player.criticizePlayer(criticizedPlayer, false);

                System.out.println("After Performing actions for player: " + player.getName());
                System.out.println("He Praised : " + player.getPraisedPlayerAsString());
                System.out.println("He Criticized: " + player.getCriticizedPlayerAsString());
            }
        }
    }

    public void clearPlayerActions() {
        for (List<Player> playerAction : playerActions) {
            playerAction.clear();
        }
    }

    public void clearPlayerPicks() {
        for (Player player : playerArr) {
            player.clearPicks();
        }
    }

    public void clearRelationshipStatusesOfAllPlayers() {
        for (Player player : this.playerArr) {
            player.clearRelationshipStatus();
        }
    }

    public void clearChosenPlayerForAllPlayers() {
        for (Player player : this.playerArr) {
            player.clearChosenPlayer();
        }
    }


    public void setPlayerActions(Player player, Player praisedPlayer, Player criticizedPlayer) {
        int actingNum = 0;
        if (!player.isEliminated()) {
            actingNum = player.getPlacement();
        }
        System.out.println(player.getName() +  " will praise " + praisedPlayer.getName() + " and criticize " + criticizedPlayer.getName() + " acting number: " + actingNum);

        Player p = playerArr[player.getPlayerIndex()];
        Player pp = playerArr[praisedPlayer.getPlayerIndex()];
        Player cp = playerArr[criticizedPlayer.getPlayerIndex()];
        
        playerActions.get(actingNum).add(p);
        playerActions.get(actingNum).add(pp);
        playerActions.get(actingNum).add(cp);
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void updateRelationshipStatuses() {
        clearRelationshipStatusesOfAllPlayers();
        for (Player player : this.playerArr) {
            player.setFinalVotes(0);
            player.clearHeadToHeadStats();
        }
        playerList.get(0).calculateHeadToHead(playerList);
        for (Player player : this.playerArr) {
            player.updateFavoriteRemainingPlayer(this.remainingPlayers);
            player.calculateHeadToHeadWinsAndSum();
        }
        for (Player player : this.playerArr) {
            player.updateRelationSums();
            //System.out.println(player.getName() + " Relationsum = " + player.getInRelationSum());
            player.updateInfluence();
            player.updateDifferenceOfOpinionFromChosenPlayer();
            player.updateDifferenceOfOpinionFromFavorite();
            //System.out.println(player.getName() + " Influence= " + player.getInfluence());
            //System.out.println(player.getName() + " InfluenceRemaining= " + player.getInfluenceRemaining());
        }
        for (Player player : this.playerArr) {
        }
    }

    public void calculatePreviewSums() {
        for (Player player : playerArr) {
            for (Player playerToEvaluate : playerArr) {
                player.praisePreview(playerToEvaluate);
                player.criticizePreview(playerToEvaluate);
            }
        }
    }

    public void resetPreviewStats() {
        for (Player player : playerArr) {
            player.resetPreviews();
            player.resetPreviewSums();
        }
    }

    public void createPlayerActionsList() {
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
        List<Player> action12 = new ArrayList<>();
        playerActions.add(action12);
    }

    public void playersPickingActions() {
        for (Player player : remainingPlayers) {
            Player[] picks = player.pickPlayers();
            if (picks == null) {
                continue;
            }
            this.setPlayerActions(player, picks[0], picks[1]);
        }
    }

    public Player getLatestLoser() {
        return this.latestLoser;
    }

    public int getRoundNUmber() {
        return roundNum;
    }


    public static void main(String[] args) {
        int[] numbeOfElectionWinners = new int[11];

        for (int i = 0; i < 1; i++) {
            int count = 0;
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
