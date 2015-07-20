package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.logic.clock.RoundMonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameState {
    private final Map<Integer, RoundMonitor> roundMonitors = new HashMap<>();
    private final double maximumToInvest;

    private double minimumBid;
    private Map<Integer, Customer> typeOfShoppers;

    public GameState(double maximumToInvest) {
        this.maximumToInvest = maximumToInvest;
    }


    public RoundMonitor getRoundMonitor(RoundStateResponse roundStateResponse) {
        return roundMonitors.get(roundStateResponse.getRoundId());
    }

    public Map<Integer, Customer> getTypeOfShoppers() {
        return typeOfShoppers;
    }

    public double getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(double minimumBid) {
        this.minimumBid = minimumBid;
    }

    public void setTypeOfShoppers(Map<Integer, Customer> typeOfShoppers) {
        this.typeOfShoppers = typeOfShoppers;
    }

    public double getMaximumToInvest() {
        return maximumToInvest;
    }

    public void addRound(RoundStateResponse roundStateResponse, RoundMonitor roundMonitor) {
        roundMonitors.put(roundStateResponse.getRoundId(), roundMonitor);
    }
}
