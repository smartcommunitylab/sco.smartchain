package it.smartcommunitylab.smartchainbackend.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
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
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.data.mongodb.database=smart-chain-backend-test")
public class PlayerManagerTest {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private GameModelManager gameModelManager;


    @Autowired
    private MongoTemplate mongo;

    @Before
    public void cleanup() {
        mongo.getDb().drop();
    }

    // @Test
    // public void subscribe_player() {
    // Player subscriber = new Player();
    // subscriber.setGameId("GAME_ID");
    // subscriber.setPlayerId("PLAYER_ID");
    //
    // playerManager.subscribe(subscriber);
    // }

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

    @Test
    public void player_profile() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");
        gameModelManager.saveGameModel(game);

        PlayerProfile profile = playerManager.getProfile("faso", "game");
        assertThat(profile, is(notNullValue()));
    }

    @Test
    public void player_got_one_usable_reward() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");

        List<ModelReward> rewards = new ArrayList<>();
        ModelReward reward = new ModelReward();
        reward.setName("drink a beer");
        reward.setCost(new Cost(80, 20, 50));
        rewards.add(reward);
        reward = new ModelReward();
        reward.setName("go to museum");
        reward.setCost(new Cost(200, 200, 200));
        rewards.add(reward);

        game.getRewards().addAll(rewards);

        gameModelManager.saveGameModel(game);

        PlayerProfile profile = playerManager.getProfile("faso", "game");
        assertThat(profile.getUsableRewards(), hasSize(1));
    }


    @Test
    public void player_got_two_usable_characters() {
        GameModel game = new GameModel();
        game.setId("game");
        game.setGamificationId("5d020a44e22362287f1677e0");
        game.setName("game");

        List<Personage> personages = new ArrayList<>();
        Personage basilisco = new Personage();
        basilisco.setName("basilisco");
        basilisco.setCost(new Cost(20, 20, 50));
        personages.add(basilisco);

        Personage orso = new Personage();
        orso.setName("orso");
        orso.setCost(new Cost(50, 20, 100));
        personages.add(orso);

        Personage lupo = new Personage();
        lupo.setName("lupo");
        lupo.setCost(new Cost(200, 200, 200));
        personages.add(lupo);

        game.getPersonages().addAll(personages);

        gameModelManager.saveGameModel(game);

        PlayerProfile profile = playerManager.getProfile("faso", "game");
        assertThat(profile.getUsablePersonages(), hasSize(2));
    }
}
