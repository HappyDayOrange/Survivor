package is.ballus.survivor;

import java.util.ArrayList;
import java.util.Arrays;

public class AIRoundStrategy {
    private Player thisPlayer;
    private Player playerToPraise;
    private Player playerToCriticize;
    private GameManager currentRound;
    private GameManager predictedRound;
    private Player[] currentPlayerArr;
    private ArrayList<Player> currentPlayers;
    private ArrayList<Player> predictedPlayers;


    public AIRoundStrategy(GameManager mainGameManager, GameManager gameManagerPrediction) {
        this.currentRound = mainGameManager;
        this.predictedRound = gameManagerPrediction;
        this.currentPlayerArr = mainGameManager.getPlayers();
        this.currentPlayers = mainGameManager.getPlayerList();
        this.predictedPlayers = gameManagerPrediction.getPlayerList();

    }


    public Player[] pickRandomToPraiseAndCriticize(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        ArrayList<Player> canPraisePlayers = new ArrayList<>();
        ArrayList<Player> canCriticizePlayers = new ArrayList<>();
        Player[] result = new Player[2];

        /*
        System.out.println("pickRandomToPraiseAndCriticize");
        System.out.println("playersToPickFrom: ");

        for (Player player : playersToPickFrom) {
            System.out.print(player.getName() + " ");
        }
        System.out.println(" ");
         */

        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canPraise(player)) {
                canPraisePlayers.add(player);
                //System.out.println("Can praise: " + player.getName());
            }
            if (decidingPlayer.canCriticize(player)) {
                canCriticizePlayers.add(player);
                //System.out.println("Can criticize: " + player.getName());
            }
        }

        playerToPraise = canPraisePlayers.get((int) (Math.random() * canPraisePlayers.size()));
        canCriticizePlayers.remove(playerToPraise);
        playerToCriticize = canCriticizePlayers.get((int) (Math.random() * canCriticizePlayers.size()));

        result[0] = playerToPraise;
        result[1] = playerToCriticize;
        return result;
    }


    public Player pickRandomToPraise(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        ArrayList<Player> canPraisePlayers = new ArrayList<>();

        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canPraise(player)) {
                canPraisePlayers.add(player);
            }
        }
        if (canPraisePlayers.isEmpty()) {
            System.out.println("No one to Praise pickRandomToPraise");
            return null;
        }
        playerToPraise = canPraisePlayers.get((int) (Math.random() * canPraisePlayers.size()));

        return playerToPraise;
    }

    public Player pickRandomToCriticize(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        ArrayList<Player> canCriticizePlayers = new ArrayList<>();

        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canCriticize(player)) {
                canCriticizePlayers.add(player);
            }
        }

        if (canCriticizePlayers.isEmpty()) {
            System.out.println("No one to Criticize pickRandomToCriticize");
            return null;
        }

        playerToCriticize = canCriticizePlayers.get((int) (Math.random() * canCriticizePlayers.size()));

        return playerToCriticize;
    }

    public Player praiseFavorite(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        if (decidingPlayer.isEliminated()) {
            return currentPlayerArr[decidingPlayer.findFavorite(playersToPickFrom, false)];
        }
        ArrayList<Player> canPraisePlayers = new ArrayList<>();
        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canPraise(player)) {
                canPraisePlayers.add(player);
            }
        }
        return currentPlayerArr[decidingPlayer.findFavorite(canPraisePlayers, false)];
    }

    public Player criticizeLeastFavorite(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        if (decidingPlayer.isEliminated()) {
            return currentPlayerArr[decidingPlayer.findLeastFavorite(playersToPickFrom)];
        }
        ArrayList<Player> canCriticizePlayers = new ArrayList<>();
        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canCriticize(player)) {
                canCriticizePlayers.add(player);
            }
        }
        return currentPlayerArr[decidingPlayer.findLeastFavorite(canCriticizePlayers)];
    }

    public ArrayList<Player> praiseForMaxOverallOpinion(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {

        int[] temp = decidingPlayer.getPraiseInPreviewSum().clone();
        for (Player player : currentPlayers) {
            if (!decidingPlayer.canPraise(player) || !playersToPickFrom.contains(player)) {
                temp[player.getPlayerIndex()] = -1000;
            }
        }
        int max = Arrays.stream(temp).max().getAsInt();
        ArrayList<Player> result = new ArrayList<>();
        if (max == -1000) {
            System.out.println("NO Legal Pick praiseForMaxOverallOpinion");
            return result;
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == max) {
                result.add(currentPlayerArr[i]);
            }
        }
        return result;
    }


    public ArrayList<Player> criticizeForMaxOverallOpinion(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {

        int[] temp = decidingPlayer.getCriticizePreviewSum().clone();
        for (Player player : currentPlayers) {
            if (!decidingPlayer.canCriticize(player) || !playersToPickFrom.contains(player)) {
                temp[player.getPlayerIndex()] = -1000;
            }
        }
        int max = Arrays.stream(temp).max().getAsInt();
        ArrayList<Player> result = new ArrayList<>();
        if (max == -1000) {
            System.out.println("NO Legal Pick criticizeForMaxOverallOpinion");
            return result;
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == max) {
                result.add(currentPlayerArr[i]);
            }
        }
        return result;
    }

    public ArrayList<Player> praiseForMaxRemainingOpinion(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {

        int[] temp = decidingPlayer.getPraisePreviewInRemainingSum().clone();
        for (Player player : currentPlayers) {
            if (!decidingPlayer.canPraise(player) || !playersToPickFrom.contains(player)) {
                temp[player.getPlayerIndex()] = -1000;
            }
        }
        int max = Arrays.stream(temp).max().getAsInt();
        ArrayList<Player> result = new ArrayList<>();
        if (max == -1000) {
            System.out.println("NO Legal Pick praiseForMaxRemainingOpinion");
            return result;
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == max) {
                result.add(currentPlayerArr[i]);
            }
        }
        return result;
    }


    public ArrayList<Player> criticizeForMaxRemainingOpinion(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {

        int[] temp = decidingPlayer.getCriticizePreviewInRemainingSum().clone();
        for (Player player : currentPlayers) {
            if (!decidingPlayer.canCriticize(player) || !playersToPickFrom.contains(player)) {
                temp[player.getPlayerIndex()] = -1000;
            }
        }
        int max = Arrays.stream(temp).max().getAsInt();
        ArrayList<Player> result = new ArrayList<>();
        if (max == -1000) {
            System.out.println("NO Legal Pick criticizeForMaxRemainingOpinion");
            return result;
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == max) {
                result.add(currentPlayerArr[i]);
            }
        }
        return result;
    }

    public ArrayList<Player> findClosestToBecomingFavorite(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        System.out.println(decidingPlayer.getName()  +  " findClosestToBecomingFavorite");
        int[] temp = decidingPlayer.getDifferenceOfOpinionFromFavoriteArray().clone();
        for (Player player : currentPlayers) {
            if (temp[player.getPlayerIndex()] < 0 || !playersToPickFrom.contains(player) || player == decidingPlayer) {
                temp[player.getPlayerIndex()] = 1000;
            }
        }
        int min = Arrays.stream(temp).min().getAsInt();
        ArrayList<Player> result = new ArrayList<>();
        if (min == 1000) {
            System.out.println("NO Legal Pick criticizeForMaxRemainingOpinion");
            return result;
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == min) {
                result.add(currentPlayerArr[i]);
            }
        }
        for (Player player : result) {
            System.out.println(decidingPlayer.getName() + " Closest to becoming favorite of " + player.getName());
        }

        return result;
    }
}



