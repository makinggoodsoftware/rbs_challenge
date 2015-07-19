package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.model.RoundStateEnum;
import com.rbs.retailtherapy.model.Stock;

import java.util.List;
import java.util.Set;

public class RoundState {
    private final boolean isBiddingOpen;
    private final boolean isTradeOpen;
    private final int numberOfShoppers;
    private final List<Stock> stocks;
    private final Dimension dimension;
    private final double initialMoney;
    private final Grid grid;

    private BidStatus bidStatus;
    private RoundStateEnum roundState;
    private Set<Coordinate> shopsBidCoordinates;

    public RoundState(boolean isBiddingOpen, boolean isTradeOpen, int numberOfShoppers, List<Stock> stocks, Dimension dimension, double initialMoney, Grid grid, BidStatus bidStatus, RoundStateEnum roundState, Set<Coordinate> shopsBidCoordinates) {
        this.isBiddingOpen = isBiddingOpen;
        this.isTradeOpen = isTradeOpen;
        this.numberOfShoppers = numberOfShoppers;
        this.stocks = stocks;
        this.dimension = dimension;
        this.initialMoney = initialMoney;
        this.grid = grid;
        this.bidStatus = bidStatus;
        this.roundState = roundState;
        this.shopsBidCoordinates = shopsBidCoordinates;
    }

    public boolean getIsBiddingOpen() {
        return isBiddingOpen;
    }

    public boolean getIsTradeOpen() {
        return isTradeOpen;
    }

    public BidStatus getBidStatus() {
        return bidStatus;
    }

    public int getNumberOfShoppers() {
        return numberOfShoppers;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public double getInitialMoney() {
        return initialMoney;
    }

    public void setBidStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }

    public RoundStateEnum getRoundState() {
        return roundState;
    }

    public Set<Coordinate> getShopsBidCoordinates() {
        return shopsBidCoordinates;
    }

    public void setShopsBidCoordinates(Set<Coordinate> shopsBidCoordinates) {
        this.shopsBidCoordinates = shopsBidCoordinates;
    }

    public Grid getGrid() {
        return grid;
    }
}
