package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FootballPlayerAnalyzerTest {

    static final String pathName = "./src/bg/sofia/uni/fmi/mjt/football/players.txt";

    @BeforeAll
    static void testCreateFile() throws IOException {
        Path path = Paths.get("./src/bg/sofia/uni/fmi/mjt/football/players.txt");
        Files.createFile(path);

        String data = """
            L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left
            C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right
            P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right
            L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right
            K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right
            V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right
            K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right
            S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right
            M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right
            A. Griezmann;Antoine Griezmann;3/21/1991;27;175.26;73;CF,ST;France;92;90;78000000;145000;Left;
            """;
        try (var bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    @Test
    void testSuccessfullyCreatedPlayers() {

        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {

            FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Assertions.assertEquals(10, footballPlayerAnalyzer.getAllPlayers().size());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testAllNationalitiesFromFile() {
        Set<String> nationalities = new LinkedHashSet<>();
        nationalities.add("Argentina");
        nationalities.add("Denmark");
        nationalities.add("France");
        nationalities.add("Italy");
        nationalities.add("Senegal");
        nationalities.add("Netherlands");
        nationalities.add("Germany");
        FootballPlayerAnalyzer footballPlayerAnalyzer = null;
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        Assertions.assertIterableEquals(nationalities, footballPlayerAnalyzer.getAllNationalities());
    }

    @Test
    void testGetHighestPaidPlayerByNationalityInvalidNationality() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality(null));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetHighestPaidPlayerByNationalitySuccessfull() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Player tmpPlayer = Player.of(
                "L. Messi;Lionel Andrés Messi Cuccittini;" +
                    "6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;" +
                    "94;94;110500000;565000;Left");
            Assertions.assertEquals(tmpPlayer, footballPlayerAnalyzer.getHighestPaidPlayerByNationality("Argentina"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetPlayerByFullNameKeywordInvalidKeyword() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> footballPlayerAnalyzer.getPlayersByFullNameKeyword(""));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void groupByPosition() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Map<Position, Set<Player>> positions = new HashMap<>();
            Assertions.assertEquals(positions, footballPlayerAnalyzer.groupByPosition());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetPlayerByFullNameKeywordSuccessfully() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Player tmpPlayerMessi = Player.of(
                "L. Messi;Lionel Andrés Messi Cuccittini;" +
                    "6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;" +
                    "94;94;110500000;565000;Left");
            Player tmpPlayerAguero = Player.of(
                "S. Agüero;Sergio Leonel Agüero del Castillo;" +
                    "6/2/1988;30;172.72;69.9;ST;Argentina;" +
                    "89;89;64500000;300000;Right");
            Set<Player> players = new LinkedHashSet<>();
            players.add(tmpPlayerMessi);
            players.add(tmpPlayerAguero);
            Assertions.assertIterableEquals(players, footballPlayerAnalyzer.getPlayersByFullNameKeyword("onel"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetTopProspectPlayerForPositionSuccessfully() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Player player = Player.of("M. Neuer;Manuel Neuer;" +
                "3/27/1986;32;193.04;92.1;" +
                "GK;Germany;89;89;38000000;130000;Right"
            );
            Optional<Player> toTest = Optional.of(player);
            Assertions.assertEquals(toTest,
                footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(Position.GK, 40_000_000));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetSimilarPlayer() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Player tmpPlayerMessi = Player.of(
                "L. Messi;Lionel Andrés Messi Cuccittini;" +
                    "6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;" +
                    "94;94;110500000;565000;Left");
            Player expected = Player.of(
                "A. Griezmann;Antoine Griezmann;3/21/1991;27;175.26;73;CF,ST;" +
                    "France;92;90;78000000;145000;Left");
            Set<Player> answer = new LinkedHashSet<>();
            answer.add(tmpPlayerMessi);
            answer.add(expected);
            Assertions.assertIterableEquals(answer, footballPlayerAnalyzer.getSimilarPlayers(tmpPlayerMessi));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetTopProspectPlayerForPositionInvalidData() {
        try (var bufferedReader = Files.newBufferedReader(Path.of(pathName))) {
            final FootballPlayerAnalyzer footballPlayerAnalyzer = new FootballPlayerAnalyzer(bufferedReader);
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(Position.CB, -10));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @AfterAll
    static void removeFile() {

        try {
            Files.delete(Path.of(pathName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
