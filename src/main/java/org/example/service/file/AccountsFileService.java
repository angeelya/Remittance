package org.example.service.file;

import org.example.exception.AccountsFileServiceException;
import org.example.model.AccountsFromAndTo;
import org.example.service.file.strings.ProcessingLines;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class AccountsFileService {
    private final String PATH = "src/main/java/org/example/information/accounts.txt";
    private File file;

    private ProcessingLines processingLines;
    private BufferedReader bufferedReader;
    private List<String> lines;

    public AccountsFileService() throws AccountsFileServiceException {
        file = new File(PATH);
        try {
            getDataFromFile();
        } catch (IOException e) {
            throw new AccountsFileServiceException("Failed to read or update accounts data");
        }
    }

    public void updateAccountsData(AccountsFromAndTo newAccountsFromAndTo) throws AccountsFileServiceException {
        try {
            lines.set(newAccountsFromAndTo.getPointAccountFrom(), newAccountsFromAndTo.getAccountFrom() + " " + newAccountsFromAndTo.getBalanceAccountFrom());
            lines.set(newAccountsFromAndTo.getPointAccountTo(), newAccountsFromAndTo.getAccountTo() + " " + newAccountsFromAndTo.getBalanceAccountTo());
            FileWriter fileWriter = new FileWriter(file);
            for (String line : lines)
                fileWriter.write(line + "\n");
            fileWriter.close();
        } catch (IOException|UnsupportedOperationException e) {
            throw new AccountsFileServiceException("Failed to update accounts data");
        }
    }

    public AccountsFromAndTo getSumAccounts(String accountFrom, String accountTo) throws AccountsFileServiceException {
        AccountsFromAndTo accountsFromAndTo = processingLines.getAccountsData(accountFrom, accountTo, lines);
        if (accountsFromAndTo == null) throw new AccountsFileServiceException("One of the accounts does not exist");
        else return accountsFromAndTo;
    }

    private void getDataFromFile() throws IOException {
        bufferedReader = new BufferedReader(new FileReader(file));
        lines= bufferedReader.lines().collect(Collectors.toList());
        processingLines = new ProcessingLines();
        bufferedReader.close();
    }


}
