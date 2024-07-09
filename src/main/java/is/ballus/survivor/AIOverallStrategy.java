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
            case 3 -> praiseAndCriticizeForMaxOverallOpinion(currentPlayers, currentPlayers);
            case 4 -> praiseAndCriticizeForMaxRemainingOpinion(currentPlayers, currentPlayers);
            case 5 -> targetClosestRemainingPlayerFromFavOpinionRemaining();
            case 6 -> targetClosestPlayerFromFavOpinionOverall();
            case 7 -> targetClosestRemainingPlayerFromFavPrioritizingTargetRemainingOpinion();
            case 8 -> targetClosestPlayerFromFavPrioritizingTargetOverallOpinion();
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

    public Player[] praiseAndCriticizeForMaxOverallOpinion(ArrayList<Player> praisePool, ArrayList<Player> criticizePool) {
        System.out.println("praiseAndCriticizeForMaxOverallOpinion " + owner.getName());
        ArrayList<Player> praiseCandidates = roundStrategy.praiseForMaxOverallOpinion(owner, praisePool);
        praiseCandidates = roundStrategy.praiseForMaxRemainingOpinion(owner, praiseCandidates);
        if (praiseCandidates.isEmpty()) {
            praiseCandidates = roundStrategy.praiseForMaxOverallOpinion(owner, currentPlayers);
            praiseCandidates = roundStrategy.praiseForMaxRemainingOpinion(owner, praiseCandidates);
        }
        ArrayList<Player> criticizeCandidates = roundStrategy.criticizeForMaxOverallOpinion(owner, criticizePool);
        criticizeCandidates = roundStrategy.criticizeForMaxRemainingOpinion(owner, criticizeCandidates);
        if (criticizeCandidates.isEmpty()) {
            criticizeCandidates = roundStrategy.criticizeForMaxOverallOpinion(owner, currentPlayers);
            criticizeCandidates = roundStrategy.criticizeForMaxRemainingOpinion(owner, criticizeCandidates);
        }
        System.out.println("praiseCandidates");
        for (Player player : praiseCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.print("\n");
        System.out.println("criticizeCandidates");
        for (Player player : criticizeCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.print("\n");
        if (praiseCandidates.size() == 1 && criticizeCandidates.size() == 1) {
            System.out.println("praiseCandidates.size() == 1 && criticizeCandidates.size() == 1 " + true);
            System.out.println("Praise " + praiseCandidates.get(0).getName() + " Criticize " + criticizeCandidates.get(0).getName());
            return new Player[]{praiseCandidates.get(0), criticizeCandidates.get(0)};
        }


        ArrayList<Player> tempList = new ArrayList<>(praiseCandidates);
        Player[] result = new Player[2];
        tempList.retainAll(criticizeCandidates);
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

    public Player[] praiseAndCriticizeForMaxRemainingOpinion(ArrayList<Player> praisePool, ArrayList<Player> criticizePool) {
        System.out.println("praiseAndCriticizeForMaxRemainingOpinion");
        ArrayList<Player> praiseCandidates = roundStrategy.praiseForMaxRemainingOpinion(owner, praisePool);
        praiseCandidates = roundStrategy.praiseForMaxOverallOpinion(owner, praiseCandidates);
        if (praiseCandidates.isEmpty()) {
            praiseCandidates = roundStrategy.praiseForMaxRemainingOpinion(owner, currentPlayers);
            praiseCandidates = roundStrategy.praiseForMaxOverallOpinion(owner, praiseCandidates);
        }
        ArrayList<Player> criticizeCandidates = roundStrategy.criticizeForMaxRemainingOpinion(owner, criticizePool);
        criticizeCandidates = roundStrategy.criticizeForMaxOverallOpinion(owner, criticizeCandidates);
        if (criticizeCandidates.isEmpty()) {
            criticizeCandidates = roundStrategy.criticizeForMaxRemainingOpinion(owner, currentPlayers);
            criticizeCandidates = roundStrategy.criticizeForMaxOverallOpinion(owner, criticizeCandidates);
        }
        System.out.println("praiseCandidates");
        for (Player player : praiseCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.print("\n");
        System.out.println("criticizeCandidates");
        for (Player player : criticizeCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.print("\n");

        ArrayList<Player> tempList = new ArrayList<>(praiseCandidates);
        Player[] result = new Player[2];
        tempList.retainAll(criticizeCandidates);
        praiseCandidates.removeAll(tempList);
        criticizeCandidates.removeAll(tempList);

        if (praiseCandidates.size() == 1 && criticizeCandidates.size() == 1) {
            System.out.println("praiseCandidates.size() == 1 && criticizeCandidates.size() == 1 " + true);
            System.out.println("Praise " + praiseCandidates.get(0).getName() + " Criticize " + criticizeCandidates.get(0).getName());
            return new Player[]{praiseCandidates.get(0), criticizeCandidates.get(0)};
        }

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

    public Player[] targetClosestPlayerFromFavOpinion(ArrayList<Player> playersToPickFrom, boolean remainingOpinion) {
        System.out.println("targetClosestPlayerFromFavOpinion");
        ArrayList<Player> targets = roundStrategy.findClosestToBecomingFavorite(owner, playersToPickFrom);
        ArrayList<Player> praiseCandidates = new ArrayList<>();
        ArrayList<Player> criticizeCandidates = new ArrayList<>();
        ArrayList<Player> temp = new ArrayList<>();

        for (Player player : targets) {
            temp.addAll(player.getSupports());
            temp.removeAll(praiseCandidates);
            praiseCandidates.addAll(temp);
        }
        praiseCandidates.removeAll(targets);
        praiseCandidates.remove(owner);
        praiseCandidates.addAll(targets);
        System.out.println("praiseCandidates");
        for (Player player : praiseCandidates) {
            System.out.print(player.getName() + " ");
        }
        temp.clear();
        System.out.println(" ");

        for (Player player : targets) {
            temp.addAll(player.getOpposes());
            temp.removeAll(criticizeCandidates);
            criticizeCandidates.addAll(temp);
        }
        criticizeCandidates.removeAll(targets);
        criticizeCandidates.remove(owner);
        System.out.println("criticizeCandidates");
        for (Player player : criticizeCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.println(" ");

        if (remainingOpinion) {
            return praiseAndCriticizeForMaxRemainingOpinion(praiseCandidates, criticizeCandidates);
        } else {
            return praiseAndCriticizeForMaxOverallOpinion(praiseCandidates, criticizeCandidates);
        }
    }

    public Player[] targetClosestPlayerFromFavPrioritizingTarget(ArrayList<Player> playersToPickFrom, boolean remainingOpinion) {
        ArrayList<Player> targets = roundStrategy.findClosestToBecomingFavorite(owner, playersToPickFrom);
        ArrayList<Player> praiseCandidates = new ArrayList<>();
        ArrayList<Player> criticizeCandidates = new ArrayList<>();
        ArrayList<Player> temp = new ArrayList<>();
        boolean addTargets = false;

         for (Player player : targets) {
            for (int i = 0; i < 3 ; i++) {
                if (i == 2) {
                    addTargets = true;
                }
                temp.addAll(player.getOutcomingRelationshipStatus(i));
                temp.removeIf(playerToCheck -> !owner.canPraise(playerToCheck));
                if (!temp.isEmpty()) {
                    temp.removeAll(praiseCandidates);
                    praiseCandidates.addAll(temp);
                    break;
                } else if (!praiseCandidates.isEmpty()) {
                    break;
                }
            }
        }
         if (addTargets) {
             praiseCandidates.addAll(targets);
         }
         temp.clear();
        for (Player player : targets) {
            for (int i = 8; i > 5 ; i--) {
                temp.addAll(player.getOutcomingRelationshipStatus(i));
                temp.removeIf(playerToCheck -> !owner.canCriticize(playerToCheck));
                if (!temp.isEmpty()) {
                    temp.removeAll(criticizeCandidates);
                    criticizeCandidates.addAll(temp);
                    break;
                } else if (!criticizeCandidates.isEmpty()) {
                    break;
                }
            }
        }
        System.out.println("praiseCandidates");
        for (Player player : praiseCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.println(" ");
        System.out.println("criticizeCandidates");
        for (Player player : criticizeCandidates) {
            System.out.print(player.getName() + " ");
        }
        System.out.println(" ");

        if (remainingOpinion) {
            return praiseAndCriticizeForMaxRemainingOpinion(praiseCandidates, criticizeCandidates);
        } else {
            return praiseAndCriticizeForMaxOverallOpinion(praiseCandidates, criticizeCandidates);
        }
    }

    public Player[] targetClosestRemainingPlayerFromFavOpinionRemaining() {
        if (currentRound.getRoundNUmber() > 8) {
            System.out.println("Changed plan after round 8");
            return targetClosestPlayerFromFavOpinion(currentPlayers, false);
        }
        return targetClosestPlayerFromFavOpinion(remainingPlayers, true);
    }

    public Player[] targetClosestPlayerFromFavOpinionOverall() {
        if (predictedPlayers.get(owner.getPlayerIndex()).isEliminated()) {
            System.out.println("Changed plan is predicted to lose");
            return targetClosestPlayerFromFavOpinion(remainingPlayers, true);
        }
        return targetClosestPlayerFromFavOpinion(currentPlayers, false);
    }

    public Player[] targetClosestRemainingPlayerFromFavPrioritizingTargetRemainingOpinion() {
        if (currentRound.getRoundNUmber() > 8) {
            System.out.println("Changed plan after round 8");
            return targetClosestPlayerFromFavOpinion(currentPlayers, false);
        }
        return targetClosestPlayerFromFavPrioritizingTarget(remainingPlayers, true);
    }

    public Player[] targetClosestPlayerFromFavPrioritizingTargetOverallOpinion() {
        if (predictedPlayers.get(owner.getPlayerIndex()).isEliminated()) {
            System.out.println("Changed plan is predicted to lose");
            return targetClosestPlayerFromFavPrioritizingTarget(remainingPlayers, true);
        }
        if (predictedPlayers.get(owner.getPlayerIndex()).getNominations() == 0) {
            System.out.println("Changed plan has no nominations");
            return targetClosestPlayerFromFavOpinion(remainingPlayers, true);
        }
        return targetClosestPlayerFromFavPrioritizingTarget(currentPlayers, false);
    }





}
