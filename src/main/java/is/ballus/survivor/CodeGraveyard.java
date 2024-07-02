package is.ballus.survivor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeGraveyard

{
        /*
    public boolean generateRelationships(ArrayList<Player> p) {
        int v = -20;
        int counter = 0;
        for (int k = 0; k < numPlayers - 1; k++) {
            int lastPlayerIndex = -1;
            ArrayList<Player> tempList = new ArrayList<>(p);
            while (!tempList.isEmpty()) {
                if (!tempList.get(0).setInitialRelations(tempList, v, lastPlayerIndex)) {
                    return false;
                }
                System.out.println(++counter);
            }
            System.out.println("Finished");
            v += 5;
        }
        for (Player player : playerList) {
            player.updateRelationSums();
            System.out.println(player.getName() + " Relationsum= " + player.getInRelationSum());
        }
        for (int i = 0; i < numPlayers; i++) {
            players[i].updateErrorListPlayers(p);
        }
        //generationNum++;
        //System.out.println("Generation Number: " + generationNum);
        updateErrorList();
        printRelationships();
        /*
        if (generationNum <=2) {
            fixErrors();
        }
        return true;
    }
     */

    /*
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
     */
        /*
    public void simulateRound() {
        for (int i = 0; i < numPlayers; i++) {
            playerArr[i].setNominations(0);
            playerArr[i].setVotes(0);
        }

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

            this.updateRelationshipStatuses();
            for (Player player: playerArr) {
                player.errorCheckIncomingRelationshipStatus();
                player.printIncomingRelationshipStatus();
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
        this.updateRelationshipStatuses();

        this.clearPlayerActions();
    }
     */

        /*
    public void updateInfluenceForMe() {
        for (Player player: incomingRelationshipStatus.get(0) ) {
            int myInfluence = this.influence - 20;
            int myInfluenceRemaining = this.influenceRemaining - 20;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }

        for (Player player: incomingRelationshipStatus.get(1) ) {
            int myInfluence = this.influence - 15;
            int myInfluenceRemaining = this.influenceRemaining - 15;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }

        for (Player player: incomingRelationshipStatus.get(2) ) {
            int myInfluence = this.influence - 10;
            int myInfluenceRemaining = this.influenceRemaining - 10;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }

        for (Player player: incomingRelationshipStatus.get(6) ) {
            int myInfluence = this.influence + 20;
            int myInfluenceRemaining = this.influenceRemaining + 20;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }

        for (Player player: incomingRelationshipStatus.get(5) ) {
            int myInfluence = this.influence + 15;
            int myInfluenceRemaining = this.influenceRemaining + 15;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }

        for (Player player: incomingRelationshipStatus.get(4) ) {
            int myInfluence = this.influence + 10;
            int myInfluenceRemaining = this.influenceRemaining + 10;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }

        for (Player player: incomingRelationshipStatus.get(3) ) {
            int myInfluence = this.influence;
            int myInfluenceRemaining = this.influenceRemaining;
            this.setInfluenceForMe(player, myInfluence);
            this.setInfluenceForMeRemaining(player, myInfluenceRemaining);
        }
    }
    */

    /*
private void elimanteZeroVotes(int[] votes, int[] candidates, int[] pool) {
        /*
        System.out.println("elimanteZeroVotes()");
        int counter = 0;
        for (int i = 0; i < numPlayers; i++) {
            if (votes[i] == 0 && pool[i] < 0) {
                votes[i] = -1;
                pool[i] -= 1;
                if (pool[i] < 0) {
                    candidates[0]--;
                }
            }
        }
        candidates[0] = numPlayers - counter;
}

private int[] findFavorite(int[] pool) {
    System.out.println("findFavorite()");
    int[] votes = new int[numPlayers];
    int temp = 0;
    int idx = -1;
    for (int i = 0; i < numPlayers; i++) {
        int max = -100;
        for (int k = 0; k < numPlayers; k++) {
            if (i == k || pool[k] <= -1 || pool[k] >= 100) {
                continue;
            }
            temp = relationshipArray[i][k];
            if (temp > max) {
                max = temp;
                idx = k;
            } else if (temp == max) {
                if (relationshipArray[k][i] > relationshipArray[idx][i]) {
                    max = temp;
                    idx = k;
                    System.out.println("First tiebreaker needed");
                } else if (relationshipArray[k][i] == relationshipArray[idx][i]) {
                    if (outRelationSum[k] > outRelationSum[idx]) {
                        max = temp;
                        idx = k;
                        System.out.println("Second tiebreaker needed");
                    } else if (outRelationSum[k] == outRelationSum[idx]) {
                        System.out.println("TIEBREAKER FAILURE");
                    }
                }
            }
        }
        votes[i] = idx;
    }
    return votes;
}



        private int tiebreaks(int[] votes, int c1, int c2, int p, boolean verbose) {
        if (verbose) {
            System.out.println("Votes:" + Arrays.toString(votes));
            System.out.println("Tiebreak " + p + " place count:" + votes[c2] + " holder:" + c1 + " contender:" + c2);
            System.out.println("Tiebreak scores:" + outRelationSum[c1] + " and " + outRelationSum[c2]);
        }
        if (outRelationSum[c1] > outRelationSum[c2]) {
            System.out.print("winner:" + c1 + "\n");
            return c1;
        } else if ((outRelationSum[c2] == outRelationSum[c1])) {
            System.out.print("TIEBREAK FAILURE!!!!\n");
            return c1;
        }
        System.out.print("winner:" + c2 + "\n");
        return c2;
    }
        private boolean findNominateLosers(int[] votes, int[] candidates, int[] pool) {
        System.out.println("findNominateLosers()");
        ArrayList<Integer> losers = new ArrayList<>();
        int min = 100;
        for (int i = 0; i < numPlayers; i++) {
            if (pool[i] >= 0) {
                if (votes[i] <= 0) {
                    pool[i] -= 1;
                    if (pool[i] < 0) {
                        candidates[0]--;
                    }
                    continue;
                }
                if (votes[i] < min) {
                    losers.clear();
                    losers.add(i);
                    min = votes[i];
                } else if (votes[i] == min) {
                    losers.add(i);
                }
            }
        }
        System.out.println("candidates: " + candidates[0] + " losers.size(): " + losers.size());
        if (candidates[0] - losers.size() <= 1) {
            if (candidates[0] > 2) {
                if (losers.size() == 2) {
                    int loser = tiebreaks(votes, losers.get(0), losers.get(1), -1, true);
                    pool[loser] = -1;
                    candidates[0]--;
                    return false;
                }
                int[] x = findNominateWinners(votes, false);

                pool[x[0]] += 100;
                //pool[x[1]] += 10;
                return false;
            } else {
                return true;
            }
        }

        for (int i = 0; i < losers.size(); i++) {
            int loserIdx = losers.get(i);
            pool[loserIdx] -= 1;
            candidates[0]--;
        }


        return false;
    }

        public int[] election(int[] pool, int[] candidates) {
        System.out.println("election()");
        int[] nominations = findFavorite(pool);
        System.out.println("nominations:" + Arrays.toString(nominations));
        int[] votes = countVotes(nominations);
        System.out.println("votes:" + Arrays.toString(votes));
        elimanteZeroVotes(votes, candidates, pool);
        if (!findNominateLosers(votes, candidates, pool)) {
            System.out.println("candidates:" + candidates[0]);
            System.out.println("pool:" + Arrays.toString(pool));
            election(pool, candidates);
        }
        //int winners = find(votes, true);
        return pool;
    }

        public void fixErrors() {
        System.out.println("errorList.size(): " + errorList.size());
        for (int i = 0; i < errorList.size(); i++) {
            List<Player> tempList = new ArrayList<>(errorList);
            for (int k = 0; k < errorList.size(); k++) {
                tempList.removeAll(errorList.get(i).getErrorListPlayers());
                if (errorList.get(i) != errorList.get(k)) {
                    errorList.get(i).setOutRelationships(errorList.get(k), -99);
                    errorList.get(i).setInRelationships(errorList.get(k), -99);
                }
            }
            errorList.get(i).updateErrorListPlayers(playerList);
        }
        System.out.println("fixErrors()");
        printRelationships();
        generateRelationships(errorList);
    }
     */

        /*
    public void updateInfluence() {
        int[] results = new int[2];
        for (List<Player> list : this.incomingRelationshipStatus) {
            list.clear();
        }
        for (int i = 0; i < numPlayers; i++) {
            if (this.getPlayerIndex() != i) {
                if (this.inRelationships[i] >= 100) {
                    this.incomingRelationshipStatus.get(0).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(0).add(this);
                    if (this.playerArr[i].isEliminated()) {
                        results[0] += 20;
                        continue;
                    }
                    results[0] += 20;
                    results[1] += 20;
                } else if (this.inRelationships[i] >= 50) {
                    this.incomingRelationshipStatus.get(1).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(1).add(this);
                    if (this.playerArr[i].isEliminated()) {
                        results[0] += 15;
                        continue;
                    }
                    results[0] += 15;
                    results[1] += 15;
                } else if (this.inRelationships[i] >= 25) {
                    this.incomingRelationshipStatus.get(2).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(2).add(this);
                    if (this.playerArr[i].isEliminated()) {
                        results[0] += 10;
                        continue;
                    }
                    results[0] += 10;
                    results[1] += 10;
                } else if (this.inRelationships[i] <= -100) {
                    this.incomingRelationshipStatus.get(6).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(6).add(this);
                    if (this.playerArr[i].isEliminated()) {
                        results[0] -= 20;
                        continue;
                    }
                    results[0] -= 20;
                    results[1] -= 20;
                } else if (this.inRelationships[i] <= -50) {
                    this.incomingRelationshipStatus.get(5).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(5).add(this);
                    if (playerArr[i].isEliminated()) {
                        results[0] -= 15;
                        continue;
                    }
                    results[0] -= 15;
                    results[1] -= 15;
                } else if (this.inRelationships[i] <= -25) {
                    this.incomingRelationshipStatus.get(4).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(4).add(this);
                    if (this.playerArr[i].isEliminated()) {
                        results[0] -= 10;
                        continue;
                    }
                    results[0] -= 10;
                    results[1] -= 10;
                }
                else {
                    this.incomingRelationshipStatus.get(3).add(this.playerArr[i]);
                    this.playerArr[i].outcomingRelationshipStatus.get(3).add(this);
                }
            }
        }
        setInfluence(results[0]);
        setInfluenceRemaining(results[1]);
    }

     */

    /*

    public void updateInfluence() {
        int totalInfluence = 0;
        int totalInfluenceRemaining = 0;

        for (List<Player> list : this.incomingRelationshipStatus) {
            list.clear();
        }


        for (int i = 0; i < numPlayers; i++) {
            if (this.getPlayerIndex() == i) {
                continue; // Skip self
            }

            int influenceDelta = 0;
            int influenceRemainingDelta = 0;

            if (this.inRelationships[i] >= 100) {
                this.incomingRelationshipStatus.get(0).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(0).add(this);
                influenceDelta = 20;
            } else if (this.inRelationships[i] >= 50) {
                this.incomingRelationshipStatus.get(1).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(1).add(this);
                influenceDelta = 15;
            } else if (this.inRelationships[i] >= 25) {
                this.incomingRelationshipStatus.get(2).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(2).add(this);
                influenceDelta = 10;
            } else if (this.inRelationships[i] <= -100) {
                this.incomingRelationshipStatus.get(6).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(6).add(this);
                influenceDelta = -20;
            } else if (this.inRelationships[i] <= -50) {
                this.incomingRelationshipStatus.get(5).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(5).add(this);
                influenceDelta = -15;
            } else if (this.inRelationships[i] <= -25) {
                this.incomingRelationshipStatus.get(4).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(4).add(this);
                influenceDelta = -10;
            } else {
                this.incomingRelationshipStatus.get(3).add(this.playerArr[i]);
                this.playerArr[i].outcomingRelationshipStatus.get(3).add(this);
                continue;
            }

            if (this.playerArr[i].isEliminated()) {
                influenceRemainingDelta = influenceDelta;
            } else {
                influenceRemainingDelta = influenceDelta;
            }

            totalInfluence += influenceDelta;
            totalInfluenceRemaining += influenceRemainingDelta;
        }

        setInfluence(totalInfluence);
        setInfluenceRemaining(totalInfluenceRemaining);
    }

     */
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(6, 3, 8));

        List<Integer> tempList = new ArrayList<>(list1);
        boolean result = tempList.retainAll(list2);
        System.out.println("");
        System.out.println("Any common elements: " + result);
    }

}

