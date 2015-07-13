package com.rbs.retailtherapy.v3

import spock.lang.Specification

class ShopDistributorSpec extends Specification {
    ShopDistributor testObj = new ShopDistributor();

    @SuppressWarnings("GrEqualsBetweenInconvertibleTypes")
    def "should distribute shops" (){
        when:
        List<Coordinate> distribution = testObj.distribute (
                new Grid(new Dimension(4, 4)),
                new Coordinate(3, 3),
                2
        );

        then:
        distribution == [
                new Coordinate(3, 3),
                new Coordinate(0, 0)
        ]
    }
}
