package bg.sofia.uni.fmi.mjt.cooking;

import java.util.List;

public record Recipe(String label,
                     List<String> dietLabels,
                     List<String> healthLabels,
                     double totalWeight,
                     List<String> cuisineType,
                     List<String> mealType,
                     List<String> dishType,
                     List<String> ingredientLines) {
}
