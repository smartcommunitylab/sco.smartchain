package it.smartcommunitylab.smartchainbackend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
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
        return gameModelRepo.save(gameModel);
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

    public boolean isSubscribed(String playerId, String gameModelId) {
        return subscriptionRepo.findById(new CompositeKey(gameModelId, playerId)).isPresent();
    }

    public String getGamificationId(String gameModelId) {
        Optional<GameModel> gameModel = gameModelRepo.findById(gameModelId);
        return gameModel.map(gm -> gm.getGamificationId())
                .orElseThrow(() -> new IllegalArgumentException("gameModel not exist"));
    }

    public Cost getPersonageCost(PersonageDTO personage) {
        final String gameModelId = personage.getGameId();
        Optional<GameModel> gameModel = gameModelRepo.findById(gameModelId);
        if (gameModel.isPresent()) {
            Optional<Personage> optPersonage =
                    gameModel.get().getPersonages().stream()
                    .filter(p -> p.getName().equals(personage.getName())).findFirst();
            return optPersonage.map(p -> p.getCost())
                    .orElseThrow(() -> new IllegalArgumentException(
                        String.format("personage %s not exist in gameModel %s", personage.getName(),
                                gameModel.get().getName())));
        } else {
            throw new IllegalArgumentException("gameModel not exist");
        }


    }
}
