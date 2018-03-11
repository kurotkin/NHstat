package com.kurotkin.rate;

import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.utils.SettingsLoader;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RateDAO {
    private InfluxDBParam infl;

    public RateDAO() {
        this.infl = new SettingsLoader("settings.yml").getInflParam();
    }


    public void save(Rate rate) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints.database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            Long time = rate.getId();
            batchPoints.point(Point
                            .measurement("Price")
                            .time(time, TimeUnit.MILLISECONDS)
                            .addField("price_usd", rate.getPrice_usd().doubleValue())
                            .addField("price_rub", rate.getPrice_rub().doubleValue())
                            .addField("percent_change_1h", rate.getPercent_change_1h().doubleValue())
                            .addField("percent_change_24h", rate.getPercent_change_24h().doubleValue())
                            .addField("percent_change_7d", rate.getPercent_change_7d().doubleValue())
                            .build());

            influxDB.write(batchPoints);
            log.info("Записано = " + rate.toString());
        }
        catch (Exception e){
            log.error("Ошибка записи стоимости валюты");
        }
    }
}
