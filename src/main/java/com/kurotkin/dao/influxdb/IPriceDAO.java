package com.kurotkin.dao.influxdb;

import com.kurotkin.controller.Rate;
import com.kurotkin.dao.PriceDAO;
import com.kurotkin.model.InfluxDBParam;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class IPriceDAO implements PriceDAO {
    private InfluxDBParam infl;

    public IPriceDAO(InfluxDBParam infl) {
        this.infl = infl;
    }

    @Override
    public void save(Rate rate) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();
            Point.Builder builder = Point.measurement("Price");
            builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

            builder.addField("price_usd", rate.getPrice_usd().doubleValue());
            builder.addField("price_rub", rate.getPrice_rub().doubleValue());
            builder.addField("percent_change_1h", rate.getPercent_change_1h().doubleValue());
            builder.addField("percent_change_24h", rate.getPercent_change_24h().doubleValue());
            builder.addField("percent_change_7d", rate.getPercent_change_7d().doubleValue());

            batchPoints.point(builder.build());
            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.out.println("Ошибка записи курса валют: " + e);
        }
    }

    @Override
    public void saveAll(List<Rate> list) {

    }
}
