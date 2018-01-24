package com.kurotkin.controller;

import com.google.gson.Gson;
import com.kurotkin.api.entities.Current;
import com.kurotkin.api.entities.ResponseProvider;
import com.kurotkin.api.entities.ResponseProviderWithError;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.math.BigDecimal;
import java.util.List;

public class Nicehash {
    private String apiUrl = "https://api.nicehash.com/api";
    private String addr;
    private BigDecimal profitability;
    private BigDecimal balance;
    private BigDecimal speed;
    private int algo;
    private String responseStr;

    public Nicehash(String addr) {
        this.addr = addr;
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

        profitability = new BigDecimal("0.0");
        balance = new BigDecimal("0.0");
        speed = new BigDecimal("0.0");
        for (int i = 0; i < currents.size(); i++) {
            Current c = currents.get(i);
            //Parsing ex.: {"profitability":"0.0004099","data":[{"a":"1.55"},"0.00056466"],"name":"NeoScrypt","suffix":"MH","algo":8}
            // or:         {"profitability":"0.00000787","data":[{},"0.00000364"],"name":"Lyra2REv2","suffix":"MH","algo":14}

            profitability.add(new BigDecimal(c.profitability));

            String dataString = c.data.toString();                          // -> [{"a":"1.55"},"0.00056466"]
            dataString = dataString.substring(1, dataString.length() - 1);  // -> {"a":"1.55"},"0.00056466"
            String aS[] = dataString.split(",");

            if (aS[0].length() > 2) {                                       //if worker is in work, NOT {}
                String speedString = aS[0].substring(4, aS[0].length() - 1);
                speed.add(new BigDecimal(speedString));
                algo = c.algo;
            }
            balance.add(new BigDecimal(aS[1]));
        }
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
}
