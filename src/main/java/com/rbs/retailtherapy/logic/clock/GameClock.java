package com.rbs.retailtherapy.logic.clock;

import com.rbs.retailtherapy.impl.HttpGameSession;
import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.logic.manager.RoundStateFactory;

import java.util.HashMap;
import java.util.Map;

public class GameClock {
    private final RoundProvider roundProvider;
    private final ParticipantImpl ParticipantImpl;
    private final RoundStateFactory roundStateFactory;

    public GameClock(RoundProvider roundProvider, ParticipantImpl ParticipantImpl, RoundStateFactory roundStateFactory) {
        this.roundProvider = roundProvider;
        this.ParticipantImpl = ParticipantImpl;
        this.roundStateFactory = roundStateFactory;
    }

    public void start() {
        RoundState expectedState = null;
        while (true) {
            expectedState = tick(expectedState);
        }
    }

    private RoundState tick(RoundState expectedState) {
        try {
            RoundStateResponse roundStateResponse = ParticipantImpl.getRoundState();
            RoundMonitor roundMonitor = roundProvider.retrieve(roundStateResponse);
            HttpGameSession httpSession = roundMonitor.getHttpSession();
            ShopResponse[] shops = httpSession.getSelfState().getShops();
            RoundState currentState = roundStateFactory.merge(roundStateResponse, expectedState, parseShops(shops));
            return roundMonitor.tick(currentState, expectedState);
        } catch (Exception e) {
            e.printStackTrace();
            return expectedState;
        }
    }

    private Map<Coordinate, ShopResponse> parseShops(ShopResponse[] shops) {
        if (shops == null) return new HashMap<>();
        Map<Coordinate, ShopResponse> parsedShops = new HashMap<>();
        for (ShopResponse shop : shops) {
            parsedShops.put(
                    new Coordinate(shop.getPosition().getCol(), shop.getPosition().getRow()),
                    shop
            );
        }
        return parsedShops;
    }
}
