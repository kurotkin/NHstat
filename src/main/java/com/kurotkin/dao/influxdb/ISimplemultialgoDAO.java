package com.kurotkin.dao.influxdb;

import com.kurotkin.dao.SimplemultialgoDAO;
import com.kurotkin.model.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ISimplemultialgoDAO implements SimplemultialgoDAO {
    private InfluxDBParam infl;

    public ISimplemultialgoDAO(InfluxDBParam infl) {
        this.infl = infl;
    }

    @Override
    public void save(NicehashSimplemultialgo simplemultialgo) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build()
                    .point(prepareBatchPoints(simplemultialgo));
            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.out.println("Ошибка записи прибыльности для алгоритма: " + e);
        }
    }

    @Override
    public void saveAll(List<NicehashSimplemultialgo> list) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            list.forEach(s -> {
                batchPoints.point(prepareBatchPoints(s));
            });

            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.err.println("Ошибка записи прибыльности для алгоритмов: " + e);
        }
    }

    private Point prepareBatchPoints(NicehashSimplemultialgo sAlgo){
        String name = sAlgo.getName();
        Point.Builder builder = Point.measurement(name);
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

        builder.addField("paying", sAlgo.getPaying().doubleValue());
        builder.addField("port", sAlgo.getPaying().doubleValue());
        return builder.build();
    }
}
