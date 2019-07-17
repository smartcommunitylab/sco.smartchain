package it.smartcommunitylab.smartchainbackend.service;

import it.smartcommunitylab.smartchainbackend.model.Cost;

public class GamificationReward {
    private String gameId;
    private double territoryScore;
    private double cultureScore;
    private double sportScore;

    public GamificationReward(String gamificationId, Cost cost) {
        gameId = gamificationId;
        territoryScore = cost.getTerritoryScore();
        cultureScore = cost.getCultureScore();
        sportScore = cost.getSportScore();
    }

    public String getGameId() {
        return gameId;
    }

    public double getTerritoryScore() {
        return territoryScore;
    }

    public double getCultureScore() {
        return cultureScore;
    }

    public double getSportScore() {
        return sportScore;
    }
}
