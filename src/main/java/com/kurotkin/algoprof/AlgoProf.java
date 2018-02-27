package com.kurotkin.algoprof;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class AlgoProf {
    @Id
    private int algo;

    private String name;
    private BigDecimal paying;
    private int port;

    public AlgoProf(int algo, BigDecimal paying, int port, String name) {
        this.algo = algo;
        this.paying = paying;
        this.port = port;
        this.name = name;
    }

    public int getAlgo() {
        return algo;
    }

    public void setAlgo(int algo) {
        this.algo = algo;
    }

    public BigDecimal getPaying() {
        return paying;
    }

    public void setPaying(BigDecimal paying) {
        this.paying = paying;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{name='" + name + '\'' +
                ", paying=" + paying +
                '}';
    }
}
