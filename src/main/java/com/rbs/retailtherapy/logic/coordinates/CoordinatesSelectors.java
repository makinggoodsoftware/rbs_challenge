package com.rbs.retailtherapy.logic.coordinates;

import com.google.common.base.Predicate;
import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.logic.coordinates.selector.FilteredCoordinatesSelector;

public class CoordinatesSelectors {
    private final Predicate<Coordinate> SHOP_FILTER = new Predicate<Coordinate>() {
        @Override
        public boolean apply(Coordinate coordinate) {
            return isShopCoordinate(coordinate);
        }
    };

    public CoordinatesSelector shopCoordinatesSelector (){
        return new FilteredCoordinatesSelector(SHOP_FILTER);
    }

    public CoordinatesSelector waitingShopBid(final RoundState expectedCurrentState) {
        return new FilteredCoordinatesSelector(new Predicate<Coordinate>() {
            @Override
            public boolean apply(Coordinate coordinate) {
                return expectedCurrentState.getShopsBidCoordinates().contains(coordinate);
            }
        });
    }

    public boolean isShopCoordinate(Coordinate coordinate) {
        return isEven(coordinate.getRow()) && isEven(coordinate.getCol());
    }

    private boolean isEven(Integer number) {
        return number % 2 == 0;
    }
}
