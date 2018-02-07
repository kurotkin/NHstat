package com.kurotkin.model;

import com.kurotkin.controller.Rate;

import java.math.BigDecimal;

public class NicehashIntegral {
    public int algoN;
    public BigDecimal speed;
    public NicehashBTC nicehashBTC;
    public NicehashUSD nicehashUSD;
    public NicehashRUB nicehashRUB;

    public NicehashIntegral(Rate rate) {
        nicehashBTC = new NicehashBTC();
        nicehashUSD = new NicehashUSD(rate);
        nicehashRUB = new NicehashRUB(rate);
        algoN = 0;
        speed = new BigDecimal("0.00");
    }
}
