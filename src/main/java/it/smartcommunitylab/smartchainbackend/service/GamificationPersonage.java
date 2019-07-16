package it.smartcommunitylab.smartchainbackend.service;

import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.model.Cost;

public class GamificationPersonage {

    private String gameId;
    private String name;
    private double territoryScore;
    private double cultureScore;
    private double sportScore;

    public GamificationPersonage(String gamificationId, PersonageDTO personage, Cost cost) {
        gameId = gamificationId;
        name = personage.getName();
        territoryScore = cost.getTerritoryScore();
        cultureScore = cost.getCultureScore();
        sportScore = cost.getSportScore();
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

    public String getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }



}
