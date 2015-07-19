package com.rbs.retailtherapy.v3.logic.coordinates.selector;

import com.google.common.base.Predicate;
import com.rbs.retailtherapy.v3.domain.Coordinate;
import com.rbs.retailtherapy.v3.logic.coordinates.CoordinatesSelector;

public class FilteredCoordinatesSelector extends BaseFilteredCoordinatesSelector implements CoordinatesSelector {
    private final Predicate<Coordinate> filter;

    public FilteredCoordinatesSelector(Predicate<Coordinate> filter) {
        this.filter = filter;
    }

    @Override
    protected boolean accept(Coordinate coordinate) {
        return filter.apply(coordinate);
    }
}
