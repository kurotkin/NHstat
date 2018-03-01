package com.kurotkin.hashbtc;

import com.kurotkin.algoprof.ScheduledTasks;
import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.rate.Rate;
import com.kurotkin.utils.SettingsLoader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class HashBTC_DAO {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private InfluxDBParam infl;

    public HashBTC_DAO() {
        this.infl = new SettingsLoader("settings.yml").getInflParam();
    }

    public void save(HashBTC hashBTC) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints.database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();
            batchPoints.point(Point
                    .measurement("Integral")
                    .time(hashBTC.getId(), TimeUnit.MILLISECONDS)
                    .addField("profitability_BTC", hashBTC.getProfitability().doubleValue())
                    .addField("balance_BTC", hashBTC.getBalance().doubleValue())
                    .addField("speed", hashBTC.getSpeed().doubleValue())
                    .build());
            influxDB.write(batchPoints);
            log.info(hashBTC.toString());
        }
        catch (Exception e){
            log.error("Ошибка записи стоимости валюты");
        }
    }
}
