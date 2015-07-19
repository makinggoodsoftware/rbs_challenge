package com.rbs.retailtherapy.logic.clock;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.logic.manager.RoundStateFactory;

public class GameClock {
    private final RoundProvider roundProvider;
    private final HttpGameClient httpGameClient;
    private final RoundStateFactory roundStateFactory;

    public GameClock(RoundProvider roundProvider, HttpGameClient httpGameClient, RoundStateFactory roundStateFactory) {
        this.roundProvider = roundProvider;
        this.httpGameClient = httpGameClient;
        this.roundStateFactory = roundStateFactory;
    }

    public void start() {
        RoundState expectedState = null;
        while(true){
            expectedState = tick(expectedState);
        }
    }

    private RoundState tick(RoundState expectedState) {
        try {
            RoundStateResponse roundStateResponse = httpGameClient.getRoundState();
            RoundMonitor roundMonitor = roundProvider.retrieve(roundStateResponse);
            RoundState currentState = roundStateFactory.from(roundStateResponse, expectedState);
            return roundMonitor.tick(currentState, expectedState);
        } catch (Exception e) {
            e.printStackTrace();
            return expectedState;
        }
    }
}
