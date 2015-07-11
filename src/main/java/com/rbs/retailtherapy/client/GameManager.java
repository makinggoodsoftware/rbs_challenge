package com.rbs.retailtherapy.client;

import com.google.gson.Gson;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.impl.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;

public class GameManager {
    private final HttpGameClient httpGameClient;
    private final RoundManagerProvider roundManagerProvider;
    private final Gson gson;

    private static final Logger LOGGER = Logger.getLogger(GameManager.class.getSimpleName());

    public GameManager(HttpGameClient httpGameClient, RoundManagerProvider roundManagerProvider, Gson gson) throws SecurityException, IOException {
        this.httpGameClient = httpGameClient;
        this.roundManagerProvider = roundManagerProvider;
        this.gson = gson;
    }

    public void start() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("customers.json");
        Reader reader = new InputStreamReader(in);
        CustomersFile customersFile = gson.fromJson(reader, CustomersFile.class);
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                RoundStateResponse roundState = httpGameClient.getRoundState();
                RoundManager roundManager = roundManagerProvider.refreshRound(roundState);
                roundManager.tick(roundState);
            } catch (Exception e) {
                LOGGER.severe(this.getClass().getSimpleName() + ": " + JsonHelper.getStackTrace(e));
            }
        }

    }


}
