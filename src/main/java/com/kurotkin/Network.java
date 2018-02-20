package com.kurotkin;

import com.kurotkin.controller.AlgoProfitabilityController;
import com.kurotkin.controller.BalanceController;
import com.kurotkin.controller.NicehashController;
import com.kurotkin.controller.RateController;
import com.kurotkin.dao.influxdb.INicehashIntegralDAO;
import com.kurotkin.dao.influxdb.IPriceDAO;
import com.kurotkin.dao.influxdb.IProfitabilityAlgorithmsDAO;
import com.kurotkin.dao.influxdb.IWorkerDAO;
import com.kurotkin.dao.mysql.HPriceDAO;
import com.kurotkin.model.*;
import com.kurotkin.utils.SettingsLoader;
import com.kurotkin.utils.TimeDelay;

import java.util.List;

public class Network {


    public static void main(String[] args) {
        SettingsLoader ss = new SettingsLoader("settings.yml");
        System.out.println(ss.toString());

        // DAOs
        IPriceDAO iPriceDAO = new IPriceDAO(ss.getInflParam());
        IProfitabilityAlgorithmsDAO iProfitabilityAlgorithmsDAO = new IProfitabilityAlgorithmsDAO(ss.getInflParam());
        INicehashIntegralDAO iNicehashIntegralDAO = new INicehashIntegralDAO(ss.getInflParam());
        IWorkerDAO iWorkerDAO_BTC = new IWorkerDAO(ss.getInflParam(), "BTC");
        IWorkerDAO iWorkerDAO_USD = new IWorkerDAO(ss.getInflParam(), "USD");
        IWorkerDAO iWorkerDAO_RUB = new IWorkerDAO(ss.getInflParam(), "RUB");

        HPriceDAO hPriceDAO = new HPriceDAO();

        while (true){
            TimeDelay td = new TimeDelay(120000L);

            RateController rateController = new RateController();
            BalanceController balanceController = new BalanceController(ss.getNicehashId(), ss.getNicehashKey());
            NicehashController nicehashController = new NicehashController(ss.getNicehash(), rateController, balanceController);
            AlgoProfitabilityController algoProfitabilityController = new AlgoProfitabilityController();

            // Price
            iPriceDAO.save(rateController.getRate());
            hPriceDAO.save(rateController.getRate());

            // Prof. algo
            iProfitabilityAlgorithmsDAO.saveAll(algoProfitabilityController.getProfAlgoList());

            // Integral Params
            iNicehashIntegralDAO.save(nicehashController.getNicehashIntegral());

            // Workers BTC
            List<Worker> workerListBTC = nicehashController.getNicehashBTC().getWorkerList();
            iWorkerDAO_BTC.saveAll(workerListBTC);

            // Workers USD
            List<Worker> workerListUSD = nicehashController.getNicehashUSD().getWorkerList();
            iWorkerDAO_USD.saveAll(workerListUSD);

            // Workers RUB
            List<Worker> workerListRUB = nicehashController.getNicehashRUB().getWorkerList();
            iWorkerDAO_RUB.saveAll(workerListRUB);

            td.getTime();
        }
    }

}
