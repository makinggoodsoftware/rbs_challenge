package com.rbs.retailtherapy.v3;

public class Investment {
    private final Double bidAmount;
    private final Integer numberOfShops;

    public Investment(Double bidAmount, Integer numberOfShops) {
        this.bidAmount = bidAmount;
        this.numberOfShops = numberOfShops;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public Integer getNumberOfShops() {
        return numberOfShops;
    }
}
