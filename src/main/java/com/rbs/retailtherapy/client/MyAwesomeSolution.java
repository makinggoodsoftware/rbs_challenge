package com.rbs.retailtherapy.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

import com.rbs.retailtherapy.entity.BuyResponse;
import com.rbs.retailtherapy.entity.BuyShopParameters;
import com.rbs.retailtherapy.entity.BuyStockParameters;
import com.rbs.retailtherapy.entity.ClockCheck;
import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.ParticipantGameParametersResponse;
import com.rbs.retailtherapy.entity.PlaceAdvertParameters;
import com.rbs.retailtherapy.entity.PlaceBlockerParameters;
import com.rbs.retailtherapy.entity.PlaceBlockerResponse;
import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.SelfStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.impl.JsonHelper;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.model.CellStatus;
import com.rbs.retailtherapy.model.Direction;
import com.rbs.retailtherapy.model.GameState;
import com.rbs.retailtherapy.model.GridCellWrapper;
import com.rbs.retailtherapy.model.ParticipantParameters;
import com.rbs.retailtherapy.model.RoundStateEnum;
import com.rbs.retailtherapy.model.Stock.StockType;
import com.rbs.retailtherapy.model.StockEntry;

public class MyAwesomeSolution {
    private final ParticipantImpl participantImpl;
    private ParticipantParameters participantParameter;
    private int participantId;
    private ShopResponse[] myShops;
    private boolean isShopRequestedDuringBuyingRound;
    private final double initialCashInRound = 10000;
    private final String baseUrl;

    private static final Logger logger = Logger.getLogger(MyAwesomeSolution.class.getSimpleName());

    public MyAwesomeSolution(String baseUrl) throws SecurityException, IOException {
        this.baseUrl = baseUrl;
        participantImpl = new ParticipantImpl(baseUrl);
    }

    public void start(String username, String password) throws Exception {

        participantParameter = new ParticipantParameters(username, password);

        while (true) {
            try {
                ParticipantGameParametersResponse gridState = checkGridState();
                logger.info(this.getClass().getSimpleName() + ": "
                        + gridState.getIsSuccess() + ", Message"
                        + gridState.getResponseMessage() + ", GameState: "
                        + gridState.getCurrentGameState().name());

                if (gridState.getCurrentGameState().equals(GameState.GameInProgress) || gridState.getCurrentGameState().equals(GameState.Started)) {
                    logger.info(this.getClass().getSimpleName() + ": Trying to join game here - " + baseUrl);
                    JoinGameResponse joinGameResponse = participantImpl.joinGame(participantParameter);

                    if (joinGameResponse.getIsSuccess()) {
                        participantId = joinGameResponse.getParticipantId();
                        logger.info("Successfully joined game, participant Id is " + participantId);

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

                        logger.info("ClockCheck: " + clockcheck);

                        if (gridState.getCurrentGameState().equals(GameState.GameInProgress)) {
                            // game in progress, now query current round
                            // state

                            RoundStateResponse roundStateResponse = participantImpl.getRoundState();
                            RoundStateEnum roundState = roundStateResponse.getRoundState();

                            logger.info("\n\t*** Current Game State: "
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
                        logger.severe("Join game failed: " + joinGameResponse.getResponseMessage());
                    }
                }
            } catch (Exception e) {
                logger.severe(this.getClass().getSimpleName() + ": " + JsonHelper.getStackTrace(e));
            }
            Thread.sleep(1000);
        }

    }

    private ParticipantGameParametersResponse checkGridState() throws Exception {
        ParticipantGameParametersResponse gameStateResponse = participantImpl.getGameParameters();
        if (gameStateResponse == null) {
            throw new Exception("Cannot get game state, please make sure the server is up. URI attempted: " + baseUrl);
        }
        return gameStateResponse;
    }

    private void doRoundOpenStuff(RoundStateResponse roundStateResponse) {
        RoundStateResponse roundState = participantImpl.getRoundState();
        // Get current round information;
        double blockerPrice = roundState.getRoundParameters().getBlockerPrice();
        logger.info("*** Blocker price for current round is " + blockerPrice);

        // Get grid cell information
        GridCellWrapper[] gridCells = roundState.getGridCells();
        logger.info("*** Number of grid cells: " + gridCells.length);

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
            logger.info("\t**********My State***********"
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
            logger.info("*** Number of shops requested: " + i);
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
                    StockType stockType =
                            StockType.values()[new Random(new Date().getTime()).nextInt(StockType.values().length - 1)];
                    BuyStockParameters buyStockParam = new BuyStockParameters();
                    buyStockParam.setQuantity(2);
                    buyStockParam.setShopOwnerId(participantId);
                    buyStockParam.setShopId(shop.getShopId());
                    buyStockParam.setStockType(stockType);
                    BuyResponse response = participantImpl.buyStock(buyStockParam);
                    logger.info("BuyStockResponse: " + buyStockParam.getStockType().name() + "; "
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

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
