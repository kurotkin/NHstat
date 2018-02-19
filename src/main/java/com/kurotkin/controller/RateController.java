package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.coinmarketcap.api.BitcoinRub;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class RateController {
    private final String apiUrl = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";

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


    public RateController() {
        try {
            HttpResponse<JsonNode> coinHttpResponse = Unirest.get(apiUrl).queryString("convert", "RUB").asJson();
            String coinResultString = "{result:" + coinHttpResponse.getBody().toString() + "}";
            ResponseBitcoinRub coinResult = new Gson().fromJson(coinResultString, ResponseBitcoinRub.class);

            BitcoinRub bitcoinRub = coinResult.result.get(0);
            price_usd = new BigDecimal(bitcoinRub.price_usd);
            price_rub = new BigDecimal(bitcoinRub.price_rub);
            percent_change_1h = new BigDecimal(bitcoinRub.percent_change_1h);
            percent_change_24h = new BigDecimal(bitcoinRub.percent_change_24h);
            percent_change_7d = new BigDecimal(bitcoinRub.percent_change_7d);
            last_updated = new Date(Long.parseLong(bitcoinRub.last_updated));
            id = System.currentTimeMillis();

        } catch (UnirestException e) {
            System.err.println("Ошибка скачивания курсов валют: " + e);
        }
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice_usd() {
        return price_usd;
    }

    public BigDecimal getPercent_change_1h() {
        return percent_change_1h;
    }

    public BigDecimal getPercent_change_24h() {
        return percent_change_24h;
    }

    public BigDecimal getPercent_change_7d() {
        return percent_change_7d;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public BigDecimal getPrice_rub() {
        return price_rub;
    }

    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "RateController{" +
                "apiUrl='" + apiUrl + '\'' +
                ", id=" + id +
                ", price_usd=" + price_usd +
                ", price_rub=" + price_rub +
                ", percent_change_1h=" + percent_change_1h +
                ", percent_change_24h=" + percent_change_24h +
                ", percent_change_7d=" + percent_change_7d +
                ", last_updated=" + last_updated +
                '}';
    }
}
