package is.ballus.survivor;

import java.util.ArrayList;

public class AIOverallStrategy {

    private Player owner;
    private Player playerToPraise;
    private Player playerToCriticize;
    private GameManager currentRound;
    private GameManager predictedRound;
    private Player[] currentPlayerArr;
    private ArrayList<Player> currentPlayers;
    private ArrayList<Player> predictedPlayers;
    private ArrayList<Player> remainingPlayers;
    private AIRoundStrategy roundStrategy;
    private int overallStrategyNumber;

    public AIOverallStrategy(GameManager mainGameManager, GameManager gameManagerPrediction, AIRoundStrategy roundStrategy, Player owner, int strategyNumber) {
        this.currentRound = mainGameManager;
        this.predictedRound = gameManagerPrediction;
        this.currentPlayerArr = mainGameManager.getPlayers();
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
            case 2 -> praiseFavoriteAndCriticizeLeastFavorite();
            case 3 -> praiseAndCriticizeForMaxOverallOpinion();
            default -> null;
            // code block
        };
    }



    public Player[] alwaysPicksRandom() {
        return roundStrategy.pickRandomToPraiseAndCriticize(owner, currentPlayers);
    }

    public Player[] praiseFavoriteRemainingAndCriticizeLeastFavoriteRemaining() {
        Player[] result = new Player[2];
        result[0] = roundStrategy.praiseFavorite(owner, remainingPlayers);
        result[1] = roundStrategy.criticizeLeastFavorite(owner, remainingPlayers);
        return result;
    }

    public Player[] praiseFavoriteAndCriticizeLeastFavorite() {
        Player[] result = new Player[2];
        result[0] = roundStrategy.praiseFavorite(owner, currentPlayers);
        result[1] = roundStrategy.criticizeLeastFavorite(owner, currentPlayers);
        return result;
    }

    public Player[] praiseAndCriticizeForMaxOverallOpinion() {
        System.out.println("praiseAndCriticizeForMaxOverallOpinion");
        ArrayList<Player> praiseCandidates = roundStrategy.praiseForMaxOverallOpinion(owner, currentPlayers);
        ArrayList<Player> criticizeCandidates = roundStrategy.criticizeForMaxOverallOpinion(owner, currentPlayers);
        System.out.println("praiseCandidates");
        for (Player player : praiseCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.print("\n");
        System.out.println("criticizeCandidates");
        for (Player player : criticizeCandidates) {
            System.out.print(player.getName() + " ");
        }

        if (praiseCandidates.size() == 1 && criticizeCandidates.size() == 1) {
            System.out.println("praiseCandidates.size() == 1 && criticizeCandidates.size() == 1 " + true);
            System.out.println("Praise " + praiseCandidates.get(0).getName() + " Criticize " + criticizeCandidates.get(0).getName());
            return new Player[]{praiseCandidates.get(0), criticizeCandidates.get(0)};
        }


        ArrayList<Player> tempList = new ArrayList<>(praiseCandidates);
        Player[] result = new Player[2];
        if (tempList.retainAll(criticizeCandidates)) {
            System.out.println("tempList.retainAll(criticizeCandidates) " + true);
            praiseCandidates.removeAll(tempList);
            criticizeCandidates.removeAll(tempList);
            if (praiseCandidates.isEmpty() && !criticizeCandidates.isEmpty()) {
                System.out.println("praiseCandidates.isEmpty() && !criticizeCandidates.isEmpty()");
                result[0]= roundStrategy.pickRandomToPraise(owner, tempList);
                result[1]= roundStrategy.pickRandomToCriticize(owner, criticizeCandidates);
            } else if (!praiseCandidates.isEmpty() && criticizeCandidates.isEmpty()) {
                System.out.println("!praiseCandidates.isEmpty() && criticizeCandidates.isEmpty()");
                result[0]= roundStrategy.pickRandomToPraise(owner, praiseCandidates);
                result[1]= roundStrategy.pickRandomToCriticize(owner, tempList);
            } else if (!praiseCandidates.isEmpty() && !criticizeCandidates.isEmpty()) {
                System.out.println("!praiseCandidates.isEmpty() && !criticizeCandidates.isEmpty()");
                result[0] = roundStrategy.pickRandomToPraise(owner, praiseCandidates);
                result[1] = roundStrategy.pickRandomToCriticize(owner, criticizeCandidates);
            } else {
                System.out.println("else");
                result[0]= roundStrategy.pickRandomToPraise(owner, tempList);
                result[1]= roundStrategy.pickRandomToCriticize(owner, tempList);
            }
            System.out.println("Praise " + result[0].getName() + " Criticize " + result[1].getName());
            return result;
        }
        System.out.println("tempList.retainAll(criticizeCandidates) " + false);
        result[0] = roundStrategy.pickRandomToPraise(owner, praiseCandidates);
        result[1] = roundStrategy.pickRandomToCriticize(owner, criticizeCandidates);
        System.out.println("Praise " + result[0].getName() + " Criticize " + result[1].getName());
        return  result;
    }







}
