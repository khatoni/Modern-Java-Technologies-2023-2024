package bg.sogia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.CsvProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvProcessorTest {

    @Test
    void testReadCsvNotCorrectData() {
        CsvProcessor csvProcessor = new CsvProcessor();
        String filepath = "C:\\Users\\Hp\\Desktop\\Modern-Java-Technologies-2023-2024\\CSV Processor\\test1.txt";
        Path path = Paths.get(filepath);
        String firstLine = "test1,test3,test3";
        String secondLine = "file1,file2,file3";
        try {
            Files.createFile(path);
            FileWriter fileWriter = new FileWriter(filepath);
            fileWriter.write(firstLine);
            fileWriter.write(System.lineSeparator());
            fileWriter.write(secondLine);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Problem in input output");
        }
        Assertions.assertDoesNotThrow(
            () -> csvProcessor.readCsv(new FileReader(filepath), ","), "Expected not to throw");
    }

    @Test
    void testWriteCsv() {
        CsvProcessor csvProcessor = new CsvProcessor();
        String filepath = "C:\\Users\\Hp\\Desktop\\Modern-Java-Technologies-2023-2024\\CSV Processor\\test1.txt";
        Path path = Paths.get(filepath);
        String firstLine = "test1,test3,test3";
        String secondLine = "file1,file2,file3";
        try {
            Files.createFile(path);
            FileWriter fileWriter = new FileWriter(filepath);
            fileWriter.write(firstLine);
            fileWriter.write(System.lineSeparator());
            fileWriter.write(secondLine);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Problem in input output");
        }

        Assertions.assertDoesNotThrow(() -> csvProcessor.writeTable(new FileWriter(filepath)), "Expected not to throw");
    }
}
