package bg.sogia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;

public class BaseTableTest {

    @Test
    void testAddDataWithNull() {
        BaseTable baseTable = new BaseTable();
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.addData(null),
            "IllegalArgumentException was expected");
    }

    @Test
    void testAddDataEmptyTable() {
        BaseTable baseTable = new BaseTable();
        String[] tokens = new String[] {"test1", "test2", "test3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(tokens), "The function must not throw");
        Assertions.assertNotNull(baseTable.getColumnNames(), "After adding the table must not be empty");
    }

    @Test
    void testAddDataNotValidSize() {
        BaseTable baseTable = new BaseTable();
        String[] tokens = new String[] {"test1", "test2", "test3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(tokens), "Expected not to throw");
        String[] values = new String[] {"csv1", "ssv2", "ttv3", "hh3"};
        Assertions.assertThrows(CsvDataNotCorrectException.class, () -> baseTable.addData(values),
            "CsvDataNotCorrectException was expected");
    }

    @Test
    void testAddDataWithInvalidToken() {
        BaseTable baseTable = new BaseTable();
        String[] tokens = new String[] {"test1", "test2", "test3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(tokens), "Expected not to throw");
        String[] values = new String[] {"csv1", "ssv2", ""};
        Assertions.assertThrows(CsvDataNotCorrectException.class, () -> baseTable.addData(values),
            "CsvDataNotCorrectException was expected");
    }

    @Test
    void testGetColumnDataWithInvalidColumnName() {
        BaseTable baseTable = new BaseTable();
        String[] tokens = new String[] {"test1", "test2", "test3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(tokens), "Expected not to throw");
        Assertions.assertThrows(IllegalArgumentException.class, () -> baseTable.getColumnData("test"),
            "IllegalArgumentException expected");
    }

    @Test
    void testGetColumnDataSuccessfully() {
        BaseTable baseTable = new BaseTable();
        String[] tokens = new String[] {"test1", "test2", "test3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(tokens), "Expected not to throw");
        String[] values = new String[] {"csv1", "ssv2", "tt2"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(values), "Expected not to throw");
        Collection<String> content = baseTable.getColumnData("test1");
        Collection<String> test = new LinkedHashSet<>();
        test.add("csv1");
        Assertions.assertIterableEquals(content, test, "The column content is not equal");
    }

    @Test
    void testGetRowsCount() {
        BaseTable baseTable = new BaseTable();
        String[] tokens = new String[] {"test1", "test2", "test3"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(tokens), "Expected not to throw");
        String[] values = new String[] {"csv1", "ssv2", "tt2"};
        Assertions.assertDoesNotThrow(() -> baseTable.addData(values), "Expected not to throw");
        Assertions.assertEquals(2, baseTable.getRowsCount(), "The expected row count is not the same");
    }

}
