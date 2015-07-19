package com.rbs.retailtherapy.v3.logic.clock;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.v3.logic.manager.GameManager;
import com.rbs.retailtherapy.v3.domain.GameState;

public class RoundProvider {
    private final GameManager gameManager;
    private final GameState gameState;

    public RoundProvider(GameManager gameManager, GameState gameState) {
        this.gameManager = gameManager;
        this.gameState = gameState;
    }

    public RoundMonitor retrieve(RoundStateResponse roundStateResponse) {
        RoundMonitor roundMonitor = gameState.getRoundMonitor(roundStateResponse);
        if (roundMonitor == null){
            roundMonitor = gameManager.onNewRound(roundStateResponse);
        }
        return roundMonitor;
    }
}
