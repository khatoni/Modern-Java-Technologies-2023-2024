package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;

public class MarkdownTablePrinter implements TablePrinter {

    private int calculateColumnLength(Table table, String columnName) {
        Collection<String> tmp = table.getColumnData(columnName);
        final int minimumSize = 3;
        int maximum = minimumSize;
        for (String element : tmp) {
            maximum = Math.max(maximum, element.length());
        }
        return Math.max(maximum, columnName.length());
    }

    private String printHeader(Table table, ColumnAlignment... alignments) {
        StringBuilder currentRow = new StringBuilder("|");
        Collection<String> columnNames = table.getColumnNames();
        for (String name : columnNames) {
            currentRow.append(" ");
            currentRow.append(name);
            int columnWidth = calculateColumnLength(table, name);
            for (int j = 0; j < columnWidth - name.length(); j++) {
                currentRow.append(" ");
            }
            currentRow.append(" |");
        }
        return new String(currentRow);
    }

    private String printAlignments(Table table, ColumnAlignment... alignments) {
        StringBuilder currentRow = new StringBuilder("|");
        int index = 0;
        for (String name : table.getColumnNames()) {
            StringBuilder tmpString = new StringBuilder();
            int columnWidth = calculateColumnLength(table, name);
            if (index < alignments.length) {
                tmpString.append(" ");
                if (alignments[index] == ColumnAlignment.LEFT || alignments[index] == ColumnAlignment.CENTER) {
                    tmpString.append(":");
                }
                for (int i = 0; i < columnWidth - alignments[index].getAlignmentCharactersCount(); i++) {
                    tmpString.append("-");
                }
                if (alignments[index] == ColumnAlignment.RIGHT || alignments[index] == ColumnAlignment.CENTER) {
                    tmpString.append(":");
                }
            } else {
                tmpString.append(" ");
                for (int i = 0; i < columnWidth - ColumnAlignment.NOALIGNMENT.getAlignmentCharactersCount(); i++) {
                    tmpString.append("-");
                }
            }
            tmpString.append(" ");
            index++;
            tmpString.append("|");
            currentRow.append(tmpString);
        }
        return new String(currentRow);
    }

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        Collection<String> rows = new ArrayList<>();
        rows.add(printHeader(table, alignments));
        rows.add(printAlignments(table, alignments));
        int rowsCount = table.getRowsCount() - 1;
        for (int i = 0; i < rowsCount; i++) {
            StringBuilder currentRow = new StringBuilder("|");
            Collection<String> columnNames = table.getColumnNames();
            for (String name : columnNames) {
                currentRow.append(" ");
                Collection<String> data = table.getColumnData(name);
                Object[] dataArr = data.toArray();
                currentRow.append(dataArr[i].toString());
                int columnWidth = calculateColumnLength(table, name);
                for (int j = 0; j < columnWidth - dataArr[i].toString().length(); j++) {
                    currentRow.append(" ");
                }
                currentRow.append(" |");
            }
            rows.add(new String(currentRow));
        }

        return rows;
    }
}
