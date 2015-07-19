package com.rbs.retailtherapy.v3.logic.strategy;

import com.rbs.retailtherapy.v3.domain.*;
import com.rbs.retailtherapy.v3.logic.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopBidder {
    private final Investor investor;
    private final CellDistributor cellDistributor;
    private final Strategizer strategizer;
    private final Coordinates coordinates;

    public ShopBidder(Investor investor, CellDistributor cellDistributor, Strategizer strategizer, Coordinates coordinates) {
        this.investor = investor;
        this.cellDistributor = cellDistributor;
        this.strategizer = strategizer;
        this.coordinates = coordinates;
    }

    public List<Bid> bid(GameState gameState, RoundState roundState) {
        ShopBiddingStrategy shopBiddingStrategy = strategizer.shopBiddingStrategy(gameState, roundState);
        Dimension wholeGrid = roundState.getDimension();
        Map<PlacementType, Investment> investments = investor.invest(
                wholeGrid.reduce(2),
                shopBiddingStrategy.getAmountToInvest(),
                gameState.getMinimumBid(),
                shopBiddingStrategy.getInvestmentConfiguration()
        );
        ArrayList<Bid> bids = new ArrayList<>();
        bids.addAll(calculateRingBids(wholeGrid, investments.get(PlacementType.OUTTER_1st_LOOP), 2));
        bids.addAll(calculateRingBids(wholeGrid, investments.get(PlacementType.OUTTER_2nd_LOOP), 6));
        bids.addAll(calculateRingBids(wholeGrid, investments.get(PlacementType.OUTTER_3rd_LOOP), 10));
        return bids;
    }

    private ArrayList<Bid> calculateRingBids(Dimension wholeGrid, Investment investment, int ringIndex) {
        ArrayList<Bid> bids = new ArrayList<>();
        List<Coordinate> shopCoordinates = cellDistributor.distributeOuterRing(wholeGrid.reduce(ringIndex), investment.getNumberOfShops());
        List<Coordinate> adjustedShopCoordinates = coordinates.adjustCoordinates (shopCoordinates, (ringIndex /2), (ringIndex /2));
        for (Coordinate adjustedShopCoordinate : adjustedShopCoordinates) {
            bids.add(new Bid(adjustedShopCoordinate, investment.getBidAmount()));
        }
        return bids;
    }
}
