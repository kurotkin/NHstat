package com.kurotkin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.kurotkin.controller.BalanceController;
import com.kurotkin.controller.NicehashController;
import com.kurotkin.controller.Rate;
import com.kurotkin.model.NicehashBTC;
import com.kurotkin.model.NicehashRUB;
import com.kurotkin.model.NicehashUSD;
import com.kurotkin.model.Worker;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Network {

    private static String Nicehash;
    private static String NicehashId;
    private static String NicehashKey;
    private static String InfluxDBUrl;
    private static String InfluxDBUser;
    private static String InfluxDBPass;
    private static String InfluxDBdbName;

    public static void main(String[] args) {
        loadSetting();
        while (true){
            Long t1 = System.currentTimeMillis();
            Rate rate = new Rate();
            BalanceController balanceController = new BalanceController(NicehashId, NicehashKey);
            NicehashController nicehashController = new NicehashController(Nicehash, rate, balanceController);
            try {
                InfluxDB influxDB = InfluxDBFactory.connect(InfluxDBUrl, InfluxDBUser, InfluxDBPass);
                influxDB.createDatabase(InfluxDBdbName);
                BatchPoints batchPoints = BatchPoints
                        .database(InfluxDBdbName)
                        //.tag("async", "true")
                        .retentionPolicy("autogen")
                        .consistency(InfluxDB.ConsistencyLevel.ALL)
                        .build();

                Point.Builder builder = Point.measurement("Bitcoin");
                builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

                double speed = nicehashController.getSpeed().doubleValue();
                builder.addField("speed", speed);

                double algo = nicehashController.getAlgo();
                builder.addField("algo", algo);

                // BTC
                NicehashBTC nicehashBTC = nicehashController.getNicehashBTC();
                double profitability_BTC = nicehashBTC.getProfitability().doubleValue();
                builder.addField("profitability_BTC", profitability_BTC);

                double balance_BTC = nicehashBTC.getBalance().doubleValue();
                builder.addField("balance_BTC", balance_BTC);

                double balanceConfirmed_BTC = nicehashBTC.getBalanceConfirmed().doubleValue();
                builder.addField("balanceConfirmed_BTC", balanceConfirmed_BTC);

                double balanceTotal_BTC = nicehashBTC.getBalanceTotal().doubleValue();
                builder.addField("balanceTotal_BTC", balanceTotal_BTC);


                // USD
                NicehashUSD nicehashUSD = nicehashController.getNicehashUSD();
                double profitability_USD = nicehashUSD.getProfitability().doubleValue();
                builder.addField("profitability_USD", profitability_USD);

                double balance_USD = nicehashUSD.getBalance().doubleValue();
                builder.addField("balance_USD", balance_USD);

                double balanceConfirmed_USD = nicehashUSD.getBalanceConfirmed().doubleValue();
                builder.addField("balanceConfirmed_USD", balanceConfirmed_USD);

                double balanceTotal_USD = nicehashUSD.getBalanceTotal().doubleValue();
                builder.addField("balanceTotal_USD", balanceTotal_USD);

                // RUB
                NicehashRUB nicehashRUB = nicehashController.getNicehashRUB();
                double profitability_RUB = nicehashRUB.getProfitability().doubleValue();
                builder.addField("profitability_RUB", profitability_RUB);

                double balance_RUB = nicehashRUB.getBalance().doubleValue();
                builder.addField("balance_RUB", balance_RUB);

                double balanceConfirmed_RUB = nicehashRUB.getBalanceConfirmed().doubleValue();
                builder.addField("balanceConfirmed_RUB", balanceConfirmed_RUB);

                double balanceTotal_RUB = nicehashRUB.getBalanceTotal().doubleValue();
                builder.addField("balanceTotal_RUB", balanceTotal_RUB);

                // Workers BTC
                List<Worker> workerListBTC = nicehashController.getNicehashBTC().getWorkerList();
                workerListBTC.forEach(worker -> {
                    builder.addField("algo_" + worker.getName() + "_profitability_BTC", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_balance_BTC", worker.getBalance().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_speed", worker.getSpeed().doubleValue());
                });

                // Workers USD
                List<Worker> workerListUSD = nicehashController.getNicehashUSD().getWorkerList();
                workerListUSD.forEach(worker -> {
                    builder.addField("algo_" + worker.getName() + "_profitability_USD", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_balance_USD", worker.getBalance().doubleValue());
                });

                // Workers RUB
                List<Worker> workerListRUB = nicehashController.getNicehashRUB().getWorkerList();
                workerListRUB.forEach(worker -> {
                    builder.addField("algo_" + worker.getName() + "_profitability_RUB", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_balance_RUB", worker.getBalance().doubleValue());
                });

                // Price
                builder.addField("price_usd", rate.getPrice_usd().doubleValue());
                builder.addField("price_rub", rate.getPrice_rub().doubleValue());
                builder.addField("percent_change_1h", rate.getPercent_change_1h().doubleValue());
                builder.addField("percent_change_24h", rate.getPercent_change_24h().doubleValue());
                builder.addField("percent_change_7d", rate.getPercent_change_7d().doubleValue());

                batchPoints.point(builder.build());
                influxDB.write(batchPoints);
            }
            catch (Exception e){
                System.out.println("Создание БД слишком долгое: " + e);
            }

            Long t2 = System.currentTimeMillis();
            Long dt = t2 - t1;
            System.out.println("Запись... Время записи " + dt + "ms");

            try {
                Thread.sleep(120000 - dt);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadSetting(){
        try {
            YamlReader reader = new YamlReader(new FileReader("settings.yml"));
            Object object = reader.read();
            Map map = (Map)object;
            Nicehash = map.get("Nicehash").toString();
            NicehashId = map.get("NicehashId").toString();
            NicehashKey = map.get("NicehashKeyOnlyRead").toString();
            InfluxDBUrl = map.get("InfluxDBUrl").toString();
            InfluxDBUser = map.get("InfluxDBUser").toString();
            InfluxDBPass = map.get("InfluxDBPass").toString();
            InfluxDBdbName = map.get("InfluxDBdbName").toString();
            System.out.println("Starting with param: " + Nicehash + " "
                    + InfluxDBUser + ":" + InfluxDBPass + "@" + InfluxDBUrl + "/"
                    + InfluxDBdbName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }
}
