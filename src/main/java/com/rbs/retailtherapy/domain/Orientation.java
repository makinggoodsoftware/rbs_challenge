package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.model.Direction;

public enum Orientation {
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST),
    NORTH_WEST(null),
    SOUTH_WEST(null),
    SOUTH_EAST(null),
    NORTH_EAST(null),
    NONE(null);

    private final Direction direction;

    Orientation(Direction direction) {
        this.direction = direction;
    }

    public Direction asDirection() {
        return direction;
    }
}
