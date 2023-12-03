package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Scanner;

public class CsvProcessor implements CsvProcessorAPI {

    private final Table table;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        Scanner scanner = new Scanner(reader);
        String escapedDelimiter = "\\Q" + delimiter + "\\E";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(escapedDelimiter);
            table.addData(tokens);
        }
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Error while trying to close the stream");
        }
    }

    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        MarkdownTablePrinter tablePrinter = new MarkdownTablePrinter();
        Collection<String> content = tablePrinter.printTable(table, alignments);
        int rowSize = content.size();
        int index = 1;
        try {
            for (String element : content) {
                writer.write(element);
                if (index != rowSize) {
                    writer.write(System.lineSeparator());
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error while writing to file");
        }
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while trying to close the stream");
        }

    }
}
