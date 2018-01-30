package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.Current;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.DataString;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.ResponseProvider;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.ResponseProviderWithError;
import com.kurotkin.model.NicehashBTC;
import com.kurotkin.model.NicehashRUB;
import com.kurotkin.model.NicehashUSD;
import com.kurotkin.model.Worker;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class NicehashController {
    private String apiUrl = "https://api.nicehash.com/api";
    private String addr;
    private String NicehashId;
    private String NicehashKey;
    private String responseStr;
    private int algo;
    private String algoName;
    private BigDecimal speed;
    private NicehashBTC nicehashBTC;
    private NicehashUSD nicehashUSD;
    private NicehashRUB nicehashRUB;

    public NicehashController(String addr, Rate rate, String NicehashId, String NicehashKey) {
        this.addr = addr;
        this.NicehashId = NicehashId;
        this.NicehashKey = NicehashKey;
        nicehashBTC = new NicehashBTC();
        nicehashUSD = new NicehashUSD(rate);
        nicehashRUB = new NicehashRUB(rate);
        speed = new BigDecimal("0.00");
        try {
           query();
        } catch (Exception E) {
            try {
                ResponseProviderWithError result = new Gson().fromJson(responseStr, ResponseProviderWithError.class);
                String aS[] = result.result.error.split(" ");
                int timeDelay = Integer.parseInt(aS[13]);
                System.out.println("Delay " + timeDelay + " sec");
                Thread.sleep(timeDelay * 1000);
                query();
            } catch (Exception Er) {
                System.err.println(responseStr);
                E.printStackTrace();
                Er.printStackTrace();
            }
        }
    }

    private void query() throws UnirestException {
        HttpResponse<JsonNode> f = Unirest.get(apiUrl).queryString("method", "stats.provider.ex")
                .queryString("addr", addr)
                .asJson();
        responseStr = f.getBody().toString();
        ResponseProvider rp = new Gson().fromJson(responseStr, ResponseProvider.class);
        BalanceController balanceController = new BalanceController(NicehashId, NicehashKey);

        List<Current> currents = rp.result.current;
        currents.stream().forEach(c -> {
            // Parse profitability
            String currentProfitabilityString = String.format(Locale.US,"%.8f", c.profitability);
            BigDecimal currentProfitability = new BigDecimal(currentProfitabilityString);
            nicehashBTC.addProfitability(currentProfitability);
            nicehashUSD.addProfitability(currentProfitability);
            nicehashRUB.addProfitability(currentProfitability);

            // Parse data
            String dataString = c.data.toString();
            dataString = trimAny(dataString);
            String aS[] = dataString.split(", ");

            // Parse balance
            BigDecimal currentBalance = new BigDecimal(aS[1]);
            nicehashBTC.addBalance(currentBalance);
            nicehashUSD.addBalance(currentBalance);
            nicehashRUB.addBalance(currentBalance);

            //  Parse confirmed balance
            nicehashBTC.addBalanceConfirmed(balanceController.getBalance());
            nicehashUSD.addBalanceConfirmed(balanceController.getBalance());
            nicehashRUB.addBalanceConfirmed(balanceController.getBalance());

            // Parse speed
            BigDecimal currentSpeed = new BigDecimal("0.00");
            if (!aS[0].equals("{}")) {                                       // if worker is in work, NOT "{}"
                double currentSpeedDouble = new Gson().fromJson(aS[0], DataString.class).a;
                String currentSpeedString = String.format(Locale.US,"%.2f", currentSpeedDouble);
                if(c.suffix.equals("H")){
                    BigDecimal currentSpeedInH = new BigDecimal(currentSpeedString);
                    currentSpeed = currentSpeedInH.multiply(new BigDecimal("0.000001"));
                }
                if(c.suffix.equals("kH")){
                    BigDecimal currentSpeedInkH = new BigDecimal(currentSpeedString);
                    currentSpeed = currentSpeedInkH.multiply(new BigDecimal("0.001"));
                }
                if(c.suffix.equals("MH")){
                    currentSpeed = new BigDecimal(currentSpeedString);
                }
                if(c.suffix.equals("GH")){
                    BigDecimal currentSpeedInMH = new BigDecimal(currentSpeedString);
                    currentSpeed = currentSpeedInMH.multiply(new BigDecimal("1000"));
                }
                speed = speed.add(currentSpeed);
                algo = c.algo;
                algoName = c.name;
            }
            nicehashBTC.addSpeed(currentSpeed);
            nicehashUSD.addSpeed(currentSpeed);
            nicehashRUB.addSpeed(currentSpeed);

            // Current worker
            Worker worker = new Worker().withName(c.name)
                    .withAlgo(c.algo)
                    .withBalance(currentBalance)
                    .withProfitability(currentProfitability)
                    .withSpeed(currentSpeed)
                    .withSuffix(c.suffix);

            nicehashBTC.addWorkers(worker);
            nicehashUSD.addWorkers(worker);
            nicehashRUB.addWorkers(worker);
        });
    }

    private String trimAny(String s) {
        return s.substring(1, s.length() - 1);
    }

    public int getAlgo() {
        return algo;
    }

    public String getAlgoName() {
        return algoName;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public NicehashBTC getNicehashBTC() {
        return nicehashBTC;
    }

    public NicehashUSD getNicehashUSD() {
        return nicehashUSD;
    }

    public NicehashRUB getNicehashRUB() {
        return nicehashRUB;
    }
}
