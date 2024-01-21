package bg.sofia.uni.fmi.mjt.cooking.command;

import bg.sofia.uni.fmi.mjt.cooking.command.Command;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByMealTypeCommand;
import bg.sofia.uni.fmi.mjt.cooking.enums.MealType;
import bg.sofia.uni.fmi.mjt.cooking.requests.Request;
import bg.sofia.uni.fmi.mjt.cooking.requests.RequestBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class SearchByMealTypeCommandTest {
    private OutputStream outputStream;
    private InputStream inputStream;
    private PrintStream printStream;

    @BeforeEach
    void setUpTestData() {
        inputStream = new ByteArrayInputStream("BREAKFAST".getBytes());
        System.setIn(inputStream);
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    @Test
    void testExecuteCommandCaptureOutputToStdOut() {
        RequestBuilder requestBuilder = new RequestBuilder();
        Command command = new SearchByMealTypeCommand(requestBuilder);
        command.execute(new Scanner(System.in));
        String toTest = outputStream.toString();
        String expected =
            "Enter meal types on one line separated with commas from the following list" + System.lineSeparator() +
                "[BREAKFAST, BRUNCH, LUNCH, DINNER, SNACK, TEATIME]";
        Assertions.assertEquals(toTest.trim(), expected.trim(),
            "The output message is not the same as the expected one");
    }

    @Test
    void testExecuteCommandParseValuesSuccessfully() {
        RequestBuilder requestBuilder = new RequestBuilder();
        Command command = new SearchByMealTypeCommand(requestBuilder);
        command.execute(new Scanner(System.in));
        Request request = requestBuilder.build();
        Collection<MealType> expected = new ArrayList<>();
        expected.add(MealType.BREAKFAST);
        Assertions.assertIterableEquals(expected, request.getMealTypes(),
            "The returned collection of parsed labels is not the same as the expected one");
    }

    @Test
    void testExecuteCommandParseUnsuccessfulParse() {
        inputStream = new ByteArrayInputStream("brikfast".getBytes());
        System.setIn(inputStream);
        RequestBuilder requestBuilder = new RequestBuilder();
        Command command = new SearchByMealTypeCommand(requestBuilder);
        Assertions.assertThrows(IllegalArgumentException.class, () -> command.execute(new Scanner(System.in)),
            "IllegalArgumentException was expected, but nothing was thrown");
    }

    @AfterEach()
    void clearStreams() {
        try {
            inputStream.close();
            printStream.close();
        } catch (IOException e) {
            throw new UncheckedIOException("There was problem when closing the inputStream for the test", e);
        }
    }
}
