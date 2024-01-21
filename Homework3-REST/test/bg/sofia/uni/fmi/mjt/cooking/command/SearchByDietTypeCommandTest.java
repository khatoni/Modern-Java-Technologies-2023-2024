package bg.sofia.uni.fmi.mjt.cooking.command;

import bg.sofia.uni.fmi.mjt.cooking.command.Command;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByDietTypeCommand;
import bg.sofia.uni.fmi.mjt.cooking.enums.HealthLabel;
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

public class SearchByDietTypeCommandTest {
    private OutputStream outputStream;
    private InputStream inputStream;
    private PrintStream printStream;

    @BeforeEach
    void setUpTestData() {
        inputStream = new ByteArrayInputStream("GLUTEN_FREE".getBytes());
        System.setIn(inputStream);
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    @Test
    void testExecuteCommandCaptureOutputToStdOut() {
        RequestBuilder requestBuilder = new RequestBuilder();
        Command command = new SearchByDietTypeCommand(requestBuilder);
        command.execute(new Scanner(System.in));
        String toTest = outputStream.toString();
        String expected =
            "Enter health labels on one line separated with commas from the following list" + System.lineSeparator() +
                "[ALCOHOL_COCTAIL, ALCOHOL_FREE, CELERY_FREE, CRUSTACEAN_FREE, DAIRY_FREE, DASH, EGG_FREE, FISH_FREE," +
                " FODMAP_FREE, GLUTEN_FREE, IMUNO_SUPORTIVE, KETO_FRIENDLY, KIDNEY_FRIENDLY, KOSHER, LOW_POTASSIUM," +
                " LOW_SUGAR, LUPINE_FREE, MEDITERRANEAN, MOLLUSK_FREE, MUSTARD_FREE, NO_OILD_ADDER, PALEO," +
                " PEANUT_FREE, PECATARIAN, PORK_FREE, RED_MEAT_FREE, SESAME_FREE, SHELLFISH_FREE, SOY_FREE," +
                " SUGAR_CONSCIOUS, SULFITE_FREE, TREE_NUT_FREE, VEGAN, VEGETARIAN, WHEAT_FREE]";
        Assertions.assertEquals(toTest.trim(), expected.trim(),
            "The output message is not the same as the expected one");
    }

    @Test
    void testExecuteCommandParseValuesSuccessfully() {
        RequestBuilder requestBuilder = new RequestBuilder();
        Command command = new SearchByDietTypeCommand(requestBuilder);
        command.execute(new Scanner(System.in));
        Request request = requestBuilder.build();
        Collection<HealthLabel> expected = new ArrayList<>();
        expected.add(HealthLabel.GLUTEN_FREE);
        Assertions.assertIterableEquals(expected, request.getHealthLabels(),
            "The returned collection of parsed labels is not the same as the expected one");
    }

    @Test
    void testExecuteCommandParseUnsuccessfulParse() {
        inputStream = new ByteArrayInputStream("glutene_free".getBytes());
        System.setIn(inputStream);
        RequestBuilder requestBuilder = new RequestBuilder();
        Command command = new SearchByDietTypeCommand(requestBuilder);
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
