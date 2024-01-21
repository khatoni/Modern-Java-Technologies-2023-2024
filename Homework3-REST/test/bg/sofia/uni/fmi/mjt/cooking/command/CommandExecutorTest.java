package bg.sofia.uni.fmi.mjt.cooking.command;

import bg.sofia.uni.fmi.mjt.cooking.command.Command;
import bg.sofia.uni.fmi.mjt.cooking.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByKeywordCommand;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByMealTypeCommand;
import bg.sofia.uni.fmi.mjt.cooking.enums.MealType;
import bg.sofia.uni.fmi.mjt.cooking.requests.Request;
import bg.sofia.uni.fmi.mjt.cooking.requests.RequestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class CommandExecutorTest {

    private CommandExecutor setCommandExecutor(Command command) {
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.addCommand(command);
        return commandExecutor;
    }

    @Test
    void testSuccessfullyAddedNewCommand() {
        Command command = new SearchByMealTypeCommand(new RequestBuilder());
        CommandExecutor commandExecutor = setCommandExecutor(command);
        Collection<Command> expected = new ArrayList<>();
        expected.add(command);
        Assertions.assertIterableEquals(expected, commandExecutor.getCommands(),
            "The expected list of commands differs from the received");
    }

    @Test
    void testAddNullCommand() {
        Command command = new SearchByMealTypeCommand(new RequestBuilder());
        CommandExecutor commandExecutor = setCommandExecutor(command);
        commandExecutor.addCommand(null);
        commandExecutor.addCommand(null);
        Collection<Command> expected = new ArrayList<>();
        expected.add(command);
        Assertions.assertIterableEquals(expected, commandExecutor.getCommands(),
            "The expected list of commands differs from the received");
    }

    @Test
    void testExecuteAllCommands() {
        RequestBuilder requestBuilder = new RequestBuilder();
        InputStream inputStream = new ByteArrayInputStream(("pizza" + System.lineSeparator() + "BREAKFAST").getBytes());
        System.setIn(inputStream);
        Command firstCommand = new SearchByKeywordCommand(requestBuilder);
        Command secondCommand = new SearchByMealTypeCommand(requestBuilder);
        CommandExecutor commandExecutor = new CommandExecutor(firstCommand, secondCommand);
        commandExecutor.executeAll(new Scanner(System.in));
        Request request = requestBuilder.build();
        Collection<String> expectedKeywords = new ArrayList<>();
        expectedKeywords.add("pizza");
        Assertions.assertIterableEquals(expectedKeywords, request.getKeywords(),
            "The expected list of keywords differs from the received one");
        Collection<MealType> expectedMeals = new ArrayList<>();
        expectedMeals.add(MealType.BREAKFAST);
        Assertions.assertIterableEquals(expectedMeals, request.getMealTypes(),
            "The expected list of meals differs from the received one");
    }
}
