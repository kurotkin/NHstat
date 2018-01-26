package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class NicehashBTC extends NicehashCurrency implements Nisehash {

    public NicehashBTC() {
        this.profitability = new BigDecimal("0.00000000");
        balance  = new BigDecimal("0.00000000");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();
    }

    @Override
    public void addProfitability(BigDecimal val) {
        this.profitability = this.profitability.add(val);
    }

    @Override
    public void addBalance(BigDecimal val) {
        this.balance = this.balance.add(val);
    }

    @Override
    public void addSpeed(BigDecimal val) {
        this.speed = this.speed.add(val);
    }
}
