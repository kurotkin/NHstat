package com.kurotkin.algoprof;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Entity
public class AlgoProf {

    @Id
    private int algo;

    private String name;
    private BigDecimal paying;
    private int port;

}
