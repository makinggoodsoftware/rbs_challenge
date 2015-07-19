package com.rbs.retailtherapy.logic.coordinates.selector;

import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelector;

public class AllFilteredCoordinatesSelector extends BaseFilteredCoordinatesSelector implements CoordinatesSelector {
    @Override
    protected boolean accept(Coordinate coordinate) {
        return true;
    }
}
