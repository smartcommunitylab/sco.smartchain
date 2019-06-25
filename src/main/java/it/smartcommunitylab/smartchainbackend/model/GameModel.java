package it.smartcommunitylab.smartchainbackend.model;

public class GameModel {
    private String id;
    private String name;
    private String gamificationId;
    // ?? private String gameContractId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGamificationId() {
        return gamificationId;
    }

    public void setGamificationId(String gamificationId) {
        this.gamificationId = gamificationId;
    }



}
