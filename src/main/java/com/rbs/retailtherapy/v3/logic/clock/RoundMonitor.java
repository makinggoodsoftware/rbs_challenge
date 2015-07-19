package com.rbs.retailtherapy.v3.logic.clock;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.v3.logic.manager.RoundManager;
import com.rbs.retailtherapy.v3.logic.manager.RoundStateFactory;
import com.rbs.retailtherapy.v3.domain.BidStatus;
import com.rbs.retailtherapy.v3.domain.RoundState;

public class RoundMonitor {
    private final RoundStateFactory roundStateFactory;
    private final RoundManager roundManager;

    public RoundMonitor(RoundStateFactory roundStateFactory, RoundManager roundManager) {
        this.roundStateFactory = roundStateFactory;
        this.roundManager = roundManager;
    }



    public RoundState tick(RoundStateResponse newState, RoundState expectedState) {
        RoundState currentState = roundStateFactory.from(newState, expectedState);
        RoundState expectedNextState = currentState;
        if (currentState.getIsBiddingOpen() && currentState.getBidStatus() == BidStatus.NOT_BID){
            expectedNextState = roundManager.onBiddingOpened (currentState);
        }
        return expectedNextState;
    }
}
