package it.smartcommunitylab.smartchainbackend.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.Subscription;
import it.smartcommunitylab.smartchainbackend.model.Subscription.CompositeKey;
import it.smartcommunitylab.smartchainbackend.repository.GameModelRepository;
import it.smartcommunitylab.smartchainbackend.repository.SubscriptionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.data.mongodb.database=smart-chain-backend-test")
public class GameModelManagerTest {

    @Autowired
    private GameModelManager gameModelManager;

    @MockBean
    private GEHelper gamificationEngineHelper;

    @MockBean
    private GameModelRepository gameModelRepo;

    @MockBean
    private SubscriptionRepository subscriptionRepo;


    @Test
    public void subscribe_to_game() {
        
        BDDMockito.given(gameModelRepo.findById(BDDMockito.anyString()))
                .will(new Answer<Optional<GameModel>>() {

                    @Override
                    public Optional<GameModel> answer(InvocationOnMock arg0) throws Throwable {
                        GameModel result = new GameModel();
                        result.setId("my-game");
                        result.setGamificationId("game-id");
                        return Optional.of(result);
                    }
                });

        BDDMockito.doNothing().when(gamificationEngineHelper)
                .subscribe(BDDMockito.any(Player.class));
        Player player = new Player();
        player.setPlayerId("player_1");
        player.setGameId("my-game");
        player.setComponents(3);

        Subscription subscription = gameModelManager.subscribe(player);
        assertThat(subscription, is(notNullValue()));
        assertThat(subscription.getId().getPlayerId(), is("player_1"));
        assertThat(subscription.getId().getGameId(), is("my-game"));
    }

    @Test
    public void subscribe_to_game_when_subscription_exists() {
        BDDMockito.given(gameModelRepo.findById(BDDMockito.anyString()))
                .will(new Answer<Optional<GameModel>>() {

                    @Override
                    public Optional<GameModel> answer(InvocationOnMock arg0) throws Throwable {
                        GameModel result = new GameModel();
                        result.setId("my-game");
                        result.setGamificationId("game-id");
                        return Optional.of(result);
                    }
                });

        BDDMockito.doNothing().when(gamificationEngineHelper)
                .subscribe(BDDMockito.any(Player.class));

        BDDMockito.given(subscriptionRepo.findById(BDDMockito.any(CompositeKey.class)))
                .will(new Answer<Optional<Subscription>>() {

                    @Override
                    public Optional<Subscription> answer(InvocationOnMock arg0) throws Throwable {
                        Subscription result = new Subscription();
                        result.setId(new CompositeKey("my-game", "player_1"));
                        return Optional.of(result);
                    }
                });

        Player player = new Player();
        player.setPlayerId("player_1");
        player.setGameId("my-game");
        player.setComponents(3);

        Subscription subscription = gameModelManager.subscribe(player);
        assertThat(subscription, is(notNullValue()));
        assertThat(subscription.getId().getPlayerId(), is("player_1"));
        assertThat(subscription.getId().getGameId(), is("my-game"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void subscribe_to_game_when_gamemodel_not_exist() {
        BDDMockito.doNothing().when(gamificationEngineHelper)
                .subscribe(BDDMockito.any(Player.class));

        BDDMockito.given(subscriptionRepo.findById(BDDMockito.any(CompositeKey.class)))
                .will(new Answer<Optional<Subscription>>() {

                    @Override
                    public Optional<Subscription> answer(InvocationOnMock arg0) throws Throwable {
                        Subscription result = new Subscription();
                        result.setId(new CompositeKey("my-game", "player_1"));
                        return Optional.of(result);
                    }
                });

        Player player = new Player();
        player.setPlayerId("player_1");
        player.setGameId("my-game");
        player.setComponents(3);

        gameModelManager.subscribe(player);
    }

}
