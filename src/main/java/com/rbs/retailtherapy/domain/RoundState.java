package com.rbs.retailtherapy.domain;

import com.google.common.collect.Multimap;
import com.rbs.retailtherapy.entity.SelfStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.entity.ShopperResponse;
import com.rbs.retailtherapy.logic.domain.ShopperTracker;
import com.rbs.retailtherapy.model.RoundStateEnum;
import com.rbs.retailtherapy.model.Stock;

import java.util.*;

public class RoundState {
    private final boolean isBiddingOpen;
    private final boolean isTradeOpen;
    private final int numberOfShoppers;
    private final List<Stock> stocks;
    private final Dimension dimension;
    private final double initialMoney;
    private final Grid grid;
    private final Map <Integer, Customer> customers;
    private final Multimap<Coordinate, ShopperResponse> shoppers;
    private final Map<Coordinate, ShopTracker> shops;
    private final SelfStateResponse selfStateResponse;
    private int currentStep;

    private BidStatus bidStatus;
    private RoundStateEnum roundState;
    private Set<Coordinate> shopsBidCoordinates;
    private Map<Coordinate, ShopResponse> myShops = new HashMap<>();
    private Set<Coordinate> stolenShops;
    private Map<Integer, ShopperTracker> userTracking = new HashMap<>();
    private final Double adPrice;

    public RoundState(boolean isBiddingOpen, boolean isTradeOpen, int numberOfShoppers, List<Stock> stocks, Dimension dimension, double initialMoney, Grid grid, Multimap<Coordinate, ShopperResponse> shoppers, Map<Integer, Customer> customers, BidStatus bidStatus, RoundStateEnum roundState, Set<Coordinate> shopsBidCoordinates, Map<Coordinate, ShopTracker> shops, SelfStateResponse selfStateResponse, int currentStep, Double adPrice) {
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
        this.shops = shops;
        this.selfStateResponse = selfStateResponse;
        this.currentStep = currentStep;
        this.adPrice = adPrice;
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

    public void setMyShops(Map<Coordinate, ShopResponse> myShops) {
        this.myShops = myShops;
    }

    public void setStolenShops(Set<Coordinate> stolenShops) {
        this.stolenShops = stolenShops;
    }

    public Multimap<Coordinate, ShopperResponse> getShoppers() {
        return shoppers;
    }

    public Customer getCustomer(int shopperId) {
        return customers.get(shopperId);
    }

    public Map<Coordinate, ShopResponse> getMyShops() {
        return myShops;
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    public void setUserTracking(Map<Integer, ShopperTracker> userTracking) {
        this.userTracking = userTracking;
    }

    public Map<Integer, ShopperTracker> getUserTracking() {
        return userTracking;
    }


    public Map<Coordinate, ShopTracker> getShops() {
        return shops;
    }

    public SelfStateResponse getSelfStateResponse() {
        return selfStateResponse;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public Double getAdPrice() {
        return adPrice;
    }
}
