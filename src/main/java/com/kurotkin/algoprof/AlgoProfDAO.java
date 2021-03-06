package com.kurotkin.algoprof;

import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.utils.SettingsLoader;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AlgoProfDAO {
    private InfluxDBParam infl;

    public AlgoProfDAO() {
        this.infl = new SettingsLoader("settings.yml").getInflParam();
    }

    public void save(List<AlgoProfModel> list) {
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
