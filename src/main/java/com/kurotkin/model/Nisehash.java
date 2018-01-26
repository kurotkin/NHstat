package com.kurotkin.model;

import java.math.BigDecimal;

public interface Nisehash {
    public void addProfitability(BigDecimal valBTC);
    public void addBalance(BigDecimal valBTC);
    public void addSpeed(BigDecimal valBTC);
}
