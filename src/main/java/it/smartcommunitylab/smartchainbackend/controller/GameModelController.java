package it.smartcommunitylab.smartchainbackend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.model.GameModel;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Challenge;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelAction;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelReward;
import it.smartcommunitylab.smartchainbackend.model.GameModel.Personage;
import it.smartcommunitylab.smartchainbackend.service.AuthService;
import it.smartcommunitylab.smartchainbackend.service.GameModelManager;

@RestController
public class GameModelController {

    @Autowired
    private GameModelManager gameModelManager;


    @Autowired
    private AuthService authService;

    @GetMapping("/api/game-model/{gameModelId}/action")
    @JsonView(JsonVisibility.Public.class)
    public List<ModelAction> getActions(@PathVariable String gameModelId) {
        return gameModelManager.getActions(gameModelId);
    }

    @GetMapping("/api/game-model/{gameModelId}/challenge")
    @JsonView(JsonVisibility.Public.class)
    public List<Challenge> getChallenges(@PathVariable String gameModelId) {
        return gameModelManager.getChallenges(gameModelId);
    }

    @GetMapping("/api/game-model/{gameModelId}/experience")
    @JsonView(JsonVisibility.Public.class)
    public List<ModelExperience> getExperiences(@PathVariable String gameModelId) {
        return gameModelManager.getExperiences(gameModelId);
    }

    @GetMapping("/api/game-model/{gameModelId}/character")
    public List<Personage> getPersonages(@PathVariable String gameModelId) {
        return gameModelManager.getPersonages(gameModelId);
    }

    @GetMapping("/api/game-model/{gameModelId}/reward")
    public List<ModelReward> getRewards(@PathVariable String gameModelId) {
        return gameModelManager.getRewards(gameModelId);
    }

    @GetMapping("/api/game-model")
    @JsonView(JsonVisibility.Public.class)
    public List<GameModel> getGameModels() {
        return gameModelManager.readGameModels();
    }

    @GetMapping("/api/game-model/subscription/{playerId}")
    @JsonView(JsonVisibility.Public.class)
    public List<GameModel> getSubscriptedGameModels(@PathVariable String playerId,
            HttpServletRequest request) {
        authService.check(request, playerId);
        return gameModelManager.readGameModels(playerId);
    }



}
