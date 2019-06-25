package it.smartcommunitylab.smartchainbackend.bean;

public class Experience {

    private String gameId;
    private String name;

    public Experience() {

    }

    public Experience(String gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

}

