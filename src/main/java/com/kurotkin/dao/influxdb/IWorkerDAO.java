package com.kurotkin.dao.influxdb;

import com.kurotkin.dao.WorkerDAO;
import com.kurotkin.model.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class IWorkerDAO implements WorkerDAO {

    private InfluxDBParam infl;
    private String typeWorker;

    public IWorkerDAO(InfluxDBParam infl, String typeWorker) {
        this.infl = infl;
    }

    @Override
    public void save(Worker worker) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            Point.Builder builder = Point.measurement(worker.getName());
            builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            builder.addField("profitability_" + typeWorker, worker.getProfitability().doubleValue());
            builder.addField("balance_" + typeWorker, worker.getBalance().doubleValue());
            builder.addField("speed", worker.getSpeed().doubleValue());

            batchPoints.point(builder.build());
            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.out.println("Ошибка записи worker: " + e);
        }
    }

    @Override
    public void saveAll(List<Worker> list) {
        list.forEach(this::save);
    }
}
