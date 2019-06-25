package it.smartcommunitylab.smartchainbackend.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.GameModel;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.data.mongodb.database=smart-chain-backend-test")
public class GameModelManagerFunctionalTest {

    @Autowired
    private GameModelManager gameModelManager;

    @MockBean
    private GEHelper gamificationEngineHelper;

    @Autowired
    private MongoTemplate mongo;

    @Before
    public void cleanup() {
        mongo.getDb().drop();
    }

    @Test
    public void saveGameModel() {
        GameModel gameModel = new GameModel();
        gameModel.setName("my-game");
        gameModel = gameModelManager.saveGameModel(gameModel);
        assertThat(gameModel.getId(), is(notNullValue()));
    }


    @Test
    public void readModels() {
        GameModel gameModel = new GameModel();
        gameModel.setName("my-game");
        gameModelManager.saveGameModel(gameModel);

        gameModel = new GameModel();
        gameModel.setName("my-game-2");
        gameModel = gameModelManager.saveGameModel(gameModel);

        assertThat(gameModelManager.readGameModels(), hasSize(2));
    }

    @Test
    public void read_subscribed_games() {
        BDDMockito.doNothing().when(gamificationEngineHelper)
                .subscribe(BDDMockito.any(Player.class));

        GameModel model1 = new GameModel();
        model1.setId("model1");
        model1.setName("model1");
        gameModelManager.saveGameModel(model1);

        GameModel model2 = new GameModel();
        model2.setId("model2");
        model2.setName("model2");
        gameModelManager.saveGameModel(model2);

        GameModel model3 = new GameModel();
        model3.setId("model3");
        model3.setName("model3");
        gameModelManager.saveGameModel(model3);

        Player p = new Player();
        p.setGameId("model1");
        p.setPlayerId("player_1");
        gameModelManager.subscribe(p);


        p = new Player();
        p.setGameId("model3");
        p.setPlayerId("player_1");
        gameModelManager.subscribe(p);

        List<GameModel> models = gameModelManager.readGameModels("player_1");
        assertThat(models, hasSize(2));

        assertThat(models, hasItem(hasProperty("id", is("model1"))));
        assertThat(models, hasItem(hasProperty("id", is("model3"))));
        assertThat(models, not(hasItem(hasProperty("id", is("model2")))));
    }



}
