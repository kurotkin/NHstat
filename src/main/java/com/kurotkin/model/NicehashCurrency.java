package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

abstract class NicehashCurrency {
    BigDecimal profitability = null;
    BigDecimal balance = null;
    BigDecimal speed = null;
    List<Worker> workerList = new ArrayList<>();

    public BigDecimal getProfitability() {
        return profitability;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }
}
