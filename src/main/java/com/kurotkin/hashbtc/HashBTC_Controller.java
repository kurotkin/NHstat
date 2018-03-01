package com.kurotkin.hashbtc;

import com.google.gson.Gson;
import com.kurotkin.algoprof.ScheduledTasks;
import com.kurotkin.api.com.coinmarketcap.api.BitcoinRub;
import com.kurotkin.api.com.coinmarketcap.api.ResponseBitcoinRub;
import com.kurotkin.rate.Rate;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

public class HashBTC_Controller {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private HashBTC hashBTC = new HashBTC();

    public HashBTC_Controller() {
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

    public HashBTC getHashBTC() {
        return hashBTC;
    }
}
