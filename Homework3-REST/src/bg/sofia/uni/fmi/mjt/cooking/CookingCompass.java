package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.cooking.exceptions.ClientErrorMessage;
import bg.sofia.uni.fmi.mjt.cooking.exceptions.RedirectionErrorMessage;
import bg.sofia.uni.fmi.mjt.cooking.exceptions.ServerErrorMessage;
import bg.sofia.uni.fmi.mjt.cooking.parsedjson.JsonHit;
import bg.sofia.uni.fmi.mjt.cooking.requests.Request;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CookingCompass implements Search {
    private final HttpClient client;
    private static final int DIVISOR = 100;
    private static final int OK_STATUS_CODE = 2;
    private static final int REDIRECTION_ERROR_STATUS_CODE = 3;
    private static final int CLIENT_ERROR_STATUS_CODE = 4;
    private static final int SERVER_ERROR_STATUS_CODE = 5;

    public CookingCompass(HttpClient client) {
        this.client = client;
    }

    @Override
    public Collection<Recipe> searchRecipe() {
        Request consoleRequest = Request.of(new Scanner(System.in), new CommandExecutor());
        HttpResponse<String> response = executeHttpRequest(consoleRequest.getHttpRequestURI());
        JsonHit parsedJson = new Gson().fromJson(response.body(), JsonHit.class);
        Collection<Recipe> answer = parseToRecipeList(response);
        if (response.statusCode() / DIVISOR == OK_STATUS_CODE && parsedJson.getNextPageLink() != null) {
            HttpResponse<String> secondPageResponse = executeHttpRequest(parsedJson.getNextPageLink());
            Collection<Recipe> secondPage = parseToRecipeList(secondPageResponse);
            answer.addAll(secondPage);
        }
        return answer;

    }

    private HttpResponse<String> executeHttpRequest(String requestURI) {
        URI uri = URI.create(requestURI);
        HttpRequest request = buildRequest(uri);
        return receiveResponse(request);
    }

    private HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder().uri(uri).build();
    }

    private HttpResponse<String> receiveResponse(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    private Optional<String> getAnswer(HttpResponse<String> response)
        throws ClientErrorMessage, RedirectionErrorMessage, ServerErrorMessage {

        if (response == null) {
            return Optional.empty();
        }
        return switch (response.statusCode() / DIVISOR) {
            case OK_STATUS_CODE -> Optional.of(response.body());
            case REDIRECTION_ERROR_STATUS_CODE -> throw new RedirectionErrorMessage();
            case CLIENT_ERROR_STATUS_CODE -> throw new ClientErrorMessage();
            case SERVER_ERROR_STATUS_CODE -> throw new ServerErrorMessage();
            default -> Optional.empty();
        };
    }

    private List<Recipe> parseToRecipeList(HttpResponse<String> response) {
        try {
            Optional<String> answer = getAnswer(response);
            if (answer.isPresent()) {
                return new JsonHit().parseFromJsonToCollection(answer.get());
            } else {
                return new ArrayList<>();
            }
        } catch (ClientErrorMessage | RedirectionErrorMessage | ServerErrorMessage e) {
            return null;
        }
    }

    public static void main(String[] args) {
        /* this is short example how to use the app
        RequestBuilder request = new RequestBuilder();
        CommandExecutor commandExecutor = new CommandExecutor();
        CookingCompass cookingCompass = new CookingCompass(HttpClient.newHttpClient());
        Collection<Recipe> recipes = cookingCompass.searchRecipe();
        System.out.println("end");
        */
    }
}
