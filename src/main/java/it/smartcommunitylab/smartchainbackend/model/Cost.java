package it.smartcommunitylab.smartchainbackend.model;

public class Cost {
    private double territoryScore;
    private double cultureScore;
    private double sportScore;


    public Cost() {}


    public Cost(double territoryScore, double cultureScore, double sportScore) {
        this.territoryScore = territoryScore;
        this.cultureScore = cultureScore;
        this.sportScore = sportScore;
    }


    public double getTerritoryScore() {
        return territoryScore;
    }

    public void setTerritoryScore(double territoryScore) {
        this.territoryScore = territoryScore;
    }

    public double getCultureScore() {
        return cultureScore;
    }

    public void setCultureScore(double cultureScore) {
        this.cultureScore = cultureScore;
    }

    public double getSportScore() {
        return sportScore;
    }

    public void setSportScore(double sportScore) {
        this.sportScore = sportScore;
    }


}
