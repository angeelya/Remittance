package org.example.service.file.strings;

import org.example.model.AccountsFromAndTo;
import org.example.model.Input;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessingLines {
    private boolean isAccountFrom = false, isAccountTo = false, isSum = false;
    private String accountFrom, accountTo;
    private BigDecimal sum;
    private List<Input> inputs;
    private Pattern patternAccount, patternSum,patternDate,patternFlag;
    private Matcher matcherAccount, matcherSum, matcherDate,matcherFlag;

    public ProcessingLines() {
        patternSum = Pattern.compile("^[-+]?\\d*[.]?\\d{1,2}$");
        patternDate = Pattern.compile("(?!\\d{4}-00-[0-3]\\d)(?!\\d{4}-1[3-9]-[0-3]\\d)" +
                "(?!\\d{4}-[0-1]\\d-00)(?!\\d{4}-[0-1]\\d-3[3-9])(\\d{4}-[0-1]\\d-[0-3]\\d)");

    }
    public ProcessingLines(Boolean flagInputFile)
    {    patternAccount = Pattern.compile("\\b\\d{5}-\\d{5}\\b");
        patternSum = Pattern.compile("^[-+]?\\d*[.]?\\d{1,2}$");
        patternFlag=Pattern.compile("[Ss]um:");
        inputs = new ArrayList<>();
    }

    public String getDateFromReportFile(String line) {
        matcherDate = patternDate.matcher(line);
        if(matcherDate.find())
            return matcherDate.group();
        else return "";
    }
    public AccountsFromAndTo getAccountsData(String from, String to, List<String> lines) {
        String line;
        AccountsFromAndTo accountsFromAndTo = new AccountsFromAndTo();
        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);
            if (line.contains(from)) {
                accountsFromAndTo.setAccountFrom(from);
                accountsFromAndTo.setBalanceAccountFrom(getAccountSum(line.replace(from,"").trim()));
                accountsFromAndTo.setPointAccountFrom(i);
            } else if (line.contains(to)) {
                accountsFromAndTo.setAccountTo(to);
                accountsFromAndTo.setBalanceAccountTo(getAccountSum(line.replace(to,"").trim()));
                accountsFromAndTo.setPointAccountTo(i);
            }
        }
        if (accountsFromAndTo.getBalanceAccountFrom() != null &&
                accountsFromAndTo.getBalanceAccountTo() != null)
            return accountsFromAndTo;
        else return null;
    }

    private BigDecimal getAccountSum(String line) {
        matcherSum = patternSum.matcher(line);
        if (matcherSum.find()) {
            return new BigDecimal(matcherSum.group());
        } else return null;
    }


    public void findDataInInputFile(String line) {
        determineWhatFieldIs(line.trim().toLowerCase());
        if (isAccountFrom && isAccountTo && isSum) {
            getInputObject();
            isAccountFrom = false;
            isAccountTo = false;
            isSum = false;
        }
    }

    private void getInputObject() {
        Input input = new Input();
        input.setFromAccount(accountFrom);
        input.setToAccount(accountTo);
        input.setSumRemittance(sum);
        inputs.add(input);
    }

    public boolean IsInputFileCorrect() {
        if (inputs.size()>0) return true;
        else return false;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    private void determineWhatFieldIs(String line) {
        matcherAccount = patternAccount.matcher(line);
        matcherFlag=patternFlag.matcher(line);
        if (line.matches("(.*)[Aa]ccount from:(.*)")) {
            if (matcherAccount.find()) {
                accountFrom = matcherAccount.group();
                isAccountFrom = true;
                isAccountTo = false;
                isSum = false;
            }
        } else if (line.matches("(.*)[Aa]ccount to:(.*)") && isAccountFrom) {
            if (matcherAccount.find()) {
                accountTo = matcherAccount.group();
                if (isAccountFrom && isAccountTo) {
                    isAccountFrom = false;
                    isAccountTo = false;
                } else {
                    isAccountTo = true;
                    isSum = false;
                }
            }
        } else if (matcherFlag.find() && isAccountFrom && isAccountTo) {
            line=line.replace(matcherFlag.group()," ").trim();
            matcherSum = patternSum.matcher(line);
            if (matcherSum.find()) {
                sum = new BigDecimal(matcherSum.group());
                isSum = true;
            }
        }


    }

}
