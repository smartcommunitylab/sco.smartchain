package it.smartcommunitylab.smartchainbackend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Subscription {

    @Id
    private CompositeKey id;

    private List<String> completedExperiences = new ArrayList<>();
    private List<String> completedActions = new ArrayList<>();


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



}
