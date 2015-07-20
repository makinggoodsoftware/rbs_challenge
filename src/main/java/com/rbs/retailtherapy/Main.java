package com.rbs.retailtherapy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rbs.retailtherapy.client.HttpGameSession;
import com.rbs.retailtherapy.domain.FixedSpacingInvestmentConfiguration;
import com.rbs.retailtherapy.domain.GameState;
import com.rbs.retailtherapy.domain.InvestmentConfiguration;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.logic.clock.GameClock;
import com.rbs.retailtherapy.logic.clock.RoundProvider;
import com.rbs.retailtherapy.logic.coordinates.Coordinates;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.logic.manager.GameManager;
import com.rbs.retailtherapy.logic.manager.GridFactory;
import com.rbs.retailtherapy.logic.manager.RoundStateFactory;
import com.rbs.retailtherapy.logic.meta.CustomersLoader;
import com.rbs.retailtherapy.logic.strategy.*;

public class Main {
    public static void main (String... args){
        String baseUrl = "http://localhost:8081/RetailTherapy/jsonServices";
        String userName = "Pulsy";
        String password = "testing";
        String customersFileName = "customers.json";
        double maximumToInvest = 0.8;
        InvestmentConfiguration baseInvestmentConfiguration = new InvestmentConfiguration(
                0.8,
                0.2,
                new FixedSpacingInvestmentConfiguration(0.6, 4),
                new FixedSpacingInvestmentConfiguration(0.25, 4),
                new FixedSpacingInvestmentConfiguration(0.15, 4)
        );

        GameState gameState = new GameState(maximumToInvest);
        ParticipantImpl participantImpl = new ParticipantImpl(baseUrl);
        GridFactory gridFactory = new GridFactory();
        RoundStateFactory roundStateFactory = new RoundStateFactory(participantImpl, gridFactory);
        Investor investor = new Investor();
        Coordinates coordinates = new Coordinates();
        CoordinatesSelectors coordinatesSelectors = new CoordinatesSelectors();
        CellDistributor potentialShopCellDistributor = new CellDistributor(coordinatesSelectors.shopCoordinatesSelector(), coordinates);
        FinanceService financeService = new FinanceService();
        Strategizer strategizer = new Strategizer(financeService, baseInvestmentConfiguration);
        ShopBidder shopBidder = new ShopBidder(investor, potentialShopCellDistributor, strategizer, coordinates);
        Gson gson = new GsonBuilder().create();
        CustomersLoader customersLoader = new CustomersLoader(gson);
        GameManager gameManger = new GameManager(gameState, participantImpl, shopBidder, customersLoader, roundStateFactory, coordinatesSelectors, userName, password, customersFileName);
        RoundProvider roundProvider = new RoundProvider(gameManger, gameState);

        new GameClock(roundProvider, participantImpl, roundStateFactory).start();
    }
}
