package it.smartcommunitylab.smartchainbackend.model;

import java.util.ArrayList;
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


    public static class ModelExperience {
        @JsonView(JsonVisibility.Public.class)
        private String experienceId;

        @JsonView(JsonVisibility.Public.class)
        private String name;

        @JsonView(JsonVisibility.Public.class)
        private String description;

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

    }

    public static class ModelAction {
        @JsonView(JsonVisibility.Public.class)
        private String actionId;

        @JsonView(JsonVisibility.Public.class)
        private String name;

        @JsonView(JsonVisibility.Public.class)
        private String description;

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
    }


    public static class ModelReward {
        private String name;
        private Cost cost;

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


    }

    public static class Personage {
        private String name;
        private Cost cost;

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
            return new EqualsBuilder().append(name, rhs.name).isEquals();

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
