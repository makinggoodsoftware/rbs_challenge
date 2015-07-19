package com.rbs.retailtherapy.logic.clock;

import com.rbs.retailtherapy.domain.BidStatus;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.logic.manager.RoundManager;

public class RoundMonitor {
    private final RoundManager roundManager;

    public RoundMonitor(RoundManager roundManager) {
        this.roundManager = roundManager;
    }



    public RoundState tick(RoundState currentState, RoundState expectedCurrentState) {
        if (currentState.getIsBiddingOpen() && currentState.getBidStatus() == BidStatus.NOT_BID){
            return roundManager.onBiddingOpened (currentState);
        }
        return currentState;
    }
}
