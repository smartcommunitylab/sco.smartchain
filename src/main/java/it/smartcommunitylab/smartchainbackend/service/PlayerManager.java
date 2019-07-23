package it.smartcommunitylab.smartchainbackend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;

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

    public PlayerProfile getProfile(String playerId, String gameModelId) {
        final String gamificationId = gameModelManager.getGamificationId(gameModelId);
        PlayerProfile profile =
                gamificationEngineHelper.getPlayerProfile(playerId, gameModelId, gamificationId);

        List<Personage> personages = gameModelManager.getPersonages(gameModelId);
        List<ModelReward> rewards = gameModelManager.getRewards(gameModelId);
        
        List<Personage> usablePersonages = usablePersonages(personages, profile);
        List<ModelReward> usableRewards = usableRewards(rewards, profile);

        profile.setUsablePersonages(usablePersonages);
        profile.setUsableRewards(usableRewards);

        return profile;
    }

    private List<Personage> usablePersonages(List<Personage> personages, PlayerProfile profile) {
        final double territoryScore = profile.getTerritoryScore();
        final double cultureScore = profile.getCultureScore();
        final double sportscore = profile.getSportScore();
        return personages.stream()
                .filter(p -> p.getCost().getCultureScore() <= cultureScore
                        && p.getCost().getTerritoryScore() <= territoryScore
                        && p.getCost().getSportScore() <= sportscore)
                .collect(Collectors.toList());
    }

    private List<ModelReward> usableRewards(List<ModelReward> rewards, PlayerProfile profile) {
        final double territoryScore = profile.getTerritoryScore();
        final double cultureScore = profile.getCultureScore();
        final double sportscore = profile.getSportScore();
        return rewards.stream()
                .filter(p -> p.getCost().getCultureScore() <= cultureScore
                        && p.getCost().getTerritoryScore() <= territoryScore
                        && p.getCost().getSportScore() <= sportscore)
                .collect(Collectors.toList());
    }

}
