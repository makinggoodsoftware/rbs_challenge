package com.rbs.retailtherapy.domain;

public class Bid {
    private final Coordinate coordinate;
    private final Double bidAmount;

    public Bid(Coordinate coordinate, Double bidAmount) {
        this.coordinate = coordinate;
        this.bidAmount = bidAmount;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Double getBidAmount() {
        return bidAmount;
    }
}
