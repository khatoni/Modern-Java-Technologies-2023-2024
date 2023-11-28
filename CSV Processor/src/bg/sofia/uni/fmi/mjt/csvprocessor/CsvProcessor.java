package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;

import java.io.Reader;
import java.io.Writer;

public class CsvProcessor implements CsvProcessorAPI {

    private Table table;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {

    }

    /**
     * Writes the content of the table to the provided Writer
     * @param writer - the Writer to which the table will be written
     */
    public void writeTable(Writer writer, ColumnAlignment... alignments) {

    }
}
