package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.*;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.Direction;
import com.rbs.retailtherapy.model.Stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class HttpGameSession {
    private static final Logger LOGGER = Logger.getLogger(HttpGameSession.class.getSimpleName());

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

    public RequestShopResponse requestShop(double bidAmount, int col, int row) {
        BuyShopParameters shopParameters = new BuyShopParameters();
        shopParameters.setBidAmount(bidAmount);
        shopParameters.setColumn(col);
        shopParameters.setRow(row);
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

    public long getLatency() {
        ClockCheck clockCheck = new ClockCheck();
        clockCheck.setParticipantClockInMilliSec(new Date().getTime());
        ClockCheck clockCheckReturned = checkClock(clockCheck);

        long timeDifferenceInMilliSec = clockCheckReturned.getTimeDifferenceInMilliSec();
        String clockcheck =
                "ClockCheck, Participant Time: "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(clockCheckReturned.getParticipantClockInMilliSec()))
                        + ", Retail Therapy Time: "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(clockCheckReturned.getRetailTherapyClockInMilliSec()))
                        + " Time Difference: "
                        + timeDifferenceInMilliSec;

        LOGGER.info("ClockCheck: " + clockcheck);
        return timeDifferenceInMilliSec;
    }
}
