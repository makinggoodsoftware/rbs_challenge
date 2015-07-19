package com.rbs.retailtherapy.v3.logic.coordinates.selector;

import com.rbs.retailtherapy.v3.domain.Coordinate;
import com.rbs.retailtherapy.v3.logic.coordinates.CoordinatesSelector;

public class AllFilteredCoordinatesSelector extends BaseFilteredCoordinatesSelector implements CoordinatesSelector {
    @Override
    protected boolean accept(Coordinate coordinate) {
        return true;
    }
}
