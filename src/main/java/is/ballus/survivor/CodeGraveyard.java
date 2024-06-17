package is.ballus.survivor;

public class CodeGraveyard

{
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
}

