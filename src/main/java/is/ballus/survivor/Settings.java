package is.ballus.survivor;

public class Settings {
    public String playerNames[] = {"Baldvin", "Ásgeir", "Hannes", "Helgi", "Guðmar", "Snorri", "Sindri", "Óskar", "Hafsteinn", "Tómas", "Jakob"};
    private int numPLayers = 11;
    private int[] PlayerStrategies = {3,3,4,4,5,5,6,6,7,7,8};

    public int getNumPLayers() {
        return numPLayers;
    }

    public void setNumPLayers(int numPLayers) {
        this.numPLayers = numPLayers;
    }


    public int getPlayerStrategy(int index) {
        return PlayerStrategies[index];
    }

    public void setPlayerStrategies(int[] playerStrategies) {
        PlayerStrategies = playerStrategies;
    }
}
