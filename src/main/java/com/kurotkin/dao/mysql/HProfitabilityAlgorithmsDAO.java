package com.kurotkin.dao.mysql;

import com.kurotkin.dao.HSimplemultialgoDAO;
import com.kurotkin.dao.SimplemultialgoDAO;
import com.kurotkin.model.NicehashSimplemultialgo;
import com.kurotkin.model.mysql.ProfitabilityAlgorithms;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HProfitabilityAlgorithmsDAO implements HSimplemultialgoDAO {

    private SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    private Point prepareBatchPoints(NicehashSimplemultialgo sAlgo){
        String name = sAlgo.getName();
        Point.Builder builder = Point.measurement(name);
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

        builder.addField("paying", sAlgo.getPaying().doubleValue());
        builder.addField("port", sAlgo.getPaying().doubleValue());
        return builder.build();
    }

    @Override
    public void save(ProfitabilityAlgorithms profitabilityAlgorithms) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(profitabilityAlgorithms);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<ProfitabilityAlgorithms> list) {

    }
}
