package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.ParticipantParameters;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class RoundManagerFactory {
    private static final Logger LOGGER = Logger.getLogger(RoundManagerFactory.class.getSimpleName());
    private final HttpGameClient httpGameClient;
    private final ParticipantParameters credentials;
    private final ArtificialIntelligence ai;

    public RoundManagerFactory(HttpGameClient httpGameClient, ParticipantParameters credentials, ArtificialIntelligence ai) {
        this.httpGameClient = httpGameClient;
        this.credentials = credentials;
        this.ai = ai;
    }

    public RoundManager newRound(RoundStateResponse roundState) {
        int participantId = loginForRound();
        RoundManager roundManager = new RoundManager(
                ai,
                new KnowledgeProvider(),
                new HttpGameSession(
                        participantId,
                        httpGameClient
                ),
                Executors.newFixedThreadPool(10));
        roundManager.onRoundStart(roundState);
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
