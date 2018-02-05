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
    private String InfluxDBUser;
    private String InfluxDBPass;
    private String InfluxDBdbName;
    private String InfluxDBUrl;

    public ISimplemultialgoDAO(String InfluxDBUrl, String InfluxDBdbName, String InfluxDBUser, String InfluxDBPass) {
        this.InfluxDBUrl = InfluxDBUrl;
        this.InfluxDBdbName = InfluxDBdbName;
        this.InfluxDBUser = InfluxDBUser;
        this.InfluxDBPass = InfluxDBPass;
    }

    @Override
    public void save(NicehashSimplemultialgo simplemultialgo) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(InfluxDBUrl, InfluxDBUser, InfluxDBPass);
            influxDB.createDatabase(InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build()
                    .point(prepareBatchPoints(simplemultialgo));
            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.out.println("Ошибка записи прибыльности для алгоритма " + simplemultialgo.getName() + ": " + e);
        }
    }

    @Override
    public void saveAll(List<NicehashSimplemultialgo> list) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(InfluxDBUrl, InfluxDBUser, InfluxDBPass);
            influxDB.createDatabase(InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            list.forEach(s -> {
                batchPoints.point(prepareBatchPoints(s));
            });

            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.out.println("Ошибка записи прибыльности для алгоритмов: " + e);
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