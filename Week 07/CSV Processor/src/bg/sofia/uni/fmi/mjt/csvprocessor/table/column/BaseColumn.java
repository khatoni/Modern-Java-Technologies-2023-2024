package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseColumn implements Column {

    private Set<String> values;
    private final int minLength = 3;
    private int maxColumnLength;

    public BaseColumn() {
        values = new LinkedHashSet<>();
        maxColumnLength = minLength;
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
        maxColumnLength = minLength;
    }

    public void addData(String data) {
        if (data.length() > maxColumnLength) {
            maxColumnLength = data.length();
        }
        values.add(data);
    }

    public Collection<String> getData() {
        return Collections.unmodifiableSet(values);
    }

    public int getMaxColumnLength() {
        return maxColumnLength;
    }

}
