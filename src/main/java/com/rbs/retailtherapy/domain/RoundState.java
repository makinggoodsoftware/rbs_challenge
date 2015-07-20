package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.entity.ShopperResponse;
import com.rbs.retailtherapy.model.RoundStateEnum;
import com.rbs.retailtherapy.model.Stock;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoundState {
    private final boolean isBiddingOpen;
    private final boolean isTradeOpen;
    private final int numberOfShoppers;
    private final List<Stock> stocks;
    private final Dimension dimension;
    private final double initialMoney;
    private final Grid grid;
    private final Map<Coordinate, ShopperResponse> shoppers;
    private final Map <Integer, Customer> customers;

    private BidStatus bidStatus;
    private RoundStateEnum roundState;
    private Set<Coordinate> shopsBidCoordinates;
    private Set<Coordinate> myShops;
    private Set<Coordinate> stolenShops;

    public RoundState(boolean isBiddingOpen, boolean isTradeOpen, int numberOfShoppers, List<Stock> stocks, Dimension dimension, double initialMoney, Grid grid, Map<Coordinate, ShopperResponse> shoppers, Map<Integer, Customer> customers, BidStatus bidStatus, RoundStateEnum roundState, Set<Coordinate> shopsBidCoordinates) {
        this.isBiddingOpen = isBiddingOpen;
        this.isTradeOpen = isTradeOpen;
        this.numberOfShoppers = numberOfShoppers;
        this.stocks = stocks;
        this.dimension = dimension;
        this.initialMoney = initialMoney;
        this.grid = grid;
        this.shoppers = shoppers;
        this.customers = customers;
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

    public void setMyShops(Set<Coordinate> myShops) {
        this.myShops = myShops;
    }

    public void setStolenShops(Set<Coordinate> stolenShops) {
        this.stolenShops = stolenShops;
    }

    public Map<Coordinate, ShopperResponse> getShoppers() {
        return shoppers;
    }

    public Customer getCustomer(int shopperId) {
        return customers.get(shopperId);
    }

    public Set<Coordinate> getMyShops() {
        return myShops;
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }
}
