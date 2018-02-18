package com.kurotkin.model;

import com.kurotkin.controller.RateController;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NicehashRUB  extends NicehashCurrency implements Nisehash  {
    private RateController rateController;

    public NicehashRUB(RateController rateController) {
        this.rateController = rateController;
        profitability = new BigDecimal("0.00");
        balance  = new BigDecimal("0.00");
        balanceConfirmed = new BigDecimal("0.00");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal valBTC) {
        BigDecimal valRUB = valBTC.multiply(rateController.getPrice_rub());
        profitability = profitability.add(valRUB);
    }

    @Override
    public void addBalance(BigDecimal valBTC) {
        BigDecimal valRUB = valBTC.multiply(rateController.getPrice_rub());
        balance = balance.add(valRUB);
    }

    @Override
    public void addBalanceConfirmed(BigDecimal valBTC) {
        BigDecimal valRUB = valBTC.multiply(rateController.getPrice_rub());
        this.balanceConfirmed = this.balanceConfirmed.add(valRUB);
    }

    @Override
    public void addSpeed(BigDecimal valBTC) {
        this.speed = this.speed.add(valBTC);
    }

    @Override
    public void addWorkers(Worker worker) {
        worker.profitability = worker.profitability.multiply(rateController.getPrice_rub());
        worker.balance = worker.balance.multiply(rateController.getPrice_rub());
        workerList.add(worker);
    }
}
