package it.smartcommunitylab.smartchainbackend.service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.aac.AACException;
import it.smartcommunitylab.aac.AACProfileService;
import it.smartcommunitylab.aac.model.BasicProfile;

@Component
public class AACHelper {

    private static final Logger logger = LogManager.getLogger(AACHelper.class);

    @Value("${profileServiceUrl}")
    @Autowired
    private String profileServiceUrl;

    private AACProfileService profileClient;


    @PostConstruct
    public void init() {
        profileClient = new AACProfileService(profileServiceUrl);
    }

    private BasicProfile getBasicProfile(HttpServletRequest request) {
        BasicProfile basicProfile = null;
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {
            token = token.replace("Bearer ", "");
            try {
                basicProfile = profileClient.findProfile(token);
            } catch (SecurityException | AACException e) {
                logger.warn("Exception getting user profile {}, {}", token, e);
                throw new UnauthorizedException(e);
            }
        } else {
            logger.warn("Exception: missing token");
            throw new UnauthorizedException("missing token");
        }
        return basicProfile;
    }

    public String getPlayerId(HttpServletRequest request) {
        return getBasicProfile(request).getUserId();
    }
}
