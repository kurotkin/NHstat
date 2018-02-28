package com.kurotkin.rate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Rate {
    @Id
    private Long id;

    private BigDecimal price_usd;
    private BigDecimal price_rub;
    private BigDecimal percent_change_1h;
    private BigDecimal percent_change_24h;
    private BigDecimal percent_change_7d;
    private Date last_updated;

    public Rate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(BigDecimal price_usd) {
        this.price_usd = price_usd;
    }

    public BigDecimal getPrice_rub() {
        return price_rub;
    }

    public void setPrice_rub(BigDecimal price_rub) {
        this.price_rub = price_rub;
    }

    public BigDecimal getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(BigDecimal percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public BigDecimal getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(BigDecimal percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public BigDecimal getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(BigDecimal percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", price_usd=" + price_usd +
                ", price_rub=" + price_rub +
                ", percent_change_1h=" + percent_change_1h +
                ", percent_change_24h=" + percent_change_24h +
                ", percent_change_7d=" + percent_change_7d +
                ", last_updated=" + last_updated +
                '}';
    }
}
