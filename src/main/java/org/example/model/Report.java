package org.example.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Report {
    private String fromAccount;
    private String toAccount;
    private BigDecimal sumRemittance;
    private String nameInputFile;
    private String statusRemittance;
    private LocalDateTime dataTime;
}
