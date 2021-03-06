package com.kurotkin.rate;

import com.google.gson.Gson;
import com.kurotkin.api.com.coinmarketcap.api.BitcoinRub;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class RateController {
    private Rate rate = new Rate();

    public RateController() {
        try {
            HttpResponse<JsonNode> coinHttpResponse = Unirest
                    .get("https://api.coinmarketcap.com/v1/ticker/bitcoin/")
                    .queryString("convert", "RUB")
                    .asJson();
            String coinResultString = "{result:" + coinHttpResponse.getBody().toString() + "}";
            ResponseBitcoinRub coinResult = new Gson().fromJson(coinResultString, ResponseBitcoinRub.class);
            BitcoinRub bitcoinRub = coinResult.result.get(0);
            rate.setId(System.currentTimeMillis());
            rate.setPrice_usd( new BigDecimal(bitcoinRub.price_usd) );
            rate.setPrice_rub( new BigDecimal(bitcoinRub.price_rub) );
            rate.setPercent_change_1h( new BigDecimal(bitcoinRub.percent_change_1h) );
            rate.setPercent_change_7d( new BigDecimal(bitcoinRub.percent_change_24h) );
            rate.setPercent_change_24h( new BigDecimal(bitcoinRub.percent_change_7d) );
            rate.setLast_updated(new Date(Long.parseLong(bitcoinRub.last_updated)));
        } catch (UnirestException e) {
            log.error("Прибыльность алгоритмов не скачалось");
        }
    }

    public Rate getRate() {
        return rate;
    }
}
