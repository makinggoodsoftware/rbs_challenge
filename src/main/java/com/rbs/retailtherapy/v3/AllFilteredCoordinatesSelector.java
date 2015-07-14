package com.rbs.retailtherapy.v3;

public class AllFilteredCoordinatesSelector extends BaseFilteredCoordinatesSelector implements CoordinatesSelector {
    @Override
    protected boolean accept(Coordinate coordinate) {
        return true;
    }
}
