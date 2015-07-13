package com.rbs.retailtherapy.v3;

public class InvestmentConfiguration {
    private final double outterInvestmentPercentage;
    private final double centerInvestmentPercentage;
    private final FixedSpacingInvestmentConfiguration outer1;
    private final FixedSpacingInvestmentConfiguration outer2;
    private final FixedSpacingInvestmentConfiguration outer3;

    public InvestmentConfiguration(
            double outterInvestmentPercentage,
            double centerInvestmentPercentage,
            FixedSpacingInvestmentConfiguration outer1,
            FixedSpacingInvestmentConfiguration outer2,
            FixedSpacingInvestmentConfiguration outer3
    ) {
        this.outterInvestmentPercentage = outterInvestmentPercentage;
        this.centerInvestmentPercentage = centerInvestmentPercentage;
        this.outer1 = outer1;
        this.outer2 = outer2;
        this.outer3 = outer3;
    }

    public double getOutterInvestmentPercentage() {
        return outterInvestmentPercentage;
    }

    public double getCenterInvestmentPercentage() {
        return centerInvestmentPercentage;
    }

    public FixedSpacingInvestmentConfiguration getOuter1() {
        return outer1;
    }

    public FixedSpacingInvestmentConfiguration getOuter2() {
        return outer2;
    }

    public FixedSpacingInvestmentConfiguration getOuter3() {
        return outer3;
    }
}
