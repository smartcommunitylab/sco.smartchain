package it.smartcommunitylab.smartchainbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.Cost;

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

    public void consumePersonage(String playerId, PersonageDTO personage) {
        final String gameModelId = personage.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final String gamificationId = gameModelManager.getGamificationId(gameModelId);

        Cost personageCost = gameModelManager.getPersonageCost(personage);

        GamificationPersonage gamificationPersonage =
                new GamificationPersonage(gamificationId, personage, personageCost);

        gamificationEngineHelper.consumePersonage(playerId, gamificationPersonage);

    }

    public void consumeReward(String playerId, GameRewardDTO reward) {
        final String gameModelId = reward.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final String gamificationId = gameModelManager.getGamificationId(gameModelId);

        Cost cost = gameModelManager.getRewardCost(reward);

        GamificationReward gamificationReward = new GamificationReward(gamificationId, cost);

        gamificationEngineHelper.consumeReward(playerId, gamificationReward);

    }


}
