package bg.sofia.uni.fmi.mjt.cooking.command;

import bg.sofia.uni.fmi.mjt.cooking.enums.MealType;
import bg.sofia.uni.fmi.mjt.cooking.requests.RequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SearchByMealTypeCommand implements Command {

    private final RequestBuilder request;

    public SearchByMealTypeCommand(RequestBuilder request) {
        this.request = request;
    }

    @Override
    public void execute(Scanner scanner) {
        print();
        String[] words = readMeals(scanner);
        List<MealType> mealTypes = parseLabels(words);
        request.setMealTypes(mealTypes);
    }

    private void print() {
        System.out.println(
            "Enter meal types on one line separated with commas from the following list" + System.lineSeparator() +
                Arrays.toString(MealType.values()));
    }

    private String[] readMeals(Scanner scanner) {
        String getLine = scanner.nextLine();
        return getLine.split(",");
    }

    private List<MealType> parseLabels(String[] mealTypes) {
        List<MealType> meals = new ArrayList<>();
        for (String mealType : mealTypes) {
            meals.add(MealType.valueOf(mealType));
        }
        return meals;
    }
}
