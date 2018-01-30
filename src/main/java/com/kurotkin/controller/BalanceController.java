package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.balance.Balance;
import com.mashape.unirest.http.Unirest;

import java.math.BigDecimal;

public class BalanceController {
    private String apiUrl = "https://api.nicehash.com/api";

    private BigDecimal balance_confirmed;

    public BalanceController(String id, String key) {
        String resultString = Unirest.get(apiUrl).queryString("method", "balance")
                .queryString("id", id)
                .queryString("key", key)
                .getBody()
                .toString();
        Balance balance = new Gson().fromJson(resultString, Balance.class);
        balance_confirmed = new BigDecimal(balance.result.balance_confirmed);
    }

    public BigDecimal getBalance() {
        return balance_confirmed;
    }
}
