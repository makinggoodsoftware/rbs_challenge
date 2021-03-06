package com.rbs.retailtherapy.logic.clock;

import com.rbs.retailtherapy.domain.Customer;
import com.rbs.retailtherapy.domain.GameState;
import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.logic.manager.GameManager;
import com.rbs.retailtherapy.logic.manager.RoundStateFactory;

import java.util.HashMap;
import java.util.Map;

public class GameClock {
    private final RoundProvider roundProvider;
    private final ParticipantImpl httpGameClient;
    private final RoundStateFactory roundStateFactory;
    private final Map<Integer, Customer> customers;
    private final GameManager gameManager;
    private final GameState gameState;

    public GameClock(RoundProvider roundProvider, ParticipantImpl httpGameClient, Map<Integer, Customer> customers, RoundStateFactory roundStateFactory, GameManager gameManager, GameState gameState) {
        this.roundProvider = roundProvider;
        this.httpGameClient = httpGameClient;
        this.customers = customers;
        this.roundStateFactory = roundStateFactory;
        this.gameManager = gameManager;
        this.gameState = gameState;
    }

    public void start() {
        RoundState expectedState = null;
        RoundState previousState = null;
        while (true) {
            try {
                RoundStateResponse roundStateResponse = httpGameClient.getRoundState();
                RoundMonitor roundMonitor = gameState.getRoundMonitor(roundStateResponse);
                RoundState currentState;
                if (roundMonitor == null){
                    System.out.println("NEW ROUND WITH ID: " + roundStateResponse.getRoundId());
                    roundMonitor = gameManager.onNewRound(roundStateResponse);
                    gameState.addRound (roundStateResponse, roundMonitor);
                    HttpGameSession httpGameSession = roundMonitor.getHttpSession();
                    ShopResponse[] shops = httpGameSession.getSelfState().getShops();
                    currentState = roundStateFactory.firstRoundState(roundStateResponse, parseShops(shops), customers, httpGameSession.getSelfState());
                } else {
                    HttpGameSession httpGameSession = roundMonitor.getHttpSession();
                    ShopResponse[] shops = httpGameSession.getSelfState().getShops();
                    currentState = roundStateFactory.merge(roundStateResponse, expectedState, parseShops(shops), customers, httpGameSession.getSelfState());
                }

                expectedState = roundMonitor.tick(previousState, currentState, expectedState);
                previousState = currentState;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<Coordinate, ShopResponse> parseShops(ShopResponse[] shops) {
        if (shops == null) return new HashMap<>();
        Map<Coordinate, ShopResponse> parsedShops = new HashMap<>();
        for (ShopResponse shop : shops) {
            parsedShops.put(
                    new Coordinate(shop.getPosition().getCol(), shop.getPosition().getRow()),
                    shop
            );
        }
        return parsedShops;
    }
}
