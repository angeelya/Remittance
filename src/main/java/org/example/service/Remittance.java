package org.example.service;

import org.example.exception.AccountsFileServiceException;
import org.example.exception.DatabaseServiceException;
import org.example.exception.ReportFileServiceException;
import org.example.model.AccountsFromAndTo;
import org.example.model.Input;
import org.example.model.Report;
import org.example.service.database.ServiceDB;
import org.example.service.file.AccountsFileService;
import org.example.service.file.ReportFileService;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Remittance {
    private List<Report> reports;
    private AccountsFileService accountsFileService;
    private ReportFileService reportFileService;
    private ServiceDB serviceDB;
    private final String MESSAGE_ERROR = "Error in processing time.";
    private String status;
    private LocalDateTime date;

    public Remittance() {
        reportFileService = new ReportFileService();
    }

    public void makeRemittance(List<Input> inputList, File file) {
        reports = new ArrayList<>();
        for (Input input : inputList) {
            status = startRemittance(input.getFromAccount(), input.getToAccount(), input.getSumRemittance());
            reports.add(getReportObject(input, file));
        }
        makeReportRecord(file.getName());
        insertIntoDatabase();
    }

    private void insertIntoDatabase() {
        try {
            serviceDB = new ServiceDB();
            for (Report report : reports)
                if (!serviceDB.insertReport(report))
                    System.out.println("Failed to insert report record into a database");
        } catch (DatabaseServiceException e) {
            System.out.println("Failed to insert report record into a database: " + e.getMessage());
        }
    }

    private void makeReportRecord(String name) {
        try {
            reportFileService.createRecordInReportFile(reports);
        } catch (ReportFileServiceException e) {
            System.out.println(e.getMessage() + "(Remittances are from input file" + name + ")");
        }
    }

    private Report getReportObject(Input input, File file) {
        Report report = new Report();
        report.setNameInputFile(file.getName());
        report.setSumRemittance(input.getSumRemittance());
        report.setFromAccount(input.getFromAccount());
        report.setToAccount(input.getToAccount());
        report.setStatusRemittance(status);
        report.setDataTime(date);
        return report;
    }

    private String startRemittance(String fromAccount, String toAccount, BigDecimal sumRemittance) {
        String result;
        AccountsFromAndTo accountsFromAndTo;
        try {
            if (!fromAccount.equals(toAccount)) {
                date = LocalDateTime.now();
                accountsFileService = new AccountsFileService();
                accountsFromAndTo = accountsFileService.getSumAccounts(fromAccount, toAccount);
                if (sumRemittance.compareTo(BigDecimal.ZERO) <= 0)
                    result = MESSAGE_ERROR + "Remittance sum is incorrect";
                else {
                    result = performRemittanceOperation(accountsFromAndTo, sumRemittance);
                }
            }
            else result=MESSAGE_ERROR+"Account from equals account to";
        } catch (AccountsFileServiceException e) {
            result = MESSAGE_ERROR + e.getMessage();
        }
        return result;
    }

    private String performRemittanceOperation(AccountsFromAndTo accountsFromAndTo, BigDecimal sumRemittance) throws AccountsFileServiceException {
        BigDecimal newBalanceAccountFrom = accountsFromAndTo.getBalanceAccountFrom().subtract(sumRemittance),
                newBalanceAccountTo = accountsFromAndTo.getBalanceAccountTo().add(sumRemittance);
        date = LocalDateTime.now();
        if (newBalanceAccountFrom.compareTo(BigDecimal.ZERO) >= 0) {
            accountsFromAndTo.setBalanceAccountFrom(newBalanceAccountFrom);
            accountsFromAndTo.setBalanceAccountTo(newBalanceAccountTo);
            accountsFileService.updateAccountsData(accountsFromAndTo);
            return "Successful";
        } else return MESSAGE_ERROR + "Insufficient funds in the account";
    }
}
