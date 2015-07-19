package com.rbs.retailtherapy.v3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.v3.domain.FixedSpacingInvestmentConfiguration;
import com.rbs.retailtherapy.v3.domain.GameState;
import com.rbs.retailtherapy.v3.domain.InvestmentConfiguration;
import com.rbs.retailtherapy.v3.logic.clock.GameClock;
import com.rbs.retailtherapy.v3.logic.clock.RoundProvider;
import com.rbs.retailtherapy.v3.logic.coordinates.Coordinates;
import com.rbs.retailtherapy.v3.logic.coordinates.CoordinatesSelectors;
import com.rbs.retailtherapy.v3.logic.manager.GameManager;
import com.rbs.retailtherapy.v3.logic.manager.RoundStateFactory;
import com.rbs.retailtherapy.v3.logic.meta.CustomersLoader;
import com.rbs.retailtherapy.v3.logic.strategy.*;

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
        HttpGameClient httpGameClient = new HttpGameClient(baseUrl);
        RoundStateFactory roundStateFactory = new RoundStateFactory(httpGameClient);
        Investor investor = new Investor();
        Coordinates coordinates = new Coordinates();
        CoordinatesSelectors coordinatesSelectors = new CoordinatesSelectors();
        CellDistributor potentialShopCellDistributor = new CellDistributor(coordinatesSelectors.shopCoordinatesSelector(), coordinates);
        FinanceService financeService = new FinanceService();
        Strategizer strategizer = new Strategizer(financeService, baseInvestmentConfiguration);
        ShopBidder shopBidder = new ShopBidder(investor, potentialShopCellDistributor, strategizer, coordinates);
        Gson gson = new GsonBuilder().create();
        CustomersLoader customersLoader = new CustomersLoader(gson);
        GameManager gameManger = new GameManager(gameState, roundStateFactory, httpGameClient, shopBidder, customersLoader, userName, password, customersFileName);
        RoundProvider roundProvider = new RoundProvider(gameManger, gameState);

        new GameClock(roundProvider, httpGameClient).start();
    }
}
