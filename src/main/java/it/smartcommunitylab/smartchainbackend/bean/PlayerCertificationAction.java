package it.smartcommunitylab.smartchainbackend.bean;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;
import it.smartcommunitylab.smartchainbackend.model.GameModel.CertificationAction;

public class PlayerCertificationAction {
    @JsonView(JsonVisibility.Public.class)
    private String certificationId;
    @JsonView(JsonVisibility.Public.class)
    private String name;
    @JsonView(JsonVisibility.Public.class)
    private String description;
    @JsonView(JsonVisibility.Public.class)
    private boolean completed;

    public PlayerCertificationAction(CertificationAction certification) {
        this(certification, false);
    }

    public PlayerCertificationAction(CertificationAction certification, boolean completed) {
        this.certificationId = certification.getCertificationId();
        this.name = certification.getName();
        this.description = certification.getDescription();
        this.completed = completed;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


}
