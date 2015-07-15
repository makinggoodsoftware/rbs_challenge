package com.rbs.retailtherapy.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.impl.JsonHelper;
import com.rbs.retailtherapy.v2.domain.Coordinate;
import com.rbs.retailtherapy.v2.domain.CustomerFileEntry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
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
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream("customers.json");
//        Reader reader = new InputStreamReader(in);
//        CustomersFile customersFile = gson.fromJson(reader, CustomersFile.class);
//        Map<CustomerFileEntry, String[]> movementsByCustomer = new HashMap<>();
//        for (CustomerFileEntry customerFileEntry : customersFile.getShoppers()) {
//            String gridPositions = customerFileEntry.getGridPositions();
//            String[] movements = explodeMovements(gridPositions);
//            movementsByCustomer.put(customerFileEntry, movements);
//        }
//
//        List<Multimap<Coordinate, CustomerFileEntry>> movementsByCoordinates = new ArrayList<>();
//        boolean shouldLookupTurn = true;
//        int currentTurn = -1;
//        Set<Map.Entry<CustomerFileEntry, String[]>> movementsByCustomerEntries = movementsByCustomer.entrySet();
//        while (shouldLookupTurn){
//            currentTurn ++;
//            Multimap<Coordinate, CustomerFileEntry> movementsThisTurn = ArrayListMultimap.create();
//            shouldLookupTurn = false;
//            for (Map.Entry<CustomerFileEntry, String[]> customerMovements : movementsByCustomerEntries) {
//                String[] movements = customerMovements.getValue();
//                if (currentTurn < movements.length) {
//                    shouldLookupTurn = true;
//                    String coordinatesForTurn = movements[currentTurn];
//                    movementsThisTurn.put(new Coordinate(1, 1), customerMovements.getKey());
//                }
//            }
//            movementsByCoordinates.add(movementsThisTurn);
//        }
//
//        Map<Coordinate, Map<Integer, CustomerFileEntry>> customerInteractionsByCoordinates = new HashMap<>();
//        int currentTurnCounter = 0;
//        for (Multimap<Coordinate, CustomerFileEntry> movementsThisTurn : movementsByCoordinates) {
//            currentTurnCounter ++;
//            Map<Coordinate, Collection<CustomerFileEntry>> coordinateCollectionMap = movementsThisTurn.asMap();
//            for (Map.Entry<Coordinate, Collection<CustomerFileEntry>> coordinateCollectionEntry : coordinateCollectionMap.entrySet()) {
//                Map<Integer, CustomerFileEntry> customerInteractions;
//                customerInteractionsByCoordinates.put(coordinateCollectionEntry.getKey(), null);
//            }
//        }


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

    private String[] explodeMovements(String gridPositions) {
        return gridPositions.substring(1, gridPositions.length() - 2).replaceAll(" ", "").split("\\]\\[");
    }


}
