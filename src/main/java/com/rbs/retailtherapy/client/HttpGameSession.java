package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.*;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.Direction;
import com.rbs.retailtherapy.model.GridCellWrapper;
import com.rbs.retailtherapy.model.Stock;

public class HttpGameSession {
    private final int participantId;
    private final HttpGameClient httpGameClient;

    public HttpGameSession(int participantId, HttpGameClient httpGameClient) {
        this.participantId = participantId;
        this.httpGameClient = httpGameClient;
    }

    public ClockCheck checkClock(ClockCheck clockCheck) {
        return httpGameClient.checkClock(clockCheck);
    }

    public RoundStateResponse getRoundState() {
        return httpGameClient.getRoundState();
    }

    public SelfStateResponse getSelfState() {
        return httpGameClient.getSelfState(participantId);
    }

    public RequestShopResponse requestShop(double bidAmount, GridCellWrapper gridCell) {
        BuyShopParameters shopParameters = new BuyShopParameters();
        shopParameters.setBidAmount(bidAmount);
        shopParameters.setColumn(gridCell.getCol());
        shopParameters.setRow(gridCell.getRow());
        shopParameters.setShopOwnerId(participantId);
        return httpGameClient.requestShop(shopParameters);
    }

    public BuyResponse buyStock(int quantity, ShopResponse shop, Stock.StockType stockType) {
        BuyStockParameters buyStockParam = new BuyStockParameters();
        buyStockParam.setShopOwnerId(participantId);
        buyStockParam.setQuantity(quantity);
        buyStockParam.setShopId(shop.getShopId());
        buyStockParam.setStockType(stockType);
        return httpGameClient.buyStock(buyStockParam);
    }

    public void placeAdvert(Direction direction, ShopResponse shop, Stock.StockType stockType) {
        PlaceAdvertParameters advertParameters = new PlaceAdvertParameters();
        advertParameters.setShopOwnerId(participantId);
        advertParameters.setStockType(stockType);
        advertParameters.setShopId(shop.getShopId());
        advertParameters.setDirection(direction);
        httpGameClient.placeAdvert(advertParameters);
    }

    public PlaceBlockerResponse placeBlocker(Direction direction, ShopResponse shop) {
        PlaceBlockerParameters blockerParameters = new PlaceBlockerParameters();
        blockerParameters.setDirection(direction);
        blockerParameters.setShopId(shop.getShopId());
        blockerParameters.setShopOwnerId(participantId);
        return httpGameClient.placeBlocker(blockerParameters);
    }
}
