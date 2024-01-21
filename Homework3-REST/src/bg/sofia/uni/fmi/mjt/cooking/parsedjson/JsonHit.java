package bg.sofia.uni.fmi.mjt.cooking.parsedjson;

import bg.sofia.uni.fmi.mjt.cooking.Recipe;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JsonHit {
    private final List<Hit> hits;
    private int from;
    private int to;
    private int count;
    @SerializedName("_links")
    private Links links;

    public JsonHit() {
        hits = null;
    }

    public List<Recipe> parseFromJsonToCollection(String json) {
        JsonHit jsonHit = new Gson().fromJson(json, JsonHit.class);
        List<Recipe> answer = new ArrayList<>();
        if (jsonHit.hits == null) {
            return null;
        }
        for (Hit hit : jsonHit.hits) {
            answer.add(hit.recipe());
        }
        return answer;
    }

    public String getNextPageLink() {
        if (links != null && links.next() != null) {
            return links.next().href();
        }
        return null;
    }
}
