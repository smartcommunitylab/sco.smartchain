package it.smartcommunitylab.smartchainbackend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;
import it.smartcommunitylab.smartchainbackend.model.Subscription;
import it.smartcommunitylab.smartchainbackend.model.Subscription.CompositeKey;
import it.smartcommunitylab.smartchainbackend.repository.GameModelRepository;
import it.smartcommunitylab.smartchainbackend.repository.SubscriptionRepository;

@Service
public class GameModelManager {


    private static final Logger logger = LogManager.getLogger(GameModelManager.class);

    @Autowired
    private GEHelper gamificationEngineHelper;

    @Autowired
    private GameModelRepository gameModelRepo;

    @Autowired
    private SubscriptionRepository subscriptionRepo;


    public GameModel saveGameModel(GameModel gameModel) {
        gameModel = setActionIds(gameModel);
        return gameModelRepo.save(gameModel);
    }

    private GameModel setActionIds(GameModel gameModel) {
        List<ModelAction> actions = gameModel.getActions();
        for (ModelAction action : actions) {
            if (StringUtils.isBlank(action.getActionId())) {
                action.setActionId(UUID.randomUUID().toString());
            }
        }
        return gameModel;
    }

    public void deleteGameModel(GameModel gameModel) {
        gameModelRepo.delete(gameModel);
    }

    public List<GameModel> readGameModels() {
        return gameModelRepo.findAll();
    }

    public List<GameModel> readGameModels(String playerId) {
         List<Subscription> subscriptions = subscriptionRepo.findByIdPlayerId(playerId);
        return subscriptions.stream()
                .map(sub -> gameModelRepo.findById(sub.getId().getGameId()).get())
                .collect(Collectors.toList());
    }

    public Subscription subscribe(Player player) {
        Validator.throwIfInvalid(player.getGameId(), "gameId cannot be blank");
        Validator.throwIfInvalid(player.getPlayerId(), "playerId cannot be blank");

        Optional<GameModel> existGameModel = gameModelRepo.findById(player.getGameId());

        if (!existGameModel.isPresent()) {
            throw new IllegalArgumentException("gameModel not exist");
        }

        CompositeKey subscriptionId = new CompositeKey(player.getGameId(), player.getPlayerId());

        Optional<Subscription> existentSubscription = subscriptionRepo.findById(subscriptionId);

        if (!existentSubscription.isPresent()) {
            Subscription subscription = new Subscription();
            subscription.setId(subscriptionId);
            subscriptionRepo.save(subscription);
            logger.info("Player {} subscribed to game model {}", subscriptionId.getPlayerId(),
                    subscriptionId.getPlayerId());
            final String gamificationId = existGameModel.get().getGamificationId();
            Player gamificationPlayer = new Player();
            gamificationPlayer.setGameId(gamificationId);
            gamificationPlayer.setPlayerId(player.getPlayerId());
            gamificationPlayer.setComponents(player.getComponents());

            // create on gamification engine
            gamificationEngineHelper.subscribe(gamificationPlayer);
            logger.info("Player {} subscribed to gamification game {}",
                    gamificationPlayer.getPlayerId(), gamificationPlayer.getGameId());

            // TODO create smart contract

            return subscription;
        } else {
            return existentSubscription.get();
        }

    }

    public List<ModelExperience> getExperiences(String gameModelId) {
        return getModel(gameModelId).getExperiences();
    }

    public List<ModelAction> getActions(String gameModelId) {
        return getModel(gameModelId).getActions();
    }

    public List<ModelReward> getRewards(String gameModelId) {
        return getModel(gameModelId).getRewards();
    }


    public List<Personage> getPersonages(String gameModelId) {
        return getModel(gameModelId).getPersonages();
    }

    private GameModel getModel(String gameModelId) {
        Optional<GameModel> gameModel = gameModelRepo.findById(gameModelId);
        if (gameModel.isPresent()) {
            return gameModel.get();
        } else {
            throw new IllegalArgumentException("gameModel not exist");
        }
    }

    public boolean isSubscribed(String playerId, String gameModelId) {
        return subscriptionRepo.findById(new CompositeKey(gameModelId, playerId)).isPresent();
    }

    public String getGamificationId(String gameModelId) {
        return getModel(gameModelId).getGamificationId();
    }

    public Cost getPersonageCost(PersonageDTO personage) {
        final String gameModelId = personage.getGameId();
        GameModel model = getModel(gameModelId);
        Optional<Personage> optPersonage = model.getPersonages().stream()
                .filter(p -> p.getName().equals(personage.getName())).findFirst();
        return optPersonage.map(p -> p.getCost())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("personage %s not exist in gameModel %s", personage.getName(),
                                model.getName())));
    }


    public Cost getRewardCost(GameRewardDTO reward) {
        final String gameModelId = reward.getGameId();
        GameModel model = getModel(gameModelId);
        Optional<ModelReward> optReward = model.getRewards().stream()
                .filter(p -> p.getName().equals(reward.getName())).findFirst();
        return optReward.map(p -> p.getCost()).orElseThrow(() -> new IllegalArgumentException(String
                .format("reward %s not exist in gameModel %s", reward.getName(), model.getName())));
    }

    public String getGamificationActionId(String gameModelId, String modelActionId) {
        GameModel model = getModel(gameModelId);
        return model.getActions().stream().filter(a -> a.getActionId().equals(modelActionId)).findFirst()
                .map(a -> a.getGamificationActionName())
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "modelActionId %s not exist in gameModel %s", modelActionId, gameModelId)));
    }
}
