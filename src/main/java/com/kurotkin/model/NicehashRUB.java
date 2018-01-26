package com.kurotkin.model;

import com.kurotkin.Work;
import com.kurotkin.controller.Rate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NicehashRUB  extends NicehashCurrency implements Nisehash  {
    private Rate rate;

    public NicehashRUB(Rate rate) {
        this.profitability = new BigDecimal("0.00");
        balance  = new BigDecimal("0.00");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal valBTC) {
        BigDecimal valRUB = valBTC.multiply(rate.getPrice_rub());
        this.profitability = this.profitability.add(valRUB);
    }

    @Override
    public void addBalance(BigDecimal valBTC) {
        BigDecimal valRUB = valBTC.multiply(rate.getPrice_rub());
        this.balance = this.balance.add(valBTC);
    }

    @Override
    public void addSpeed(BigDecimal valBTC) {
        this.speed = this.speed.add(valBTC);
    }

    @Override
    public void addWorkers(Worker worker) {
        worker.profitability = worker.profitability.multiply(rate.getPrice_rub());
        worker.balance = worker.balance.multiply(rate.getPrice_rub());
        workerList.add(worker);
    }
}
