package com.rbs.retailtherapy.v3;

import java.util.Set;

public interface CoordinatesSelector {
    Set<Coordinate> outterRingCoordinates(Dimension dimension);

    Integer right(Dimension dimension);

    Integer top(Dimension dimension);

    Integer left(Dimension dimension);

    Integer bottom(Dimension dimension);

    Set<Coordinate> horizontalLine(Dimension dimension, Integer onRow);

    Set<Coordinate> verticalLine(Dimension dimension, Integer onCol);
}
