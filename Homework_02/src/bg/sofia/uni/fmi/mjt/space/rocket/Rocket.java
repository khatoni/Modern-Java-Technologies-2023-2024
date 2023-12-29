package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Optional;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {
    private static final int ID_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int WIKI_INDEX = 2;
    private static final int HEIGHT_INDEX = 3;

    public static Rocket of(String line) {
        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String id = tokens[ID_INDEX];
        String name = tokens[NAME_INDEX];
        Optional<String> wiki = Optional.empty();

        if (tokens.length > WIKI_INDEX) {
            String wikiString = tokens[WIKI_INDEX];
            if (wikiString != null && !wikiString.isBlank()) {
                wiki = Optional.of(tokens[WIKI_INDEX]);
            }
        }
        Optional<Double> height = Optional.empty();
        if (tokens.length > HEIGHT_INDEX) {
            String heightString = tokens[HEIGHT_INDEX];
            if (heightString != null && !heightString.isBlank()) {
                String[] doubleTokens = heightString.split("\s");
                height = Optional.of(Double.valueOf(doubleTokens[0]));
            }
        }

        return new Rocket(id, name, wiki, height);
    }
}
