package com.rbs.retailtherapy.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CellDistributor {
    private final CoordinatesSelector coordinatesSelector;
    private final Coordinates coordinates;

    public CellDistributor(CoordinatesSelector coordinatesSelector, Coordinates coordinates) {
        this.coordinatesSelector = coordinatesSelector;
        this.coordinates = coordinates;
    }

    public List<Coordinate> distributeOuterRing(
            Dimension dimension,
            int numberOfDivisions
    ) {
        Set<Coordinate> allOutterRingCoordinates = coordinatesSelector.outterRingCoordinates(dimension);
        List<Coordinate> sortedCoordinates = coordinates.sortSquare(allOutterRingCoordinates, dimension.topLeft(), dimension.bottomRight());

        List<Coordinate> outterRingCoordinates = new ArrayList<>();
        int stepSize = sortedCoordinates.size() / numberOfDivisions;
        for (int i=0; i<numberOfDivisions; i++){
            outterRingCoordinates.add(sortedCoordinates.get(i*stepSize));
        }
        return outterRingCoordinates;
    }
}
