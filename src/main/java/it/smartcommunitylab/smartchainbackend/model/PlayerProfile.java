package it.smartcommunitylab.smartchainbackend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.bean.ConsumptionDTO;
import it.smartcommunitylab.smartchainbackend.bean.GamificationPlayerProfile;
import it.smartcommunitylab.smartchainbackend.bean.PlayerExperience;
import it.smartcommunitylab.smartchainbackend.bean.UnusablePersonage;
import it.smartcommunitylab.smartchainbackend.bean.UnusableReward;
import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;

public class PlayerProfile {
    @JsonView(JsonVisibility.Public.class)
    private String playerId;
    @JsonView(JsonVisibility.Public.class)
    private String gameId;
    @JsonView(JsonVisibility.Public.class)
    private double territoryScore;
    @JsonView(JsonVisibility.Public.class)
    private double cultureScore;
    @JsonView(JsonVisibility.Public.class)
    private double sportScore;



    @JsonView(JsonVisibility.Public.class)
    private List<Personage> usablePersonages = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<ModelReward> usableRewards = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<ModelAction> completedActions = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<PlayerExperience> completedExperiences = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<PlayerExperience> startedExperiences = new ArrayList<>();

    @JsonView(JsonVisibility.Public.class)
    private List<ConsumptionDTO> consumedPersonages = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<ConsumptionDTO> consumedRewards = new ArrayList<>();

    @JsonView(JsonVisibility.Public.class)
    private List<UnusablePersonage> unusablePersonages = new ArrayList<>();

    @JsonView(JsonVisibility.Public.class)
    private List<UnusableReward> unusableRewards = new ArrayList<>();

    public PlayerProfile() {

    }

    public PlayerProfile(String gameModelId, GamificationPlayerProfile gamificationProfile) {
        gameId = gameModelId;
        playerId = gamificationProfile.getPlayerId();
        territoryScore = gamificationProfile.getTotalTerritoryScore();
        cultureScore = gamificationProfile.getTotalCultureScore();
        sportScore = gamificationProfile.getTotalSportScore();
    }

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

    public List<PlayerExperience> getCompletedExperiences() {
        return completedExperiences;
    }

    public void setCompletedExperiences(List<PlayerExperience> completedExperiences) {
        this.completedExperiences = completedExperiences;
    }

    public List<PlayerExperience> getStartedExperiences() {
        return startedExperiences;
    }

    public void setStartedExperiences(List<PlayerExperience> startedExperiences) {
        this.startedExperiences = startedExperiences;
    }

    public List<ConsumptionDTO> getConsumedPersonages() {
        return consumedPersonages;
    }

    public void setConsumedPersonages(List<ConsumptionDTO> consumedPersonages) {
        this.consumedPersonages = consumedPersonages;
    }

    public List<ConsumptionDTO> getConsumedRewards() {
        return consumedRewards;
    }

    public void setConsumedRewards(List<ConsumptionDTO> consumedRewards) {
        this.consumedRewards = consumedRewards;
    }

    public List<UnusablePersonage> getUnusablePersonages() {
        return unusablePersonages;
    }

    public void setUnusablePersonages(List<UnusablePersonage> unusablePersonages) {
        this.unusablePersonages = unusablePersonages;
    }

    public List<UnusableReward> getUnusableRewards() {
        return unusableRewards;
    }

    public void setUnusableRewards(List<UnusableReward> unusableRewards) {
        this.unusableRewards = unusableRewards;
    }

}
