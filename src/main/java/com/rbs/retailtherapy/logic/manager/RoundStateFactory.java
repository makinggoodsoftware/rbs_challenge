package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.Stock;
import com.rbs.retailtherapy.model.StocksPerRound;
import com.rbs.retailtherapy.domain.BidStatus;
import com.rbs.retailtherapy.domain.Dimension;
import com.rbs.retailtherapy.domain.RoundState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoundStateFactory {
    private final HttpGameClient httpGameClient;

    public RoundStateFactory(HttpGameClient httpGameClient) {
        this.httpGameClient = httpGameClient;
    }

    public RoundState from(RoundStateResponse newState, RoundState expectedState) {
        if (expectedState == null){
            return firstRoundState (newState);
        } else {
            return nextRoundState (newState, expectedState);
        }
    }

    private RoundState nextRoundState(RoundStateResponse newState, RoundState expectedState) {
        return new RoundState(
                true,
                false,
                newState.getRoundParameters().getShoppersCount(),
                expectedState.getStocks(),
                expectedState.getDimension(),
                newState.getRoundParameters().getInitialBatcoins(),
                expectedState.getBidStatus()
        );
    }

    private RoundState firstRoundState(RoundStateResponse newState) {
        StocksPerRound[] stocksPerRound = httpGameClient.getGameParameters().getStocks();
        List<Stock> stocks= findStocks (stocksPerRound, newState.getRoundId());
        return new RoundState(
                true,
                false,
                newState.getRoundParameters().getShoppersCount(),
                stocks,
                new Dimension(41, 41),
                newState.getRoundParameters().getInitialBatcoins(),
                BidStatus.NOT_BID
        );

    }

    private List<Stock> findStocks(StocksPerRound[] stocksPerRound, int roundId) {
        for (StocksPerRound perRound : stocksPerRound) {
            if (perRound.getRoundId() == roundId){
                return Arrays.asList(perRound.getStocks());
            }
        }
        throw new IllegalStateException("Can't find the stocks");
    }
}
