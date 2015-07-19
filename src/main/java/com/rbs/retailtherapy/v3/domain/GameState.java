package com.rbs.retailtherapy.v3.domain;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.v3.logic.clock.RoundMonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameState {
    private final Map<Integer, RoundMonitor> roundMonitor = new HashMap<>();
    private final double maximumToInvest;

    private double minimumBid;
    private List<Customer> typeOfShoppers;

    public GameState(double maximumToInvest) {
        this.maximumToInvest = maximumToInvest;
    }


    public RoundMonitor getRoundMonitor(RoundStateResponse roundStateResponse) {
        return roundMonitor.get(roundStateResponse.getRoundId());
    }

    public List<Customer> getTypeOfShoppers() {
        return typeOfShoppers;
    }

    public double getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(double minimumBid) {
        this.minimumBid = minimumBid;
    }

    public void setTypeOfShoppers(List<Customer> typeOfShoppers) {
        this.typeOfShoppers = typeOfShoppers;
    }

    public double getMaximumToInvest() {
        return maximumToInvest;
    }
}
