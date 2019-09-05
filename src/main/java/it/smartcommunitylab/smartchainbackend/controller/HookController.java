package it.smartcommunitylab.smartchainbackend.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.smartchainbackend.bean.Action;
import it.smartcommunitylab.smartchainbackend.bean.CertificationActionDTO;
import it.smartcommunitylab.smartchainbackend.bean.Experience;
import it.smartcommunitylab.smartchainbackend.service.AACHelper;
import it.smartcommunitylab.smartchainbackend.service.PlayerManager;

@RestController
public class HookController {

    @Autowired
    private AACHelper aacHelper;

    @Autowired
    private PlayerManager playerManager;


    @GetMapping("hook/action/{gameModelId}/{actionId}")
    public void hookAction(@PathVariable String gameModelId, @PathVariable String actionId,
            HttpServletRequest request) {
        final String playerId = aacHelper.getPlayerId(request);
        Action action = new Action();
        action.setGameId(gameModelId);
        action.setId(actionId);
        action.setPlayerId(playerId);
        playerManager.playAction(playerId, action);
    }

    @GetMapping("hook/experience/{gameModelId}/{experienceId}")
    public void hookExperience(@PathVariable String gameModelId, @PathVariable String experienceId,
            HttpServletRequest request) {
        final String playerId = aacHelper.getPlayerId(request);
        Experience exp = new Experience();
        exp.setGameId(gameModelId);
        exp.setId(experienceId);
        exp.setPlayerId(playerId);
        playerManager.playExperience(playerId, exp);
    }

    @GetMapping("hook/certification/{gameModelId}/{experienceId}/{certificationId}")
    public void hookCertification(@PathVariable String gameModelId,
            @PathVariable String experienceId,
            @PathVariable String certificationId, HttpServletRequest request) {
        final String playerId = aacHelper.getPlayerId(request);
        CertificationActionDTO certification = new CertificationActionDTO();
        certification.setGameModelId(gameModelId);
        certification.setExperienceId(experienceId);
        certification.setCertificationId(certificationId);
        certification.setPlayerId(playerId);
        playerManager.certificateExperience(certification);
    }
}
