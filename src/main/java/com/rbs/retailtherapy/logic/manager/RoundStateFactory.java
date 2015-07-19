package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.domain.BidStatus;
import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.Dimension;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.Stock;
import com.rbs.retailtherapy.model.StocksPerRound;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RoundStateFactory {
    private final HttpGameClient httpGameClient;
    private final GridFactory gridFactory;

    public RoundStateFactory(HttpGameClient httpGameClient, GridFactory gridFactory) {
        this.httpGameClient = httpGameClient;
        this.gridFactory = gridFactory;
    }

    public RoundState merge(RoundStateResponse newState, RoundState base, Map<Coordinate, ShopResponse> myShops) {
        if (base == null){
            return firstRoundState (newState, myShops);
        } else {
            return nextRoundState (newState, base, myShops);
        }
    }

    private RoundState nextRoundState(RoundStateResponse newState, RoundState expectedState, Map<Coordinate, ShopResponse> myShops) {
        return new RoundState(
                true,
                false,
                newState.getRoundParameters().getShoppersCount(),
                expectedState.getStocks(),
                expectedState.getDimension(),
                newState.getRoundParameters().getInitialBatcoins(),
                gridFactory.from(newState.getGridCells(), myShops),
                expectedState.getBidStatus(),
                newState.getRoundState(),
                expectedState.getShopsBidCoordinates());
    }

    private RoundState firstRoundState(RoundStateResponse newState, Map<Coordinate, ShopResponse> myShops) {
        StocksPerRound[] stocksPerRound = httpGameClient.getGameParameters().getStocks();
        List<Stock> stocks= findStocks (stocksPerRound, newState.getRoundId());
        return new RoundState(
                true,
                false,
                newState.getRoundParameters().getShoppersCount(),
                stocks,
                new Dimension(41, 41),
                newState.getRoundParameters().getInitialBatcoins(),
                gridFactory.from(newState.getGridCells(), myShops),
                BidStatus.NOT_BID,
                newState.getRoundState(),
                new HashSet<Coordinate>());

    }

    private List<Stock> findStocks(StocksPerRound[] stocksPerRound, int roundId) {
        for (StocksPerRound perRound : stocksPerRound) {
            if (perRound.getRoundId() == roundId){
                return Arrays.asList(perRound.getStocks());
            }
        }
        throw new IllegalStateException("Can't find the stocks");
    }

    public RoundState copy(RoundState state) {
        return new RoundState(
                state.getIsBiddingOpen(),
                state.getIsTradeOpen(),
                state.getNumberOfShoppers(),
                state.getStocks(),
                state.getDimension(),
                state.getInitialMoney(),
                state.getGrid(),
                state.getBidStatus(),
                state.getRoundState(),
                state.getShopsBidCoordinates());
    }
}
