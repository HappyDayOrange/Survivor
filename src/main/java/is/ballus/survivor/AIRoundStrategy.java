package is.ballus.survivor;

import java.util.ArrayList;

public class AIRoundStrategy {
    private Player thisPlayer;
    private  Player playerToPraise;
    private  Player playerToCriticize;
    private GameManager currentRound;
    private GameManager predictedRound;
    private ArrayList<Player> currentPlayers;
    private ArrayList<Player> predictedPlayers;








    public AIRoundStrategy(GameManager mainGameManager, GameManager gameManagerPrediction) {
        this.currentRound = mainGameManager;
        this.predictedRound = gameManagerPrediction;
        this.currentPlayers = mainGameManager.getPlayerList();
        this.predictedPlayers = gameManagerPrediction.getPlayerList();

    }






    public Player[] pickRandomToPraiseAndCriticize(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        ArrayList<Player> canPraisePlayers = new ArrayList<>();
        ArrayList<Player> canCriticizePlayers = new ArrayList<>();
        Player[] result = new Player[2];

        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canPraise(player)) {
                canPraisePlayers.add(player);
            }
            if (decidingPlayer.canCriticize(player)) {
                canCriticizePlayers.add(player);
            }
        }

        playerToPraise = canPraisePlayers.get((int)(Math.random() * canPraisePlayers.size()));
        canCriticizePlayers.remove(playerToPraise);
        playerToCriticize = canCriticizePlayers.get((int)(Math.random() * canCriticizePlayers.size()));

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

        playerToPraise = canPraisePlayers.get((int)(Math.random() * canPraisePlayers.size()));

        return playerToPraise;
    }

    public Player pickRandomToCriticize(Player decidingPlayer, ArrayList<Player> playersToPickFrom) {
        ArrayList<Player> canCriticizePlayers = new ArrayList<>();

        for (Player player : playersToPickFrom) {
            if (decidingPlayer.canCriticize(player)) {
                canCriticizePlayers.add(player);
            }
        }

        playerToCriticize = canCriticizePlayers.get((int)(Math.random() * canCriticizePlayers.size()));

    return playerToCriticize;
    }
}

