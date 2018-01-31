package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

abstract class NicehashCurrency {
    BigDecimal profitability;
    BigDecimal balance;
    BigDecimal balanceConfirmed;
    BigDecimal speed;
    List<Worker> workerList;

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
        if(balanceConfirmed != null){
            return balance.add(balanceConfirmed);
        } else {
            return balance;
        }
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }
}
