package com.rbs.retailtherapy.v3

import com.rbs.retailtherapy.domain.Coordinate
import com.rbs.retailtherapy.domain.Dimension
import com.rbs.retailtherapy.logic.coordinates.selector.AllFilteredCoordinatesSelector
import spock.lang.Specification

class AllCoordinatesSelectorSpec extends Specification {
    AllFilteredCoordinatesSelector testObj = new AllFilteredCoordinatesSelector()

    def "should select all outer ring coordinates" (){
        expect:
        testObj.outterRingCoordinates(new Dimension(2, 2)) == [
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(1,0),
                new Coordinate(0,0),
        ] as Set
    }
}
