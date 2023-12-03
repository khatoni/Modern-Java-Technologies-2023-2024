package bg.sogia.uni.fmi.mjt.csvprocessor.table.column;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;


public class BaseColumnTest {

    @Test
    void testAddData(){
        Set<String> tmp = new LinkedHashSet<>();
        Column column = new BaseColumn(tmp);
        String toTest = "test";
        column.addData(toTest);
        Assertions.assertTrue(column.getData().contains(toTest),
            "The string must be in the column after adding");
    }

    @Test
    void testGetMaximumColumnLengthAfterAdd(){
        BaseColumn column = new BaseColumn();
        String toTest1 = "test";
        column.addData(toTest1);
        Assertions.assertTrue(column.getData().contains(toTest1),
            "The string must be in the column after adding");
        String toTest2 = "testing";
        column.addData(toTest2);
        Assertions.assertEquals(toTest2.length(), column.getMaxColumnLength(),
            "The size of the column must change after adding");

    }
}
