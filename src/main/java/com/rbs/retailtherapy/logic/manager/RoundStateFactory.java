package com.rbs.retailtherapy.logic.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.rbs.retailtherapy.domain.*;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.SelfStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.entity.ShopperResponse;
import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.model.RoundStateEnum;
import com.rbs.retailtherapy.model.ShopOwnerResponse;
import com.rbs.retailtherapy.model.Stock;
import com.rbs.retailtherapy.model.StocksPerRound;

import java.util.*;

public class RoundStateFactory {
    private final ParticipantImpl participantImpl;
    private final GridFactory gridFactory;
    private final String myTeamName;

    public RoundStateFactory(ParticipantImpl participantImpl, GridFactory gridFactory, String myTeamName) {
        this.participantImpl = participantImpl;
        this.gridFactory = gridFactory;
        this.myTeamName = myTeamName;
    }

    public RoundState merge(RoundStateResponse newState, RoundState base, Map<Coordinate, ShopResponse> myShops, Map<Integer, Customer> customers, SelfStateResponse selfStateResponse) {
        if (base == null) {
            return firstRoundState(newState, myShops, customers, selfStateResponse);
        } else {
            return nextRoundState(newState, base, myShops, selfStateResponse);
        }
    }

    private RoundState nextRoundState(RoundStateResponse newState, RoundState expectedState, Map<Coordinate, ShopResponse> myShops, SelfStateResponse selfStateResponse) {
        RoundState roundState = new RoundState(
                true,
                newState.getRoundState() == RoundStateEnum.TRADING,
                newState.getRoundParameters().getShoppersCount(),
                expectedState.getStocks(),
                expectedState.getDimension(),
                newState.getRoundParameters().getInitialBatcoins(),
                gridFactory.from(newState.getGridCells(), myShops),
                parse(newState.getShoppers()),
                expectedState.getCustomers(),
                expectedState.getBidStatus(),
                newState.getRoundState(),
                expectedState.getShopsBidCoordinates(),
                asMap(expectedState, newState.getShopOwners(), selfStateResponse),
                selfStateResponse,
                expectedState.getCurrentStep(),
                expectedState.getAdPrice());
        roundState.setUserTracking(expectedState.getUserTracking());
        return roundState;
    }

    private Map<Coordinate, ShopTracker> asMap(RoundState expectedState, ShopOwnerResponse[] shopOwners, SelfStateResponse selfStateResponse) {
        if (shopOwners == null) return new HashMap<>();
        Map<Coordinate, ShopTracker> asMap = new HashMap<>();
        for (ShopOwnerResponse shopOwner : shopOwners) {
            if (shopOwner.getShops() != null) {
                for (ShopResponse shopResponse : shopOwner.getShops()) {
                    Coordinate coordinate = new Coordinate(shopResponse.getPosition().getCol(), shopResponse.getPosition().getRow());
                    boolean isMine = shopOwner.getTeamName().equals(myTeamName);
                    ShopTracker shopTracker;
                    if (isMine) {
                        shopTracker = new ShopTracker(withCoordinate(selfStateResponse.getShops(), coordinate), isMine);
                        if (expectedState != null){
                            ShopTracker previousTracker = expectedState.getShops().get(coordinate);
                            if (previousTracker != null){
                                shopTracker.setStockBoughtOn(previousTracker.getStockBoughtOn());
                            }
                        }
                    } else {
                        shopTracker = new ShopTracker(shopResponse, isMine);
                    }
                    asMap.put(coordinate, shopTracker);
                }
            }
        }
        return asMap;
    }

    private ShopResponse withCoordinate(ShopResponse[] shops, Coordinate coordinate) {
        for (ShopResponse shop : shops) {
            if (shop.getPosition().getRow() == coordinate.getRow() && shop.getPosition().getCol() == coordinate.getCol())
                return shop;
        }
        throw new IllegalStateException();
    }

    public RoundState firstRoundState(RoundStateResponse newState, Map<Coordinate, ShopResponse> myShops, Map<Integer, Customer> customers, SelfStateResponse selfStateResponse) {
        StocksPerRound[] stocksPerRound = participantImpl.getGameParameters().getStocks();
        List<Stock> stocks = findStocks(stocksPerRound, newState.getRoundId());
        return new RoundState(
                true,
                newState.getRoundState() == RoundStateEnum.TRADING,
                newState.getRoundParameters().getShoppersCount(),
                stocks,
                new Dimension(41, 41),
                newState.getRoundParameters().getInitialBatcoins(),
                gridFactory.from(newState.getGridCells(), myShops),
                parse(newState.getShoppers()),
                customers,
                BidStatus.NOT_BID,
                newState.getRoundState(),
                new HashSet<Coordinate>(),
                asMap(null, newState.getShopOwners(), selfStateResponse),
                selfStateResponse,
                1,
                newState.getRoundParameters().getAdvertPrice());

    }

    private Multimap<Coordinate, ShopperResponse> parse(ShopperResponse[] shoppers) {
        if (shoppers == null) return ArrayListMultimap.create();
        Multimap<Coordinate, ShopperResponse> parsedShoppers = ArrayListMultimap.create();
        for (ShopperResponse shopper : shoppers) {
            parsedShoppers.put(new Coordinate(shopper.getCurrentPosition().getCol(), shopper.getCurrentPosition().getRow()), shopper);
        }
        return parsedShoppers;
    }

    private List<Stock> findStocks(StocksPerRound[] stocksPerRound, int roundId) {
        for (StocksPerRound perRound : stocksPerRound) {
            if (perRound.getRoundId() == roundId) {
                return Arrays.asList(perRound.getStocks());
            }
        }
        throw new IllegalStateException("Can't find the stocks");
    }

    public RoundState copy(RoundState state) {
        RoundState roundState = new RoundState(
                state.getIsBiddingOpen(),
                state.getIsTradeOpen(),
                state.getNumberOfShoppers(),
                state.getStocks(),
                state.getDimension(),
                state.getInitialMoney(),
                state.getGrid(),
                state.getShoppers(),
                state.getCustomers(),
                state.getBidStatus(),
                state.getRoundState(),
                state.getShopsBidCoordinates(),
                state.getShops(),
                state.getSelfStateResponse(),
                state.getCurrentStep(),
                state.getAdPrice());

        roundState.setUserTracking(state.getUserTracking());
        return roundState;
    }
}
