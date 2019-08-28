package it.smartcommunitylab.smartchainbackend.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.basic.api.ClassificationControllerApi;
import it.smartcommunitylab.basic.api.ExecutionControllerApi;
import it.smartcommunitylab.basic.api.PlayerControllerApi;
import it.smartcommunitylab.model.ClassificationBoard;
import it.smartcommunitylab.model.ClassificationPosition;
import it.smartcommunitylab.model.PlayerStateDTO;
import it.smartcommunitylab.model.ext.ExecutionDataDTO;
import it.smartcommunitylab.model.ext.GameConcept;
import it.smartcommunitylab.model.ext.PointConcept;
import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GamificationPlayerProfile;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.config.GEProps;
import it.smartcommunitylab.smartchainbackend.service.Rankings.Position;
import it.smartcommunitylab.smartchainbackend.service.Rankings.Ranking;

@Component
public class GEHelper {
    private static final Logger logger = LogManager.getLogger(GEHelper.class);

    private static final String componentsCustomField = "components";

    private static final String experienceAction = "experience";
    private static final String consumePersonageAction = "consume-character";
    private static final String consumeRewardAction = "consume-reward";

    private static final String totalTerritoryScoreName = "total_territory";
    private static final String totalCultureScoreName = "total_culture";
    private static final String totalSportScoreName = "total_sport";

    private static final String personageTerritoryScoreName = "character_territory";
    private static final String personageCultureScoreName = "character_culture";
    private static final String personageSportScoreName = "character_sport";

    private static final String rewardTerritoryScoreName = "reward_territory";
    private static final String rewardCultureScoreName = "reward_culture";
    private static final String rewardSportScoreName = "reward_sport";

    private static final String territoryDailyClassificationId = "daily classification territory";
    private static final String territoryWeeklyClassificationId = "weekly classification territory";
    private static final String territoryMonthlyClassificationId =
            "monthly classification territory";
    private static final String cultureDailyClassificationId = "daily classification culture";
    private static final String cultureWeeklyClassificationId = "weekly classification culture";
    private static final String cultureMonthlyClassificationId = "monthly classification culture";
    private static final String sportDailyClassificationId = "daily classification sport";
    private static final String sportWeeklyClassificationId = "weekly classification sport";
    private static final String sportMonthlyClassificationId = "monthly classification sport";


    public enum RankingType {
        TERRITORY(territoryDailyClassificationId, territoryWeeklyClassificationId,
                territoryMonthlyClassificationId), CULTURE(cultureDailyClassificationId,
                        cultureWeeklyClassificationId,
                        cultureMonthlyClassificationId), SPORT(sportDailyClassificationId,
                                sportWeeklyClassificationId, sportMonthlyClassificationId);

        private String dailyClassificationId;
        private String weeklyClassificationId;
        private String monthlyClassificationId;

        private RankingType(String dailyClassificationId, String weeklyClassificationId,
                String monthlyClassificationId) {
            this.dailyClassificationId = dailyClassificationId;
            this.weeklyClassificationId = weeklyClassificationId;
            this.monthlyClassificationId = monthlyClassificationId;
        }

        public String getDailyClassificationId() {
            return dailyClassificationId;
        }

        public String getWeeklyClassificationId() {
            return weeklyClassificationId;
        }

        public String getMonthlyClassificationId() {
            return monthlyClassificationId;
        }
    }


    @Autowired
    private GEProps gamificationEngineProps;

    private ApiClient apiClient;

    @PostConstruct
    public void init() {
        apiClient = new ApiClient(gamificationEngineProps.getUrl());
        apiClient.setUsername(gamificationEngineProps.getUsername());
        apiClient.setPassword(gamificationEngineProps.getPassword());
    }

    public void subscribe(Player subscriber) {
        PlayerStateDTO playerState = new PlayerStateDTO();
        playerState.setPlayerId(subscriber.getPlayerId());
        playerState.setGameId(subscriber.getGameId());
        playerState.setCustomData(new HashMap<>());
        playerState.getCustomData().put(componentsCustomField, subscriber.getComponents());
        try {
            new PlayerControllerApi(apiClient).createPlayerUsingPOST1(
                    subscriber.getGameId(),
                    playerState);
        } catch (ApiException e) {
            logger.error("Exception calling gamification-engine API: {}", e.getMessage());
            throw new GEHelperException(e);
        }
    }

    public void unsubscribe(Player unsubscriber) {
        try {
            new PlayerControllerApi(apiClient).deletePlayerUsingDELETE1(unsubscriber.getGameId(),
                    unsubscriber.getPlayerId());
        } catch (ApiException e) {
            logger.error("Exception calling gamification-engine API");
            throw new GEHelperException(e);
        }
    }

    public GamificationPlayerProfile getPlayerProfile(String playerId, String gameModelId,
            String gamificationId) {
        PlayerStateDTO state = null;
        try {
            state = new PlayerControllerApi(apiClient).readStateUsingGET(gamificationId, playerId);
        } catch (ApiException | IOException e) {
            logger.error("Exception calling gamification-engine API");
            throw new GEHelperException(e);
        }
        GamificationPlayerProfile profile = new GamificationPlayerProfile();
        profile.setPlayerId(playerId);
        profile.setGamificationId(gamificationId);
        profile.setTotalTerritoryScore(extractScore(totalTerritoryScoreName, state));
        profile.setTotalCultureScore(extractScore(totalCultureScoreName, state));
        profile.setTotalSportScore(extractScore(totalSportScoreName, state));

        profile.setPersonageTerritoryScore(extractScore(personageTerritoryScoreName, state));
        profile.setPersonageCultureScore(extractScore(personageCultureScoreName, state));
        profile.setPersonageSportScore(extractScore(personageSportScoreName, state));

        profile.setRewardTerritoryScore(extractScore(rewardTerritoryScoreName, state));
        profile.setRewardCultureScore(extractScore(rewardCultureScoreName, state));
        profile.setRewardSportScore(extractScore(rewardSportScoreName, state));

        return profile;
    }


    public Ranking getRanking(String gamificationId, String playerId, RankingType type) {
        Ranking ranking = new Ranking();
        ranking.setDaily(getPosition(gamificationId, playerId, type.getDailyClassificationId()));
        ranking.setWeekly(getPosition(gamificationId, playerId, type.getWeeklyClassificationId()));
        ranking.setMonthly(
                getPosition(gamificationId, playerId, type.getMonthlyClassificationId()));
        return ranking;
    }

    private Position getPosition(String gamificationId, String playerId, String classificationId) {
        Position position = new Position(1, 0);
        try {
            position = getPosition(playerId,
                    getBoard(gamificationId, classificationId));
        } catch (GEHelperException e) {
            logger.warn("Exception reading classification board {} of gamification game {}",
                    classificationId, gamificationId);
        }
        return position;
    }

    private Position getPosition(String playerId, ClassificationBoard board) {
        final List<ClassificationPosition> positions = board.getBoard();
        int position = 0, nextPosition = 1;
        Double lastScore = null;
        boolean sameScore = false;
        for (ClassificationPosition p : positions) {
            sameScore = lastScore != null && lastScore.equals(p.getScore());
            if (!sameScore) {
                position = nextPosition;
            }
            lastScore = p.getScore();
            nextPosition++;
            if (p.getPlayerId().equals(playerId)) {
                return new Position(position, p.getScore());
            }
        }
        return new Position(1, 0);

    }
    private ClassificationBoard getBoard(String gamificationId, String classificationId) {
        ClassificationBoard board = null;
        try {
            board = new ClassificationControllerApi(apiClient).getIncrementalClassificationUsingGET(
                    gamificationId, classificationId, System.currentTimeMillis(), -1, -1, -1);
        } catch (ApiException e) {
            logger.error("Exception calling gamification-engine API");
            throw new GEHelperException(e);
        }
        return board;
    }


    private double extractScore(String scoreName, PlayerStateDTO state) {
        Set<GameConcept> scores = state.getState().get("PointConcept");
        return scores.stream().map(gc -> (PointConcept) gc)
                .filter(p -> p.getName().equals(scoreName))
                .findFirst().map(s -> s.getScore()).orElseThrow(() -> new IllegalArgumentException(
                        String.format("score %s not found", scoreName)));
    }

    public void action(String playerId, Action action) {
        ExecutionDataDTO executionData = new ExecutionDataDTO();
        executionData.setGameId(action.getGameId());
        executionData.setData(action.getParams());
        executionData.setPlayerId(playerId);
        // fix issue in gamification sdk v2.0.1
        executionData.setExecutionMoment(new Date());
        try {
            new ExecutionControllerApi(apiClient).executeActionUsingPOST(action.getGameId(),
                    action.getId(), executionData);
        } catch (ApiException e) {
            logger.error("Exception calling gamification-engine API");
            throw new GEHelperException(e);
        }
    }

    public void experience(String playerId, Experience exp) {
        action(playerId, convert(exp));
    }

    public void consumePersonage(String playerId, GamificationPersonage personage) {
        action(playerId, convert(personage));
    }

    public void consumeReward(String playerId, GamificationReward reward) {
        action(playerId, convert(reward));

    }

    private Action convert(GamificationReward reward) {
        Action action = new Action();
        action.setGameId(reward.getGameId());
        action.setId(consumeRewardAction);
        action.setParams(new HashMap<>());
        action.getParams().put("territory", reward.getTerritoryScore());
        action.getParams().put("culture", reward.getCultureScore());
        action.getParams().put("sport", reward.getSportScore());

        return action;
    }

    private Action convert(GamificationPersonage personage) {
        Action action = new Action();
        action.setGameId(personage.getGameId());
        action.setId(consumePersonageAction);
        action.setParams(new HashMap<>());
        action.getParams().put("character", personage.getName());
        action.getParams().put("territory", personage.getTerritoryScore());
        action.getParams().put("culture", personage.getCultureScore());
        action.getParams().put("sport", personage.getSportScore());

        return action;
    }

    private Action convert(Experience exp) {
        Action action = new Action();
        action.setGameId(exp.getGameId());
        action.setId(experienceAction);
        action.setParams(new HashMap<>());
        action.getParams().put("name", exp.getId());

        return action;
    }
    

    public static class GEHelperException extends RuntimeException {

        private static final long serialVersionUID = 4317283112971157529L;

        public GEHelperException() {
            super();
        }

        public GEHelperException(String message, Throwable cause) {
            super(message, cause);
        }

        public GEHelperException(String message) {
            super(message);
        }

        public GEHelperException(Throwable cause) {
            super(cause);
        }
        
    }

}
