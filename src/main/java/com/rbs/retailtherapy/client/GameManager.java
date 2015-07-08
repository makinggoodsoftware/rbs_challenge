package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.*;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.model.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class GameManager {
    private final ParticipantImpl participantImpl;
    private final ParticipantParameters participantParameter;
    private final double initialCashInRound = 10000;

    private int participantId;
    private ShopResponse[] myShops;
    private boolean isShopRequestedDuringBuyingRound;
    private final String baseUrl;

    private static final Logger LOGGER = Logger.getLogger(GameManager.class.getSimpleName());

    public GameManager(ParticipantImpl participantImpl, ParticipantParameters participantParameter, String baseUrl) {
        this.participantImpl = participantImpl;
        this.participantParameter = participantParameter;
        this.baseUrl = baseUrl;
    }

    public void onNewGridState(ParticipantGameParametersResponse gridState) {
        LOGGER.info(this.getClass().getSimpleName() + ": " + gridState.getIsSuccess() + ", Message" + gridState.getResponseMessage() + ", GameState: " + gridState.getCurrentGameState().name());

        if (gridState.getCurrentGameState().equals(GameState.GameInProgress) || gridState.getCurrentGameState().equals(GameState.Started)) {
            LOGGER.info(this.getClass().getSimpleName() + ": Trying to join game here - " + baseUrl);
            JoinGameResponse joinGameResponse = participantImpl.joinGame(participantParameter);

            if (joinGameResponse.getIsSuccess()) {
                participantId = joinGameResponse.getParticipantId();
                LOGGER.info("Successfully joined game, participant Id is " + participantId);

                ClockCheck clockCheck = new ClockCheck();
                clockCheck.setParticipantClockInMilliSec(new Date().getTime());
                ClockCheck clockCheckReturned = participantImpl.checkClock(clockCheck);

                String clockcheck =
                        "ClockCheck, Participant Time: "
                                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(clockCheckReturned.getParticipantClockInMilliSec()))
                                + ", Retail Therapy Time: "
                                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(clockCheckReturned.getRetailTherapyClockInMilliSec()))
                                + " Time Difference: "
                                + clockCheckReturned.getTimeDifferenceInMilliSec();

                LOGGER.info("ClockCheck: " + clockcheck);

                if (gridState.getCurrentGameState().equals(GameState.GameInProgress)) {
                    // game in progress, now query current round
                    // state

                    RoundStateResponse roundStateResponse = participantImpl.getRoundState();
                    RoundStateEnum roundState = roundStateResponse.getRoundState();

                    LOGGER.info("\n\t*** Current Game State: "
                            + gridState.getCurrentGameState() + " ***"
                            + "\n\t*** Current Round State: " + roundState + " ***"
                            + "\n\t*** Current Round ID: "
                            + roundStateResponse.getRoundId() + " ***");

                    switch (roundState) {
                        case OPEN:
                            doRoundOpenStuff(roundStateResponse);
                            break;
                        case STARTED:
                            doRoundStartedStuff(roundStateResponse);
                            break;
                        case TRADING:
                            doTradingRoundStuff(roundStateResponse);
                            break;
                        case FINISHED:
                            doRoundFinishedStuff(roundStateResponse);
                            break;
                        default:
                            doNothing();
                            break;
                    }
                }

            } else {
                LOGGER.severe("Join game failed: " + joinGameResponse.getResponseMessage());
            }
        }

    }

    private void doRoundOpenStuff(RoundStateResponse roundStateResponse) {
        RoundStateResponse roundState = participantImpl.getRoundState();
        // Get current round information;
        double blockerPrice = roundState.getRoundParameters().getBlockerPrice();
        LOGGER.info("*** Blocker price for current round is " + blockerPrice);

        // Get grid cell information
        GridCellWrapper[] gridCells = roundState.getGridCells();
        LOGGER.info("*** Number of grid cells: " + gridCells.length);

    }

    private void doRoundStartedStuff(RoundStateResponse roundStateResponse) {
        requestShop(roundStateResponse);
    }

    private void doTradingRoundStuff(RoundStateResponse roundStateResponse) {
        requestShop(roundStateResponse);
        buyStock(roundStateResponse);
        // Get shopper information
        roundStateResponse.getShoppers();
        // Trading phase in round
        // Get my current state
        SelfStateResponse selfStateResponse = participantImpl.getSelfState(participantId);
        if (selfStateResponse.getIsSuccess()) {
            LOGGER.info("\t**********My State***********"
                    + "\n\tGame score: "
                    + selfStateResponse.getCashInGame()
                    + "\n\tCash In Round: "
                    + selfStateResponse.getCashInRound()
                    + "\n\tNumber of shops: "
                    + (selfStateResponse.getShops() == null ? 0
                    : selfStateResponse.getShops().length)
                    + "\n\t*****************************");
            myShops =
                    (selfStateResponse.getShops() != null && selfStateResponse.getShops().length > 0) ? selfStateResponse.getShops()
                            : null;
        }

        if (myShops != null) {
            placeAdvertsAndBlockers(roundStateResponse);
        }

    }

    private void doRoundFinishedStuff(RoundStateResponse roundStateResponse) {
        // reset/clean up
        myShops = null;
    }

    private void doNothing() {
    }

    private void requestShop(RoundStateResponse roundStateResponse) {
        if (roundStateResponse.getRoundState().equals(RoundStateEnum.STARTED)
                && isShopRequestedDuringBuyingRound)
            return;
        GridCellWrapper[] gridCells = roundStateResponse.getGridCells();
        SelfStateResponse selfStateResponse = participantImpl.getSelfState(participantId);
        // Bid for shops in bidding phase
        myShops = selfStateResponse.getShops();
        int bidAmount = 50;
        int numberOfShopsToBid =
                Double.valueOf(selfStateResponse.getCashInRound() * 0.4 / bidAmount).intValue();
        if (myShops == null || myShops.length == 0) {
            int i = 0;
            for (GridCellWrapper gridCell : gridCells) {
                int row = gridCell.getRow();
                int col = gridCell.getCol();
                if (gridCell.getStatus() == CellStatus.AllocatedForShop && i < numberOfShopsToBid
                        && (row + col) % 4 == 2) {

                    BuyShopParameters buyShopParam = new BuyShopParameters();
                    buyShopParam.setBidAmount(50);
                    buyShopParam.setColumn(gridCell.getCol());
                    buyShopParam.setRow(gridCell.getRow());
                    buyShopParam.setShopOwnerId(participantId);
                    RequestShopResponse response = participantImpl.requestShop(buyShopParam);
                    if (response.getIsSuccess())
                        i++;
                }
            }
            LOGGER.info("*** Number of shops requested: " + i);
            if (i > 0)
                isShopRequestedDuringBuyingRound = true;
        }
    }

    private void buyStock(RoundStateResponse roundStateResponse) {
        SelfStateResponse selfStateResponse = participantImpl.getSelfState(participantId);
        if (selfStateResponse.getIsSuccess()) {
            myShops =
                    (selfStateResponse.getShops() != null && selfStateResponse.getShops().length > 0) ? selfStateResponse.getShops()
                            : null;
        }

        if (myShops != null) {
            for (ShopResponse shop : Arrays.asList(myShops)) {
                if (shop.getStockShelf().length <= 1 && isCashLeftEnough(40, roundStateResponse)) {

                    // Buy stock
                    Stock.StockType stockType =
                            Stock.StockType.values()[new Random(new Date().getTime()).nextInt(Stock.StockType.values().length - 1)];
                    BuyStockParameters buyStockParam = new BuyStockParameters();
                    buyStockParam.setQuantity(2);
                    buyStockParam.setShopOwnerId(participantId);
                    buyStockParam.setShopId(shop.getShopId());
                    buyStockParam.setStockType(stockType);
                    BuyResponse response = participantImpl.buyStock(buyStockParam);
                    LOGGER.info("BuyStockResponse: " + buyStockParam.getStockType().name() + "; "
                            + response.getIsSuccess() + ", Message: " + response.getMessage());
                }
            }
        }
    }

    private boolean isCashLeftEnough(double i, RoundStateResponse roundStateResponse) {
        SelfStateResponse selfStateResponse = participantImpl.getSelfState(participantId);
        if (selfStateResponse.getIsSuccess() && roundStateResponse.getIsSuccess()) {
            return selfStateResponse.getCashInRound() >= initialCashInRound * (i * 1.0 / 100.0);
        }
        return false;
    }

    private void placeAdvertsAndBlockers(RoundStateResponse roundStateResponse) {

        boolean isBlockerPlaced = false;

        for (ShopResponse shop : Arrays.asList(myShops)) {

            for (StockEntry stock : shop.getStockShelf()) {
                if (isCashLeftEnough(5, roundStateResponse)) {
                    // Place Adverts
                    PlaceAdvertParameters adverParam = new PlaceAdvertParameters();
                    adverParam.setDirection(Direction.values()[new Random(new Date().getTime()).nextInt(Direction.values().length - 1)]);
                    adverParam.setShopId(shop.getShopId());
                    adverParam.setShopOwnerId(participantId);
                    adverParam.setStockType(stock.getStockType());
                    participantImpl.placeAdvert(adverParam);
                }
            }

            // Place blockers

            if (isCashLeftEnough(15, roundStateResponse) && !isBlockerPlaced) {
                PlaceBlockerParameters blockerParam = new PlaceBlockerParameters();
                blockerParam.setDirection(Direction.values()[new Random(new Date().getTime()).nextInt(Direction.values().length - 1)]);
                blockerParam.setShopOwnerId(participantId);
                blockerParam.setShopId(shop.getShopId());
                PlaceBlockerResponse response = participantImpl.placeBlocker(blockerParam);
                if (response.getIsSuccess())
                    isBlockerPlaced = true;
            }

        }
    }

}
