package com.rbs.retailtherapy.v3.logic.strategy;

import com.rbs.retailtherapy.v3.domain.GameState;
import com.rbs.retailtherapy.v3.domain.InvestmentConfiguration;
import com.rbs.retailtherapy.v3.domain.RoundState;
import com.rbs.retailtherapy.v3.domain.ShopBiddingStrategy;

public class Strategizer {
    private final FinanceService financeService;
    private final InvestmentConfiguration baseInvestmentConfiguration;

    public Strategizer(FinanceService financeService, InvestmentConfiguration baseInvestmentConfiguration) {
        this.financeService = financeService;
        this.baseInvestmentConfiguration = baseInvestmentConfiguration;
    }

    public ShopBiddingStrategy shopBiddingStrategy(GameState gameState, RoundState roundState) {
        Double averageAvailableProfit = financeService.averageProfits(
                roundState.getNumberOfShoppers(),
                gameState.getTypeOfShoppers(),
                roundState.getStocks()
        );

        double profitTarget = averageAvailableProfit * 3;
        double willingToSpend = profitTarget / 2;
        double maximumToInvest = roundState.getInitialMoney() * gameState.getMaximumToInvest();
        double amountToInvest = willingToSpend > roundState.getInitialMoney() ? maximumToInvest : willingToSpend;
        return new ShopBiddingStrategy(amountToInvest, baseInvestmentConfiguration);
    }
}
