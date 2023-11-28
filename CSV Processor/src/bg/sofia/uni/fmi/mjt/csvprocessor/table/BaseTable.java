package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SequencedSet;

public class BaseTable implements Table {

    private Map<String, Column> columns;

    public BaseTable() {
        columns = new LinkedHashMap<>();
    }

    public void addData(String[] data) throws CsvDataNotCorrectException {

        if (data == null) {
            throw new IllegalArgumentException("the data is null");
        }

        if (data.length != columns.size()) {
            throw new CsvDataNotCorrectException("The number of elements differ from the number of columns");
        }

        if (columns.isEmpty()) {
            for (int i = 0; i < data.length; i++) {
                columns.putIfAbsent(data[i], new BaseColumn());
            }
        }

        if (data.length != columns.size()) {
            throw new CsvDataNotCorrectException("The number of elements differ from the number of columns");
        }

        int index = 0;
        for (Map.Entry<String, Column> element : columns.entrySet()) {
            Column tmpColumn = columns.get(element.getKey());
            tmpColumn.addData(data[index++]);
            columns.put(element.getKey(), tmpColumn);
        }
    }


    public Collection<String> getColumnNames() {
        Collection<String> answer = new ArrayList<>();
        for (String name : columns.keySet()) {
            answer.add(name);
        }
        return Collections.unmodifiableCollection(answer);
    }


    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank() || !columns.containsKey(column)) {
            throw new IllegalArgumentException("Column is null or blank");
        }

        Column tmpColumn = columns.get(column);
        Collection<String> answer = tmpColumn.getData();
        return Collections.unmodifiableCollection(answer);
    }

    public int getRowsCount() {
        SequencedSet<String> tmp = new LinkedHashSet<>(columns.keySet());
        String name = tmp.getFirst();
        return columns.get(name).getData().size();

    }

}
