package org.example.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Input {
    private String fromAccount;
    private String toAccount;
    private BigDecimal sumRemittance;
}
