package com.kurotkin.model.mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "rate")
public class Rate {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "price_usd")
    private BigDecimal price_usd;

    @Column(name = "price_rub")
    private BigDecimal price_rub;

    @Column(name = "percent_change_1h")
    private BigDecimal percent_change_1h;

    @Column(name = "percent_change_24h")
    private BigDecimal percent_change_24h;

    @Column(name = "percent_change_7d")
    private BigDecimal percent_change_7d;

    @Column(name = "last_updated")
    private Date last_updated;

    public Rate() {
    }

    public Rate(Long id, BigDecimal price_usd, BigDecimal price_rub,
                BigDecimal percent_change_1h, BigDecimal percent_change_24h,
                BigDecimal percent_change_7d, Date last_updated) {
        this.id = id;
        this.price_usd = price_usd;
        this.price_rub = price_rub;
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        this.percent_change_7d = percent_change_7d;
        this.last_updated = last_updated;
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

    public void setPrice_usd(String price_usd) {
        this.price_usd = new BigDecimal(price_usd);
    }

    public BigDecimal getPrice_rub() {
        return price_rub;
    }

    public void setPrice_rub(String price_rub) {
        this.price_rub = new BigDecimal(price_rub);
    }

    public BigDecimal getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(String percent_change_1h) {
        this.percent_change_1h = new BigDecimal(percent_change_1h);
    }

    public BigDecimal getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(String percent_change_24h) {
        this.percent_change_24h = new BigDecimal(percent_change_24h);
    }

    public BigDecimal getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(String percent_change_7d) {
        this.percent_change_7d = new BigDecimal(percent_change_7d);
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
