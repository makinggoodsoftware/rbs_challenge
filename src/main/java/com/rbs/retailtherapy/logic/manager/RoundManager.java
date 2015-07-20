package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.*;
import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.logic.strategy.ShopBidder;
import com.rbs.retailtherapy.model.CellStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoundManager {
    private final HttpGameSession httpGameSession;
    private final ShopBidder shopBidder;
    private final GameState gameState;
    private final RoundStateFactory roundStateFactory;
    private final CoordinatesSelectors coordinatesSelectors;

    public RoundManager(HttpGameSession httpGameSession, ShopBidder shopBidder, GameState gameState, RoundStateFactory roundStateFactory, CoordinatesSelectors coordinatesSelectors) {
        this.httpGameSession = httpGameSession;
        this.shopBidder = shopBidder;
        this.gameState = gameState;
        this.roundStateFactory = roundStateFactory;
        this.coordinatesSelectors = coordinatesSelectors;
    }

    public void onNewGame(RoundState currentState) {
        System.out.println("NEW GAME");
        System.out.println("Round state " + currentState.getRoundState ());
    }

    public RoundState onBiddingOpened(RoundState currentState) {
        RoundState afterBidding = roundStateFactory.copy(currentState);
        System.out.println("BIDDING OPENED!!");
        Set<Coordinate> shopsBidsCoordinates = new HashSet<>();
        List<Bid> bids = shopBidder.bid(gameState, currentState);
        for (Bid bid : bids) {
            RequestShopResponse requestShopResponse = httpGameSession.requestShop(bid.getBidAmount(), bid.getCoordinate().getCol(), bid.getCoordinate().getRow());
            System.out.println("Requesting shop in " + bid.getCoordinate() + " result is: " + requestShopResponse.getIsSuccess());
            shopsBidsCoordinates.add(bid.getCoordinate());
        }
        afterBidding.setBidStatus(BidStatus.BID_SENT);
        afterBidding.setShopsBidCoordinates(shopsBidsCoordinates);
        return afterBidding;
    }

    public RoundState onWaitingBids(RoundState currentState, RoundState expectedCurrentState) {
        RoundState afterWaiting = roundStateFactory.copy(expectedCurrentState);
        Set<Coordinate> previousShopBids = coordinatesSelectors.waitingShopBid(expectedCurrentState).allCoordinates(currentState.getDimension());
        Set<Coordinate> thisShopBids = new HashSet<>();
        Set<Coordinate> adjudicatedShopBids = new HashSet<>();
        Set<Coordinate> stolenShopBids = new HashSet<>();

        for (Coordinate shopBidCoordinate : previousShopBids) {
            Cell cell = currentState.getGrid().getCell(shopBidCoordinate);
            if (cell.getType() == CellStatus.Shop){
                if (cell.isMine()){
                    System.out.println("GOT A NEW SHOP in " + shopBidCoordinate);
                    adjudicatedShopBids.add(shopBidCoordinate);
                } else {
                    System.out.printf("SHOP stolen! in " + shopBidCoordinate);
                    stolenShopBids.add(shopBidCoordinate);
                }
            } else if (cell.getType() == CellStatus.AllocatedForShop){
                thisShopBids.add(shopBidCoordinate);
            } else {
                System.out.println("=============");
                System.out.println("Coordinate " + shopBidCoordinate);
                System.out.println("Expecting it to be SHOP or WAITING_FOR_SHOP_ALLOCATION");
                System.out.println("=============");
            }
        }
        afterWaiting.setShopsBidCoordinates(thisShopBids);
        afterWaiting.setMyShops(adjudicatedShopBids);
        afterWaiting.setStolenShops(stolenShopBids);
        if (afterWaiting.getShopsBidCoordinates().size() == 0) {
            afterWaiting.setBidStatus(BidStatus.BID_COMPLETE);
        }
        return afterWaiting;
    }

    public HttpGameSession getHttpSession() {
        return httpGameSession;
    }

    public RoundState waitingForTradeToOpen(RoundState currentState, RoundState expectedCurrentState) {
        return expectedCurrentState;
    }
}
