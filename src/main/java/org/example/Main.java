package org.example;

import org.example.exception.InputFilesServiceException;
import org.example.exception.ReportFileServiceException;
import org.example.model.Input;
import org.example.service.Remittance;
import org.example.service.file.InputFilesService;
import org.example.service.file.ReportFileService;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner in= new Scanner(System.in) ,dateIn= new Scanner(System.in);
    static int choice;

    public static void main(String[] args) {
        boolean isNotFirst = false;
        while (true) {
            System.out.println("Enter number:\n 1-start the remittances\n 2-get the entire remittances history " +
                    "\n 3-get the remittances history by dates \n 4-exit\n");
            if (isNotFirst) in.nextLine();//clear buffer
            if (in.hasNextInt()) {
                choice = in.nextInt();
                if (choice == 1) startRemittances();
                else if (choice == 2) getRemittancesHistory();
                else if (choice == 3) getRemittancesHistoryByDates();
                else if (choice == 4) break;
                else System.out.println("There aren't such options");
            } else {
                System.out.println("It isn't number");
                isNotFirst = true;
            }
        }
        in.close();
    }


    private static void startRemittances() {
        try {
            File[] files = InputFilesService.getListFile();
            for (int i = 0; i < files.length; i++) {
                InputFilesService inputFilesService = new InputFilesService();
                File file = files[i];
                List<Input> inputs = inputFilesService.getInformationFromFiles(file);
                if (inputs!=null) {//if file is correct, else file is deleted
                    Remittance remittance = new Remittance();
                    remittance.makeRemittance(inputs, file);
                }
            }
        } catch (InputFilesServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getRemittancesHistory() {
        ReportFileService reportFileService = new ReportFileService();
        try {
            reportFileService.getReportsHistory();
        } catch (ReportFileServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getRemittancesHistoryByDates() {
        ReportFileService reportFileService = new ReportFileService();
        System.out.println("Enter data from and date to, example 2023-11-21/2023-11-23");
        String [] date= dateIn.nextLine().split("/");
        try {
            LocalDate from = LocalDate.parse(date[0]), to = LocalDate.parse(date[1]);
            reportFileService.getReportsHistoryByDate(from,to);
        } catch (DateTimeParseException e) {
            System.out.println("Incorrect data entered");
        } catch (ReportFileServiceException e) {
            System.out.println(e.getMessage());
        }
    }

}