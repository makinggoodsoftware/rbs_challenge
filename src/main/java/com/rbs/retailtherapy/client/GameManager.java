package com.rbs.retailtherapy.client;

import java.io.IOException;
import java.util.logging.Logger;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.JsonHelper;
import com.rbs.retailtherapy.impl.HttpGameClient;

public class GameManager {
    private final HttpGameClient httpGameClient;
    private final RoundManagerProvider roundManagerProvider;

    private static final Logger LOGGER = Logger.getLogger(GameManager.class.getSimpleName());

    public GameManager(HttpGameClient httpGameClient, RoundManagerProvider roundManagerProvider) throws SecurityException, IOException {
        this.httpGameClient = httpGameClient;
        this.roundManagerProvider = roundManagerProvider;
    }

    public void start() throws Exception {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                RoundStateResponse roundState = httpGameClient.getRoundState();
                RoundManager roundManager = roundManagerProvider.refreshRound(roundState);
                roundManager.tick();
            } catch (Exception e) {
                LOGGER.severe(this.getClass().getSimpleName() + ": " + JsonHelper.getStackTrace(e));
            }
            Thread.sleep(1000);
        }

    }


}
