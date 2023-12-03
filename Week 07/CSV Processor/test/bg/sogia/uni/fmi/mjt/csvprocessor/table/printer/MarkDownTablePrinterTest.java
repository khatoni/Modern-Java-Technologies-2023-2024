package bg.sogia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class MarkDownTablePrinterTest {

    @Test
    void testPrintTableSuccessfully() {
        Table table = new BaseTable();
        Assertions.assertDoesNotThrow(() -> table.addData(new String[] {"one", "two"}));
        Assertions.assertDoesNotThrow(() -> table.addData(new String[] {"a", "b"}));
        Collection<String> test = new ArrayList<>();
        test.add("| one | two |");
        test.add("| :-- | --: |");
        test.add("| a   | b   |");
        Assertions.assertIterableEquals(test, new MarkdownTablePrinter().printTable(table, ColumnAlignment.LEFT, ColumnAlignment.RIGHT));

    }
}
