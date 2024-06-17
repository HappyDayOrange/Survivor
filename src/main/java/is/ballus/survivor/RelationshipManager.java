package is.ballus.survivor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class RelationshipManager {
    private int[][] relationshipArray;
    private int[][][] relationshipArrayHistory;
    private int[] inRelationSum;
    private int[] outRelationSum;
    private Player[] players;
    private List<Player> playerList;
    private ArrayList<Player> errorList = new ArrayList<Player>();
    public int numPlayers;
    public int generationNum = 0;

    public RelationshipManager(int size, Player[] players) {
        this.relationshipArray = new int[size][size];
        this.relationshipArrayHistory = new int[size][][];
        this.numPlayers = size;
        this.inRelationSum = new int[size];
        this.outRelationSum = new int[size];
        this.players = players;
        playerList = new LinkedList<Player>(Arrays.asList(players));
    }

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
        */
        return true;
    }

    public void updateErrorList() {
        errorList.clear();
        for (int i = 0; i<numPlayers; i++) {
            if (players[i].getNumErrors() != 0) {
                errorList.add(players[i]);
            }
        }
    }

    public void printRelationships() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.println(players[i].getName() + ": " + Arrays.toString(players[i].getOutRelationships()) + " num errors: " + players[i].getNumErrors() + " missing values" + players[i].getErrorListValues() + " neutral Opinion: " + players[i].getErrorListPlayersAsString());
        }
    }

    /*
    public void generateOutRelationships(Player[] playerlist) {
        for (int i = 0; i < numPlayers; i++) {
            int t = 0;
            int r = 0;
            int p = 0;
            int x = 0;
            int sum = 0;
            int sumStream = 0;
            for (int k = 0; k < numPlayers; k++) {
                if (i == k) {
                    this.relationshipArray[k][i] = 0;
                    continue;
                }
                if (k == numPlayers - 1 || (k == numPlayers - 2 && i == numPlayers - 1)) {
                    this.relationshipArray[k][i] = -sum;
                    break;
                }
                t = Math.abs(sum) * 2;
                r = (10 - t) / 2;
                p = sum;
                x = (int) Math.round(Math.random() * (10 - t) - (r + p));
                this.relationshipArray[k][i] = x;
                sumStream = IntStream.of(relationshipArray[i]).sum();
                sum += x;
            }

        }
        updatePlayers(playerlist);
        updateInOutSums(playerlist);
    }
    */



    private void updatePlayers(Player[] list) {
        for (int i = 0; i < numPlayers; i++) {
            list[i].outRelationships = relationshipArray[i];
            list[i].calculateOutRelationSum();
            for (int k = 0; k < numPlayers; k++) {
                list[i].inRelationships[k] = relationshipArray[k][i];
                list[i].calculateInRelationSum();
            }
        }
    }

    public void updateInOutSums(Player[] list) {
        for (int i = 0; i < numPlayers; i++) {
            outRelationSum[i] = IntStream.of(list[i].outRelationships).sum();
            outRelationSum[i] = list[i].outRelationSum;
            inRelationSum[i] = IntStream.of(list[i].inRelationships).sum();
            inRelationSum[i] = list[i].inRelationSum;
        }
    }

    public static void main(String[] args) {

    }
}

