package bg.sofia.uni.fmi.mjt.cooking.enums;

public enum HealthLabel {
    ALCOHOL_COCTAIL("alcohol-cocktail"),
    ALCOHOL_FREE("alcohol-free"),
    CELERY_FREE("celery-free"),
    CRUSTACEAN_FREE("crustacean-free"),
    DAIRY_FREE("dairy-free"),
    DASH("DASH"),
    EGG_FREE("egg-free"),
    FISH_FREE("fish-free"),
    FODMAP_FREE("fodmap-free"),
    GLUTEN_FREE("gluten-free"),
    IMUNO_SUPORTIVE("immuno-supportive"),
    KETO_FRIENDLY("keto-friendly"),
    KIDNEY_FRIENDLY("kidney-friendly"),
    KOSHER("kosher"),
    LOW_POTASSIUM("low-potassium"),
    LOW_SUGAR("low-sugar"),
    LUPINE_FREE("lupine-free"),
    MEDITERRANEAN("Mediterranean"),
    MOLLUSK_FREE("mollusk-free"),
    MUSTARD_FREE("mustard-free"),
    NO_OILD_ADDER("No-oil-added"),
    PALEO("paleo"),
    PEANUT_FREE("peanut-free"),
    PECATARIAN("pecatarian"),
    PORK_FREE("pork-free"),
    RED_MEAT_FREE("red-meat-free"),
    SESAME_FREE("sesame-free"),
    SHELLFISH_FREE("shellfish-free"),
    SOY_FREE("soy-free"),
    SUGAR_CONSCIOUS("sugar-conscious"),
    SULFITE_FREE("sulfite-free"),
    TREE_NUT_FREE("tree-nut-free"),
    VEGAN("vegan"),
    VEGETARIAN("vegetarian"),
    WHEAT_FREE("wheat-free");

    private final String uriAnalog;

    HealthLabel(String uriAnalog) {
        this.uriAnalog = uriAnalog;
    }

    public String getUriAnalog() {
        return uriAnalog;
    }
}
