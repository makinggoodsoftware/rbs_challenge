package com.rbs.retailtherapy.v3

import com.rbs.retailtherapy.domain.Coordinate
import com.rbs.retailtherapy.domain.Dimension
import com.rbs.retailtherapy.logic.coordinates.Coordinates
import com.rbs.retailtherapy.logic.coordinates.CoordinatesSelectors
import com.rbs.retailtherapy.logic.strategy.CellDistributor
import spock.lang.Specification

class CellDistributorSpec extends Specification {
    CellDistributor testObj = new CellDistributor(new CoordinatesSelectors().shopCoordinatesSelector(), new Coordinates());

    def "should distribute shops when it diveides perfectly" (){
        when:
        List<Coordinate> distribution = testObj.distributeOuterRing(
                new Dimension(3, 3),
                4
        );

        then:
        distribution == [
                new Coordinate(0, 2),
                new Coordinate(2, 2),
                new Coordinate(2, 0),
                new Coordinate(0, 0),
        ]
    }

    def "should distribute shops when it DOESNT divide perfectly" (){
        when:
        List<Coordinate> distribution = testObj.distributeOuterRing(
                new Dimension(3, 3),
                3
        );

        then:
        distribution == [
                new Coordinate(0, 2),
                new Coordinate(2, 2),
                new Coordinate(2, 0),
        ]
    }

}
