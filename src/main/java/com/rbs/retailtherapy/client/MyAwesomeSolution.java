package com.rbs.retailtherapy.client;

import java.io.IOException;
import java.util.logging.Logger;

import com.rbs.retailtherapy.entity.ParticipantGameParametersResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.impl.JsonHelper;
import com.rbs.retailtherapy.impl.ParticipantImpl;

public class MyAwesomeSolution {
    private final ParticipantImpl participantImpl;
    private int participantId;
    private ShopResponse[] myShops;
    private boolean isShopRequestedDuringBuyingRound;
    private final double initialCashInRound = 10000;
    private final String baseUrl;
    private final GameManager gameManager;

    private static final Logger logger = Logger.getLogger(MyAwesomeSolution.class.getSimpleName());

    public MyAwesomeSolution(String baseUrl, GameManager gameManager) throws SecurityException, IOException {
        this.baseUrl = baseUrl;
        this.gameManager = gameManager;
        participantImpl = new ParticipantImpl(baseUrl);
    }

    public void start(String username, String password) throws Exception {

        while (true) {
            try {
                ParticipantGameParametersResponse gridState = checkGridState();
                gameManager.onNewGridState(gridState);
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



}
