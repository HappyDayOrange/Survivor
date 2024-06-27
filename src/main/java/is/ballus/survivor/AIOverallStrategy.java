package is.ballus.survivor;

import java.util.ArrayList;

public class AIOverallStrategy {

    private Player owner;
    private Player playerToPraise;
    private Player playerToCriticize;
    private GameManager currentRound;
    private GameManager predictedRound;
    private ArrayList<Player> currentPlayers;
    private ArrayList<Player> predictedPlayers;
    private ArrayList<Player> remainingPlayers;
    private AIRoundStrategy roundStrategy;
    private int overallStrategyNumber;

    public AIOverallStrategy(GameManager mainGameManager, GameManager gameManagerPrediction, AIRoundStrategy roundStrategy, Player owner, int strategyNumber) {
        this.currentRound = mainGameManager;
        this.predictedRound = gameManagerPrediction;
        this.currentPlayers = mainGameManager.getPlayerList();
        this.predictedPlayers = gameManagerPrediction.getPlayerList();
        this.remainingPlayers = mainGameManager.getRemainingPlayers();
        this.owner = owner;
        this.roundStrategy = roundStrategy;
        this.overallStrategyNumber = strategyNumber;
    }






    public Player[] pickPlayers() {
        return switch (overallStrategyNumber) {
            case 0 -> null;
            case 1 -> alwaysPicksRandom();
            default -> null;
            // code block
        };
    }



    public Player[] alwaysPicksRandom() {
        return roundStrategy.pickRandomToPraiseAndCriticize(owner, currentPlayers);
    }







}
