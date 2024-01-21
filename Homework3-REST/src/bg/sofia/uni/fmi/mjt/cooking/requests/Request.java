package bg.sofia.uni.fmi.mjt.cooking.requests;

import bg.sofia.uni.fmi.mjt.cooking.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByDietTypeCommand;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByKeywordCommand;
import bg.sofia.uni.fmi.mjt.cooking.command.SearchByMealTypeCommand;
import bg.sofia.uni.fmi.mjt.cooking.enums.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.enums.MealType;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Request {
    private static final String APPLICATION_KEY = "3caadca961f77282b1d6dc507ddab4cc";
    private static final String APPLICATION_ID = "e53d72b3";
    private final String host = "https://api.edamam.com/api/recipes/v2?type=public";
    private final List<String> keywords;
    private final List<MealType> mealTypes;
    private final List<HealthLabel> healthLabels;

    public Request(RequestBuilder requestBuilder) {
        this.keywords = requestBuilder.keywords;
        this.mealTypes = requestBuilder.mealTypes;
        this.healthLabels = requestBuilder.healthLabels;
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public Collection<HealthLabel> getHealthLabels() {
        return healthLabels;
    }

    public Collection<MealType> getMealTypes() {
        return mealTypes;
    }

    public Collection<String> getKeywords() {
        return keywords;
    }

    public String getHttpRequestURI() {
        StringBuilder uri = new StringBuilder(getEndPoint());
        String substringWord = "public";
        return uri
            .insert(uri.lastIndexOf(substringWord) + substringWord.length(), addKeywords())
            .append(addMealTypes())
            .append(addHealthLabels())
            .toString();
    }

    public static Request of(Scanner scanner, CommandExecutor commandExecutor) {
        RequestBuilder requestBuilder = new RequestBuilder();
        System.out.println("Are you going to use search by keyword: YES/NO");
        if (scanner.nextLine().equals("YES")) {
            commandExecutor.addCommand(new SearchByKeywordCommand(requestBuilder));
        }
        System.out.println("Are you going to use search by meal type: YES/NO");
        if (scanner.nextLine().equals("YES")) {
            commandExecutor.addCommand(new SearchByMealTypeCommand(requestBuilder));
        }
        System.out.println("Are you going to use search by diet type: YES/NO");
        if (scanner.nextLine().equals("YES")) {
            commandExecutor.addCommand(new SearchByDietTypeCommand(requestBuilder));
        }
        commandExecutor.executeAll(scanner);
        return new Request(requestBuilder);
    }

    private String getEndPoint() {
        return new StringBuilder(host)
            .append("&app_id=")
            .append(APPLICATION_ID)
            .append("&app_key=")
            .append(APPLICATION_KEY)
            .toString();
    }

    private String addKeywords() {
        if (keywords == null || keywords.isEmpty()) {
            return "";
        }
        StringBuilder keywordsBuilder = new StringBuilder();
        for (String keyword : keywords) {
            keywordsBuilder.append("&q=")
                .append(keyword);
        }
        return keywordsBuilder.toString();
    }

    private String addMealTypes() {
        if (mealTypes == null || mealTypes.isEmpty()) {
            return "";
        }
        StringBuilder mealBuilder = new StringBuilder();
        for (MealType mealType : mealTypes) {
            mealBuilder.append("&mealType=")
                .append(mealType.getUriAnalog());
        }
        return mealBuilder.toString();
    }

    private String addHealthLabels() {
        if (healthLabels == null || healthLabels.isEmpty()) {
            return "";
        }
        StringBuilder healthLabelBuilder = new StringBuilder();
        for (HealthLabel healthLabel : healthLabels) {
            healthLabelBuilder.append("&health=")
                .append(healthLabel.getUriAnalog());
        }
        return healthLabelBuilder.toString();
    }

}

