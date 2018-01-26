package com.kurotkin.model;

import java.math.BigDecimal;

public class Worker {
    private String name;
    private int algo;
    public BigDecimal profitability;
    public BigDecimal balance;
    private BigDecimal speed;
    private String suffix;

    public Worker(BigDecimal profitability, BigDecimal balance, BigDecimal speed, int algo, String name, String suffix) {
        this.profitability = profitability;
        this.balance = balance;
        this.speed = speed;
        this.algo = algo;
        this.name = name;
        this.suffix = suffix;
    }

    public Worker() {
    }

    public Worker withProfitability(BigDecimal profitability) {
        this.profitability = profitability;
        return this;
    }

    public Worker withBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Worker withSpeed(BigDecimal speed) {
        this.speed = speed;
        return this;
    }

    public Worker withAlgo(int algo) {
        this.algo = algo;
        return this;
    }

    public Worker withName(String name) {
        this.name = name;
        return this;
    }

    public Worker withSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    // Getters
    public BigDecimal getProfitability() {
        return profitability;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public int getAlgo() {
        return algo;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", algo=" + algo +
                ", profitability=" + profitability +
                ", balance=" + balance +
                ", speed=" + speed +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
