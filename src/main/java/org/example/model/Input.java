package org.example.model;

import lombok.Data;

@Data
public class Input {
    private String fromAccount;
    private String toAccount;
    private Double sumRemittance;
}
