package com.rbs.retailtherapy.v3;

public class EvenFilteredCoordinatesSelector extends BaseFilteredCoordinatesSelector implements CoordinatesSelector {
    @Override
    protected boolean accept(Coordinate coordinate) {
        return false;
    }
}
