package it.smartcommunitylab.smartchainbackend.bean;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;

public class UnusablePersonage {
    @JsonView(JsonVisibility.Public.class)
    private String personageId;
    @JsonView(JsonVisibility.Public.class)
    private String name;
    @JsonView(JsonVisibility.Public.class)
    private Cost missingScore;
    @JsonView(JsonVisibility.Public.class)
    private String iconUrl;
    @JsonView(JsonVisibility.Public.class)
    private String description;


    public UnusablePersonage() {

    }

    public UnusablePersonage(Personage personage) {
        personageId = personage.getPersonageId();
        name = personage.getName();
        iconUrl = personage.getIconUrl();
        description = personage.getDescription();
    }

    public String getPersonageId() {
        return personageId;
    }

    public void setPersonageId(String personageId) {
        this.personageId = personageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cost getMissingScore() {
        return missingScore;
    }

    public void setMissingScore(Cost missingScore) {
        this.missingScore = missingScore;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
