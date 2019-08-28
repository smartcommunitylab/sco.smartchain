package it.smartcommunitylab.smartchainbackend.bean;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;

public class UnusableReward {
    @JsonView(JsonVisibility.Public.class)
    private String rewardId;
    @JsonView(JsonVisibility.Public.class)
    private String name;
    @JsonView(JsonVisibility.Public.class)
    private Cost missingScore;
    @JsonView(JsonVisibility.Public.class)
    private String iconUrl;
    @JsonView(JsonVisibility.Public.class)
    private String description;


    public UnusableReward(ModelReward reward) {
        rewardId = reward.getRewardId();
        name = reward.getName();
        iconUrl = reward.getIconUrl();
        description = reward.getDescription();
    }

    public UnusableReward() {

    }


    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
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
