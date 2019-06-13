package it.smartcommunitylab.smartchainbackend.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Player;

@Service
public class PlayerManager {

    @Autowired
    private GEHelper gamificationEngineHelper;

    public void subscribe(Player subscriber) {
        if (StringUtils.isBlank(subscriber.getGameId())) {
            throw new IllegalArgumentException("gameId in subscriber cannot be blank");
        }

        if (StringUtils.isBlank(subscriber.getPlayerId())) {
            throw new IllegalArgumentException("playerId in subscriber cannot be blank");
        }

        gamificationEngineHelper.subscribe(subscriber);
    }
}
