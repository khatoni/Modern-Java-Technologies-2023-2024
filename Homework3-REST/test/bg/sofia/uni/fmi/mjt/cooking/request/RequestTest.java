package bg.sofia.uni.fmi.mjt.cooking.request;

import bg.sofia.uni.fmi.mjt.cooking.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.cooking.enums.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.enums.MealType;
import bg.sofia.uni.fmi.mjt.cooking.requests.Request;
import bg.sofia.uni.fmi.mjt.cooking.requests.RequestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class RequestTest {

    @Test
    void testGetHTTPRequestURINullKeywords() {
        RequestBuilder builder = new RequestBuilder();
        builder.setKeywords(null);
        builder.setMealTypes(List.of(MealType.BREAKFAST));
        builder.setDietTypes(List.of(HealthLabel.GLUTEN_FREE));
        Request request = builder.build();
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&app_id=e53d72b3&" +
                "app_key=3caadca961f77282b1d6dc507ddab4cc&mealType=Breakfast&health=gluten-free";
        Assertions.assertEquals(expected, request.getHttpRequestURI(),
            "The received uri is not valid in terms of the parameters");
    }

    @Test
    void testGetHTTPRequestURINullMealTypes() {
        RequestBuilder builder = new RequestBuilder();
        builder.setKeywords(List.of("pizza"));
        builder.setMealTypes(null);
        builder.setDietTypes(List.of(HealthLabel.GLUTEN_FREE));
        Request request = builder.build();
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&q=pizza&app_id=e53d72b3&" +
                "app_key=3caadca961f77282b1d6dc507ddab4cc&health=gluten-free";
        Assertions.assertEquals(expected, request.getHttpRequestURI(),
            "The received uri is not valid in terms of the parameters");
    }

    @Test
    void testGetHTTPRequestURINullHealthTypes() {
        RequestBuilder builder = new RequestBuilder();
        builder.setKeywords(List.of("pizza"));
        builder.setMealTypes(List.of(MealType.DINNER, MealType.LUNCH));
        builder.setDietTypes(null);
        Request request = builder.build();
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&q=pizza&app_id=e53d72b3&" +
                "app_key=3caadca961f77282b1d6dc507ddab4cc&mealType=Dinner&mealType=Lunch";
        Assertions.assertEquals(expected, request.getHttpRequestURI(),
            "The received uri is not valid in terms of the parameters");
    }

    @Test
    void testGetHTTPRequestURINoNullData() {
        RequestBuilder builder = new RequestBuilder();
        builder.setKeywords(List.of("pizza"));
        builder.setMealTypes(List.of(MealType.DINNER, MealType.LUNCH));
        builder.setDietTypes(List.of(HealthLabel.DAIRY_FREE));
        Request request = builder.build();
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&q=pizza&app_id=e53d72b3&" +
                "app_key=3caadca961f77282b1d6dc507ddab4cc&mealType=Dinner&mealType=Lunch&health=dairy-free";
        Assertions.assertEquals(expected, request.getHttpRequestURI(),
            "The received uri is not valid in terms of the parameters");
    }

    @Test
    void testStaticFactoryMethodOf() {
        InputStream inputStream = new ByteArrayInputStream(
            ("YES" + System.lineSeparator() + "YES" + System.lineSeparator() + "YES" + System.lineSeparator() +
                "pizza" + System.lineSeparator() + "BREAKFAST" + System.lineSeparator() + "GLUTEN_FREE").getBytes());
        System.setIn(inputStream);
        Request request = Request.of(new Scanner(System.in), new CommandExecutor());
        Collection<String> expectedKeywords = List.of("pizza");
        Collection<HealthLabel> expectedHealthLabels = List.of(HealthLabel.GLUTEN_FREE);
        Collection<MealType> expectedMealTypes = List.of(MealType.BREAKFAST);
        Assertions.assertIterableEquals(expectedKeywords, request.getKeywords());
        Assertions.assertIterableEquals(expectedHealthLabels, request.getHealthLabels());
        Assertions.assertIterableEquals(expectedMealTypes, request.getMealTypes());
    }
}
