package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NicehashRUB  extends NicehashCurrency implements Nisehash  {

    public NicehashRUB() {
        this.profitability = new BigDecimal("0.00");
        balance  = new BigDecimal("0.00");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal valBTC) {
        this.profitability = this.profitability.add(valBTC);
    }

    @Override
    public void addBalance(BigDecimal valBTC) {
        this.balance = this.balance.add(valBTC);
    }

    @Override
    public void addSpeed(BigDecimal valBTC) {
        this.speed = this.speed.add(valBTC);
    }
}
