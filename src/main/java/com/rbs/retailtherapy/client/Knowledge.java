package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.RoundStateResponse;

public class Knowledge {
    private final Dimension dimension;
    private final RoundStateResponse roundState;

    public Knowledge(Dimension dimension, RoundStateResponse roundState) {
        this.dimension = dimension;
        this.roundState = roundState;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public RoundStateResponse getRoundState() {
        return roundState;
    }
}
