package com.rbs.retailtherapy.v3;

import com.google.common.base.Predicate;

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
