package com.kurotkin.dao.influxdb;

import com.kurotkin.dao.NicehashIntegralDAO;
import com.kurotkin.model.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class INicehashIntegralDAO implements NicehashIntegralDAO {

    private InfluxDBParam infl;

    public INicehashIntegralDAO(InfluxDBParam infl) {
        this.infl = infl;
    }

    @Override
    public void save(NicehashIntegral nicehashIntegral) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(infl.InfluxDBUrl, infl.InfluxDBUser, infl.InfluxDBPass);
            influxDB.createDatabase(infl.InfluxDBdbName);
            BatchPoints batchPoints = BatchPoints
                    .database(infl.InfluxDBdbName)
                    .retentionPolicy("autogen")
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            Point.Builder builder = Point.measurement("Integral");
            builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

            double speed = nicehashIntegral.speed.doubleValue();
            builder.addField("speed", speed);
            builder.addField("algoN", nicehashIntegral.algoN);

            // BTC
            NicehashBTC nicehashBTC = nicehashIntegral.nicehashBTC;
            double profitability_BTC = nicehashBTC.getProfitability().doubleValue();
            builder.addField("profitability_BTC", profitability_BTC);

            double balance_BTC = nicehashBTC.getBalance().doubleValue();
            builder.addField("balance_BTC", balance_BTC);

            double balanceConfirmed_BTC = nicehashBTC.getBalanceConfirmed().doubleValue();
            builder.addField("balanceConfirmed_BTC", balanceConfirmed_BTC);

            double balanceTotal_BTC = nicehashBTC.getBalanceTotal().doubleValue();
            builder.addField("balanceTotal_BTC", balanceTotal_BTC);


            // USD
            NicehashUSD nicehashUSD = nicehashIntegral.nicehashUSD;
            double profitability_USD = nicehashUSD.getProfitability().doubleValue();
            builder.addField("profitability_USD", profitability_USD);

            double balance_USD = nicehashUSD.getBalance().doubleValue();
            builder.addField("balance_USD", balance_USD);

            double balanceConfirmed_USD = nicehashUSD.getBalanceConfirmed().doubleValue();
            builder.addField("balanceConfirmed_USD", balanceConfirmed_USD);

            double balanceTotal_USD = nicehashUSD.getBalanceTotal().doubleValue();
            builder.addField("balanceTotal_USD", balanceTotal_USD);

            // RUB
            NicehashRUB nicehashRUB = nicehashIntegral.nicehashRUB;
            double profitability_RUB = nicehashRUB.getProfitability().doubleValue();
            builder.addField("profitability_RUB", profitability_RUB);

            double balance_RUB = nicehashRUB.getBalance().doubleValue();
            builder.addField("balance_RUB", balance_RUB);

            double balanceConfirmed_RUB = nicehashRUB.getBalanceConfirmed().doubleValue();
            builder.addField("balanceConfirmed_RUB", balanceConfirmed_RUB);

            double balanceTotal_RUB = nicehashRUB.getBalanceTotal().doubleValue();
            builder.addField("balanceTotal_RUB", balanceTotal_RUB);

            batchPoints.point(builder.build());
            influxDB.write(batchPoints);
        }
        catch (Exception e){
            System.err.println("Ошибка создания записи по интегральным параметрам: " + e);
        }
    }

    @Override
    public void saveAll(List<NicehashIntegral> list) {

    }
}
