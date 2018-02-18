package com.kurotkin.model;

import com.kurotkin.controller.RateController;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NicehashUSD extends NicehashCurrency implements Nisehash {
    private RateController rateController;

    public NicehashUSD(RateController rateController) {
        this.rateController = rateController;
        profitability = new BigDecimal("0.00");
        balance  = new BigDecimal("0.00");
        balanceConfirmed = new BigDecimal("0.00");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal valBTC) {
        BigDecimal valUSD = valBTC.multiply(rateController.getPrice_usd());
        profitability = profitability.add(valUSD);
    }

    @Override
    public void addBalance(BigDecimal valBTC) {
        BigDecimal valUSD = valBTC.multiply(rateController.getPrice_usd());
        balance = balance.add(valUSD);
    }

    @Override
    public void addBalanceConfirmed(BigDecimal valBTC) {
        BigDecimal valUSD = valBTC.multiply(rateController.getPrice_usd());
        balanceConfirmed = balanceConfirmed.add(valUSD);
    }

    @Override
    public void addSpeed(BigDecimal valBTC) {
        speed = speed.add(valBTC);
    }

    @Override
    public void addWorkers(Worker worker) {
        worker.profitability = worker.profitability.multiply(rateController.getPrice_usd());
        worker.balance = worker.balance.multiply(rateController.getPrice_usd());
        workerList.add(worker);
    }
}
