package it.smartcommunitylab.smartchainbackend.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("sec")
public class AuthManager implements AuthService {

    private static final Logger logger = LogManager.getLogger(AuthManager.class);

    @Autowired
    private AACHelper aacHelper;

    @Override
    public void check(HttpServletRequest request, String playerId) {
        final String authenticatedPlayer = aacHelper.getPlayerId(request);
        if (!playerId.equals(authenticatedPlayer)) {
            logger.warn("Player {} tries to play as player {}",
                    authenticatedPlayer, playerId);
            throw new UnauthorizedException(
                    String.format("token not authorized for player %s", playerId));
        }
    }
}
