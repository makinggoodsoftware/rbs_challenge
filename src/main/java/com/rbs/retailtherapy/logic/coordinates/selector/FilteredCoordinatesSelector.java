package com.rbs.retailtherapy.logic.coordinates.selector;

import com.google.common.base.Predicate;
import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelector;

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
