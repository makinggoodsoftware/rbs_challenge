package com.rbs.retailtherapy.domain;

import java.util.List;

public class Path {
    private final Direction direction;
    private final Orientation orientation;
    private final List<Coordinate> coordinates;

    public Path(Direction direction, Orientation orientation, List<Coordinate> coordinates) {
        this.direction = direction;
        this.orientation = orientation;
        this.coordinates = coordinates;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }
}
