package it.smartcommunitylab.smartchainbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.smartcommunitylab.smartchainbackend.bean.Action;

@Service
public class GameManager {

    @Autowired
    private GEHelper gamificationEngineHelper;

    public void playAction(Action action) {
    }


}
