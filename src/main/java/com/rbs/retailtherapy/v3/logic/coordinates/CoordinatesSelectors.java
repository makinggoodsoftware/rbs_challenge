package com.rbs.retailtherapy.v3.logic.coordinates;

import com.google.common.base.Predicate;
import com.rbs.retailtherapy.v3.domain.Coordinate;
import com.rbs.retailtherapy.v3.logic.coordinates.selector.FilteredCoordinatesSelector;

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

    public boolean isShopCoordinate(Coordinate coordinate) {
        return isEven(coordinate.getRow()) && isEven(coordinate.getCol());
    }

    private boolean isEven(Integer number) {
        return number % 2 == 0;
    }
}