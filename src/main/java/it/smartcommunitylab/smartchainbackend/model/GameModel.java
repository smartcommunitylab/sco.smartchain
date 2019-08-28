package it.smartcommunitylab.smartchainbackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModelProperty;
import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;

public class GameModel {
    @JsonView(JsonVisibility.Public.class)
    private String id;
    @JsonView(JsonVisibility.Public.class)
    private String name;

    @ApiModelProperty(hidden = true)
    @JsonView(JsonVisibility.Internal.class)
    private String gamificationId;
    @JsonView(JsonVisibility.Public.class)
    private List<Personage> personages = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<ModelReward> rewards = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<ModelAction> actions = new ArrayList<>();
    @JsonView(JsonVisibility.Public.class)
    private List<ModelExperience> experiences = new ArrayList<>();

    private List<Challenge> challenges = new ArrayList<>();


    public Personage getPersonage(String personageId) {
        return personages.stream().filter(p -> p.personageId.equals(personageId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String
                        .format("personageId  %s not exist in game-model %s", personageId, id)));
    }

    public ModelReward getReward(String rewardId) {
        return rewards.stream().filter(r -> r.rewardId.equals(rewardId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("rewardId  %s not exist in game-model %s", rewardId, id)));
    }


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

    public static class Challenge {
        @JsonView(JsonVisibility.Public.class)
        private String challengeId;
        @JsonView(JsonVisibility.Public.class)
        private String name;
        @JsonView(JsonVisibility.Public.class)
        private String description;
        @JsonView(JsonVisibility.Public.class)
        private String iconUrl;

        @JsonView(JsonVisibility.Internal.class)
        @ApiModelProperty(hidden = true)
        private String gamificationType;

        public String getChallengeId() {
            return challengeId;
        }

        public void setChallengeId(String challengeId) {
            this.challengeId = challengeId;
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

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getGamificationType() {
            return gamificationType;
        }

        public void setGamificationType(String gamificationType) {
            this.gamificationType = gamificationType;
        }


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

        @JsonView(JsonVisibility.Public.class)
        private String executionUrl;

        @JsonView(JsonVisibility.Public.class)
        private List<CertificationAction> certificationActions = new ArrayList<>();

        @JsonView(JsonVisibility.Internal.class)
        @ApiModelProperty(hidden = true)
        private String gamificationExperienceName;

        @JsonView(JsonVisibility.Public.class)
        private Revenue revenueScore;


        public CertificationAction getCertificationAction(String certificationId) {
            return certificationActions.stream()
                    .filter(c -> c.getCertificationId().equals(certificationId)).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("certificationId %s not exist in experience %s",
                                    certificationId, experienceId)));
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

        public List<CertificationAction> getCertificationActions() {
            return certificationActions;
        }

        public void setCertificationActions(List<CertificationAction> certificationActions) {
            this.certificationActions = certificationActions;
        }


        public boolean containsAnyCertification(Collection<String> completedCertifications) {
            for (String certificateId : completedCertifications) {
                if (certificationActions.stream()
                        .anyMatch(c -> c.getCertificationId().equals(certificateId))) {
                    return true;
                }
            }
            return false;
        }

        public boolean isCompleted(Collection<String> completedCertifications) {
            List<String> certificationIds = certificationActions.stream()
                    .map(c -> c.getCertificationId()).collect(Collectors.toList());
            return completedCertifications.containsAll(certificationIds);
        }


        public Revenue getRevenueScore() {
            return revenueScore;
        }


        public void setRevenueScore(Revenue revenueScore) {
            this.revenueScore = revenueScore;
        }


        public String getExecutionUrl() {
            return executionUrl;
        }


        public void setExecutionUrl(String executionUrl) {
            this.executionUrl = executionUrl;
        }

    }

    public static class CertificationAction {
        @JsonView(JsonVisibility.Public.class)
        private String certificationId;
        @JsonView(JsonVisibility.Public.class)
        private String name;
        @JsonView(JsonVisibility.Public.class)
        private String description;

        @JsonView(JsonVisibility.Public.class)
        private String executionUrl;

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

        public String getExecutionUrl() {
            return executionUrl;
        }

        public void setExecutionUrl(String executionUrl) {
            this.executionUrl = executionUrl;
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

        @JsonView(JsonVisibility.Public.class)
        private String executionUrl;

        @JsonView(JsonVisibility.Internal.class)
        @ApiModelProperty(hidden = true)
        private String gamificationActionName;

        @JsonView(JsonVisibility.Public.class)
        private Revenue revenueScore;

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

        public String getExecutionUrl() {
            return executionUrl;
        }

        public void setExecutionUrl(String executionUrl) {
            this.executionUrl = executionUrl;
        }

        public Revenue getRevenueScore() {
            return revenueScore;
        }

        public void setRevenueScore(Revenue revenueScore) {
            this.revenueScore = revenueScore;
        }
    }

    public static class Revenue {
        @JsonView(JsonVisibility.Public.class)
        private double territoryScore;
        @JsonView(JsonVisibility.Public.class)
        private double cultureScore;
        @JsonView(JsonVisibility.Public.class)
        private double sportScore;

        public Revenue() {
        }

        public Revenue(double territory, double culture, double sport) {
            this.territoryScore = territory;
            this.cultureScore = culture;
            this.sportScore = sport;
        }

        public double getTerritoryScore() {
            return territoryScore;
        }

        public void setTerritoryScore(double territoryScore) {
            this.territoryScore = territoryScore;
        }

        public double getCultureScore() {
            return cultureScore;
        }

        public void setCultureScore(double cultureScore) {
            this.cultureScore = cultureScore;
        }

        public double getSportScore() {
            return sportScore;
        }

        public void setSportScore(double sportScore) {
            this.sportScore = sportScore;
        }


    }

    public static class ModelReward {
        @JsonView(JsonVisibility.Public.class)
        private String rewardId;
        @JsonView(JsonVisibility.Public.class)
        private String name;
        @JsonView(JsonVisibility.Public.class)
        private Cost cost;
        @JsonView(JsonVisibility.Public.class)
        private String iconUrl;
        @JsonView(JsonVisibility.Public.class)
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
        @JsonView(JsonVisibility.Public.class)
        private String personageId;
        @JsonView(JsonVisibility.Public.class)
        private String name;
        @JsonView(JsonVisibility.Public.class)
        private Cost cost;
        @JsonView(JsonVisibility.Public.class)
        private String iconUrl;
        @JsonView(JsonVisibility.Public.class)
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

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }
}
