package com.kurotkin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.kurotkin.controller.AlgoProfitabilityController;
import com.kurotkin.controller.BalanceController;
import com.kurotkin.controller.NicehashController;
import com.kurotkin.controller.Rate;
import com.kurotkin.dao.influxdb.INicehashIntegralDAO;
import com.kurotkin.dao.influxdb.IPriceDAO;
import com.kurotkin.dao.influxdb.ISimplemultialgoDAO;
import com.kurotkin.model.*;
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
    private static InfluxDBParam inflParam;

    public static void main(String[] args) {
        loadSetting();
        while (true){
            Long t1 = System.currentTimeMillis();
            Rate rate = new Rate();
            BalanceController balanceController = new BalanceController(NicehashId, NicehashKey);
            NicehashController nicehashController = new NicehashController(Nicehash, rate, balanceController);
            AlgoProfitabilityController algoProfitabilityController = new AlgoProfitabilityController();

            // Price
            IPriceDAO iPriceDAO = new IPriceDAO(inflParam);
            iPriceDAO.save(rate);

            // Prof. algo
            ISimplemultialgoDAO iSimplemultialgoDAO = new ISimplemultialgoDAO(inflParam);
            iSimplemultialgoDAO.saveAll(algoProfitabilityController.getProfAlgoList());

            // Integral Params
            INicehashIntegralDAO iNicehashIntegralDAO = new INicehashIntegralDAO(inflParam);
            iNicehashIntegralDAO.save(nicehashController.getNicehashIntegral());

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
            inflParam = new InfluxDBParam(InfluxDBUrl, InfluxDBdbName, InfluxDBUser, InfluxDBPass);
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
