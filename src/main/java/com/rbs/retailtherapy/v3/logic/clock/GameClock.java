package com.rbs.retailtherapy.v3.logic.clock;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.v3.domain.RoundState;

public class GameClock {
    private final RoundProvider roundProvider;
    private final HttpGameClient httpGameClient;

    public GameClock(RoundProvider roundProvider, HttpGameClient httpGameClient) {
        this.roundProvider = roundProvider;
        this.httpGameClient = httpGameClient;
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
            return roundMonitor.tick(roundStateResponse, expectedState);
        } catch (Exception e) {
            e.printStackTrace();
            return expectedState;
        }
    }
}
