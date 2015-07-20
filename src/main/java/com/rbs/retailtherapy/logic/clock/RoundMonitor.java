package com.rbs.retailtherapy.logic.clock;

import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.BidStatus;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.logic.manager.RoundManager;

public class RoundMonitor {
    private final RoundManager roundManager;

    public RoundMonitor(RoundManager roundManager) {
        this.roundManager = roundManager;
    }



    public RoundState tick(RoundState previousState, RoundState currentState, RoundState expectedCurrentState) {
        if (expectedCurrentState == null){
            roundManager.onNewRound(currentState);
        }
        if (currentState.getIsBiddingOpen() && currentState.getBidStatus() == BidStatus.NOT_BID){
            return roundManager.onBiddingOpened (currentState);
        }
        if (currentState.getBidStatus() == BidStatus.BID_SENT){
            return roundManager.onWaitingBids (currentState, expectedCurrentState);
        }
        if (currentState.getBidStatus() == BidStatus.BID_COMPLETE && ! currentState.getIsTradeOpen()){
            return roundManager.waitingForTradeToOpen (expectedCurrentState);
        }
        if (currentState.getIsTradeOpen()){
            if (! previousState.getIsTradeOpen()) {
                return roundManager.onFirstTradingStep (currentState, expectedCurrentState);
            } else {
                if (! previousState.getShoppers().keySet().equals(currentState.getShoppers().keySet())) {
                    return roundManager.onTradingStep (currentState, expectedCurrentState);
                }
            }
        }
        return currentState;
    }

    public HttpGameSession getHttpSession() {
        return roundManager.getHttpSession ();
    }
}
