package com.kurotkin.algoprof;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class AlgoProf {
    @Id
    public int algo;

    public BigDecimal paying;
    public int port;
    public String name;

    public AlgoProf(int algo, BigDecimal paying, int port, String name) {
        this.algo = algo;
        this.paying = paying;
        this.port = port;
        this.name = name;
    }
}
