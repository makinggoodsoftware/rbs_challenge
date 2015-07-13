package com.rbs.retailtherapy.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShopDistributor {
    public List<Coordinate> distribute(
            Grid grid,
            Coordinate startPoint,
            int numberOfDivisions
    ) {
        Set<Coordinate> allOutterRingCoordinates = grid.getDimension().outterRingCoordinates();
        List<Coordinate> sortedCoordinates = sort(allOutterRingCoordinates, startPoint);
        List<Coordinate> shopCoordinates = new ArrayList<Coordinate>();

        List<Coordinate> outterRingCoordinates = new ArrayList<>();
        int steps = shopCoordinates.size() / numberOfDivisions;
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
