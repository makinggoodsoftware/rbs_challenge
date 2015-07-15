package com.rbs.retailtherapy.v3

import spock.lang.Specification

class CoordinatesSpec extends Specification {
    Coordinates testObj = new Coordinates()


    def "should sort the coordinates" (){
        expect:
        testObj.sortSquare([
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(2, 0),
            new Coordinate(3, 0),
            new Coordinate(0, 1),
            new Coordinate(0, 2),
            new Coordinate(0, 3),
            new Coordinate(3, 3),
            new Coordinate(3, 2),
            new Coordinate(3, 1),
            new Coordinate(1, 3),
            new Coordinate(2, 3),
        ] as Set, new Coordinate(0,3), new Coordinate(3, 0)) == [
            new Coordinate(0, 3),
            new Coordinate(1, 3),
            new Coordinate(2, 3),
            new Coordinate(3, 3),
            new Coordinate(3, 2),
            new Coordinate(3, 1),
            new Coordinate(3, 0),
            new Coordinate(2, 0),
            new Coordinate(1, 0),
            new Coordinate(0, 0),
            new Coordinate(0, 1),
            new Coordinate(0, 2),
        ]
    }
}
