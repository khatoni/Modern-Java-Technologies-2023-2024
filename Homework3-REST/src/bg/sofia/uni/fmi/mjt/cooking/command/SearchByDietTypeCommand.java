package bg.sofia.uni.fmi.mjt.cooking.command;

import bg.sofia.uni.fmi.mjt.cooking.enums.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.requests.RequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SearchByDietTypeCommand implements Command {
    private final RequestBuilder request;

    public SearchByDietTypeCommand(RequestBuilder request) {
        this.request = request;
    }

    @Override
    public void execute(Scanner scanner) {
        print();
        String[] words = readDietLabels(scanner);
        List<HealthLabel> healthLabels = parseLabels(words);
        request.setDietTypes(healthLabels);
    }

    private void print() {
        System.out.println(
            "Enter health labels on one line separated with commas from the following list" + System.lineSeparator() +
                Arrays.toString(HealthLabel.values()));
    }

    private String[] readDietLabels(Scanner scanner) {
        String getLine = scanner.nextLine();
        return getLine.split(",");
    }

    private List<HealthLabel> parseLabels(String[] dietLabels) {
        List<HealthLabel> healthLabels = new ArrayList<>();
        for (String dietLabel : dietLabels) {
            healthLabels.add(HealthLabel.valueOf(dietLabel));
        }
        return healthLabels;
    }
}
