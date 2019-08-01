package it.smartcommunitylab.smartchainbackend.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.basic.api.ExecutionControllerApi;
import it.smartcommunitylab.basic.api.PlayerControllerApi;
import it.smartcommunitylab.model.PlayerStateDTO;
import it.smartcommunitylab.model.ext.ExecutionDataDTO;
import it.smartcommunitylab.model.ext.GameConcept;
import it.smartcommunitylab.model.ext.PointConcept;
import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.config.GEProps;
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;

@Component
public class GEHelper {



    private static final Logger logger = LogManager.getLogger(GEHelper.class);

    private static final String componentsCustomField = "components";

    private static final String experienceAction = "experience";
    private static final String consumePersonageAction = "consume-character";
    private static final String consumeRewardAction = "consume-reward";

    private static final String territoryScoreName = "total_territory";
    private static final String cultureScoreName = "total_culture";
    private static final String sportScoreName = "total_sport";

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

    public PlayerProfile getPlayerProfile(String playerId, String gameModelId,
            String gamificationId) {
        PlayerStateDTO state = null;
        try {
            state = new PlayerControllerApi(apiClient).readStateUsingGET(gamificationId, playerId);
        } catch (ApiException | IOException e) {
            logger.error("Exception calling gamification-engine API");
            throw new GEHelperException(e);
        }
        PlayerProfile profile = new PlayerProfile();
        profile.setPlayerId(playerId);
        profile.setGameId(gameModelId);
        profile.setTerritoryScore(extractScore(territoryScoreName, state));
        profile.setCultureScore(extractScore(cultureScoreName, state));
        profile.setSportScore(extractScore(sportScoreName, state));
        return profile;
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
