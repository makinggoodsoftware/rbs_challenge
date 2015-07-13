package com.rbs.retailtherapy.v3

import spock.lang.Specification

import static PlacementType.OUTTER_1st_LOOP
import static PlacementType.OUTTER_2nd_LOOP
import static PlacementType.OUTTER_3rd_LOOP
import static com.rbs.retailtherapy.v3.PlacementType.CENTER

class InvestorSpec extends Specification {
    Investor investor = new Investor()

    def "should invest evenly" (){
        given:
        InvestmentConfiguration investmentConfiguration = new InvestmentConfiguration(
                50,
                0.8,
                0.9,
                0.1,
                new FixedSpacingInvestmentConfiguration(
                        0.6,
                        1
                ),
                new FixedSpacingInvestmentConfiguration(
                        0.25,
                        1
                ),
                new FixedSpacingInvestmentConfiguration(
                        0.15,
                        1
                )
        )

        when:
        Map<PlacementType, Investment> invest = investor.invest(10000, investmentConfiguration, new Dimension(41, 41))

        then:
        invest.get(OUTTER_1st_LOOP).bidAmount == 56.84d
        invest.get(OUTTER_1st_LOOP).numberOfShops == 76

        invest.get(OUTTER_2nd_LOOP).bidAmount == 50.0d
        invest.get(OUTTER_2nd_LOOP).numberOfShops == 36

        invest.get(OUTTER_3rd_LOOP).bidAmount == 63.53d
        invest.get(OUTTER_3rd_LOOP).numberOfShops == 17

        invest.get(CENTER).bidAmount == 50.0d
        invest.get(CENTER).numberOfShops == 16
    }
}
