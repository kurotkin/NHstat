package com.kurotkin.model;

import java.math.BigDecimal;
import java.util.List;

public interface Nisehash {
    public void addProfitability(BigDecimal valBTC);
    public void addBalance(BigDecimal valBTC);
    public void addSpeed(BigDecimal valBTC);
    public void addWorkers(Worker worker);
    public void addBalanceConfirmed(BigDecimal valBTC);
}
