package it.smartcommunitylab.smartchainbackend.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.basic.api.PlayerControllerApi;
import it.smartcommunitylab.model.PlayerStateDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.config.GEProps;

@Component
public class GEHelper {

    private static final Logger logger = LogManager.getLogger(GEHelper.class);


    private final String componentsCustomField = "components";

    @Autowired
    private GEProps gamificationEngineProps;

    private ApiClient apiClient;

    @PostConstruct
    public void init() {
        apiClient = new ApiClient(gamificationEngineProps.getUrl());
        apiClient.setUsername(gamificationEngineProps.getUsername());
        apiClient.setPassword(gamificationEngineProps.getPassword());
    }
    public void subscribe(Player subscriber) {
        PlayerStateDTO playerState = new PlayerStateDTO();
        playerState.setPlayerId(subscriber.getPlayerId());
        playerState.setCustomData(new HashMap<>());
        playerState.getCustomData().put(componentsCustomField, subscriber.getComponents());
        try {
            new PlayerControllerApi(apiClient).createPlayerUsingPOST1(
                    subscriber.getGameId(),
                    playerState);
        } catch (ApiException e) {
            logger.error("Exception calling gamification-engine API");
            throw new GEHelperException(e);
        }
    }

    
    public static class GEHelperException extends RuntimeException {

        private static final long serialVersionUID = 4317283112971157529L;

        public GEHelperException() {
            super();
        }

        public GEHelperException(String message, Throwable cause) {
            super(message, cause);
        }

        public GEHelperException(String message) {
            super(message);
        }

        public GEHelperException(Throwable cause) {
            super(cause);
        }
        
    }
}
