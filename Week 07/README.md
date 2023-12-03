## CSV Processor 📃
Задачата тази седмица е да се имплементира прост converter на CSV таблици към Markdown такива. Идеята е да се прочете един CSV файл, да се конвертира към Markdown таблица и впоследствие да се запише в Markdown файл (файл с разширение .md). За целта, CSV файлът трябва да отговаря на следните изисквания:

* Първият ред от файла съдържа имената на колоните от таблицата.
* Всеки следващ ред трябва да съдържа точно толкова на брой стойности, разделени от даден разделител, колкото са посочените колони в първия ред.
### Формат на генерираната таблица
Важно : Спазвайте стриктно инструкциите за форматиране на таблицата. Един символ невнимание може да доведе до огромно количество фейлващи тестове. Интервал не означава произволен символ за whitespace!

* Всеки ред започва и завършва с права черта |
* Преди всяка права черта (без първата) има поне един интервал.
* След всяка права черта (без последната) има точно един интервал.
* Броят символи на най-дългия елемент на колона ще приемаме за maxLength на колоната. За улеснение приемаме, че maxLength е минимум 3.
* Тогава всяка една колона има следния формат |\s.......\s| където \s е интервал, а броят на . е равен на maxLength.
* Ако данните в една колона са по-кратки от maxLength, допълваме интервали, а за втори ред -
* Вторият ред се състои от същия на брой колони, но са запълнени с тирета. Броят на тиретата отговаря на дължината на най-дългата данна от съответната колона.
* Вторият ред носи информация за подравняване на всяка една от колоните - ляво, дясно, централно. За да подравним даден ред, слагаме символа ':' на страната, на която искаме да е подреден, а за централно и от двете. Символът заема мястото на едно тире -. Пример :
    * | :---------- | за подравняване вляво
    * | ----------: | за подравняване вдясно
    * | :---------: | за подравняване в центъра
    * | ----------- | за подравняване по подразбиране
* Първият ред съдържа имената на колоните
* Всеки следващ ред съответства на ред данни от оригиналния файл, форматирани по описания начин
* Краят на таблицата е обработеният последен ред от CSV файла. НЯМА допълнителен празен ред след него. CSV файлът също не завършва с празен ред.
* Hint: Когато парсвате редовете по зададен разделител, обърнете внимание, че String.split функцията приема не низ, а регулярен израз за разделител. Съответно, някои символи имат  
* специално значение и трябва да бъдат escape-нати \\.. ТУК можете да намерите списък на всички такива символи. За добро ескейпване, можем да смятаме, че в началото на разделителя 
* слагаме стринга "\\Q", а в края му - "\\E". Тези два низа означават, че всичко, написано между тях, се интерпретира като текст, а не като regex.
Пример за правилно форматиране:

CSV:

```
hdr,testheader,z
testcolumn,b,c
```
Markdown:
```
| hdr        | testheader | z   |
| ---------: | :--------- | :-: |
| testcolumn | b          | c   |
```

Пояснение: Всяка колона започва със задължителен интервал. За първата колона, maxLength е равно на броя символи в testcolumn - 10. Искаме всеки запис от тази колона да има точно 10 + 2 (интервал в начало и край) символа. Следователно, за да запазим подравняването, добавяме 10 - 3 (дължината на hdr) интервала към него + 1 задължителен, който има преди всяка права черта. За втория ред добавяме низ, състоящ се от тирета и двуеточия, по начина описан по-горе. Неговият размер трябва да е равен на maxLength.

Пояснение 2: В третата колона се вижда изискването за минимален брой символи(3).

## CSVProcessor
В пакета `bg.sofia.uni.fmi.mjt.csvprocessor` създайте публичен клас `CsvProcessor`, който има конструктори

```java
public CsvProcessor() {
    this(new BaseTable());
}
```
        
```java
public CsvProcessor(Table table) {
    this.table = table;
}
```
и имплементира интерфейса CsvProcessorAPI:

```java
package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;

import java.io.Reader;
import java.io.Writer;

public interface CsvProcessorAPI {

    /**
     * Reads a CSV data from Reader
     * @param reader the Reader from which the CSV will be read
     * @param delimiter the delimeter used to split the CSV (such as ,.- and so on)
     * @throws CsvDataNotCorrectException if the CSV data is in wrong format
     */
    void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException;

    /**
     * Writes the content of the table to the provided Writer
     * @param writer - the Writer to which the table will be written
     */
    void writeTable(Writer writer, ColumnAlignment... alignments);

}
```

## Table
Прочетените данни се съхраняват в таблици, състоящи се от редове и колони. В пакета `bg.sofia.uni.fmi.mjt.csvprocessor.table` създайте публичен клас `BaseTable`, който имплементира следния интерфейс:
```java
package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;

import java.util.Collection;

public interface Table {
    
    /**
     * Adds data to the table. If the table doesn't have any columns,
     * the data parameter is interpreted as the table headers.
     * If the table contains columns, then the data parameter is interpreted as a row of data.
     * @param data - the data to be added
     * @throws CsvDataNotCorrectException - if the data is in incorrect format - if the count of 
     * the provided data parts is less or more than the number of columns in the table
     * @throws IllegalArgumentException if data is null
     */
     void addData(String[] data) throws CsvDataNotCorrectException;

    /**
     * Returns unmodifiable collection of the names of all columns
     */
     Collection<String> getColumnNames();

    /**
     * Returns unmodifiable collection of all strings in a specific column
     * @param column - the column for which the data will be returned
     * @throws IllegalArgumentException is column is null or blank or there is no corresponding column with that name
     * in the table
     */
     Collection<String> getColumnData(String column);

    /**
     * Returns the total count of all rows.
     */
     int getRowsCount();
     
}
```
В пакетa `bg.sofia.uni.fmi.mjt.csvprocessor.table.column` създайте публичен клас `BaseColumn`, чрез който се моделират колоните на таблиците. Той трябва да имплементира интерфейса `Column`:
```java
package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.Collection;

public interface Column {

    /**
     * Adds new data string to the column
     * @param data - the string to be added
     * @throws IllegalArgumentException if data is null or blank
     */
    void addData(String data);

    /**
     * Returns an unmodifiable collection of all data stored in the column
     */
    Collection<String> getData();
}
```

и да има следните конструктори:
```
    public BaseColumn() {
        this(new LinkedHashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
    }
```
## TablePrinter
За да е точно представянето на таблицата, създайте и клас `MarkdownTablePrinter`, който се грижи за правилното форматиране на таблицата преди принтиране. Той имплементира интерфейса `TablePrinter`:
```java
package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.Collection;

public interface TablePrinter {

  /**
   * Returns unmodifiable collection of strings, each one representing a formatted single row that needs to be printed
   * @param table - the table to be printed
   * @param alignments - the applied alignments for columns; if the number of given alignments is 
   * smaller than the number of columns, the remaining columns have NOALIGNMENT. If it's more, ignore the extra ones.
   */
  Collection<String> printTable(Table table, ColumnAlignment... alignments);

}
```
Подреждането на таблицата се моделира от enum-а `ColumnAlignment`
```

package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

public enum ColumnAlignment {
  LEFT(1),

  CENTER(2),

  RIGHT(1),

  NOALIGNMENT(0);

  private final int alignmentCharactersCount;

  ColumnAlignment(int count) {
    this.alignmentCharactersCount = count;
  }

  public int getAlignmentCharactersCount() {
    return alignmentCharactersCount;
  }
}
```
## Тестване
Валидирайте решението си чрез unit тестове.

## Пакети
Спазвайте имената на пакетите на всички по-горе описани типове, тъй като в противен случай, решението ви няма да може да бъде автоматично тествано.
```
src
└── bg.sofia.uni.fmi.mjt.csvprocessor
    ├── CsvProcessor.java
    ├── CsvProcessorAPI.java
    ├── exceptions
    │   └── CsvDataNotCorrectException.java
    └── table
        ├── BaseTable.java
        ├── Table.java
        ├── column
        │   ├── BaseColumn.java
        │   ├── Column.java
        │   └── (...)
        └── printer
            ├── ColumnAlignment.java
            ├── MarkdownTablePrinter.java
            ├── TablePrinter.java
            └── (...)
test
└── bg.sofia.uni.fmi.mjt.csvprocessor
    └── (...)
```