package it.smartcommunitylab.smartchainbackend.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"default", "no-sec"})
public class DummieAuthManager implements AuthService {

    @Override
    public void check(HttpServletRequest request, String playerId) {
        return; // do nothing
    }

}
