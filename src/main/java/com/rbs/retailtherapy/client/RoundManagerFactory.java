package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.ParticipantParameters;

import java.util.logging.Logger;

public class RoundManagerFactory {
    private static final Logger LOGGER = Logger.getLogger(RoundManagerFactory.class.getSimpleName());
    private final HttpGameClient httpGameClient;
    private final ParticipantParameters credentials;

    public RoundManagerFactory(HttpGameClient httpGameClient, ParticipantParameters credentials) {
        this.httpGameClient = httpGameClient;
        this.credentials = credentials;
    }

    public RoundManager newRound(RoundStateResponse roundState) {
        RoundManager roundManager = new RoundManager(roundState, new HttpGameSession(loginForRound(), httpGameClient));
        roundManager.onRoundStart();
        return roundManager;
    }

    private int loginForRound() {
        while (true){
            LOGGER.info("Trying to login with credentials: " + credentials.getUserName());
            JoinGameResponse joinGameResponse = httpGameClient.joinGame(credentials);

            if (joinGameResponse.getIsSuccess()) {
                LOGGER.info("Login successful: " + credentials.getUserName());
                return joinGameResponse.getParticipantId();
            }
        }
    }
}
