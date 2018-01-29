package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

abstract class NicehashCurrency {
    BigDecimal profitability = null;
    BigDecimal balance = null;
    BigDecimal balanceConfirmed = null;
    BigDecimal speed = null;
    List<Worker> workerList = new ArrayList<>();

    public BigDecimal getProfitability() {
        return profitability;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getBalanceConfirmed() {
        return balanceConfirmed;
    }

    public BigDecimal getBalanceTotal() {
        return balance;
    }

    public BigDecimal getSpeed() {
        return speed.add(balanceConfirmed);
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }
}
