package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate,
                     int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality,
                     int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {
    private static final int NAME_INDEX = 0;
    private static final int FULL_NAME_INDEX = 1;
    private static final int BIRTH_TOKENS_INDEX = 2;
    private static final int AGE_INDEX = 3;
    private static final int HEIGHT_INDEX = 4;
    private static final int WEIGHT_INDEX = 5;
    private static final int NATIONALITY_INDEX = 7;
    private static final int OVERALL_RATING_INDEX = 8;
    private static final int POTENTIAL_INDEX = 9;
    private static final int VALUE_INDEX = 10;
    private static final int WAGE_IDNEX = 11;
    private static final int PREFERRED_FOOT_INDEX = 12;
    private static final String PEAK_ATTRIBUTE_DELIMITER = ";";

    public static Player of(String line) {
        String[] tokens = line.split(PEAK_ATTRIBUTE_DELIMITER);
        String name = tokens[NAME_INDEX];
        String fullName = tokens[FULL_NAME_INDEX];
        String[] birthTokens = tokens[BIRTH_TOKENS_INDEX].split("\\/");
        String year = birthTokens[2];
        String month = birthTokens[0];
        String day = birthTokens[1];
        LocalDate birthDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        int age = Integer.parseInt(tokens[AGE_INDEX]);
        double heightCm = Double.parseDouble(tokens[HEIGHT_INDEX]);
        double weightKg = Double.parseDouble(tokens[WEIGHT_INDEX]);
        List<Position> positions = new ArrayList<>();
        String nationality = tokens[NATIONALITY_INDEX];
        int overallRating = Integer.parseInt(tokens[OVERALL_RATING_INDEX]);
        int potential = Integer.parseInt(tokens[POTENTIAL_INDEX]);
        long valueEuro = Long.parseLong(tokens[VALUE_INDEX]);
        long wageEuro = Long.parseLong(tokens[WAGE_IDNEX]);
        Foot preferredFoot = switch (tokens[PREFERRED_FOOT_INDEX]) {
            case "Left" -> Foot.LEFT;
            case "Right" -> Foot.RIGHT;
            default -> null;
        };
        return new Player(name, fullName, birthDate,
            age, heightCm, weightKg,
            positions, nationality,
            overallRating, potential, valueEuro,
            wageEuro, preferredFoot);
    }
}
