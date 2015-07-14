package com.rbs.retailtherapy.v3;

import java.util.HashMap;
import java.util.Map;

public class Investor {

    public final Map<PlacementType, Investment> invest(Dimension gridDimension, double amountToInvest, double minimumBid, InvestmentConfiguration investmentConfiguration){
        double totalOuterToInvest = amountToInvest * investmentConfiguration.getOutterInvestmentPercentage();
        Investment outerFirstLoopInvestment = getInvestment(
                gridDimension.reduce(1).half(),
                totalOuterToInvest * investmentConfiguration.getOuter1().getInvestmentPercentage(),
                investmentConfiguration.getOuter1().getDesiredSpacing(),
                minimumBid
        );
        Investment outerSecondLoopInvestment = getInvestment(
                gridDimension.reduce(3).half(),
                totalOuterToInvest * investmentConfiguration.getOuter2().getInvestmentPercentage(),
                investmentConfiguration.getOuter2().getDesiredSpacing(),
                minimumBid
        );
        Investment outerThirdLoopInvestment = getInvestment(
                gridDimension.reduce(5).half(),
                totalOuterToInvest * investmentConfiguration.getOuter3().getInvestmentPercentage(),
                investmentConfiguration.getOuter3().getDesiredSpacing(),
                minimumBid
        );
        double totalCenetrToInvest = amountToInvest * investmentConfiguration.getCenterInvestmentPercentage();
        Double numberOfShops = totalCenetrToInvest / minimumBid;
        Investment centerLoopInvestment = new Investment(minimumBid, Math.round(numberOfShops.intValue()));

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
