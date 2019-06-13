package it.smartcommunitylab.smartchainbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.service.PlayerManager;

@RestController
public class PlayerController {

    @Autowired
    private PlayerManager playerManager;

    @PostMapping(value = "/api/subscribe")
    public void subscribe(@RequestBody Player subscriber) {
        playerManager.subscribe(subscriber);
    }

    @PostMapping(value = "/api/unsubscribe")
    public void unsubscribe(Player player) {
    }

}
