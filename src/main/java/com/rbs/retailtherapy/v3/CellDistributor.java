package com.rbs.retailtherapy.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CellDistributor {
    private final CoordinatesSelector shopCoordinatesSelector;

    public CellDistributor(CoordinatesSelector shopCoordinatesSelector) {
        this.shopCoordinatesSelector = shopCoordinatesSelector;
    }

    public List<Coordinate> distribute(
            Grid grid,
            Coordinate startPoint,
            int numberOfDivisions
    ) {
        Set<Coordinate> allOutterRingCoordinates = shopCoordinatesSelector.outterRingCoordinates(grid.getDimension());
        List<Coordinate> sortedCoordinates = sort(allOutterRingCoordinates, startPoint);

        List<Coordinate> outterRingCoordinates = new ArrayList<>();
        int steps = sortedCoordinates.size() / numberOfDivisions;
        int indexToExtract = 0;
        for (int i=0; i<steps; i++){
            outterRingCoordinates.add(sortedCoordinates.get(indexToExtract));
        }
        return outterRingCoordinates;
    }

    private List<Coordinate> sort(Set<Coordinate> allOutterRingCoordinates, Coordinate startPoint) {
        return null;
    }
}
