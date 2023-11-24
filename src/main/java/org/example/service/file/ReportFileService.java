package org.example.service.file;

import org.example.exception.ReportFileServiceException;
import org.example.model.Report;
import org.example.service.file.strings.ProcessingLines;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReportFileService {
    private final String PATH = "src/main/java/org/example/information/report.txt";
    private File file;
    private BufferedReader bufferedReader;
    private ProcessingLines processingLines;

    public ReportFileService() {
        file = new File(PATH);
        processingLines = new ProcessingLines();
    }

    public void createRecordInReportFile(List<Report> reports) throws ReportFileServiceException {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            for (Report report : reports)
                fileWriter.write(report.getDataTime().toString() + " | " + report.getNameInputFile() +
                        " | remittance from " + report.getFromAccount() + " to " + report.getToAccount() + " "
                        + report.getSumRemittance() + " | " + report.getStatusRemittance() + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new ReportFileServiceException("Failed to create new record in the report file");
        }
    }

    public void getReportsHistory() throws ReportFileServiceException {
        try {
            String line;
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new ReportFileServiceException("Failed to get reports history");
        }
    }

    public void getReportsHistoryByDate(LocalDate from, LocalDate to) throws ReportFileServiceException {
        try {
            String line, date;
            LocalDate localDate;
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                if (!(date = processingLines.getDateFromReportFile(line)).isEmpty()) {
                    localDate = LocalDate.parse(date);
                    if (localDate.compareTo(from) >= 0 && localDate.compareTo(to) <= 0)
                        System.out.println(line);
                }
            }
        } catch (IOException | DateTimeParseException e) {
            throw new ReportFileServiceException("Failed to get reports history");
        }
    }
}
