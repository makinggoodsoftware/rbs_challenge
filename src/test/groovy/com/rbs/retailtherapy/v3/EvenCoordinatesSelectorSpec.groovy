package com.rbs.retailtherapy.v3

import spock.lang.Specification

class EvenCoordinatesSelectorSpec extends Specification {
    EvenFilteredCoordinatesSelector testObj = new EvenFilteredCoordinatesSelector()

    def "should select all outer ring coordinates" (){
        expect:
        testObj.outterRingCoordinates(new Dimension(4, 4)) == [
                new Coordinate(0,3),
                new Coordinate(2,3),
                new Coordinate(0,1),
                new Coordinate(2,1),
        ] as Set
    }
}
