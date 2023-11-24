package org.example.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountsFromAndTo {
    private String accountFrom;
    private String accountTo;
    private BigDecimal balanceAccountFrom;
    private BigDecimal balanceAccountTo;
    private Integer pointAccountFrom;
    private Integer pointAccountTo;
}
