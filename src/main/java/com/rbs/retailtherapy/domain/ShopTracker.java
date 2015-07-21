package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.entity.ShopResponse;

public class ShopTracker {
    private final ShopResponse shopResponse;
    private final boolean isMine;
    private int stockBoughtOn = 0;

    public ShopTracker(ShopResponse shopResponse, boolean isMine) {
        this.shopResponse = shopResponse;
        this.isMine = isMine;
    }

    public ShopResponse getShopResponse() {
        return shopResponse;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setStockBoughtOn(int stockBoughtOn) {
        this.stockBoughtOn = stockBoughtOn;
    }

    public int getStockBoughtOn() {
        return stockBoughtOn;
    }
}
