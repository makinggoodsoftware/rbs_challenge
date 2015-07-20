package com.rbs.retailtherapy.domain;

import java.util.List;

public class PathType {
    private final List<Path> paths;
    private final boolean loops;

    public PathType(List<Path> paths, boolean loops) {
        this.paths = paths;
        this.loops = loops;
    }

}
