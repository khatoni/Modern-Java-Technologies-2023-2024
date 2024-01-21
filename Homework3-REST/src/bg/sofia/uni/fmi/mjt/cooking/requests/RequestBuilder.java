package bg.sofia.uni.fmi.mjt.cooking.requests;

import bg.sofia.uni.fmi.mjt.cooking.enums.HealthLabel;
import bg.sofia.uni.fmi.mjt.cooking.enums.MealType;

import java.util.List;

public class RequestBuilder {
    List<String> keywords = null;
    List<MealType> mealTypes = null;
    List<HealthLabel> healthLabels = null;

    public RequestBuilder setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public RequestBuilder setMealTypes(List<MealType> mealTypes) {
        this.mealTypes = mealTypes;
        return this;
    }

    public RequestBuilder setDietTypes(List<HealthLabel> healthLabels) {
        this.healthLabels = healthLabels;
        return this;
    }

    public Request build() {
        return new Request(this);
    }
}
