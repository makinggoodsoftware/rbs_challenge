package com.rbs.retailtherapy.domain;

import java.util.List;

public class Path {
    private final Direction direction;
    private final Orientation orientation;
    private final List<Coordinate> coordinates;
    private final boolean exhaustsDirection;

    public Path(Direction direction, Orientation orientation, List<Coordinate> coordinates, boolean exhaustsDirection) {
        this.direction = direction;
        this.orientation = orientation;
        this.coordinates = coordinates;
        this.exhaustsDirection = exhaustsDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isExhaustsDirection() {
        return exhaustsDirection;
    }
}
