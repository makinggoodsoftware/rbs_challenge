package com.rbs.retailtherapy.logic.strategy;

import com.rbs.retailtherapy.domain.*;
import com.rbs.retailtherapy.logic.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopBidder {
    private final Investor investor;
    private final CellDistributor potentialShopCellDistributor;
    private final Strategizer strategizer;
    private final Coordinates coordinates;

    public ShopBidder(Investor investor, CellDistributor potentialShopCellDistributor, Strategizer strategizer, Coordinates coordinates) {
        this.investor = investor;
        this.potentialShopCellDistributor = potentialShopCellDistributor;
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

        Investment centerInvestment = investments.get(PlacementType.CENTER);
        ArrayList<Coordinate> centerCoordinates = new ArrayList<>();
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 14));
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 18));
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 22));
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 26));
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 30));
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 34));
        centerCoordinates.addAll(distributeCenterCells(wholeGrid, investments, 38));
        for (int i=0; i < centerInvestment.getNumberOfShops(); i ++){
            if (i < centerCoordinates.size()){
                bids.add(new Bid(centerCoordinates.get(i), centerInvestment.getBidAmount()));
            }
        }

        return bids;
    }

    private List<Coordinate> distributeCenterCells(Dimension wholeGrid, Map<PlacementType, Investment> investments, int centerDelta) {
        Dimension centerCells = wholeGrid.reduce(centerDelta);
        Investment centerInvestment = investments.get(PlacementType.CENTER);
        List<Coordinate> centerCoordinates = potentialShopCellDistributor.distributeOuterRing(centerCells, centerInvestment.getNumberOfShops());
        return coordinates.adjustCoordinates(centerCoordinates, centerDelta / 2, centerDelta /2);
    }

    private ArrayList<Bid> calculateRingBids(Dimension wholeGrid, Investment investment, int ringIndex) {
        ArrayList<Bid> bids = new ArrayList<>();
        List<Coordinate> shopCoordinates = potentialShopCellDistributor.distributeOuterRing(wholeGrid.reduce(ringIndex), investment.getNumberOfShops());
        List<Coordinate> adjustedShopCoordinates = coordinates.adjustCoordinates (shopCoordinates, (ringIndex /2), (ringIndex /2));
        for (Coordinate adjustedShopCoordinate : adjustedShopCoordinates) {
            bids.add(new Bid(adjustedShopCoordinate, investment.getBidAmount()));
        }
        return bids;
    }
}
