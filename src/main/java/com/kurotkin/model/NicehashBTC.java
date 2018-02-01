package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NicehashBTC extends NicehashCurrency implements Nisehash {

    public NicehashBTC() {
        profitability = new BigDecimal("0.00000000");
        balance  = new BigDecimal("0.00000000");
        balanceConfirmed = new BigDecimal("0.00000000");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal valBTC) {
        profitability = profitability.add(valBTC);
    }

    @Override
    public void addBalance(BigDecimal valBTC) {
        balance = balance.add(valBTC);
    }

    @Override
    public void addBalanceConfirmed(BigDecimal valBTC) {
        balanceConfirmed = balanceConfirmed.add(valBTC);
    }

    @Override
    public void addSpeed(BigDecimal valBTC) {
        speed = speed.add(valBTC);
    }

    @Override
    public void addWorkers(Worker worker) {
        workerList.add(worker);
    }
}
