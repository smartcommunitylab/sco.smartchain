package it.smartcommunitylab.smartchainbackend.bean;

public class CertificationActionDTO {
    private String gameModelId;
    private String playerId;
    private String experienceId;
    private String certificationId;

    public String getGameModelId() {
        return gameModelId;
    }

    public void setGameModelId(String gameModelId) {
        this.gameModelId = gameModelId;
    }

    public String getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
