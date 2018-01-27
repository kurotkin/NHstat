package com.kurotkin.model;

import com.kurotkin.controller.Rate;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NicehashUSD extends NicehashCurrency implements Nisehash {
    private Rate rate;

    public NicehashUSD(Rate rate) {
        this.rate = rate;
        this.profitability = new BigDecimal("0.00");
        balance  = new BigDecimal("0.00");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal valBTC) {
        BigDecimal valUSD = valBTC.multiply(rate.getPrice_usd());
        this.profitability = this.profitability.add(valUSD);
    }

    @Override
    public void addBalance(BigDecimal valBTC) {
        BigDecimal valUSD = valBTC.multiply(rate.getPrice_usd());
        this.balance = this.balance.add(valUSD);
    }

    @Override
    public void addSpeed(BigDecimal valBTC) {
        this.speed = this.speed.add(valBTC);
    }

    @Override
    public void addWorkers(Worker worker) {
        worker.profitability = worker.profitability.multiply(rate.getPrice_usd());
        worker.balance = worker.balance.multiply(rate.getPrice_usd());
        workerList.add(worker);
    }
}
