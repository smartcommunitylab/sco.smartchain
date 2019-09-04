package it.smartcommunitylab.smartchainbackend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.GameModel.CertificationAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Challenge;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;
import it.smartcommunitylab.smartchainbackend.model.Subscription;
import it.smartcommunitylab.smartchainbackend.model.Subscription.CompositeKey;
import it.smartcommunitylab.smartchainbackend.repository.GameModelRepository;
import it.smartcommunitylab.smartchainbackend.repository.SubscriptionRepository;
import it.smartcommunitylab.smartchainbackend.service.GEHelper.GEHelperException;

@Service
public class GameModelManager {


    private static final Logger logger = LogManager.getLogger(GameModelManager.class);

    @Autowired
    private GEHelper gamificationEngineHelper;

    @Autowired
    private GameModelRepository gameModelRepo;

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    @Value("${executionUrlPath}")
    private String executionUrlPath;


    public GameModel saveGameModel(GameModel gameModel) {
        gameModel = setActionIds(gameModel);
        gameModel = setExperienceIds(gameModel);
        gameModel = setRewardIds(gameModel);
        gameModel = setCertificationActionIds(gameModel);
        return gameModelRepo.save(gameModel);
    }

    private GameModel setCertificationActionIds(GameModel gameModel) {
        List<ModelExperience> experiences = gameModel.getExperiences();
        for (ModelExperience experience : experiences) {
            for (CertificationAction certification : experience.getCertificationActions()) {
                if (StringUtils.isBlank(certification.getCertificationId())) {
                    certification.setCertificationId(UUID.randomUUID().toString());
                }
            }
        }
        return gameModel;
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

    private GameModel setExperienceIds(GameModel gameModel) {
        List<ModelExperience> experiences = gameModel.getExperiences();
        for (ModelExperience experience : experiences) {
            if (StringUtils.isBlank(experience.getExperienceId())) {
                experience.setExperienceId(UUID.randomUUID().toString());
            }
        }
        return gameModel;
    }


    private GameModel setRewardIds(GameModel gameModel) {
        List<ModelReward> rewards = gameModel.getRewards();
        for (ModelReward reward : rewards) {
            if (StringUtils.isBlank(reward.getRewardId())) {
                reward.setRewardId(UUID.randomUUID().toString());
            }
        }
        return gameModel;
    }

    public void deleteGameModel(GameModel gameModel) {
        gameModelRepo.delete(gameModel);
    }

    public List<GameModel> readGameModels() {
        List<GameModel> models = gameModelRepo.findAll();
        models.forEach(m -> {
            m = setExecutionUrls(m);
        });
        return models;
    }

    public List<GameModel> readGameModels(String playerId) {
         List<Subscription> subscriptions = subscriptionRepo.findByIdPlayerId(playerId);
        List<GameModel> models = subscriptions.stream()
                .map(sub -> gameModelRepo.findById(sub.getId().getGameId()).get())
                .collect(Collectors.toList());
        models.forEach(m -> {
            m = setExecutionUrls(m);
        });
        return models;
    }


    public Subscription unsubscribe(Player unsubscriber) {
        Validator.throwIfInvalid(unsubscriber.getGameId(), "gameId cannot be blank");
        Validator.throwIfInvalid(unsubscriber.getPlayerId(), "playerId cannot be blank");

        Optional<GameModel> existGameModel = gameModelRepo.findById(unsubscriber.getGameId());

        if (!existGameModel.isPresent()) {
            throw new IllegalArgumentException("gameModel not exist");
        }

        CompositeKey subscriptionId =
                new CompositeKey(unsubscriber.getGameId(), unsubscriber.getPlayerId());

        Optional<Subscription> existentSubscription = subscriptionRepo.findById(subscriptionId);

        if (existentSubscription.isPresent()) {
            Subscription subscription = existentSubscription.get();
            Player gamificationPlayer = new Player();
            final String gamificationId = existGameModel.get().getGamificationId();
            gamificationPlayer.setGameId(gamificationId);
            gamificationPlayer.setPlayerId(unsubscriber.getPlayerId());
            gamificationEngineHelper.unsubscribe(gamificationPlayer);
            subscriptionRepo.delete(subscription);
            logger.info("Player {} unsubscribed from game model {}", subscriptionId.getPlayerId(),
                    subscriptionId.getGameId());
            return subscription;
        }

        return null;
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
            subscription.setComponents(player.getComponents());
            subscriptionRepo.save(subscription);
            logger.info("Player {} subscribed to game model {}", subscriptionId.getPlayerId(),
                    subscriptionId.getGameId());
            final String gamificationId = existGameModel.get().getGamificationId();
            Player gamificationPlayer = new Player();
            gamificationPlayer.setGameId(gamificationId);
            gamificationPlayer.setPlayerId(player.getPlayerId());
            gamificationPlayer.setComponents(player.getComponents());

            // create on gamification engine
            try {
            gamificationEngineHelper.subscribe(gamificationPlayer);
            } catch (GEHelperException e) {
                if (e.getMessage() == null || !e.getMessage().contains("Bad Request")) {
                    throw e;
                }
            }
            logger.info("Player {} subscribed to gamification game {}",
                    gamificationPlayer.getPlayerId(), gamificationPlayer.getGameId());

            return subscription;
        } else {
            logger.warn("Player {} already subscribed to game model {}",
                    subscriptionId.getPlayerId(), subscriptionId.getGameId());
            return existentSubscription.get();
        }

    }

    public List<ModelExperience> getExperiences(String gameModelId) {
        return getModel(gameModelId).getExperiences();
    }

    public List<ModelAction> getActions(String gameModelId) {
        return getModel(gameModelId).getActions();
    }

    public List<Challenge> getChallenges(String gameModelId) {
        return getModel(gameModelId).getChallenges();
    }

    public List<ModelReward> getRewards(String gameModelId) {
        return getModel(gameModelId).getRewards();
    }


    public List<Personage> getPersonages(String gameModelId) {
        return getModel(gameModelId).getPersonages();
    }

    public GameModel getModel(String gameModelId) {
        Optional<GameModel> gameModel = gameModelRepo.findById(gameModelId);
        if (gameModel.isPresent()) {
            GameModel model = gameModel.get();
            model = setExecutionUrls(model);
            return model;
        } else {
            throw new IllegalArgumentException("gameModel not exist");
        }
    }

    private GameModel setExecutionUrls(GameModel model) {
        model = setActionExecutionUrls(model);
        model = setExperienceExecutionUrls(model);
        model = setCertificationExecutionUrls(model);
        return model;
    }

    private GameModel setCertificationExecutionUrls(GameModel model) {
      model.getExperiences().forEach(e -> e.getCertificationActions().forEach(c -> {
            c.setExecutionUrl(generateCertificationExecutionUrl(model, e, c));
        }));

        return model;
    }



    private GameModel setExperienceExecutionUrls(GameModel model) {
        model.getExperiences().forEach(e -> {
            e.setExecutionUrl(generateExperienceExecutionUrl(model, e));
        });
        return model;
    }

    private GameModel setActionExecutionUrls(GameModel model) {
        model.getActions().forEach(a -> {
            a.setExecutionUrl(generateActionExecutionUrl(model, a));
        });
        return model;
    }

    private String generateActionExecutionUrl(GameModel model, ModelAction a) {
        if (StringUtils.isBlank(executionUrlPath)) {
            logger.warn("executionUrlPath not configured in application.properties");
            return "";
        }
        return String.format("%s/action/%s/%s", executionUrlPath, model.getId(), a.getActionId());
    }

    private String generateExperienceExecutionUrl(GameModel model, ModelExperience e) {
        if (StringUtils.isBlank(executionUrlPath)) {
            logger.warn("executionUrlPath not configured in application.properties");
            return "";
        }
        return String.format("%s/experience/%s/%s", executionUrlPath, model.getId(),
                e.getExperienceId());
    }

    private String generateCertificationExecutionUrl(GameModel model, ModelExperience e,
            CertificationAction c) {
        if (StringUtils.isBlank(executionUrlPath)) {
            logger.warn("executionUrlPath not configured in application.properties");
            return "";
        }
        return String.format("%s/certification/%s/%s/%s", executionUrlPath,
                model.getId(), e.getExperienceId(), c.getCertificationId());
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
                .filter(p -> p.getPersonageId().equals(personage.getId())).findFirst();
        return optPersonage.map(p -> p.getCost())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("personage %s not exist in gameModel %s", personage.getId(),
                                model.getId())));
    }


    public Cost getRewardCost(GameRewardDTO reward) {
        final String gameModelId = reward.getGameId();
        GameModel model = getModel(gameModelId);
        Optional<ModelReward> optReward = model.getRewards().stream()
                .filter(r -> r.getRewardId().equals(reward.getId())).findFirst();
        return optReward.map(r -> r.getCost()).orElseThrow(() -> new IllegalArgumentException(String
                .format("reward %s not exist in gameModel %s", reward.getId(), model.getName())));
    }

    public String getGamificationActionId(String gameModelId, String modelActionId) {
        GameModel model = getModel(gameModelId);
        return model.getActions().stream().filter(a -> a.getActionId().equals(modelActionId)).findFirst()
                .map(a -> a.getGamificationActionName())
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "modelActionId %s not exist in gameModel %s", modelActionId, gameModelId)));
    }

    public String getGamificationExperienceId(String gameModelId, String modelExperienceId) {
        GameModel model = getModel(gameModelId);
        return model.getExperiences().stream()
                .filter(e -> e.getExperienceId().equals(modelExperienceId)).findFirst()
                .map(e -> e.getGamificationExperienceName())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("modelExperienceId %s not exist in gameModel %s",
                                modelExperienceId, gameModelId)));
    }
}
