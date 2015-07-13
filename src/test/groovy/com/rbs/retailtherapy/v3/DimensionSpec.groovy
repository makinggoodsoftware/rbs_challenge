package com.rbs.retailtherapy.v3

import spock.lang.Specification

class DimensionSpec extends Specification {
    def "should reduce the dimension" (){
        expect:
        new Dimension(41, 41).perimeter() == 160
        new Dimension(41, 41).reduce(1) == new Dimension(40, 40)
        new Dimension(40, 40).perimeter() == 156
        new Dimension(40, 40).shops() == new Dimension(20, 20)
        new Dimension(41, 41).shops() == new Dimension(20, 20)
        new Dimension(40, 40).shops().perimeter() == 76
        new Dimension(2, 2).perimeter() == 4
        new Dimension(2, 2).outterRingCoordinates() == [
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(1,0),
                new Coordinate(0,0),
        ] as Set
    }
}
