package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.entities.Current;
import com.kurotkin.api.entities.ResponseProvider;
import com.kurotkin.api.entities.ResponseProviderWithError;
import com.kurotkin.model.Worker;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Nicehash {
    private String apiUrl = "https://api.nicehash.com/api";
    private String addr;
    private BigDecimal profitability;
    private BigDecimal balance;
    private BigDecimal speed;
    private int algo;
    private String responseStr;
    private Rate rate;
    private List<Worker> workerList;

    public Nicehash(String addr, Rate rate) {
        this.addr = addr;
        this.rate = rate;
        profitability = new BigDecimal("0.00000000");
        balance  = new BigDecimal("0.00000000");
        speed  = new BigDecimal("0.00");
        workerList = new ArrayList<>();

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
        List<Current> currents = rp.result.current;

        BigDecimal price_rub = rate.getPrice_rub();
        for (int i = 0; i < currents.size(); i++) {
            Current c = currents.get(i);
            //Parsing ex.: {"profitability":"0.0004099","data":[{"a":"1.55"},"0.00056466"],"name":"NeoScrypt","suffix":"MH","algo":8}
            // or:         {"profitability":"0.00000787","data":[{},"0.00000364"],"name":"Lyra2REv2","suffix":"MH","algo":14}

            String currentProfitabilityString = String.format(Locale.US,"%.8f", c.profitability);
            String currentProfitabilityString2 = currentProfitabilityString;
            BigDecimal currentProfitability = new BigDecimal(currentProfitabilityString);

            // Parse data
            String dataString = c.data.toString();                          // -> [{"a":"1.55"},"0.00056466"]
            dataString = trimAny(dataString);                               // -> {"a":"1.55"},"0.00056466"
            String aS[] = dataString.split(", ");
            BigDecimal currentBalance = new BigDecimal(aS[1]);

            BigDecimal currentSpeed = new BigDecimal("0.00");
            if (aS[0].length() > 2) {                                       // if worker is in work, NOT "{}"
                String speedString = trimAny(aS[0]);
                speedString = speedString.substring(2, speedString.length());
                currentSpeed = new BigDecimal(speedString);
                speed = speed.add(currentSpeed);
                algo = c.algo;
            }

            // Summation
            profitability = profitability.add(currentProfitability);
            balance = balance.add(currentBalance);

            // Current worker
            Worker worker = new Worker().withAlgo(algo)
                    .withBalance(currentBalance.multiply(price_rub))
                    .withName(c.name)
                    .withProfitability(currentProfitability.multiply(price_rub))
                    .withSpeed(currentSpeed)
                    .withSuffix(c.suffix);
            workerList.add(worker);
        }

        profitability = profitability.multiply(price_rub);
        balance = balance.multiply(price_rub);
        balance.setScale(2, BigDecimal.ROUND_DOWN);
        speed.setScale(2, BigDecimal.ROUND_DOWN);
    }

    private String trimAny(String s) {
        return s.substring(1, s.length() - 1);
    }

    public BigDecimal getProfitability() {
        return profitability;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public int getAlgo() {
        return algo;
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    @Override
    public String toString() {
        return "Nicehash{" +
                "profitability=" + profitability +
                ", balance=" + balance +
                ", speed=" + speed +
                ", algo=" + algo +
                '}';
    }
}
