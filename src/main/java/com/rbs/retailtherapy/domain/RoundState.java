package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.model.Stock;

import java.util.List;

public class RoundState {
    private final boolean isBiddingOpen;
    private final boolean isTradeOpen;
    private final int numberOfShoppers;
    private final List<Stock> stocks;
    private final Dimension dimension;
    private final double initialMoney;

    private BidStatus bidStatus;

    public RoundState(boolean isBiddingOpen, boolean isTradeOpen, int numberOfShoppers, List<Stock> stocks, Dimension dimension, double initialMoney, BidStatus bidStatus) {
        this.isBiddingOpen = isBiddingOpen;
        this.isTradeOpen = isTradeOpen;
        this.numberOfShoppers = numberOfShoppers;
        this.stocks = stocks;
        this.dimension = dimension;
        this.initialMoney = initialMoney;
        this.bidStatus = bidStatus;
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
}
