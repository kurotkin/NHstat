package com.kurotkin.balance;

import com.google.gson.Gson;
import com.kurotkin.utils.SettingsLoader;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Slf4j
public class BalanceController {
    private Balance balance;
    private String responseStr;

    public BalanceController() {
        SettingsLoader settingsLoader = new SettingsLoader("settings.yml");
        try {
            responseStr = Unirest.get("https://api.nicehash.com/api")
                    .queryString("method", "balance")
                    .queryString("id", settingsLoader.getNicehashId())
                    .queryString("key", settingsLoader.getNicehashKey())
                    .asJson()
                    .getBody()
                    .toString();
        } catch (UnirestException e) {
            log.error("Полный баланс не скачался");
        }

        balance = new Balance();
        balance.setId(System.currentTimeMillis());
        try {
            com.kurotkin.api.com.nicehash.api.balance.Balance balanceResponse = new Gson()
                    .fromJson(responseStr, com.kurotkin.api.com.nicehash.api.balance.Balance.class);
            BigDecimal balance_confirmed = new BigDecimal(balanceResponse.result.balance_confirmed);
            balance.setBalanceConfirmed(balance_confirmed);
            // TODO balance





        } catch (Exception E) {
            log.error("Полный баланс не прочитался");
        }
    }

    public Balance getBalance() {
        return balance;
    }
}
