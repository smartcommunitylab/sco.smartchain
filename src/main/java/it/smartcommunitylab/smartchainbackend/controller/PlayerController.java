package it.smartcommunitylab.smartchainbackend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.CertificationActionDTO;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.bean.GameRewardDTO;
import it.smartcommunitylab.smartchainbackend.bean.PersonageDTO;
import it.smartcommunitylab.smartchainbackend.bean.Player;
import it.smartcommunitylab.smartchainbackend.bean.PlayerChallenge;
import it.smartcommunitylab.smartchainbackend.model.PlayerProfile;
import it.smartcommunitylab.smartchainbackend.service.AuthService;
import it.smartcommunitylab.smartchainbackend.service.GameModelManager;
import it.smartcommunitylab.smartchainbackend.service.PlayerManager;
import it.smartcommunitylab.smartchainbackend.service.Rankings;

@RestController
public class PlayerController {

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private GameModelManager gameModelManager;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/api/subscribe")
    public void subscribe(@RequestBody Player subscriber, HttpServletRequest request) {
        authService.check(request, subscriber.getPlayerId());
        gameModelManager.subscribe(subscriber);
    }

    @PostMapping(value = "/api/unsubscribe")
    public void unsubscribe(@RequestBody Player unsubscriber, HttpServletRequest request) {
        authService.check(request, unsubscriber.getPlayerId());
        gameModelManager.unsubscribe(unsubscriber);
    }

    @GetMapping("api/profile/{gameModelId}/{playerId}")
    @JsonView(JsonVisibility.Public.class)
    public PlayerProfile getProfile(@PathVariable String gameModelId,
            @PathVariable String playerId, HttpServletRequest request) {
        authService.check(request, playerId);
        return playerManager.getProfile(playerId, gameModelId);
    }

    @GetMapping("api/challenge/{gameModelId}/{playerId}")
    public List<PlayerChallenge> getChallenges(@PathVariable String gameModelId,
            @PathVariable String playerId, HttpServletRequest request) {
        authService.check(request, playerId);
        return playerManager.challenges(gameModelId, playerId);
    }

    @GetMapping("api/ranking/{gameModelId}/{playerId}")
    @JsonView(JsonVisibility.Public.class)
    public Rankings getRankings(@PathVariable String gameModelId, @PathVariable String playerId,
            HttpServletRequest request) {
        authService.check(request, playerId);
        return playerManager.getRankings(playerId, gameModelId);
    }

    @PostMapping("api/play/action")
    public void playAction(@RequestBody Action action, HttpServletRequest request) {
        authService.check(request, action.getPlayerId());
        playerManager.playAction(action.getPlayerId(), action);
    }

    @PostMapping("api/play/experience")
    public void playExperience(@RequestBody Experience exp, HttpServletRequest request) {
        authService.check(request, exp.getPlayerId());
        playerManager.playExperience(exp.getPlayerId(), exp);
    }

    @PostMapping("api/experience/certificate")
    public void certificateExperienceAction(
            @RequestBody CertificationActionDTO certification, HttpServletRequest request) {
        authService.check(request, certification.getPlayerId());
        playerManager.certificateExperience(certification);
    }

    @PostMapping("api/consume/character")
    public void consumePersonage(@RequestBody PersonageDTO personage, HttpServletRequest request) {
        authService.check(request, personage.getConsumer());
        playerManager.consumePersonage(personage.getConsumer(), personage);
    }

    @PostMapping("api/consume/reward")
    public void consumeReward(@RequestBody GameRewardDTO reward, HttpServletRequest request) {
        authService.check(request, reward.getConsumer());
        playerManager.consumeReward(reward.getConsumer(), reward);
    }

}
