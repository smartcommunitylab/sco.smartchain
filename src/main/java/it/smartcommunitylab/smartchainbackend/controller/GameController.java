package it.smartcommunitylab.smartchainbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.service.GameManager;

@RestController
public class GameController {

    @Autowired
    private GameManager gameManager;

    @PostMapping(value = "/api/game/action")
    public void gameAction(@RequestBody Action action) {
        gameManager.playAction(action);
    }
}
