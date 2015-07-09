package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.*;
import com.rbs.retailtherapy.model.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class RoundManager {
    private static final Logger LOGGER = Logger.getLogger(RoundManager.class.getSimpleName());

    private final RoundStateResponse roundState;
    private final HttpGameSession httpGameSession;

    private ShopResponse[] myShops;
    private boolean isShopRequestedDuringBuyingRound;
    private double initialCashInRound = 1000;

    public RoundManager(RoundStateResponse roundState, HttpGameSession httpGameSession) {
        this.roundState = roundState;
        this.httpGameSession = httpGameSession;
    }

    public void onRoundStart() {
        logRoundStarted();
    }

    private void logRoundStarted() {
        String msg = "***************************************************";
        String msg1 = "Starting new round: " + roundState.getRoundId();
        String msg2 = "Current state is: " + roundState.getCurrentGameState();
        String msg3 = "***************************************************";
        LOGGER.info(
                System.getProperty("line.separator") +
                msg + System.getProperty("line.separator") +
                msg1 + System.getProperty("line.separator") +
                msg2 + System.getProperty("line.separator") +
                msg3
        );
    }

    public void tick() {
        ClockCheck clockCheck = new ClockCheck();
        clockCheck.setParticipantClockInMilliSec(new Date().getTime());
        ClockCheck clockCheckReturned = httpGameSession.checkClock(clockCheck);

        String clockcheck =
                "ClockCheck, Participant Time: "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(clockCheckReturned.getParticipantClockInMilliSec()))
                        + ", Retail Therapy Time: "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(clockCheckReturned.getRetailTherapyClockInMilliSec()))
                        + " Time Difference: "
                        + clockCheckReturned.getTimeDifferenceInMilliSec();

        LOGGER.info("ClockCheck: " + clockcheck);

        if (roundState.getCurrentGameState().equals(GameState.GameInProgress)) {
            switch (roundState.getRoundState()) {
                case OPEN:
                    doRoundOpenStuff();
                    break;
                case STARTED:
                    doRoundStartedStuff(roundState);
                    break;
                case TRADING:
                    doTradingRoundStuff(roundState);
                    break;
                case FINISHED:
                    doRoundFinishedStuff(roundState);
                    break;
                default:
                    doNothing();
                    break;
            }
        }
    }

    private void doRoundOpenStuff() {
        RoundStateResponse roundState = httpGameSession.getRoundState();
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
        SelfStateResponse selfStateResponse = httpGameSession.getSelfState();
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
        SelfStateResponse selfStateResponse = httpGameSession.getSelfState();
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

                    RequestShopResponse response = httpGameSession.requestShop(50, gridCell);
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
        SelfStateResponse selfStateResponse = httpGameSession.getSelfState();
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
                    BuyResponse response = httpGameSession.buyStock(2, shop, stockType);
                    LOGGER.info("BuyStockResponse: " + stockType.name() + "; "
                            + response.getIsSuccess() + ", Message: " + response.getMessage());
                }
            }
        }
    }

    private boolean isCashLeftEnough(double i, RoundStateResponse roundStateResponse) {
        SelfStateResponse selfStateResponse = httpGameSession.getSelfState();
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
                    Direction direction = Direction.values()[new Random(new Date().getTime()).nextInt(Direction.values().length - 1)];
                    httpGameSession.placeAdvert(direction, shop, stock.getStockType());
                }
            }

            // Place blockers

            if (isCashLeftEnough(15, roundStateResponse) && !isBlockerPlaced) {
                Direction direction = Direction.values()[new Random(new Date().getTime()).nextInt(Direction.values().length - 1)];
                PlaceBlockerResponse response = httpGameSession.placeBlocker(direction, shop);
                if (response.getIsSuccess())
                    isBlockerPlaced = true;
            }

        }
    }
}
