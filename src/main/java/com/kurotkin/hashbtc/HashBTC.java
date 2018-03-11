package com.kurotkin.hashbtc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HashBTC {
    @Id
    private Long id;

    private BigDecimal profitability = new BigDecimal("0.00");
    private BigDecimal profitabilityRUB = new BigDecimal("0.00");
    private BigDecimal profitabilityUSD = new BigDecimal("0.00");
    private BigDecimal balance  = new BigDecimal("0.00");
    private BigDecimal speed  = new BigDecimal("0.00");

    public void addProfitability(BigDecimal profitability){
        this.profitability = this.profitability.add(profitability);
    }

    public void addProfitabilityRUB(BigDecimal profitability){
        this.profitabilityRUB = this.profitabilityRUB.add(profitability);
    }

    public void addProfitabilityUSD(BigDecimal profitability){
        this.profitabilityUSD = this.profitabilityUSD.add(profitability);
    }

    public void addBalance(BigDecimal balance){
        this.balance = this.balance.add(balance);
    }

    public void addSpeed(BigDecimal speed){
        this.speed = this.speed.add(speed);
    }

}
