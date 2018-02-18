package com.kurotkin.model;

import com.kurotkin.controller.RateController;

import java.math.BigDecimal;

public class NicehashIntegral {
    public int algoN;
    public BigDecimal speed;
    public NicehashBTC nicehashBTC;
    public NicehashUSD nicehashUSD;
    public NicehashRUB nicehashRUB;

    public NicehashIntegral(RateController rateController) {
        nicehashBTC = new NicehashBTC();
        nicehashUSD = new NicehashUSD(rateController);
        nicehashRUB = new NicehashRUB(rateController);
        algoN = 0;
        speed = new BigDecimal("0.00");
    }
}
