package it.smartcommunitylab.smartchainbackend.bean;

public class Experience {

    private String gameId;
    private String playerId;
    private String id;

    public Experience() {

    }

    public Experience(String gameId, String name) {
        this.gameId = gameId;
        this.id = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}

