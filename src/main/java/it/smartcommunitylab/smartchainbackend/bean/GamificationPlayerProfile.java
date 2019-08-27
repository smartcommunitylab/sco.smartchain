package it.smartcommunitylab.smartchainbackend.bean;

public class GamificationPlayerProfile {
    private String gamificationId;
    private String playerId;

    private double totalTerritoryScore;
    private double totalCultureScore;
    private double totalSportScore;

    private double personageTerritoryScore;
    private double personageCultureScore;
    private double personageSportScore;

    private double rewardTerritoryScore;
    private double rewardCultureScore;
    private double rewardSportScore;

    public double getTotalTerritoryScore() {
        return totalTerritoryScore;
    }

    public void setTotalTerritoryScore(double totalTerritoryScore) {
        this.totalTerritoryScore = totalTerritoryScore;
    }

    public double getTotalCultureScore() {
        return totalCultureScore;
    }

    public void setTotalCultureScore(double totalCultureScore) {
        this.totalCultureScore = totalCultureScore;
    }

    public double getTotalSportScore() {
        return totalSportScore;
    }

    public void setTotalSportScore(double totalSportScore) {
        this.totalSportScore = totalSportScore;
    }

    public double getPersonageTerritoryScore() {
        return personageTerritoryScore;
    }

    public void setPersonageTerritoryScore(double personageTerritoryScore) {
        this.personageTerritoryScore = personageTerritoryScore;
    }

    public double getPersonageCultureScore() {
        return personageCultureScore;
    }

    public void setPersonageCultureScore(double personageCultureScore) {
        this.personageCultureScore = personageCultureScore;
    }

    public double getPersonageSportScore() {
        return personageSportScore;
    }

    public void setPersonageSportScore(double personageSportScore) {
        this.personageSportScore = personageSportScore;
    }

    public double getRewardTerritoryScore() {
        return rewardTerritoryScore;
    }

    public void setRewardTerritoryScore(double rewardTerritoryScore) {
        this.rewardTerritoryScore = rewardTerritoryScore;
    }

    public double getRewardCultureScore() {
        return rewardCultureScore;
    }

    public void setRewardCultureScore(double rewardCultureScore) {
        this.rewardCultureScore = rewardCultureScore;
    }

    public double getRewardSportScore() {
        return rewardSportScore;
    }

    public void setRewardSportScore(double rewardSportScore) {
        this.rewardSportScore = rewardSportScore;
    }

    public String getGamificationId() {
        return gamificationId;
    }

    public void setGamificationId(String gamificationId) {
        this.gamificationId = gamificationId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }



}
