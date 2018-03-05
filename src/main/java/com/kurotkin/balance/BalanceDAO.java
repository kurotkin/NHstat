package com.kurotkin.balance;

import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.utils.SettingsLoader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BalanceDAO {
    private static final Logger log = LoggerFactory.getLogger(BalanceDAO.class);
    private InfluxDBParam infl;

    public BalanceDAO() {
        this.infl = new SettingsLoader("settings.yml").getInflParam();
    }

    public void save(Balance balance) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints.database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            Long time = balance.getId();
            batchPoints.point(Point
                    .measurement("Balance")
                    .time(time, TimeUnit.MILLISECONDS)
                    .addField("balance", balance.getBalance().doubleValue())
                    .addField("balanceConfirmed", balance.getBalanceConfirmed().doubleValue())
                    .addField("balanceTotal", balance.getBalanceTotal().doubleValue())
                    .build());

            influxDB.write(batchPoints);
            log.info("Записано = " + balance.toString());
        }
        catch (Exception e){
            log.error("Ошибка записи полного баланса");
        }
    }
}
