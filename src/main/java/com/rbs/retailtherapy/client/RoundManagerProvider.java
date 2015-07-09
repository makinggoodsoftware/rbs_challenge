package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.RoundStateResponse;

import java.util.HashMap;
import java.util.Map;

public class RoundManagerProvider {
    private final RoundManagerFactory roundManagerFactory;
    private Map<Integer, RoundManager> roundManagers = new HashMap<Integer, RoundManager>();

    public RoundManagerProvider(RoundManagerFactory roundManagerFactory) {
        this.roundManagerFactory = roundManagerFactory;
    }


    private RoundManager forRound(int roundId) {
        return roundManagers.get(roundId);
    }

    private void add(int roundId, RoundManager roundManager) {
        if (forRound(roundId) != null) throw new IllegalStateException("Adding twice the round manager for the same roundId. " + roundId);
        roundManagers.put(roundId, roundManager);
    }

    public RoundManager refreshRound(RoundStateResponse roundState) {
        int roundId = roundState.getRoundId();
        RoundManager roundManager = forRound(roundId);
        if (roundManager == null) {
            roundManager = roundManagerFactory.newRound(roundState);
            add(roundId, roundManager);
        }

        return roundManager;
    }
}
