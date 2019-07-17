package it.smartcommunitylab.smartchainbackend.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class GameModel {
    private String id;
    private String name;
    private String gamificationId;
    // ?? private String gameContractId;

    private List<Personage> personages = new ArrayList<>();
    private List<GameReward> rewards = new ArrayList<>();

    public static class GameReward {
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

    public List<GameReward> getRewards() {
        return rewards;
    }

    public void setRewards(List<GameReward> rewards) {
        this.rewards = rewards;
    }



}
