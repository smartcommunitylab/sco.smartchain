package it.smartcommunitylab.smartchainbackend.service;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.Cost;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerManagerTest {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private GameModelManager gameModelManager;

    @Test
    public void subscribe_player() {
        Player subscriber = new Player();
        subscriber.setGameId("GAME_ID");
        subscriber.setPlayerId("PLAYER_ID");

        playerManager.subscribe(subscriber);
    }

    @Test
    public void play_action() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");
        gameModelManager.saveGameModel(game);

        Player player = new Player();
        player.setGameId("game");
        player.setPlayerId("faso");
        player.setComponents(2);
        gameModelManager.subscribe(player);

        Action action = new Action();
        action.setGameId("game");
        action.setName("bring-a-friend");
        playerManager.playAction("faso", action);
    }

    @Test
    public void play_action_with_params() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");
        gameModelManager.saveGameModel(game);

        Player player = new Player();
        player.setGameId("game");
        player.setPlayerId("faso");
        player.setComponents(2);
        gameModelManager.subscribe(player);

        Action action = new Action();
        action.setGameId("game");
        action.setName("spend-50");
        action.setParams(new HashMap<>());
        action.getParams().put("value", 80.0);
        playerManager.playAction("faso", action);
    }

    // player not subscribed to game

    @Test
    public void play_experience() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");
        gameModelManager.saveGameModel(game);

        Experience exp = new Experience("game", "forra-del-lupo");
        playerManager.playExperience("faso", exp);
    }

    @Test
    public void consume_character() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");
        
        Personage basilisco = new Personage();
        basilisco.setName("basilisco");
        Cost basiliscoCost = new Cost();
        basiliscoCost.setTerritoryScore(10d);
        basiliscoCost.setCultureScore(5d);
        basiliscoCost.setSportScore(3d);
        basilisco.setCost(basiliscoCost);
        
        game.getPersonages().add(basilisco);
        gameModelManager.saveGameModel(game);

        PersonageDTO character = new PersonageDTO();
        character.setGameId("game");
        character.setName("basilisco");
        playerManager.consumePersonage("faso", character);
    }


    @Test
    public void consume_reward() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");

        ModelReward reward =
                new ModelReward();
        reward.setName("my-reward");
        Cost cost = new Cost();
        cost.setTerritoryScore(10d);
        cost.setCultureScore(5d);
        cost.setSportScore(3d);
        reward.setCost(cost);

        game.getRewards().add(reward);
        gameModelManager.saveGameModel(game);
        
        GameRewardDTO rewardDTO = new GameRewardDTO();
        rewardDTO.setGameId("game");
        rewardDTO.setName("my-reward");
        playerManager.consumeReward("faso", rewardDTO);

    }
}
