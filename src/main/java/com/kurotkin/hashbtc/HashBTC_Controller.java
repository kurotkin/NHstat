package com.kurotkin.hashbtc;

import com.google.gson.Gson;
import com.kurotkin.algoprof.ScheduledTasks;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.Current;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.DataString;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.ResponseProvider;
import com.kurotkin.api.com.nicehash.api.stats.provider.ex.ResponseProviderWithError;
import com.kurotkin.model.Worker;
import com.kurotkin.utils.SettingsLoader;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class HashBTC_Controller {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private HashBTC hashBTC = new HashBTC();
    private String responseStr;

    public HashBTC_Controller() {
        String addr = new SettingsLoader("settings.yml").getNicehash();
        try {
            responseStr = Unirest.get("https://api.nicehash.com/api")
                    .queryString("method", "stats.provider.ex")
                    .queryString("addr", addr)
                    .asJson()
                    .getBody()
                    .toString();
        } catch (UnirestException e) {
            log.error("Интегральные параметры не скачались");
        }
        hashBTC.setId(System.currentTimeMillis());
        try {
            query();
        } catch (Exception E) {
            log.error("API занят");
            queryWithError();
        }
    }

    public HashBTC getHashBTC() {
        return hashBTC;
    }

    private void query(){
        ResponseProvider rp = new Gson().fromJson(responseStr, ResponseProvider.class);
        List<Current> currents = rp.result.current;
        currents.forEach(c -> {
            // Parse data
            String dataString = c.data.toString();
            dataString = dataString.substring(1, dataString.length() - 1);
            String aS[] = dataString.split(", ");

            // Parse balance
            BigDecimal currentBalance = new BigDecimal(aS[1]);
            hashBTC.addProfitability(currentBalance);   // TODO добавить порверку на null

            // Parse speed
            BigDecimal currentSpeed = calcSpeed(aS[0], c.suffix);
            hashBTC.addSpeed(currentSpeed);

            // Parse profitability
            String currentProfitabilityString = String.format(Locale.US,"%.8f", c.profitability);
            BigDecimal currentProfitability;
            if(currentSpeed.equals(new BigDecimal("0.00"))){
                currentProfitability = new BigDecimal("0.00");
            }else{
                currentProfitability = new BigDecimal(currentProfitabilityString);
            }
            hashBTC.addProfitability(currentProfitability);
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
            System.err.println("Ошибка Nicehash: " + Er);
        }
    }
}
