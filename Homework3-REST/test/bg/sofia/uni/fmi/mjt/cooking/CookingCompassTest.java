package bg.sofia.uni.fmi.mjt.cooking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Collection;

public class CookingCompassTest {

    private HttpClient client;
    private HttpResponse response;

    @BeforeEach
    void setData() {
        client = Mockito.mock(HttpClient.class);
        response = Mockito.mock(HttpResponse.class);
    }

    private void setMockWhenData(int code) throws IOException, InterruptedException {
        Mockito.when(response.statusCode()).thenReturn(code);
        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(response);
    }

    @Test
    void searchRecipeStatusCodeRedirection() throws IOException, InterruptedException {
        setMockWhenData(300);
        CookingCompass cookingCompass = new CookingCompass(client);
        InputStream inputStream = new ByteArrayInputStream((
            "YES" + System.lineSeparator() + "NO" + System.lineSeparator() + "NO" + System.lineSeparator() +
                "fmi").getBytes());
        System.setIn(inputStream);
        Assertions.assertNull(cookingCompass.searchRecipe(), "The searchRecipe function should return null");
    }

    @Test
    void searchRecipeStatusCodeClientError() throws IOException, InterruptedException {
        setMockWhenData(400);
        CookingCompass cookingCompass = new CookingCompass(client);
        InputStream inputStream = new ByteArrayInputStream((
            "YES" + System.lineSeparator() + "NO" + System.lineSeparator() + "NO" + System.lineSeparator() +
                "fmi").getBytes());
        System.setIn(inputStream);
        Assertions.assertNull(cookingCompass.searchRecipe(), "The searchRecipe function should return null");
    }

    @Test
    void searchRecipeStatusCodeServerError() throws IOException, InterruptedException {
        setMockWhenData(500);
        CookingCompass cookingCompass = new CookingCompass(client);
        InputStream inputStream = new ByteArrayInputStream((
            "YES" + System.lineSeparator() + "NO" + System.lineSeparator() + "NO" + System.lineSeparator() +
                "fmi").getBytes());
        System.setIn(inputStream);
        Assertions.assertNull(cookingCompass.searchRecipe(), "The searchRecipe function should return null");
    }

    @Test
    void searchRecipeStatusCodeOKOnlyOnePage() throws IOException, InterruptedException {
        setMockWhenData(200);
        String testJson = """
            {
              "from": 1,
              "to": 8,
              "count": 8,
              "_links": {},
              "hits": [
                {
                  "recipe": {
                    "dietLabels": ["Low-Sodium"],
                    "healthLabels": [
                      "Sugar-Conscious",
                      "Low Potassium",
                      "Mollusk-Free",
                      "Kosher",
                      "Alcohol-Cocktail"
                    ],
                    "cautions": ["Sulfites"],
                    "mealType": ["lunch/dinner"]
                  }
                }
              ]
            }""";
        Mockito.when(response.body()).thenReturn(testJson);
        CookingCompass cookingCompass = new CookingCompass(client);
        InputStream inputStream = new ByteArrayInputStream((
            "YES" + System.lineSeparator() + "NO" + System.lineSeparator() + "NO" + System.lineSeparator() +
                "fmi").getBytes());
        System.setIn(inputStream);
        Collection<Recipe> answer = cookingCompass.searchRecipe();
        Assertions.assertEquals(1, answer.size(), "The expected size is 1, because of the mock response");
    }

    @Test
    void testSearchRecipeTwoPages() throws IOException, InterruptedException {
        setMockWhenData(200);
        String testJson = """
            {
              "from": 1,
              "to": 20,
              "count": 100000,
              "_links": { "next": {
                  "href": "https://api.edamam.com/api/recipes/v2?app_key=3caadca961f77282b1d6dc507ddab4cc&_cont=CHcVQBtNNQphDmgVQntAEX4BY0t3AgsDSmxJCmsaalx6DQoORHdcETASNQYiDAEFQzETUGcbZwcnDAdURGFEUmsWN1UiDFcEUQhcETRRPAhhDgEHDg%3D%3D&health=celery-free&diet=high-fiber&type=public&app_id=e53d72b3",
                  "title": "Next page"
                }},
              "hits": [
                {
                  "recipe": {
                    "dietLabels": ["Low-Sodium"],
                    "healthLabels": [
                      "Sugar-Conscious",
                      "Low Potassium",
                      "Mollusk-Free",
                      "Kosher",
                      "Alcohol-Cocktail"
                    ],
                    "cautions": ["Sulfites"],
                    "mealType": ["lunch/dinner"]
                  }
                }
              ]
            }""";
        Mockito.when(response.body()).thenReturn(testJson);
        CookingCompass cookingCompass = new CookingCompass(client);
        InputStream inputStream = new ByteArrayInputStream((
            "YES" + System.lineSeparator() + "NO" + System.lineSeparator() + "NO" + System.lineSeparator() +
                "fmi").getBytes());
        System.setIn(inputStream);
        Collection<Recipe> answer = cookingCompass.searchRecipe();
        Assertions.assertEquals(2, answer.size(),
            "Expected size of the collection is 2, because the same mock response is used for the first and second page");
    }
}
