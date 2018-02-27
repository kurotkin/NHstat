package com.kurotkin.algoprof;

import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.utils.SettingsLoader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlgoProfDAO {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private InfluxDBParam infl;

    public AlgoProfDAO() {
        this.infl = new SettingsLoader("settings.yml").getInflParam();
    }

    public void save(List<AlgoProf> list) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints.database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            Long time = System.currentTimeMillis();
            list.forEach(s -> {batchPoints.point(Point.measurement(s.getName())
                    .time(time, TimeUnit.MILLISECONDS)
                    .addField("paying", s.getPaying().doubleValue())
                    .build());
                log.info("Записано = " + s.toString());
            });

            influxDB.write(batchPoints);
        }
        catch (Exception e){
            log.error("Ошибка записи прибыльности для алгоритмов");
        }
    }
}
