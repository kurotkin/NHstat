package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.List;

public interface Nisehash {
    void addProfitability(BigDecimal valBTC);
    void addBalance(BigDecimal valBTC);
    void addSpeed(BigDecimal valBTC);
    void addWorkers(Worker worker);
    void addBalanceConfirmed(BigDecimal valBTC);
}
