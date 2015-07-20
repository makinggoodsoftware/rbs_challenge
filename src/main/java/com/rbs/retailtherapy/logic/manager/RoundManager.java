package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.*;
import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.entity.ShopperResponse;
import com.rbs.retailtherapy.logic.coordinates.Coordinates;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.logic.strategy.ShopBidder;
import com.rbs.retailtherapy.model.CellStatus;
import com.rbs.retailtherapy.model.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoundManager {
    private final HttpGameSession httpGameSession;
    private final ShopBidder shopBidder;
    private final GameState gameState;
    private final RoundStateFactory roundStateFactory;
    private final CoordinatesSelectors coordinatesSelectors;
    private final Coordinates coordinates;

    public RoundManager(HttpGameSession httpGameSession, ShopBidder shopBidder, GameState gameState, RoundStateFactory roundStateFactory, CoordinatesSelectors coordinatesSelectors, Coordinates coordinates) {
        this.httpGameSession = httpGameSession;
        this.shopBidder = shopBidder;
        this.gameState = gameState;
        this.roundStateFactory = roundStateFactory;
        this.coordinatesSelectors = coordinatesSelectors;
        this.coordinates = coordinates;
    }

    public void onNewRound(RoundState currentState) {
        System.out.println("Round state " + currentState.getRoundState());
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

    public RoundState waitingForTradeToOpen(RoundState expectedCurrentState) {
        return expectedCurrentState;
    }

    public RoundState onFirstTradingStep(RoundState currentState) {
        System.out.println("TRADING JUST OPENED");
        return trackShoppers(currentState);
    }

    public RoundState onTradingStep(RoundState currentState) {
        return trackShoppers(currentState);
    }

    private RoundState trackShoppers(RoundState currentState) {
        Map<Coordinate, ShopperResponse> shoppers = currentState.getShoppers();
        Map<Coordinate, Coordinate> influenceArea = coordinates.influenceArea (currentState.getDimension(), currentState.getMyShops());
        for (Map.Entry<Coordinate, ShopperResponse> coordinateShopperResponseEntry : shoppers.entrySet()) {
            System.out.println("Shopper in: " + coordinateShopperResponseEntry.getKey());
            ShopperResponse shopper = coordinateShopperResponseEntry.getValue();
            Coordinate nextMovement = guessNextMovement (currentState, shopper);
            if (influenceArea.values().contains(nextMovement)){
                System.out.println("Shopper about to enter influence Area!");
            }
        }
        return currentState;
    }

    private Coordinate guessNextMovement(RoundState currentState, ShopperResponse shopper) {
        Position currentPosition = shopper.getCurrentPosition();
        int shopperId = shopper.getShopperId();
        Customer customer = currentState.getCustomer(shopperId);
        return coordinates.nextCoordinate (currentState.getDimension(), currentPosition, customer.getPathType());
    }
}
