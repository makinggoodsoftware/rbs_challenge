package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.domain.Customer;
import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.GameState;
import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.logic.clock.RoundMonitor;
import com.rbs.retailtherapy.logic.coordinates.Coordinates;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.logic.strategy.ShopBidder;
import com.rbs.retailtherapy.model.ParticipantParameters;

import java.util.Map;

public class GameManager {
    private final GameState gameState;
    private final ParticipantImpl participantImpl;
    private final ShopBidder shopBidder;
    private final String userName;
    private final String password;
    private final RoundStateFactory roundStateFactory;
    private final CoordinatesSelectors coordinatesSelectors;
    private final Map<Integer, Customer> customers;

    public GameManager(GameState gameState, ParticipantImpl participantImpl, ShopBidder shopBidder, RoundStateFactory roundStateFactory, CoordinatesSelectors coordinatesSelectors, String userName, String password, Coordinates coordinates, Map<Integer, Customer> customers) {
        this.gameState = gameState;
        this.participantImpl = participantImpl;
        this.shopBidder = shopBidder;
        this.coordinatesSelectors = coordinatesSelectors;
        this.userName = userName;
        this.password = password;
        this.roundStateFactory = roundStateFactory;
        this.customers = customers;
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
        double blockerPrice = roundStateResponse.getRoundParameters().getBlockerPrice();
        System.out.println("Blocker price: " + blockerPrice);
        if (blockerPrice < 10){
            blockerPrice = 15;
        }
        this.gameState.setMinimumBid(blockerPrice);
        this.gameState.setTypeOfShoppers(customers);
    }
}
