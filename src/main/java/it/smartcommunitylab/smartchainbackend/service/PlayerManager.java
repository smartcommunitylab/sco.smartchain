package it.smartcommunitylab.smartchainbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.Player;

@Service
public class PlayerManager {

    @Autowired
    private GEHelper gamificationEngineHelper;

    @Autowired
    private GameModelManager gameModelManager;

    public void subscribe(Player subscriber) {

        Validator.throwIfInvalid(subscriber.getGameId(), "gameId in subscriber cannot be blank");
        Validator.throwIfInvalid(subscriber.getPlayerId(),
                "playerId in subscriber cannot be blank");

        // subscribe
        gamificationEngineHelper.subscribe(subscriber);
    }

    public void playAction(String playerId, Action action) {
        final String gameModelId = action.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final String gamificationId = gameModelManager.getGamificationId(gameModelId);
        Action gamificationAction = new Action();
        gamificationAction.setGameId(gamificationId);
        gamificationAction.setName(action.getName());
        gamificationAction.setParams(action.getParams());

        gamificationEngineHelper.action(playerId, gamificationAction);
    }

    public void playExperience(String playerId, Experience exp) {
        final String gameModelId = exp.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final String gamificationId = gameModelManager.getGamificationId(gameModelId);
        Experience gamificationExperience = new Experience();
        gamificationExperience.setGameId(gamificationId);
        gamificationExperience.setName(exp.getName());

        gamificationEngineHelper.experience(playerId, gamificationExperience);

    }
}
