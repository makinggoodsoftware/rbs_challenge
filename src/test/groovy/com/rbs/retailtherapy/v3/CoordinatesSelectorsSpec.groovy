package com.rbs.retailtherapy.v3

import spock.lang.Specification

class CoordinatesSelectorsSpec extends Specification {
    CoordinatesSelectors testObj = new CoordinatesSelectors()

    def "should tell if a coordinate is valid for a shop" (){
        expect:
        testObj.isShopCoordinate(new Coordinate(0, 0))
        !testObj.isShopCoordinate(new Coordinate(0, 1))
        testObj.isShopCoordinate(new Coordinate(0, 2))
        !testObj.isShopCoordinate(new Coordinate(1, 0))
        !testObj.isShopCoordinate(new Coordinate(1, 1))
        !testObj.isShopCoordinate(new Coordinate(1, 2))
        testObj.isShopCoordinate(new Coordinate(2, 0))
        !testObj.isShopCoordinate(new Coordinate(2, 1))
        testObj.isShopCoordinate(new Coordinate(2, 2))
    }
}
