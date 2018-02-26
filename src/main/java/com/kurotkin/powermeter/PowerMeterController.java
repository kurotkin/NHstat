package com.kurotkin.powermeter;

import com.kurotkin.model.InfluxDBParam;
import com.kurotkin.utils.SettingsLoader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class PowerMeterController {

    private InfluxDBParam infl;

    public PowerMeterController() {
        infl = new SettingsLoader("settings.yml").getInflParam();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void add(@RequestParam(value = "power", required = true) String powerString) {
        System.out.println(powerString);
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();
            Point.Builder builder = Point.measurement("Power");
            builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            builder.addField("current", Double.parseDouble(powerString));
            batchPoints.point(builder.build());
            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.err.println("Ошибка записи мощности: " + e);
        }
    }
}
