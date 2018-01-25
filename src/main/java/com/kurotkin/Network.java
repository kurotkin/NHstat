package com.kurotkin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.kurotkin.controller.Nicehash;
import com.kurotkin.controller.Rate;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
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
            Nicehash nicehash = new Nicehash(Nicehash, rate);

            BigDecimal speed = nicehash.getSpeed();
            BigDecimal price_usd;
            BigDecimal percent_change_1h;
            BigDecimal percent_change_24h;
            BigDecimal percent_change_7d;
            BigDecimal price_rub;

            int algo = nicehash.getAlgo();
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
                builder.addField("profitability", nicehash.getProfitability().doubleValue());
                builder.addField("profitability", nicehash.getProfitability().doubleValue());

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

    public static void loadSetting(){
        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader("settings.yml"));
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
