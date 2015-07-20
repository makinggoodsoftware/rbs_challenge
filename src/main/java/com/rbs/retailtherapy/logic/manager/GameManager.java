package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.GameState;
import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.logic.clock.RoundMonitor;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.logic.meta.CustomersLoader;
import com.rbs.retailtherapy.logic.strategy.ShopBidder;
import com.rbs.retailtherapy.model.ParticipantParameters;

public class GameManager {
    private final GameState gameState;
    private final ParticipantImpl participantImpl;
    private final ShopBidder shopBidder;
    private final CustomersLoader customersLoader;
    private final String userName;
    private final String password;
    private final String customersFileName;
    private final RoundStateFactory roundStateFactory;
    private final CoordinatesSelectors coordinatesSelectors;

    public GameManager(GameState gameState, ParticipantImpl participantImpl, ShopBidder shopBidder, CustomersLoader customersLoader, RoundStateFactory roundStateFactory, CoordinatesSelectors coordinatesSelectors, String userName, String password, String customersFileName) {
        this.gameState = gameState;
        this.participantImpl = participantImpl;
        this.shopBidder = shopBidder;
        this.customersLoader = customersLoader;
        this.coordinatesSelectors = coordinatesSelectors;
        this.userName = userName;
        this.password = password;
        this.customersFileName = customersFileName;
        this.roundStateFactory = roundStateFactory;
    }

    public RoundMonitor onNewRound(RoundStateResponse roundStateResponse) {
        ParticipantParameters participantParameters = new ParticipantParameters(userName, password);
        JoinGameResponse joinGameResponse = participantImpl.joinGame(participantParameters);
        HttpGameSession httpGameSession = new HttpGameSession(joinGameResponse.getParticipantId(), participantImpl);
        RoundMonitor roundMonitor = new RoundMonitor(new RoundManager(httpGameSession, shopBidder, gameState, roundStateFactory, coordinatesSelectors));
        updateGameState (roundStateResponse);
        return roundMonitor;
    }

    private void updateGameState(RoundStateResponse roundStateResponse) {
        this.gameState.setMinimumBid(roundStateResponse.getRoundParameters().getBlockerPrice());
        this.gameState.setTypeOfShoppers(customersLoader.loadCustomersFromCpFileName(customersFileName));
    }
}
