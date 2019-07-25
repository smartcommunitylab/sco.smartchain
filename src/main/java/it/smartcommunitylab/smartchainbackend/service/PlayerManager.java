package it.smartcommunitylab.smartchainbackend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;
import it.smartcommunitylab.smartchainbackend.model.Subscription;
import it.smartcommunitylab.smartchainbackend.model.Subscription.CompositeKey;
import it.smartcommunitylab.smartchainbackend.repository.SubscriptionRepository;

@Service
public class PlayerManager {

    @Autowired
    private GEHelper gamificationEngineHelper;

    @Autowired
    private GameModelManager gameModelManager;

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    private static final Logger logger = LogManager.getLogger(PlayerManager.class);

    // public void subscribe(Player subscriber) {
    //
    // Validator.throwIfInvalid(subscriber.getGameId(), "gameId in subscriber cannot be blank");
    // Validator.throwIfInvalid(subscriber.getPlayerId(),
    // "playerId in subscriber cannot be blank");
    //
    // final String gamificationId = gameModelManager.getGamificationId(subscriber.getGameId());
    // subscriber.setGameId(gamificationId);
    // // subscribe
    // gamificationEngineHelper.subscribe(subscriber);
    //
    // // save the subscription
    //
    // }

    public void playAction(String playerId, Action action) {
        final String gameModelId = action.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final GameModel model = gameModelManager.getModel(gameModelId);
        final ModelAction modelAction = model.getAction(action.getId());
        final String gamificationId = model.getGamificationId();
        final String gamificationActionId =
                gameModelManager.getGamificationActionId(gameModelId, action.getId());
        Action gamificationAction = new Action();
        gamificationAction.setGameId(gamificationId);
        gamificationAction.setId(gamificationActionId);
        gamificationAction.setParams(action.getParams());
        gamificationEngineHelper.action(playerId, gamificationAction);
        completeAction(action);
        logger.info("Player {} completed action {} (id: {})", playerId, modelAction.getName(),
                modelAction.getActionId());
    }

    public void playExperience(String playerId, Experience experience) {
        final String gameModelId = experience.getGameId();
        boolean isSubscribed = gameModelManager.isSubscribed(playerId, gameModelId);
        if (!isSubscribed) {
            throw new IllegalArgumentException(
                    String.format("%s is not subscribed to game %s", playerId, gameModelId));
        }

        final GameModel model = gameModelManager.getModel(gameModelId);
        final ModelExperience modelExperience = model.getExperience(experience.getId());
        final String gamificationId = model.getGamificationId();
        final String gamificationExperienceId = 
                gameModelManager.getGamificationExperienceId(gameModelId, experience.getId());
        Experience gamificationExperience = new Experience();
        gamificationExperience.setGameId(gamificationId);
        gamificationExperience.setId(gamificationExperienceId);
        gamificationEngineHelper.experience(playerId, gamificationExperience);
        completeExperience(experience);
        logger.info("Player {} completed experience {} (id: {})", playerId,
                modelExperience.getName(), modelExperience.getExperienceId());


    }

    private void completeExperience(Experience experience) {
       final String gameModelId = experience.getGameId();
       final String playerId = experience.getPlayerId();
        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));
        subscription.ifPresent(s -> {
            s.getCompletedExperiences().add(experience.getId());
            subscriptionRepo.save(s);
        });
    }

    private void completeAction(Action action) {
        final String gameModelId = action.getGameId();
        final String playerId = action.getPlayerId();
        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));
        subscription.ifPresent(s -> {
            s.getCompletedActions().add(action.getId());
            subscriptionRepo.save(s);
        });
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
        final GameModel model = gameModelManager.getModel(gameModelId);
        final String gamificationId = model.getGamificationId();
        PlayerProfile profile =
                gamificationEngineHelper.getPlayerProfile(playerId, gameModelId, gamificationId);

        List<Personage> personages = gameModelManager.getPersonages(gameModelId);
        List<ModelReward> rewards = gameModelManager.getRewards(gameModelId);
        
        List<Personage> usablePersonages = usablePersonages(personages, profile);
        List<ModelReward> usableRewards = usableRewards(rewards, profile);

        profile.setUsablePersonages(usablePersonages);
        profile.setUsableRewards(usableRewards);

        Optional<Subscription> subscription =
                subscriptionRepo.findById(new CompositeKey(gameModelId, playerId));
        subscription.ifPresent(s -> {
            List<ModelExperience> modelExperiences =
                    model.getExperiences(s.getCompletedExperiences());
            profile.setCompletedExperiences(modelExperiences);

            List<ModelAction> modelActions = model.getActions(s.getCompletedActions());
            profile.setCompletedActions(modelActions);

        });

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
