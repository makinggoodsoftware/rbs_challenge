package com.rbs.retailtherapy.v3;

import java.util.HashMap;
import java.util.Map;

public class Investor {

    public final Map<PlacementType, Investment> invest (double amountToInvest, InvestmentConfiguration  investmentConfiguration, Dimension gridDimension){
        double totalToInvest = amountToInvest * investmentConfiguration.getToInvestPercentage();
        double totalOuterToInvest = totalToInvest * investmentConfiguration.getOutterInvestmentPercentage();
        Investment outerFirstLoopInvestment = getInvestment(
                gridDimension.reduce(1).shops(),
                totalOuterToInvest * investmentConfiguration.getOuter1().getInvestmentPercentage(),
                investmentConfiguration.getOuter1().getDesiredSpacing(),
                investmentConfiguration.getMinimumBid()
        );
        Investment outerSecondLoopInvestment = getInvestment(
                gridDimension.reduce(3).shops(),
                totalOuterToInvest * investmentConfiguration.getOuter2().getInvestmentPercentage(),
                investmentConfiguration.getOuter2().getDesiredSpacing(),
                investmentConfiguration.getMinimumBid()
        );
        Investment outerThirdLoopInvestment = getInvestment(
                gridDimension.reduce(5).shops(),
                totalOuterToInvest * investmentConfiguration.getOuter3().getInvestmentPercentage(),
                investmentConfiguration.getOuter3().getDesiredSpacing(),
                investmentConfiguration.getMinimumBid()
        );
        double totalCenetrToInvest = totalToInvest * investmentConfiguration.getCenterInvestmentPercentage();
        Double numberOfShops = totalCenetrToInvest / investmentConfiguration.getMinimumBid();
        Investment centerLoopInvestment = new Investment(investmentConfiguration.getMinimumBid(), Math.round(numberOfShops.intValue()));

        HashMap<PlacementType, Investment> investments = new HashMap<PlacementType, Investment>();
        investments.put(PlacementType.OUTTER_1st_LOOP, outerFirstLoopInvestment);
        investments.put(PlacementType.OUTTER_2nd_LOOP, outerSecondLoopInvestment);
        investments.put(PlacementType.OUTTER_3rd_LOOP, outerThirdLoopInvestment);
        investments.put(PlacementType.CENTER, centerLoopInvestment);
        return investments;
    }

    private Investment getInvestment(Dimension dimension, double toInvest, int desiredSpacing, double minimumBid) {
        int perimeter = dimension.perimeter();
        int numberOfShops = perimeter / desiredSpacing;
        double bidAmount = toInvest / numberOfShops;
        Double idealBidAmount = roundTwoDec(bidAmount);
        if (idealBidAmount < minimumBid){
            return getInvestment(dimension, toInvest, desiredSpacing + 1, minimumBid);
        }
        return new Investment(idealBidAmount, numberOfShops);
    }

    private Double roundTwoDec(double bidAmount) {
        return Math.round( bidAmount * 100.0 ) / 100.0;
    }
}
