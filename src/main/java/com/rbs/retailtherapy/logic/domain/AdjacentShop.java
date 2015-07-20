package com.rbs.retailtherapy.logic.domain;

import com.rbs.retailtherapy.domain.Orientation;
import com.rbs.retailtherapy.entity.ShopResponse;

public class AdjacentShop {
    private final Orientation orientation;
    private final ShopResponse shop;

    public AdjacentShop(Orientation orientation, ShopResponse shop) {
        this.orientation = orientation;
        this.shop = shop;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public ShopResponse getShop() {
        return shop;
    }
}
