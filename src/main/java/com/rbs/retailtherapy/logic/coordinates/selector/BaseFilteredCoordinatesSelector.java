package com.rbs.retailtherapy.logic.coordinates.selector;

import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.Dimension;
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelector;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseFilteredCoordinatesSelector implements CoordinatesSelector {
    @Override
    public Set<Coordinate> allCoordinates(Dimension dimension) {
        Set<Coordinate> all = new HashSet<>();
        Integer top = top(dimension);
        for (int i = 0; i <= top; i++){
            all.addAll(horizontalLine(dimension, i));
        }
        return all;
    }

    @Override
    public Set<Coordinate> outterRingCoordinates(Dimension dimension){
        Set<Coordinate> ring = new HashSet<>();
        ring.addAll(horizontalLine(dimension, top(dimension)));
        ring.addAll(verticalLine(dimension, right(dimension)));
        ring.addAll(horizontalLine(dimension, bottom(dimension)));
        ring.addAll(verticalLine(dimension, left(dimension)));
        return ring;
    }

    @Override
    public Integer right(Dimension dimension) {
        return dimension.getRows() - 1;
    }

    @Override
    public Integer top(Dimension dimension) {
        return dimension.getCols() - 1;
    }


    @Override
    public Integer left(Dimension dimension) {
        return 0;
    }

    @Override
    public Integer bottom(Dimension dimension) {
        return 0;
    }

    @Override
    public Set<Coordinate> horizontalLine(Dimension dimension, Integer onRow) {
        HashSet<Coordinate> coordinates = new HashSet<>();
        for (int i = 0; i < dimension.getCols(); i = i + 1){
            Coordinate coordinate = new Coordinate(i, onRow);
            if (accept(coordinate)){
                coordinates.add(coordinate);
            }
        }
        return coordinates;
    }

    @Override
    public Set<Coordinate> verticalLine(Dimension dimension, Integer onCol) {
        Set<Coordinate> coordinates = new HashSet<>();
        for (int i = 0; i < dimension.getRows(); i = i + 1){
            Coordinate coordinate = new Coordinate(onCol, i);
            if (accept(coordinate)){
                coordinates.add(coordinate);
            }
        }
        return coordinates;
    }

    protected abstract boolean accept(Coordinate coordinate);

}
