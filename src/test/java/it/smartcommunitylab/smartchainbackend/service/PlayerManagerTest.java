package it.smartcommunitylab.smartchainbackend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.smartcommunitylab.smartchainbackend.bean.Player;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerManagerTest {

    @Autowired
    private PlayerManager playerManager;

    @Test
    public void should_subscribe_player() {
        Player subscriber = new Player();
        subscriber.setGameId("GAME_ID");
        subscriber.setPlayerId("PLAYER_ID");

        playerManager.subscribe(subscriber);
    }

}
