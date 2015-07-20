package com.rbs.retailtherapy.logic.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.rbs.retailtherapy.entity.PlaceAdvertResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.*;
import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.entity.ShopperResponse;
import com.rbs.retailtherapy.logic.coordinates.Coordinates;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.logic.domain.AdjacentShop;
import com.rbs.retailtherapy.logic.domain.ShopperTracker;
import com.rbs.retailtherapy.logic.strategy.ShopBidder;
import com.rbs.retailtherapy.model.CellStatus;
import com.rbs.retailtherapy.model.Position;
import com.rbs.retailtherapy.model.Stock;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

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
        Map<Coordinate, ShopResponse> adjudicatedShopBids = new HashMap<>();
        Set<Coordinate> stolenShopBids = new HashSet<>();

        for (Coordinate shopBidCoordinate : previousShopBids) {
            Cell cell = currentState.getGrid().getCell(shopBidCoordinate);
            if (cell.getType() == CellStatus.Shop){
                if (cell.isMine()){
                    System.out.println("GOT A NEW SHOP in " + shopBidCoordinate);
                    adjudicatedShopBids.put(shopBidCoordinate, currentState.getShops().get(shopBidCoordinate).getShopResponse() );
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

    public RoundState onFirstTradingStep(RoundState currentState, RoundState expectedCurrentState) {
        System.out.println("TRADING JUST OPENED");
        return handleShoppers(currentState, expectedCurrentState, influenceArea(currentState.getShops()));
    }

    public RoundState onTradingStep(RoundState currentState, RoundState expectedCurrentState) {
        return handleShoppers(currentState, expectedCurrentState, influenceArea(currentState.getShops()));
    }

    private Multimap<Coordinate, AdjacentShop> influenceArea(Map<Coordinate, ShopTracker> allShops) {
        Multimap<Coordinate, AdjacentShop> influenceArea = ArrayListMultimap.create();
        for (Map.Entry<Coordinate, ShopTracker> myShopTrackerEntry : allShops.entrySet()) {
            ShopTracker shopTracker = myShopTrackerEntry.getValue();
            if (shopTracker.isMine()){
                ShopResponse shop = shopTracker.getShopResponse();
                Position shopPosition = shop.getPosition();
                influenceArea.put(developCoordinate(shopPosition, Orientation.NORTH), new AdjacentShop(Orientation.NORTH, shop));
                influenceArea.put(developCoordinate(shopPosition, Orientation.SOUTH), new AdjacentShop(Orientation.SOUTH, shop));
                influenceArea.put(developCoordinate(shopPosition, Orientation.EAST), new AdjacentShop(Orientation.EAST, shop));
                influenceArea.put(developCoordinate(shopPosition, Orientation.WEST), new AdjacentShop(Orientation.WEST, shop));
            }
        }
        return influenceArea;
    }

    private RoundState handleShoppers(RoundState currentState, RoundState expectedRoundState, Multimap<Coordinate, AdjacentShop> influenceArea) {
        System.out.println("Cash in round: " + currentState.getSelfStateResponse().getCashInRound());
        System.out.println("Cash in game: " + currentState.getSelfStateResponse().getCashInGame());
        if (expectedRoundState != null){
            for (ShopperTracker expectedUserTracking : expectedRoundState.getUserTracking().values()) {
                Coordinate matchingCoordinate = movementSatisfied(currentState, expectedUserTracking);
                if (matchingCoordinate != null) {
                    System.out.println("Shopper " + expectedUserTracking.getShopper().getShopperId() + "  was found where expected: " + matchingCoordinate);
                } else {
                    System.out.println("Shopper " + expectedUserTracking.getShopper().getShopperId() + "  not found where expected!");
                }
            }
        }
        RoundState roundState = trackShoppers(currentState);
        placeAds(influenceArea, roundState);
        return roundState;
    }

    private Coordinate movementSatisfied(RoundState currentState, ShopperTracker expectedUserTracking) {
        Collection<Coordinate> expectedMovements = expectedUserTracking.getPossibleMovements().values();
        for (Coordinate expectedMovement : expectedMovements) {
            Collection<ShopperResponse> shopperResponses = currentState.getShoppers().get(expectedMovement);
            if (isShopperInCollectionById (expectedUserTracking.getShopper(), shopperResponses)){
                return expectedMovement;
            }
        }
        return null;
    }

    private boolean isShopperInCollectionById(ShopperResponse shopper, Collection<ShopperResponse> from) {
        int shopperId = shopper.getShopperId();
        for (ShopperResponse shopperResponse : from) {
            if (shopperResponse.getShopperId() == shopperId) return true;
        }
        return false;
    }

    private void placeAds(Multimap<Coordinate, AdjacentShop> influenceArea, RoundState roundState) {
        for (Map.Entry<Integer, ShopperTracker> shopperTrackingEntries : roundState.getUserTracking().entrySet()) {
            ShopperTracker shopperTracker = shopperTrackingEntries.getValue();
            Map<Orientation, Coordinate> possibleMovements = shopperTracker.getPossibleMovements();
            for (Coordinate coordinate : possibleMovements.values()) {
                Collection<AdjacentShop> adjacentShops = influenceArea.get(coordinate);
                if (adjacentShops != null && adjacentShops.size() > 0){
                    System.out.println("Shopper with ID: " + shopperTracker.getShopper().getShopperId() + " is possibly going to be in: " + coordinate);
                    System.out.println("Shopper can be intercepted! Planting an Ad");
                    AdjacentShop adjacentShop = adjacentShops.iterator().next();
                    Stock.StockType stockType = Stock.StockType.valueOf(shopperTracker.getShopper().getStocks()[0]);
                    ShopResponse shop = adjacentShop.getShop();
                    double cashInRound = roundState.getSelfStateResponse().getCashInRound();
                    Double toSpend = cashInRound / 10;
                    int toBuy = 1;
                    for (Stock stock : roundState.getStocks()) {
                        if (stock.getStockType() == stockType) {
                            toBuy = toSpend.intValue() / ((Double)stock.getWholesalePrice()).intValue();
                        }
                    }
                    httpGameSession.buyStock(toBuy, shop, stockType);
                    PlaceAdvertResponse placeAdvertResponse = httpGameSession.placeAdvert(
                            adjacentShop.getOrientation().asDirection(),
                            shop,
                            stockType
                    );
                    System.out.println("Ad response was: " + placeAdvertResponse.getIsSuccess() + "[" + placeAdvertResponse.getResponseMessage() + "]");
                }
            }
        }
    }

    private RoundState trackShoppers(RoundState currentState) {
        RoundState afterTrackingShoppers = roundStateFactory.copy(currentState);
        Multimap<Coordinate, ShopperResponse> shoppers = currentState.getShoppers();
        Map<Integer, ShopperTracker> shopperTrackers = new HashMap<>();
        for (Map.Entry<Coordinate, Collection<ShopperResponse>> coordinateShopperResponseEntry : shoppers.asMap().entrySet()) {
            Coordinate coordinate = coordinateShopperResponseEntry.getKey();
            Collection<ShopperResponse> shoppersInSameCoordinate = coordinateShopperResponseEntry.getValue();
            for (ShopperResponse shopper : shoppersInSameCoordinate) {
                System.out.println("Shopper " +  shopper.getShopperId() + " in: " + coordinate);
                ShopperTracker nextMovement = guessFirstMovement(currentState, shopper);
                shopperTrackers.put(shopper.getShopperId(), nextMovement);
            }
        }
        afterTrackingShoppers.setUserTracking (shopperTrackers);
        return afterTrackingShoppers;
    }

    private ShopperTracker guessFirstMovement(RoundState currentState, ShopperResponse shopper) {
        Position currentPosition = shopper.getCurrentPosition();
        int shopperId = shopper.getShopperId();
        Customer customer = currentState.getCustomer(shopperId);
        Path firstPath = customer.getPathType().getPaths().get(0);
        List<Orientation> possibleOrientations = guessPossibleOrientations (currentState.getDimension(), currentPosition, firstPath);
        Map<Orientation, Coordinate> possibleMovements = new HashMap<>();
        for (Orientation possibleOrientation : possibleOrientations) {
            Coordinate possibleMovement = developCoordinate (currentPosition, possibleOrientation);
            possibleMovements.put(possibleOrientation, possibleMovement);
        }
        return new ShopperTracker(currentPosition, possibleMovements, shopper);
    }

    private Coordinate developCoordinate(Position currentPosition, Orientation possibleOrientation) {
        switch (possibleOrientation){
            case EAST:
                return delta(currentPosition, 1, 0);
            case WEST:
                return delta(currentPosition, -1, 0);
            case NORTH:
                return delta(currentPosition, 0, 1);
            case SOUTH:
                return delta(currentPosition, 0, -1);
            case NORTH_EAST:
                return delta(currentPosition, 1, 1);
            case NORTH_WEST:
                return delta(currentPosition, -1, 1);
            case SOUTH_EAST:
                return delta(currentPosition, 1, -1);
            case SOUTH_WEST:
                return delta(currentPosition, -1, -1);
        }
        throw new IllegalStateException();
    }

    private Coordinate delta(Position currentPosition, int deltaCol, int deltaRow) {
        return new Coordinate(currentPosition.getCol() + deltaCol, currentPosition.getRow() + deltaRow);
    }

    private List<Orientation> guessPossibleOrientations(Dimension dimension, Position position, Path path) {
        if (path.getDirection() == Direction.HORIZONTAL){
            if (position.getCol() == 0) return singletonList(Orientation.EAST);
            if (position.getCol() == dimension.getCols() - 1) return singletonList(Orientation.WEST);
            return asList(Orientation.EAST, Orientation.WEST);
        }
        if (path.getDirection() == Direction.VERTICAL){
            if (position.getRow() == 0) return singletonList(Orientation.NORTH);
            if (position.getRow() == dimension.getRows() - 1) return singletonList(Orientation.SOUTH);
            return asList(Orientation.NORTH, Orientation.SOUTH);
        }
        if (path.getDirection() == Direction.DIAGONAL){
            if (position.getRow() == 0 && position.getCol() == 0) return singletonList(Orientation.NORTH_EAST);
            if (position.getRow() == 0 && position.getCol() == dimension.getCols() - 1) return singletonList(Orientation.NORTH_WEST);
            if (position.getRow() == dimension.getRows() - 1 && position.getCol() == 0) return singletonList(Orientation.SOUTH_EAST);
            if (position.getRow() == dimension.getRows() - 1 && position.getCol() == dimension.getCols() - 1) return singletonList(Orientation.SOUTH_WEST);
            if (position.getRow() == 0) return asList(Orientation.NORTH_EAST, Orientation.NORTH_WEST);
            if (position.getRow() == dimension.getRows() - 1) return asList(Orientation.SOUTH_WEST, Orientation.SOUTH_EAST);
            if (position.getCol() == 0) return asList(Orientation.NORTH_EAST, Orientation.SOUTH_EAST);
            if (position.getCol() == dimension.getCols() - 1) return asList(Orientation.SOUTH_WEST, Orientation.NORTH_WEST);
            return asList(Orientation.NORTH_EAST, Orientation.NORTH_WEST, Orientation.SOUTH_EAST, Orientation.SOUTH_WEST);
        }
        return null;
    }
}
