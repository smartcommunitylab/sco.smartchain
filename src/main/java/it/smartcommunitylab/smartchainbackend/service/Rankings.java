package it.smartcommunitylab.smartchainbackend.service;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;

public class Rankings {
    @JsonView(JsonVisibility.Public.class)
    private Ranking territoryRanking;
    @JsonView(JsonVisibility.Public.class)
    private Ranking cultureRanking;
    @JsonView(JsonVisibility.Public.class)
    private Ranking sportRanking;


    public Rankings() {
        
    }

    public Rankings(Ranking territoryRanking, Ranking cultureRanking, Ranking sportRanking) {
        this.territoryRanking = territoryRanking;
        this.cultureRanking = cultureRanking;
        this.sportRanking = sportRanking;
    }

    public static class Ranking {
        @JsonView(JsonVisibility.Public.class)
        private Position daily;
        @JsonView(JsonVisibility.Public.class)
        private Position weekly;
        @JsonView(JsonVisibility.Public.class)
        private Position monthly;

        public Position getDaily() {
            return daily;
        }

        public void setDaily(Position daily) {
            this.daily = daily;
        }

        public Position getWeekly() {
            return weekly;
        }

        public void setWeekly(Position weekly) {
            this.weekly = weekly;
        }

        public Position getMonthly() {
            return monthly;
        }

        public void setMonthly(Position monthly) {
            this.monthly = monthly;
        }

    }

    public static class Position {
        @JsonView(JsonVisibility.Public.class)
        private int position;
        @JsonView(JsonVisibility.Public.class)
        private double score;

        public Position() {}

        public Position(int position, double score) {
            this.position = position;
            this.score = score;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    public Ranking getCultureRanking() {
        return cultureRanking;
    }

    public void setCultureRanking(Ranking cultureRanking) {
        this.cultureRanking = cultureRanking;
    }

    public Ranking getTerritoryRanking() {
        return territoryRanking;
    }

    public void setTerritoryRanking(Ranking territoryRanking) {
        this.territoryRanking = territoryRanking;
    }

    public Ranking getSportRanking() {
        return sportRanking;
    }

    public void setSportRanking(Ranking sportRanking) {
        this.sportRanking = sportRanking;
    }
}
