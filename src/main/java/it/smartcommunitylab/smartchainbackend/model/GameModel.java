package it.smartcommunitylab.smartchainbackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;

public class GameModel {
    private String id;
    private String name;
    private String gamificationId;
    // ?? private String gameContractId;

    private List<Personage> personages = new ArrayList<>();
    private List<ModelReward> rewards = new ArrayList<>();
    private List<ModelAction> actions = new ArrayList<>();
    private List<ModelExperience> experiences = new ArrayList<>();


    public ModelExperience getExperience(String experienceId) {
        return experiences.stream().filter(e -> experienceId.equals(e.getExperienceId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("experienceId  %s not exist in game-model %s", experienceId, id)));
    }

    public List<ModelExperience> getExperiences(Collection<String> experienceIds) {
        List<ModelExperience> modelExperiences = new ArrayList<>();
        for (String expId : experienceIds) {
            experiences.stream().filter(e -> e.getExperienceId().equals(expId)).findFirst()
                    .ifPresent(e -> modelExperiences.add(e));
        }
        return modelExperiences;
    }

    public List<ModelAction> getActions(Collection<String> actionIds) {
        List<ModelAction> modelActions = new ArrayList<>();
        for (String actionId : actionIds) {
            actions.stream().filter(a -> a.getActionId().equals(actionId)).findFirst()
                    .ifPresent(a -> modelActions.add(a));
        }
        return modelActions;
    }

    public ModelAction getAction(String actionId) {
        return actions.stream().filter(e -> actionId.equals(e.getActionId())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("actionId  %s not exist in game-model %s", actionId, id)));
    }

    public static class ModelExperience {
        @JsonView(JsonVisibility.Public.class)
        private String experienceId;

        @JsonView(JsonVisibility.Public.class)
        private String name;

        @JsonView(JsonVisibility.Public.class)
        private String description;

        @JsonView(JsonVisibility.Public.class)
        private String iconUrl;

        @JsonView(JsonVisibility.Internal.class)
        private String gamificationExperienceName;

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

        public String getExperienceId() {
            return experienceId;
        }

        public void setExperienceId(String experienceId) {
            this.experienceId = experienceId;
        }

        public String getGamificationExperienceName() {
            return gamificationExperienceName;
        }

        public void setGamificationExperienceName(String gamificationExperienceName) {
            this.gamificationExperienceName = gamificationExperienceName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

    }

    public static class ModelAction {
        @JsonView(JsonVisibility.Public.class)
        private String actionId;

        @JsonView(JsonVisibility.Public.class)
        private String name;

        @JsonView(JsonVisibility.Public.class)
        private String description;

        @JsonView(JsonVisibility.Public.class)
        private String iconUrl;

        @JsonView(JsonVisibility.Internal.class)
        private String gamificationActionName;

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

        public String getActionId() {
            return actionId;
        }

        public void setActionId(String id) {
            this.actionId = id;
        }

        public String getGamificationActionName() {
            return gamificationActionName;
        }

        public void setGamificationActionName(String gamificationActionName) {
            this.gamificationActionName = gamificationActionName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }


    public static class ModelReward {
        private String rewardId;
        private String name;
        private Cost cost;
        private String iconUrl;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Cost getCost() {
            return cost;
        }

        public void setCost(Cost cost) {
            this.cost = cost;
        }

        public String getRewardId() {
            return rewardId;
        }

        public void setRewardId(String rewardId) {
            this.rewardId = rewardId;
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

    public static class Personage {
        private String personageId;
        private String name;
        private Cost cost;
        private String iconUrl;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Cost getCost() {
            return cost;
        }

        public void setCost(Cost cost) {
            this.cost = cost;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            Personage rhs = (Personage) obj;
            return new EqualsBuilder().append(personageId, rhs.personageId).isEquals();

        }

        public String getPersonageId() {
            return personageId;
        }

        public void setPersonageId(String personageId) {
            this.personageId = personageId;
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

    public List<Personage> getPersonages() {
        return personages;
    }

    public void setPersonages(List<Personage> personages) {
        this.personages = personages;
    }

    public List<ModelReward> getRewards() {
        return rewards;
    }

    public void setRewards(List<ModelReward> rewards) {
        this.rewards = rewards;
    }

    public List<ModelAction> getActions() {
        return actions;
    }

    public void setActions(List<ModelAction> actions) {
        this.actions = actions;
    }

    public List<ModelExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ModelExperience> experiences) {
        this.experiences = experiences;
    }
}
