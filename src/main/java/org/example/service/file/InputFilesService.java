package org.example.service.file;

import org.example.exception.InputFilesServiceException;
import org.example.model.Input;
import org.example.service.file.strings.ProcessingLines;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class InputFilesService {
    private static final String DIRECTORY = "src/main/java/org/example/information/input";
    private final String ARCHIVE = "src/main/java/org/example/information/archive/";
    private List<Input> correctFileData;


    public static File[] getListFile() throws InputFilesServiceException {
        File[] files;
        files = new File(DIRECTORY).listFiles(((dir, name) -> name.endsWith(".txt")));
        if (files.length == 0) throw new InputFilesServiceException("All remittances is completed");
        else return files;
    }

    public List<Input> getInformationFromFiles(File file) throws InputFilesServiceException {
        if (file.isFile()) {
            try {
                checkDataInFile(file);
            } catch (InputFilesServiceException e) {
                throw new InputFilesServiceException("Failed to get information from input file or move:" + e.getMessage());
            }
        }
        return correctFileData;
    }

    private void checkDataInFile(File file) throws InputFilesServiceException {
        try {
            ProcessingLines processingLines = new ProcessingLines(true);
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                processingLines.findDataInInputFile(line);
            }
            bufferedReader.close();
            checkFileForCorrectness(processingLines, file);
        } catch (IOException | InputFilesServiceException e) {
            throw new InputFilesServiceException(e.getMessage());
        }
    }

    private void checkFileForCorrectness(ProcessingLines processingLines, File file) throws IOException, InputFilesServiceException {
        if (processingLines.IsInputFileCorrect()) {
            correctFileData = processingLines.getInputs();
            writeCorrectDataInInputFile(file);
            File secondPlace = new File(ARCHIVE + file.getName());
            Files.move(file.toPath(), secondPlace.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else deleteFile(file);
    }

    private void writeCorrectDataInInputFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        for (Input input : correctFileData) {
            fileWriter.write("account from: "+input.getFromAccount()+"\n");
            fileWriter.write("account to: "+input.getToAccount()+"\n");
            fileWriter.write("sum: "+input.getSumRemittance()+"\n\n");
        }
        fileWriter.close();
    }

    private void deleteFile(File file) throws InputFilesServiceException {
        if (!file.delete()) throw new InputFilesServiceException("Incorrect file " + file.getName() + " isn't deleted");
    }
}
