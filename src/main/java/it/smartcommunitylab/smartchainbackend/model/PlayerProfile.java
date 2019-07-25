package it.smartcommunitylab.smartchainbackend.model;

import java.util.ArrayList;
import java.util.List;

import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;

public class PlayerProfile {

    private String playerId;
    private String gameId;
    private String name;
    private double territoryScore;
    private double cultureScore;
    private double sportScore;

    private List<Personage> usablePersonages = new ArrayList<>();
    private List<ModelReward> usableRewards = new ArrayList<>();

    private List<ModelAction> completedActions = new ArrayList<>();
    private List<ModelExperience> completedExperiences = new ArrayList<>();


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Personage> getUsablePersonages() {
        return usablePersonages;
    }

    public void setUsablePersonages(List<Personage> usablePersonages) {
        this.usablePersonages = usablePersonages;
    }

    public List<ModelReward> getUsableRewards() {
        return usableRewards;
    }

    public void setUsableRewards(List<ModelReward> usableRewards) {
        this.usableRewards = usableRewards;
    }

    public List<ModelAction> getCompletedActions() {
        return completedActions;
    }

    public void setCompletedActions(List<ModelAction> completedActions) {
        this.completedActions = completedActions;
    }

    public List<ModelExperience> getCompletedExperiences() {
        return completedExperiences;
    }

    public void setCompletedExperiences(List<ModelExperience> completedExperiences) {
        this.completedExperiences = completedExperiences;
    }


}
