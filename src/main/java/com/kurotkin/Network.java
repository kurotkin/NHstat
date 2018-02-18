package com.kurotkin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.kurotkin.controller.AlgoProfitabilityController;
import com.kurotkin.controller.BalanceController;
import com.kurotkin.controller.NicehashController;
import com.kurotkin.controller.RateController;
import com.kurotkin.dao.influxdb.INicehashIntegralDAO;
import com.kurotkin.dao.influxdb.IPriceDAO;
import com.kurotkin.dao.influxdb.ISimplemultialgoDAO;
import com.kurotkin.dao.influxdb.IWorkerDAO;
import com.kurotkin.model.*;
import com.kurotkin.utils.SettingsLoader;
import com.kurotkin.utils.TimeDelay;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class Network {


    public static void main(String[] args) {
        SettingsLoader ss = new SettingsLoader("settings.yml");
        System.out.println(ss.toString());

        while (true){
            TimeDelay td = new TimeDelay(120000L);

            RateController rateController = new RateController();
            BalanceController balanceController = new BalanceController(ss.getNicehashId(), ss.getNicehashKey());
            NicehashController nicehashController = new NicehashController(ss.getNicehash(), rateController, balanceController);
            AlgoProfitabilityController algoProfitabilityController = new AlgoProfitabilityController();

            // Price
            IPriceDAO iPriceDAO = new IPriceDAO(ss.getInflParam());
            iPriceDAO.save(rateController);

            // Prof. algo
            ISimplemultialgoDAO iSimplemultialgoDAO = new ISimplemultialgoDAO(ss.getInflParam());
            iSimplemultialgoDAO.saveAll(algoProfitabilityController.getProfAlgoList());

            // Integral Params
            INicehashIntegralDAO iNicehashIntegralDAO = new INicehashIntegralDAO(ss.getInflParam());
            iNicehashIntegralDAO.save(nicehashController.getNicehashIntegral());

            // Workers BTC
            List<Worker> workerListBTC = nicehashController.getNicehashBTC().getWorkerList();
            IWorkerDAO iWorkerDAO_BTC = new IWorkerDAO(ss.getInflParam(), "BTC");
            iWorkerDAO_BTC.saveAll(workerListBTC);

            // Workers USD
            List<Worker> workerListUSD = nicehashController.getNicehashUSD().getWorkerList();
            IWorkerDAO iWorkerDAO_USD = new IWorkerDAO(ss.getInflParam(), "USD");
            iWorkerDAO_USD.saveAll(workerListUSD);

            // Workers RUB
            List<Worker> workerListRUB = nicehashController.getNicehashRUB().getWorkerList();
            IWorkerDAO iWorkerDAO_RUB = new IWorkerDAO(ss.getInflParam(), "RUB");
            iWorkerDAO_RUB.saveAll(workerListRUB);

            td.getTime();
        }
    }

}
