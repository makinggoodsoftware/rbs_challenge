package com.rbs.retailtherapy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rbs.retailtherapy.domain.*;
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

import java.util.Map;

public class Main {
    public static void main (String... args){
//        String baseUrl = "http://localhost:8081/RetailTherapy/jsonServices";
//        String userName = "Pulsy";
//        String password = "testing";

        String baseUrl = "http://stomv11236:8081/RetailTherapy/jsonServices";
        String userName = "Making Good Software";
        String password = "eastex01";

        String customersFileName = "customers.json";
        double maximumToInvest = 0.6;
        InvestmentConfiguration baseInvestmentConfiguration = new InvestmentConfiguration(
                0.7,
                0.3,
                new FixedSpacingInvestmentConfiguration(0.6, 4),
                new FixedSpacingInvestmentConfiguration(0.25, 4),
                new FixedSpacingInvestmentConfiguration(0.15, 4)
        );


        Gson gson = new GsonBuilder().create();
        Dimension initialDimension = new Dimension(41, 41);
        CustomersLoader customersLoader = new CustomersLoader(initialDimension, gson);
        Map<Integer, Customer> customers = customersLoader.loadCustomersFromCpFileName(customersFileName);

        GameState gameState = new GameState(maximumToInvest);
        ParticipantImpl participantImpl = new ParticipantImpl(baseUrl);
        GridFactory gridFactory = new GridFactory();
        RoundStateFactory roundStateFactory = new RoundStateFactory(participantImpl, gridFactory, userName);
        Investor investor = new Investor();
        Coordinates coordinates = new Coordinates();
        CoordinatesSelectors coordinatesSelectors = new CoordinatesSelectors();
        CellDistributor potentialShopCellDistributor = new CellDistributor(coordinatesSelectors.shopCoordinatesSelector(), coordinates);
        FinanceService financeService = new FinanceService();
        Strategizer strategizer = new Strategizer(financeService, baseInvestmentConfiguration);
        ShopBidder shopBidder = new ShopBidder(investor, potentialShopCellDistributor, strategizer, coordinates);
        GameManager gameManager = new GameManager(gameState, participantImpl, shopBidder, roundStateFactory, coordinatesSelectors, userName, password, coordinates, customers);
        RoundProvider roundProvider = new RoundProvider(gameManager, gameState);

        new GameClock(roundProvider, participantImpl, customers, roundStateFactory, gameManager, gameState).start();
    }
}
