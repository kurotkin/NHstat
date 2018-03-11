package com.kurotkin.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rate {
    @Id
    private Long id;

    private BigDecimal price_usd  = new BigDecimal("0.00");;
    private BigDecimal price_rub  = new BigDecimal("0.00");;
    private BigDecimal percent_change_1h  = new BigDecimal("0.00");
    private BigDecimal percent_change_24h = new BigDecimal("0.00");
    private BigDecimal percent_change_7d = new BigDecimal("0.00");
    private Date last_updated;

}
