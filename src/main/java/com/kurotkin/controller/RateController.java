package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.coinmarketcap.api.BitcoinRub;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.kurotkin.model.mysql.Rate;
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
    private Rate rate;


    public RateController() {
        try {
            HttpResponse<JsonNode> coinHttpResponse = Unirest.get(apiUrl).queryString("convert", "RUB").asJson();
            String coinResultString = "{result:" + coinHttpResponse.getBody().toString() + "}";
            ResponseBitcoinRub coinResult = new Gson().fromJson(coinResultString, ResponseBitcoinRub.class);

            BitcoinRub bitcoinRub = coinResult.result.get(0);
            rate = new Rate();
            rate.setId(System.currentTimeMillis());
            rate.setPrice_usd( bitcoinRub.price_usd );
            rate.setPrice_rub( bitcoinRub.price_rub );
            rate.setPercent_change_1h( bitcoinRub.percent_change_1h );
            rate.setPercent_change_7d( bitcoinRub.percent_change_24h );
            rate.setPercent_change_24h( bitcoinRub.percent_change_7d );
            rate.setLast_updated(new Date(Long.parseLong(bitcoinRub.last_updated)));


        } catch (UnirestException e) {
            System.err.println("Ошибка скачивания курсов валют: " + e);
        }
    }

    public Long getId() {
        return rate.getId();
    }

    public BigDecimal getPrice_usd() {
        return rate.getPrice_usd();
    }

    public BigDecimal getPercent_change_1h() {
        return rate.getPercent_change_1h();
    }

    public BigDecimal getPercent_change_24h() {
        return rate.getPercent_change_24h();
    }

    public BigDecimal getPercent_change_7d() {
        return rate.getPercent_change_7d();
    }

    public Date getLast_updated() {
        return rate.getLast_updated();
    }

    public BigDecimal getPrice_rub() {
        return rate.getPrice_rub();
    }

    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "RateController{" +
                rate.toString() +
                '}';
    }
}
