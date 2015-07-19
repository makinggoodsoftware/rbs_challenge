package com.rbs.retailtherapy.v3

import com.rbs.retailtherapy.model.Stock
import com.rbs.retailtherapy.v3.domain.Customer
import com.rbs.retailtherapy.v3.logic.strategy.FinanceService
import spock.lang.Specification

class FinanceServiceSpec extends Specification {
    FinanceService testObj = new FinanceService()

    def "should calculate the average profits available" (){
        expect:
        testObj.averageProfits(
                1,
                [
                    new Customer(1, [Stock.StockType.CAMERA], 100.0, [])
                ],
                [
                    new Stock(Stock.StockType.CAMERA, Stock.Classification.CHEAP, 10.0d, 20.0d)
                ]
        ) == 50.0d
    }
}
