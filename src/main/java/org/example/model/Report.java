package org.example.model;

import lombok.Data;

@Data
public class Report {
    private String fromAccount;
    private String toAccount;
    private Double sumRemittance;
    private String nameInputFile;
    private String statusRemittance;
    private String dataTime;
}
