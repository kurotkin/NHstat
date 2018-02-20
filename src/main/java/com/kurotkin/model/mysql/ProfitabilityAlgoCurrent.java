package com.kurotkin.model.mysql;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "profitability_algo_current")
public class ProfitabilityAlgoCurrent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "algo")
    private int algo;

    @Column(name = "port")
    private int port;

    @Column(name = "paying")
    private BigDecimal paying;


}
