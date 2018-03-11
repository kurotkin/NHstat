package com.kurotkin.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Balance {
    @Id
    private Long id;

    private BigDecimal balanceTotal;
    private BigDecimal balance;
    private BigDecimal balanceConfirmed;

}
