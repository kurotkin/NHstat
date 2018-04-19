package com.kurotkin.hashbtc;

import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.utils.SettingsLoader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HashBTC_DAO {
    private static final Logger log = LoggerFactory.getLogger(HashBTC_DAO.class);
    private InfluxDBParam infl;

    public HashBTC_DAO() {
        this.infl = new SettingsLoader("settings.yml").getInflParam();
    }

    public void save(HashBTCModel hashBTCModel) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints.database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();
            batchPoints.point(Point
                    .measurement("Integral")
                    .time(hashBTCModel.getId(), TimeUnit.MILLISECONDS)
                    .addField("profitability_BTC", hashBTCModel.getProfitability().doubleValue())
                    .addField("balance_BTC", hashBTCModel.getBalance().doubleValue())
                    .addField("speed", hashBTCModel.getSpeed().doubleValue())
                    .build());
            influxDB.write(batchPoints);
            log.info("Записано = " + hashBTCModel.toString());
        }
        catch (Exception e){
            log.error("Ошибка записи стоимости валюты");
        }
    }
}
