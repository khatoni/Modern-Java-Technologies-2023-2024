package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.security.auth.kerberos.EncryptionKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MJTSpaceScannerTest {

    static final String missionsPathName = "./src/bg/sofia/uni/fmi/mjt/space/missions.txt";
    static final String rocketsPathName = "./src/bg/sofia/uni/fmi/mjt/space/rockets.txt";

    @BeforeAll
    static void testCreateMissionsFile() throws IOException {
        Path path = Paths.get(missionsPathName);
        Files.createFile(path);

        String data = """
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
            2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
            3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Success
            16,CASC,"LC-9, Taiyuan Satellite Launch Center, China","Fri Jul 03, 2020",Long March 4B | Gaofen Duomo & BY-02,StatusActive,"64.68 ",Success
            17,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Tue Jun 30, 2020",Falcon 9 Block 5 | GPS III SV03,StatusActive,"50.0 ",Success
            18,CASC,"LC-2, Xichang Satellite Launch Center, China","Tue Jun 23, 2020",Long March 3B/E | Beidou-3 G3,StatusActive,"29.15 ",Success
            19,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Wed Jun 17, 2020","Long March 2D | Gaofen-9 03, Pixing III A & HEAD-5",StatusActive,"29.75 ",Success
            """;
        try (var bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    @BeforeAll
    static void testCreateRocketsFile() throws IOException {
        Path path = Paths.get(rocketsPathName);
        Files.createFile(path);

        String data = """
            0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
            7,Vega,https://en.wikipedia.org/wiki/Vega_(rocket),29.9 m
            8,Vega C,https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m
            9,Vega E,https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m
            107,Black Arrow,https://en.wikipedia.org/wiki/Black_Arrow,13.0 m
            108,Blue Scout II,https://en.wikipedia.org/wiki/RM-90_Blue_Scout_II,24.0 m
            109,Ceres-1,,19.0 m
            110,Commercial Titan III,https://en.wikipedia.org/wiki/Commercial_Titan_III,47.0 m
            111,Conestoga-1620,https://en.wikipedia.org/wiki/Conestoga_(rocket),
            """;
        try (var bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    @Test
    void testGetAllMissionsByStatusNull() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getAllMissions(null), "IllegalArgumentException was expected");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetAllMissionsByStatusSuccessfully() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            Assertions.assertIterableEquals(spaceScanner.getAllMissions(),
                spaceScanner.getAllMissions(MissionStatus.SUCCESS),
                "The returned collection is not the same as the expected one");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsNullArguments() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null, LocalDate.now()),
                "Expected IllegalArgumentException, because one of the arguments is null");
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.now(), null),
                "Expected IllegalArgumentException, because one of the arguments is null");
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null, null),
                "Expected IllegalArgumentException, because both of the arguments is null");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsFromIsAfterTo() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.now().plusDays(3), LocalDate.now()),
                "Expected TimeFrameMismatchException, because from is before to");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsSuccessfully() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            String companyName = "CASC";
            Assertions.assertEquals(companyName,
                spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.MIN, LocalDate.MAX),
                "The expected company was CASC, but other company was given as a result");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsSuccessfullyEmptyMissions() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            String companyName = "";
            Assertions.assertEquals(companyName,
                spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.now(), LocalDate.now().plusDays(5)),
                "The expected company was the empty string, but other company was given as a result");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetMissionsPerCountrySuccessfully() {

        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Collection<Mission> missionList = spaceScanner.getAllMissions();
            Map<String, Collection<Mission>> countriesMap = new HashMap<>();
            Collection<Mission> missionInChina = new ArrayList<>();
            Collection<Mission> missionsInUSA = new ArrayList<>();
            Collection<Mission> missionsInKazakhstan = new ArrayList<>();
            for (Mission mission : missionList) {
                if (mission.location().contains("China")) {
                    missionInChina.add(mission);
                } else if (mission.location().contains("USA")) {
                    missionsInUSA.add(mission);
                } else {
                    missionsInKazakhstan.add(mission);
                }
            }
            countriesMap.put("Kazakhstan", missionsInKazakhstan);
            countriesMap.put("USA", missionsInUSA);
            countriesMap.put("China", missionInChina);

            Assertions.assertTrue(compareMaps(countriesMap, spaceScanner.getMissionsPerCountry()),
                "The countries map is not the same as the expected one");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static boolean compareMaps(Map<String, Collection<Mission>> first,
                                       Map<String, Collection<Mission>> second) {
        if (!first.keySet().equals(second.keySet())) {
            return false;
        }

        return first.entrySet().stream()
            .allMatch(entry -> areCollectionsEqual(entry.getValue(), second.get(entry.getKey())));
    }

    private static boolean areCollectionsEqual(Collection<Mission> first, Collection<Mission> second) {
        // Check if two collections are equivalent
        return first.size() == second.size() && first.containsAll(second);
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNegativeNumberOfMissions() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(-2, MissionStatus.SUCCESS,
                    RocketStatus.STATUS_RETIRED), "IllegalArgumentException was expected");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullStatus() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(5, null,
                    RocketStatus.STATUS_RETIRED), "IllegalArgumentException was expected");

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(5, MissionStatus.FAILURE,
                    null), "IllegalArgumentException was expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithStatusSuccessSuccessfully() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            Mission missionOne = Mission.of(
                "18,CASC,\"LC-2, Xichang Satellite Launch Center, China\",\"Tue Jun 23, 2020\",Long March 3B/E | Beidou-3 G3,StatusActive,\"29.15 \",Success");
            List<Mission> toTestList = new ArrayList<>();
            toTestList.add(missionOne);
            Assertions.assertIterableEquals(toTestList,
                spaceScanner.getTopNLeastExpensiveMissions(1, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompany() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Map<String, String> expected = new LinkedHashMap<>();
            expected.put("CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China");
            expected.put("Roscosmos", "Site 200/39, Baikonur Cosmodrome, Kazakhstan");
            expected.put("SpaceX", "LC-39A, Kennedy Space Center, Florida, USA");

            Assertions.assertEquals(expected, spaceScanner.getMostDesiredLocationForMissionsPerCompany(),
                "The returned map is not the same as the expected one");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyNullArguments() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.now()),
                "IllegalArgumentException was expected");
            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.now(), null),
                "IllegalArgumentException was expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyFromBeforeTo() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            Assertions.assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.now(),
                    LocalDate.now().minusDays(3)),
                "TimeFrameMismatchException was expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanySuccessfully() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Map<String, String> expected = new LinkedHashMap<>();
            expected.put("SpaceX", "LC-39A, Kennedy Space Center, Florida, USA");
            expected.put("Roscosmos", "Site 200/39, Baikonur Cosmodrome, Kazakhstan");
            expected.put("CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China");
            Assertions.assertEquals(expected,
                spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.MIN, LocalDate.now()),
                "The provided map is not the same as the expected one");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetTopNTallestRocketsNegativeN() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            Assertions.assertThrows(IllegalArgumentException.class, () -> spaceScanner.getTopNTallestRockets(-2),
                "IllegalArgumentException was expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetTopNTallestRocketsSuccessfully() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));
            List<Rocket> toTest = new ArrayList<>();
            Rocket rocketTest =
                Rocket.of("110,Commercial Titan III,https://en.wikipedia.org/wiki/Commercial_Titan_III,47.0 m");
            toTest.add(rocketTest);
            Assertions.assertEquals(toTest, spaceScanner.getTopNTallestRockets(1));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @Test
    void testGetWikiPagesForRockets() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Map<String, Optional<String>> expected = new LinkedHashMap<>();
            expected.put("Tsyklon-3", Optional.of("https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m"));
            expected.put("Vega", Optional.of("https://en.wikipedia.org/wiki/Vega_(rocket),29.9 m"));
            expected.put("Vega C", Optional.of("https://en.wikipedia.org/wiki/Vega_(rocket)"));
            expected.put("Vega E", Optional.of("https://en.wikipedia.org/wiki/Vega_(rocket),35.0 m"));
            expected.put("Black Arrow", Optional.of("https://en.wikipedia.org/wiki/Black_Arrow,13.0"));
            expected.put("Blue Scout II", Optional.of("https://en.wikipedia.org/wiki/RM-90_Blue_Scout_II"));
            expected.put("Ceres-1", Optional.empty());
            expected.put("Commercial Titan III", Optional.of("https://en.wikipedia.org/wiki/Commercial_Titan_III"));
            expected.put("Conestoga-1620", Optional.of("https://en.wikipedia.org/wiki/Conestoga_(rocket))"));

            Assertions.assertEquals(expected, spaceScanner.getWikiPageForRocket(),
                "The provided map is not the same as the expecte one");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsNegativeN() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(-1, MissionStatus.PARTIAL_FAILURE,
                    RocketStatus.STATUS_RETIRED),
                "IllegalArgumentException was expeceted");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsNullArguments() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, null,
                    RocketStatus.STATUS_RETIRED),
                "IllegalArgumentException was expeceted");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsSuccessfully() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            List<String> expectedWikiPage = new ArrayList<>();
            Assertions.assertEquals(expectedWikiPage,
                spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, MissionStatus.SUCCESS,
                    RocketStatus.STATUS_ACTIVE),
                "The provided list is not the same as the expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testSaveMostReliableRocketWithNullArguments() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.saveMostReliableRocket(null, LocalDate.now(), LocalDate.now()),
                "IllegalArgumentException is expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void testSaveMostReliableRocketWithFromAfterTo() {
        try (var missionsBufferedReader = Files.newBufferedReader(Path.of(missionsPathName));
             var rocketsBufferedReader = Files.newBufferedReader(Path.of(rocketsPathName))) {

            MJTSpaceScanner spaceScanner =
                new MJTSpaceScanner(missionsBufferedReader, rocketsBufferedReader, new EncryptionKey(new byte[5], 5));

            Assertions.assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), LocalDate.now().plusDays(3),
                    LocalDate.now()),
                "TimeFrameMismatchException is expected");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @AfterAll
    static void removeMissionsFile() {
        try {
            Files.delete(Path.of(missionsPathName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @AfterAll
    static void removeFile() {
        try {
            Files.delete(Path.of(rocketsPathName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
