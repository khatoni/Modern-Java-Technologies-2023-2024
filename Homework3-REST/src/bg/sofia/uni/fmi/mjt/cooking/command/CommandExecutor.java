package bg.sofia.uni.fmi.mjt.cooking.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CommandExecutor {
    private final List<Command> commands;

    public CommandExecutor(Command... commands) {
        this.commands = new ArrayList<>();
        if (commands != null) {
            for (Command command : commands) {
                if (command != null) {
                    this.commands.add(command);
                }
            }
        }
    }

    public void addCommand(Command command) {
        if (command != null) {
            commands.add(command);
        }
    }

    public void executeAll(Scanner scanner) {

        for (Command command : commands) {
            command.execute(scanner);
        }
    }

    public Collection<Command> getCommands() {
        return commands;
    }
}
