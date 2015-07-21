package com.rbs.retailtherapy.logic.clock;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.logic.manager.GameManager;
import com.rbs.retailtherapy.domain.GameState;

public class RoundProvider {
    private final GameManager gameManager;
    private final GameState gameState;

    public RoundProvider(GameManager gameManager, GameState gameState) {
        this.gameManager = gameManager;
        this.gameState = gameState;
    }

}
