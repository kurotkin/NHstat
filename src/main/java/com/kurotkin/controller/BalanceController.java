package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.balance.Balance;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;

public class BalanceController {
    private BigDecimal balance_confirmed;

    public BalanceController(String id, String key) {
        try{
            String resultString = Unirest.get("https://api.nicehash.com/api")
                    .queryString("method", "balance")
                    .queryString("id", id)
                    .queryString("key", key)
                    .asJson()
                    .getBody()
                    .toString();
            Balance balance = new Gson().fromJson(resultString, Balance.class);
            balance_confirmed = new BigDecimal(balance.result.balance_confirmed);
        } catch(NullPointerException E){
            System.err.println("Баланс Null");
        } catch (UnirestException e) {
            System.err.println("Полный баланс не скачался");
        }

    }

    public BigDecimal getBalance() {
        return balance_confirmed;
    }
}
