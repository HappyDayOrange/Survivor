package is.ballus.survivor;

import java.util.*;
import java.util.stream.IntStream;

public class RelationshipManager {
    private int[][] relationshipArray;
    private int[][][] relationshipArrayHistory;
    private int[] inRelationSum;
    private int[] outRelationSum;
    private Player[] players;
    private List<Player> playerList;
    private List<List<Player>> generateRelationshipsHelper = new ArrayList<>();
    private ArrayList<Player> errorList = new ArrayList<Player>();
    public int numPlayers;
    public int generationNum = 0;
    private int errorCounter = 0;

    public RelationshipManager(int size, Player[] players) {
        generationNum++;
        this.relationshipArray = new int[size][size];
        this.relationshipArrayHistory = new int[size][][];
        this.numPlayers = size;
        this.inRelationSum = new int[size];
        this.outRelationSum = new int[size];
        this.players = players;
        playerList = new LinkedList<Player>(Arrays.asList(players));
        List<Player> pick0 = new ArrayList<>();
        generateRelationshipsHelper.add(pick0);
        List<Player> pick1 = new ArrayList<>();
        generateRelationshipsHelper.add(pick1);
        List<Player> pick2 = new ArrayList<>();
        generateRelationshipsHelper.add(pick2);
        List<Player> pick3 = new ArrayList<>();
        generateRelationshipsHelper.add(pick3);
        List<Player> pick4 = new ArrayList<>();
        generateRelationshipsHelper.add(pick4);
        List<Player> pick5 = new ArrayList<>();
        generateRelationshipsHelper.add(pick5);
        List<Player> pick6 = new ArrayList<>();
        generateRelationshipsHelper.add(pick6);
        List<Player> pick7 = new ArrayList<>();
        generateRelationshipsHelper.add(pick7);
        List<Player> pick8 = new ArrayList<>();
        generateRelationshipsHelper.add(pick8);
    }
    public boolean generateRelationships(ArrayList<Player> p) {
        int counter = 0;
        ArrayList<Player> shuffledPlayerList = new ArrayList<>(p);
        Collections.shuffle(shuffledPlayerList);
        ArrayList<Player> drawList = new ArrayList<>(p);
        Collections.shuffle(drawList);
        ArrayList<Player> discardList = new ArrayList<>();
        ArrayList<Player> hasDrawnList = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            int value = -20;
            hasDrawnList.clear();
            Player picker = shuffledPlayerList.get(i);
            System.out.println("picker: " + picker.getName());
            ArrayList<Player> drawListWithoutPicker = new ArrayList<>(drawList);
            addDiscardToDraw(drawListWithoutPicker, drawList, hasDrawnList, discardList);
            drawListWithoutPicker.remove(picker);
            /*
            System.out.println("drawListWithoutPicker");
            for (Player player : drawListWithoutPicker) {
                System.out.println(player.getName());
            }
             */
            for (int k = 0; k < 9; k++) {
                if ( value != 0) {
                    if (drawListWithoutPicker.isEmpty()) {
                        addDiscardToDraw(drawListWithoutPicker, drawList, hasDrawnList, discardList);
                        drawListWithoutPicker.remove(picker);
                    }
                if (!drawPlayer(picker, drawListWithoutPicker, drawList, hasDrawnList, discardList, value, k)) {
                    return false;
                    }
                }
                value += 5;
            }
            System.out.println(++counter);
            System.out.println("Finished");
        }
        for (Player player : playerList) {
            player.updateRelationSums();
            System.out.println(player.getName() + " inRelationsum= " + player.getInRelationSum());
            System.out.println(player.getName() + " outRelationsum= " + player.getOutRelationSum());
        }
        for (int i = 0; i < numPlayers; i++) {
            players[i].updateErrorListPlayers(p);
        }
        updateErrorList();
        printRelationships();
        System.out.println("SUCCESS!!!");
        return true;
    }

    public boolean drawPlayer(Player picker, ArrayList<Player> drawListWithoutPicker, ArrayList<Player> drawList, ArrayList<Player> hasDrawnList, ArrayList<Player> discardList, int value , int k) {
        if (errorCounter == 10) {
            System.out.println("GAVE UP!!!!");
            return false;
        }
        if (drawListWithoutPicker.isEmpty()) {
            addDiscardToDraw(drawListWithoutPicker, drawList, hasDrawnList, discardList);
            drawListWithoutPicker.remove(picker);
        }
        Player pick;
        if (!generateRelationshipsHelper.get(k).isEmpty() && drawListWithoutPicker.contains(generateRelationshipsHelper.get(k).get(0)) ) {
             pick = generateRelationshipsHelper.get(k).get(0);
            generateRelationshipsHelper.get(k).remove(pick);
            drawListWithoutPicker.remove(pick);
            drawList.remove(pick);
        } else {
            pick = drawListWithoutPicker.get(0);
            if (pick.getHasBeenPickedAsNumber(k)) {
                errorCounter++;
                drawListWithoutPicker.remove(pick);
                drawList.remove(pick);
                discardList.add(pick);
                System.out.println("removing " + pick.getName() + " and calling drawPlayer() again");
                return drawPlayer(picker, drawListWithoutPicker, drawList, hasDrawnList, discardList, value, k);
            }
            drawListWithoutPicker.remove(pick);
            drawList.remove(pick);
            generateRelationshipsHelper.get(8 - k).add(pick);
        }
        System.out.println(picker.getName() + " draws: " + pick.getName());
        picker.changeOpinionOfMe(pick, value, false);
        discardList.add(pick);
        pick.setHasBeenPickedAsNumber(k);
        hasDrawnList.add(pick);
        errorCounter = 0;
        return true;
    }

    public void addDiscardToDraw(ArrayList<Player> drawListWithoutPicker, ArrayList<Player> drawList, ArrayList<Player> hasDrawnList, ArrayList<Player> discardList) {
        Collections.shuffle(discardList);
        System.out.println("discardList");
        for (Player player : discardList) {
            System.out.println(player.getName());
        }
        drawListWithoutPicker.addAll(discardList);
        System.out.println("drawList before merge");
        for (Player player : drawList) {
            System.out.println(player.getName());
        }
        drawList.addAll(discardList);
        System.out.println("drawList after merge");
        for (Player player : drawList) {
            System.out.println(player.getName());
        }
        discardList.clear();
        drawList.removeAll(hasDrawnList);
        drawListWithoutPicker.removeAll(hasDrawnList);
        discardList.addAll(hasDrawnList);
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

