package com.kurotkin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
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
    private static String InfluxDBUrl;
    private static String InfluxDBUser;
    private static String InfluxDBPass;
    private static String InfluxDBdbName;

    public static void main(String[] args) {
        loadSetting();
        while (true){
            Long t1 = System.currentTimeMillis();
            Rate rate = new Rate();
            NicehashController nicehashController = new NicehashController(Nicehash, rate);
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

                NicehashBTC nicehashBTC = nicehashController.getNicehashBTC();
                builder.addField("profitability_BTC", nicehashBTC.getProfitability().doubleValue());
                builder.addField("balance_BTC", nicehashBTC.getBalance().doubleValue());
                builder.addField("speed", nicehashController.getSpeed().doubleValue());
                builder.addField("algo", nicehashController.getAlgo());

                NicehashUSD nicehashUSD = nicehashController.getNicehashUSD();
                builder.addField("profitability_USD", nicehashUSD.getProfitability().doubleValue());
                builder.addField("balance_USD", nicehashUSD.getBalance().doubleValue());

                NicehashRUB nicehashRUB = nicehashController.getNicehashRUB();
                builder.addField("profitability_RUB", nicehashRUB.getProfitability().doubleValue());
                builder.addField("balance_RUB", nicehashRUB.getBalance().doubleValue());

                List<Worker> workerListBTC = nicehashController.getNicehashBTC().getWorkerList();
                for (int i = 0; i < workerListBTC.size(); i++){
                    Worker worker = workerListBTC.get(i);
                    builder.addField("algo_" + worker.getName() + "_profitability_BTC", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_balance_BTC", worker.getBalance().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_speed", worker.getSpeed().doubleValue());
                }

                List<Worker> workerListUSD = nicehashController.getNicehashUSD().getWorkerList();
                for (int i = 0; i < workerListUSD.size(); i++){
                    Worker worker = workerListUSD.get(i);
                    builder.addField("algo_" + worker.getName() + "_profitability_USD", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_balance_USD", worker.getBalance().doubleValue());
                }

                List<Worker> workerListRUB = nicehashController.getNicehashRUB().getWorkerList();
                for (int i = 0; i < workerListRUB.size(); i++){
                    Worker worker = workerListRUB.get(i);
                    builder.addField("algo_" + worker.getName() + "_profitability_RUB", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "_balance_RUB", worker.getBalance().doubleValue());
                }

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
            Nicehash = map.get("NicehashController").toString();
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
