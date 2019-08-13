package it.smartcommunitylab.smartchainbackend.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    void check(HttpServletRequest request, String playerId);

}
