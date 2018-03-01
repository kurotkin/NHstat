package com.kurotkin.hashbtc;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class HashBTC {
    @Id
    private Long id;

    private BigDecimal profitability = new BigDecimal("0.00");
    private BigDecimal balance  = new BigDecimal("0.00");
    private BigDecimal speed  = new BigDecimal("0.00");

    public void addProfitability(BigDecimal profitability){
        this.profitability = this.profitability.add(profitability);
    }

    public void addBalance(BigDecimal balance){
        this.balance = this.balance.add(balance);
    }

    public void addSpeed(BigDecimal speed){
        this.speed = this.speed.add(speed);
    }

    public HashBTC() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getProfitability() {
        return profitability;
    }

    public void setProfitability(BigDecimal profitability) {
        this.profitability = profitability;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "HashBTC{" +
                "id=" + id +
                ", profitability=" + profitability +
                ", balance=" + balance +
                ", speed=" + speed +
                '}';
    }
}
