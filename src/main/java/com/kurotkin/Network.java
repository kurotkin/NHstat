package com.kurotkin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.kurotkin.controller.Nicehash;
import com.kurotkin.controller.Rate;
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
            //System.out.println(rate.toString());
            Nicehash nicehash = new Nicehash(Nicehash, rate);
            //System.out.println(nicehash.toString());
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
                builder.addField("profitability", nicehash.getProfitability().doubleValue());
                builder.addField("balance", nicehash.getBalance().doubleValue());
                builder.addField("speed", nicehash.getSpeed().doubleValue());
                builder.addField("algo", nicehash.getAlgo());

                builder.addField("price_usd", rate.getPrice_usd().doubleValue());
                builder.addField("price_rub", rate.getPrice_rub().doubleValue());
                builder.addField("percent_change_1h", rate.getPercent_change_1h().doubleValue());
                builder.addField("percent_change_24h", rate.getPercent_change_24h().doubleValue());
                builder.addField("percent_change_7d", rate.getPercent_change_7d().doubleValue());

                List<Worker> workerList = nicehash.getWorkerList();
                for (int i = 0; i < workerList.size(); i++){
                    Worker worker = workerList.get(i);
                    builder.addField("algo_" + worker.getName() + "profitability", worker.getProfitability().doubleValue());
                    builder.addField("algo_" + worker.getName() + "balance", worker.getBalance().doubleValue());
                    builder.addField("algo_" + worker.getName() + "speed", worker.getSpeed().doubleValue());
                }
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
            InfluxDBUrl = map.get("InfluxDBUrl").toString();
            InfluxDBUser = map.get("InfluxDBUser").toString();
            InfluxDBPass = map.get("InfluxDBPass").toString();
            InfluxDBdbName = map.get("InfluxDBdbName").toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }
}
