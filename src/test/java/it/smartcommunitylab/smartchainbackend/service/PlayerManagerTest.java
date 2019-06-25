package it.smartcommunitylab.smartchainbackend.service;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.GameModel;

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


}
