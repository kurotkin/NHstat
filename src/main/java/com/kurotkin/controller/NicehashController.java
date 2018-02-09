package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.Current;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.DataString;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.ResponseProvider;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.ResponseProviderWithError;
import com.kurotkin.model.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class NicehashController {
    private String addr;
    private String responseStr;
    private NicehashIntegral nicehashIntegral;

    public NicehashController(String addr, Rate rate, BalanceController balanceController) {
        this.addr = addr;
        nicehashIntegral = new NicehashIntegral(rate);

        //Confirmed balance
        nicehashIntegral.nicehashBTC.addBalanceConfirmed(balanceController.getBalance());
        nicehashIntegral.nicehashUSD.addBalanceConfirmed(balanceController.getBalance());
        nicehashIntegral.nicehashRUB.addBalanceConfirmed(balanceController.getBalance());

        try {
           query();
        } catch (Exception E) {
            queryWithError();
        }
    }


    private void query() throws UnirestException {
        responseStr = Unirest.get("https://api.nicehash.com/api")
                .queryString("method", "stats.provider.ex")
                .queryString("addr", addr)
                .asJson()
                .getBody()
                .toString();
        ResponseProvider rp = new Gson().fromJson(responseStr, ResponseProvider.class);

        List<Current> currents = rp.result.current;
        currents.forEach(c -> {
            // Parse data
            String dataString = c.data.toString();
            dataString = trimAny(dataString);
            String aS[] = dataString.split(", ");

            // Parse balance
            BigDecimal currentBalance = new BigDecimal(aS[1]);
            nicehashIntegral.nicehashBTC.addBalance(currentBalance);
            nicehashIntegral.nicehashUSD.addBalance(currentBalance);
            nicehashIntegral.nicehashRUB.addBalance(currentBalance);

            // Parse speed
            BigDecimal currentSpeed = calcSpeed(aS[0], c.suffix);
            nicehashIntegral.speed = nicehashIntegral.speed.add(currentSpeed);
            nicehashIntegral.nicehashBTC.addSpeed(currentSpeed);
            nicehashIntegral.nicehashUSD.addSpeed(currentSpeed);
            nicehashIntegral.nicehashRUB.addSpeed(currentSpeed);

            // Parse profitability
            String currentProfitabilityString = String.format(Locale.US,"%.8f", c.profitability);
            BigDecimal currentProfitability;
            if(currentSpeed.equals(new BigDecimal("0.00"))){
                currentProfitability = new BigDecimal("0.00");
            }else{
                currentProfitability = new BigDecimal(currentProfitabilityString);
            }
            nicehashIntegral.nicehashBTC.addProfitability(currentProfitability);
            nicehashIntegral.nicehashUSD.addProfitability(currentProfitability);
            nicehashIntegral.nicehashRUB.addProfitability(currentProfitability);

            // Current worker
            Worker worker = new Worker().withName(c.name)
                    .withAlgo(c.algo)
                    .withBalance(currentBalance)
                    .withProfitability(currentProfitability)
                    .withSpeed(currentSpeed)
                    .withSuffix(c.suffix);

            nicehashIntegral.nicehashBTC.addWorkers(worker);
            nicehashIntegral.nicehashUSD.addWorkers(worker);
            nicehashIntegral.nicehashRUB.addWorkers(worker);
        });
    }

    private BigDecimal calcSpeed(String str, String suffix){
        BigDecimal currentSpeed = new BigDecimal("0.00");
        if (!str.equals("{}")) {                                       // if worker is in work, NOT "{}"
            double currentSpeedDouble = new Gson().fromJson(str, DataString.class).a;
            String currentSpeedString = String.format(Locale.US,"%.2f", currentSpeedDouble);
            if(suffix.equals("H")){
                BigDecimal currentSpeedInH = new BigDecimal(currentSpeedString);
                currentSpeed = currentSpeedInH.multiply(new BigDecimal("0.000001"));
            }
            if(suffix.equals("kH")){
                BigDecimal currentSpeedInkH = new BigDecimal(currentSpeedString);
                currentSpeed = currentSpeedInkH.multiply(new BigDecimal("0.001"));
            }
            if(suffix.equals("MH")){
                currentSpeed = new BigDecimal(currentSpeedString);
            }
            if(suffix.equals("GH")){
                BigDecimal currentSpeedInMH = new BigDecimal(currentSpeedString);
                currentSpeed = currentSpeedInMH.multiply(new BigDecimal("1000"));
            }
        }
        return currentSpeed;
    }

    private void queryWithError(){
        try {
            ResponseProviderWithError result = new Gson().fromJson(responseStr, ResponseProviderWithError.class);
            String aS[] = result.result.error.split(" ");
            int timeDelay = Integer.parseInt(aS[13]);
            System.out.println("Delay " + timeDelay + " sec");
            Thread.sleep(timeDelay * 1000);
            query();
        } catch (Exception Er) {
            System.err.println(responseStr);
            Er.printStackTrace();
        }
    }

    private String trimAny(String s) {
        return s.substring(1, s.length() - 1);
    }


    public BigDecimal getSpeed() {
        return nicehashIntegral.speed;
    }

    public NicehashBTC getNicehashBTC() {
        return nicehashIntegral.nicehashBTC;
    }

    public NicehashUSD getNicehashUSD() {
        return nicehashIntegral.nicehashUSD;
    }

    public NicehashRUB getNicehashRUB() {
        return nicehashIntegral.nicehashRUB;
    }

    public NicehashIntegral getNicehashIntegral() {
        return nicehashIntegral;
    }

}
