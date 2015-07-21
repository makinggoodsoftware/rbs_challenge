package com.rbs.retailtherapy.logic.domain;

import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.Customer;
import com.rbs.retailtherapy.domain.Orientation;
import com.rbs.retailtherapy.entity.ShopperResponse;
import com.rbs.retailtherapy.model.Position;

import java.util.Map;

public class ShopperTracker {
    private final Position currentPosition;
    private final Map<Orientation, Coordinate> possibleMovements;
    private final ShopperResponse shopper;
    private final Customer customer;
    private Orientation latestHint;

    public ShopperTracker(Position currentPosition, Map<Orientation, Coordinate> possibleMovements, Customer customer, ShopperResponse shopper) {
        this.currentPosition = currentPosition;
        this.possibleMovements = possibleMovements;
        this.customer = customer;
        this.shopper = shopper;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Map<Orientation, Coordinate> getPossibleMovements() {
        return possibleMovements;
    }

    public ShopperResponse getShopper() {
        return shopper;
    }

    public void setLatestHint(Orientation latestHint) {
        this.latestHint = latestHint;
    }

    public Orientation getLatestHint() {
        return latestHint;
    }

    public Customer getCustomer() {
        return customer;
    }
}
