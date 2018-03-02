package com.kurotkin.balance;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Balance {
    @Id
    private Long id;

    private BigDecimal balanceTotal = new BigDecimal("0.00000000");
    private BigDecimal balance  = new BigDecimal("0.00000000");
    private BigDecimal balanceConfirmed = new BigDecimal("0.00000000");

    public Balance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(BigDecimal balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceConfirmed() {
        return balanceConfirmed;
    }

    public void setBalanceConfirmed(BigDecimal balanceConfirmed) {
        this.balanceConfirmed = balanceConfirmed;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", balanceTotal=" + balanceTotal +
                ", balance=" + balance +
                ", balanceConfirmed=" + balanceConfirmed +
                '}';
    }
}
