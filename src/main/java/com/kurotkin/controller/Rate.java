package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.coinmarketcap.api.BitcoinRub;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.Date;

public class Rate {
    private String apiUrl = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
    private String name;
    private BigDecimal price_usd;
    private BigDecimal percent_change_1h;
    private BigDecimal percent_change_24h;
    private BigDecimal percent_change_7d;
    private Date last_updated;
    private BigDecimal price_rub;

    public Rate() {
        HttpResponse<JsonNode> coinHttpResponse = null;
        try {
            coinHttpResponse = Unirest.get(apiUrl).queryString("convert", "RUB").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        try {
            String coinResultString = "{result:" + coinHttpResponse.getBody().toString() + "}";
            ResponseBitcoinRub coinResult = new Gson().fromJson(coinResultString, ResponseBitcoinRub.class);

            BitcoinRub bitcoinRub = coinResult.result.get(0);
            name = bitcoinRub.name;
            price_usd = new BigDecimal(bitcoinRub.price_usd);
            percent_change_1h = new BigDecimal(bitcoinRub.percent_change_1h);
            percent_change_24h = new BigDecimal(bitcoinRub.percent_change_24h);
            percent_change_7d = new BigDecimal(bitcoinRub.percent_change_7d);
            price_rub = new BigDecimal(bitcoinRub.price_rub);
            last_updated = new Date(Long.parseLong(bitcoinRub.last_updated));

        } catch (Exception E) {
            E.printStackTrace();
        }

    }
}
