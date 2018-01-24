package com.kurotkin;

import com.google.gson.Gson;
import com.kurotkin.controller.Nicehash;
import com.kurotkin.controller.Rate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Map;

public class Network {
    private static String Nicehash;
    private static String InfluxDBUrl;
    private static String InfluxDBUser;
    private static String InfluxDBPass;
    private static String InfluxDBdbName;
    public static void main(String[] args) {


        while (true){
            Long t1 = System.currentTimeMillis();

            String HTTPgetterUrl = Settings.getHTTPgetterUrl();
            HTTPgetter getJson = new HTTPgetter(HTTPgetterUrl);
            String jsonLine = getJson.get();

            Gson gson = new Gson();
            Device[] data = gson.fromJson(jsonLine, Device[].class);

            Influxdb influxdb = new Influxdb();
            influxdb.insert(data);

            Long t2 = System.currentTimeMillis();
            Long dt = t2 - t1;

            System.out.println("Запись... Время записи " + dt + "ms");

            try {
                Thread.sleep(50000 - dt);
            }
            catch (IllegalArgumentException e){
                System.out.println("Очень долго: " + e);
            }

        }


        while (true){
            Rate rate = new Rate();
            Nicehash nicehash = new Nicehash("3MocyP1djGcdvyg693nMhsQtNo3AL7Uve1", rate);

            BigDecimal profitability = nicehash.getProfitability();
            BigDecimal balance = nicehash.getBalance();
            BigDecimal speed = nicehash.getSpeed();
            int algo = nicehash.getAlgo();

            String toArduino = createStringToSerial(profitability, balance, speed, algo);
            writeBytes(toArduino);
            System.out.println(toArduino);
            Thread.sleep(60000);
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
