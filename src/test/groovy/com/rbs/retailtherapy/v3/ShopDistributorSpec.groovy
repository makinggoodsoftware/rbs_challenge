package com.rbs.retailtherapy.v3

import spock.lang.Specification

import static com.rbs.retailtherapy.v3.Path.SQUARE

class ShopDistributorSpec extends Specification {
    ShopDistributor testObj = new ShopDistributor();

    @SuppressWarnings("GrEqualsBetweenInconvertibleTypes")
    def "should distribute shops" (){
        when:
        List<Coordinate> distribution = testObj.distribute (
                new Grid(new Dimension(4, 4)),
                new Coordinate(3, 3),
                SQUARE,
                2,
                2
        );

        then:
        distribution == [
                new Coordinate(3, 3),
                new Coordinate(0, 0)
        ]
    }
}
