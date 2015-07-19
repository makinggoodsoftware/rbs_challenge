package com.rbs.retailtherapy.v3.logic.manager;

import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.v3.domain.Bid;
import com.rbs.retailtherapy.v3.domain.BidStatus;
import com.rbs.retailtherapy.v3.domain.GameState;
import com.rbs.retailtherapy.v3.client.HttpGameSession;
import com.rbs.retailtherapy.v3.domain.RoundState;
import com.rbs.retailtherapy.v3.logic.strategy.ShopBidder;

import java.util.List;

public class RoundManager {
    private final HttpGameSession httpGameSession;
    private final ShopBidder shopBidder;
    private final GameState gameState;

    public RoundManager(HttpGameSession httpGameSession, ShopBidder shopBidder, GameState gameState) {
        this.httpGameSession = httpGameSession;
        this.shopBidder = shopBidder;
        this.gameState = gameState;
    }

    public RoundState onWaitingForGameToStart(RoundState currentGameState) {
        System.out.println("Waiting for game to start...");
        return currentGameState;
    }

    public RoundState onBiddingOpened(RoundState currentState) {
        System.out.println("BIDDING OPENED!!");
        List<Bid> bids = shopBidder.bid(gameState, currentState);
        for (Bid bid : bids) {
            RequestShopResponse requestShopResponse = httpGameSession.requestShop(bid.getBidAmount(), bid.getCoordinate().getCol(), bid.getCoordinate().getRow());
            System.out.println("Requesting shop in " + bid.getCoordinate() + " result is: " + requestShopResponse.getIsSuccess());
        }
        currentState.setBidStatus(BidStatus.BID_SENT);
        return currentState;
    }
}
