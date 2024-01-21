package bg.sofia.uni.fmi.mjt.cooking.enums;

public enum MealType {

    BREAKFAST("Breakfast"),

    BRUNCH("Brunch"),

    LUNCH("Lunch"),

    DINNER("Dinner"),

    SNACK("Snack"),

    TEATIME("Teatime");
    private final String uriAnalog;

    MealType(String uriAnalog) {
        this.uriAnalog = uriAnalog;
    }

    public String getUriAnalog() {
        return uriAnalog;
    }
}
