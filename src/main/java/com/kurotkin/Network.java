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
import com.kurotkin.dao.influxdb.IWorkerDAO;
import com.kurotkin.model.*;
import com.kurotkin.utils.TimeDelay;
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

    private static InfluxDBParam inflParam;

    public static void main(String[] args) {
        loadSetting();
        while (true){
            TimeDelay td = new TimeDelay(120000L);

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

            // Workers BTC
            List<Worker> workerListBTC = nicehashController.getNicehashBTC().getWorkerList();
            IWorkerDAO iWorkerDAO_BTC = new IWorkerDAO(inflParam, "BTC");
            iWorkerDAO_BTC.saveAll(workerListBTC);

            // Workers USD
            List<Worker> workerListUSD = nicehashController.getNicehashUSD().getWorkerList();
            IWorkerDAO iWorkerDAO_USD = new IWorkerDAO(inflParam, "USD");
            iWorkerDAO_USD.saveAll(workerListUSD);

            // Workers RUB
            List<Worker> workerListRUB = nicehashController.getNicehashRUB().getWorkerList();
            IWorkerDAO iWorkerDAO_RUB = new IWorkerDAO(inflParam, "RUB");
            iWorkerDAO_RUB.saveAll(workerListRUB);

            td.getTime();
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
            inflParam = new InfluxDBParam().withUrl(map.get("InfluxDBUrl").toString())
                    .withDBName(map.get("InfluxDBdbName").toString())
                    .withDBUser(map.get("InfluxDBUser").toString())
                    .withDBPass(map.get("InfluxDBPass").toString());
            System.out.println("Starting with param: " + Nicehash );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }
}
