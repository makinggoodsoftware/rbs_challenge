package com.rbs.retailtherapy.v3.logic.manager;

import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.ParticipantParameters;
import com.rbs.retailtherapy.v3.client.HttpGameSession;
import com.rbs.retailtherapy.v3.domain.GameState;
import com.rbs.retailtherapy.v3.logic.clock.RoundMonitor;
import com.rbs.retailtherapy.v3.logic.meta.CustomersLoader;
import com.rbs.retailtherapy.v3.logic.strategy.ShopBidder;

public class GameManager {
    private final GameState gameState;
    private final RoundStateFactory roundStateFactory;
    private final HttpGameClient httpGameClient;
    private final ShopBidder shopBidder;
    private final CustomersLoader customersLoader;
    private final String userName;
    private final String password;
    private final String customersFileName;

    public GameManager(GameState gameState, RoundStateFactory roundStateFactory, HttpGameClient httpGameClient, ShopBidder shopBidder, CustomersLoader customersLoader, String userName, String password, String customersFileName) {
        this.gameState = gameState;
        this.roundStateFactory = roundStateFactory;
        this.httpGameClient = httpGameClient;
        this.shopBidder = shopBidder;
        this.customersLoader = customersLoader;
        this.userName = userName;
        this.password = password;
        this.customersFileName = customersFileName;
    }

    public RoundMonitor onNewRound(RoundStateResponse roundStateResponse) {
        ParticipantParameters participantParameters = new ParticipantParameters(userName, password);
        JoinGameResponse joinGameResponse = httpGameClient.joinGame(participantParameters);
        HttpGameSession httpGameSession = new HttpGameSession(joinGameResponse.getParticipantId(), httpGameClient);
        RoundMonitor roundMonitor = new RoundMonitor(roundStateFactory, new RoundManager(httpGameSession, shopBidder, gameState));
        updateGameState (roundStateResponse);
        return roundMonitor;
    }

    private void updateGameState(RoundStateResponse roundStateResponse) {
        this.gameState.setMinimumBid(roundStateResponse.getRoundParameters().getBlockerPrice());
        this.gameState.setTypeOfShoppers(customersLoader.loadCustomersFromCpFileName(customersFileName));
    }
}
