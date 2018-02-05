package com.kurotkin.model;

import java.math.BigDecimal;

public class NicehashSimplemultialgo {
    private BigDecimal paying;
    private int port;
    private String name;
    private int algo;

    public NicehashSimplemultialgo(BigDecimal paying, int port, String name, int algo) {
        this.paying = paying;
        this.port = port;
        this.name = name;
        this.algo = algo;
    }

    public BigDecimal getPaying() {
        return paying;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public int getAlgo() {
        return algo;
    }
}
