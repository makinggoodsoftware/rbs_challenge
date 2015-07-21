package com.rbs.retailtherapy.logic.strategy;

import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.Dimension;
import com.rbs.retailtherapy.logic.coordinates.Coordinates;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelector;

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
        if (numberOfDivisions == 0) {
            throw new IllegalStateException();
        }
        Set<Coordinate> allOutterRingCoordinates = coordinatesSelector.outterRingCoordinates(dimension);
        List<Coordinate> sortedCoordinates = coordinates.sortSquare(allOutterRingCoordinates, dimension.topLeft(), dimension.bottomRight());

        List<Coordinate> outterRingCoordinates = new ArrayList<>();
        int stepSize = sortedCoordinates.size() / numberOfDivisions;
        if (stepSize == 0){
            stepSize = 1;
        }
        for (int i=0; i<numberOfDivisions; i++){
            int toGet = i * stepSize;
            if (toGet < sortedCoordinates.size()){
                outterRingCoordinates.add(sortedCoordinates.get(toGet));
            }
        }
        return outterRingCoordinates;
    }

    public List<Coordinate> distributeAll(
            Dimension dimension,
            int numberOfDivisions
    ) {
        if (numberOfDivisions == 0) {
            throw new IllegalStateException();
        }
        Set<Coordinate> allCoordinates = coordinatesSelector.allCoordinates(dimension);
        List<Coordinate> allCoordinatesAsList = new ArrayList<>(allCoordinates);
        List<Coordinate> allCoordinatesSplitted = new ArrayList<>();
        int stepSize = allCoordinatesAsList.size() / numberOfDivisions;
        for (int i=0; i<numberOfDivisions; i++){
            allCoordinatesSplitted.add(allCoordinatesAsList.get(i*stepSize));
        }
        return allCoordinatesSplitted;
    }
}
