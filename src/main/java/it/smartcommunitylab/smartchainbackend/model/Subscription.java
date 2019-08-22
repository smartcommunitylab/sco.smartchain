package it.smartcommunitylab.smartchainbackend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Subscription {

    @Id
    private CompositeKey id;

    private int components = 1;
    private List<String> completedExperiences = new ArrayList<>();
    private List<String> completedActions = new ArrayList<>();
    private List<String> completedCertifications = new ArrayList<>();

    private List<Consumption> consumedPersonages = new ArrayList<>();
    private List<Consumption> consumedRewards = new ArrayList<>();


    public static class CompositeKey implements Serializable {

        private static final long serialVersionUID = 6657973855256981618L;

        private String gameId;
        private String playerId;


        public CompositeKey(String gameId, String playerId) {
            this.gameId = gameId;
            this.playerId = playerId;
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

    public static class Consumption {
        private String id;
        private Date timestamp;


        public Consumption() {}


        public Consumption(String id) {
            this.id = id;
            timestamp = new Date();
        }


        public Consumption(String id, Date timestamp) {
            this(id);
            this.timestamp = timestamp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }

    public List<String> getCompletedExperiences() {
        return completedExperiences;
    }

    public void setCompletedExperiences(List<String> completedExperiences) {
        this.completedExperiences = completedExperiences;
    }

    public List<String> getCompletedActions() {
        return completedActions;
    }

    public void setCompletedActions(List<String> completedActions) {
        this.completedActions = completedActions;
    }

    public List<String> getCompletedCertifications() {
        return completedCertifications;
    }

    public void setCompletedCertifications(List<String> completedCertifications) {
        this.completedCertifications = completedCertifications;
    }

    public int getComponents() {
        return components;
    }

    public void setComponents(int components) {
        this.components = components;
    }

    public List<Consumption> getConsumedPersonages() {
        return consumedPersonages;
    }

    public void setConsumedPersonages(List<Consumption> consumedPersonages) {
        this.consumedPersonages = consumedPersonages;
    }

    public List<Consumption> getConsumedRewards() {
        return consumedRewards;
    }

    public void setConsumedRewards(List<Consumption> consumedRewards) {
        this.consumedRewards = consumedRewards;
    }



}
