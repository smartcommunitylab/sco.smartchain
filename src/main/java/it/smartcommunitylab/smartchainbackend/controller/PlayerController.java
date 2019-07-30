package it.smartcommunitylab.smartchainbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;
import it.smartcommunitylab.smartchainbackend.service.GameModelManager;
import it.smartcommunitylab.smartchainbackend.service.PlayerManager;

@RestController
public class PlayerController {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private GameModelManager gameModelManager;

    @PostMapping(value = "/api/subscribe")
    public void subscribe(@RequestBody Player subscriber) {
        gameModelManager.subscribe(subscriber);
    }

    @PostMapping(value = "/api/unsubscribe")
    public void unsubscribe(@RequestBody Player unsubscriber) {
        gameModelManager.unsubscribe(unsubscriber);
    }

    @GetMapping("api/profile/{gameModelId}/{playerId}")
    public PlayerProfile getProfile(@PathVariable String gameModelId,
            @PathVariable String playerId) {
        return playerManager.getProfile(playerId, gameModelId);
    }

    @PostMapping("api/play/action")
    public void playAction(@RequestBody Action action) {
        playerManager.playAction(action.getPlayerId(), action);
    }

    @PostMapping("api/play/experience")
    public void playExperience(@RequestBody Experience exp) {
        playerManager.playExperience(exp.getPlayerId(), exp);
    }

    @PostMapping("api/consume/character")
    public void consumePersonage(@RequestBody PersonageDTO personage) {
        playerManager.consumePersonage(personage.getConsumer(), personage);
    }

    @PostMapping("api/consume/reward")
    public void consumeReward(@RequestBody GameRewardDTO reward) {
        playerManager.consumeReward(reward.getConsumer(), reward);
    }

}
