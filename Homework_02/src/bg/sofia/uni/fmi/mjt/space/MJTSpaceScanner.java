package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MJTSpaceScanner implements SpaceScannerAPI {

    private final SecretKey secretKey;
    private final List<Mission> missions;
    private final List<Rocket> rockets;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        this.secretKey = secretKey;

        try (var br = new BufferedReader(missionsReader)) {
            missions = br.lines()//
                .skip(1)//
                .map(Mission::of)//
                .toList();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }

        try (var br = new BufferedReader(rocketsReader)) {
            rockets = br.lines()//
                .skip(1)//
                .map(Rocket::of)//
                .toList();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    @Override
    public Collection<Mission> getAllMissions() {
        if (missions == null || missions.isEmpty()) {
            return Collections.emptyList();
        }
        return missions;
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("The missionStatus is null");
        }
        if (missions == null || missions.isEmpty()) {
            return Collections.emptyList();
        }

        return missions
            .stream()
            .filter(m -> m.missionStatus() == missionStatus)
            .toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("The from or to is null");
        }
        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To is before from");
        }
        if (missions == null || missions.isEmpty()) {
            return "";
        }

        Map.Entry<String, Long> answer =
            missions
                .stream()
                .filter(m -> m.date().isAfter(from) && m.date().isBefore(to))
                .filter(m -> m.missionStatus() == MissionStatus.SUCCESS)
                .map(Mission::company)
                .collect(Collectors.groupingBy(m -> m, Collectors.counting()))
                .entrySet()
                .stream()
                .distinct()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        return answer == null ? "" : answer.getKey();
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {

        return missions
            .stream()
            .collect(Collectors.groupingBy(this::getCountryFromMission, Collectors.toCollection(HashSet::new)));
    }

    private String getCountryFromMission(Mission mission) {
        String[] tokens = mission.location().split(",");
        return tokens[tokens.length - 1];
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {

        if (n <= 0 || missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("n is <= 0 or missionStatus or rocketStatus is null");
        }

        return missions
            .stream()
            .sorted(Comparator.comparing(m -> m.cost().orElse(Double.MAX_VALUE)))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {

        List<String> companies = missions
            .stream()
            .map(Mission::company)
            .distinct()
            .toList();
        Map<String, String> answer = new HashMap<>();

        for (String companyName : companies) {
            Map<String, Long> locations = missions
                .stream()
                .filter(m -> m.company().equals(companyName))
                .collect(Collectors.groupingBy(Mission::location, Collectors.counting()));
            String answerLocation =
                locations.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .limit(1).toString();
            answer.put(companyName, answerLocation);
        }
        return answer;
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from or to is null");
        }

        if (from.isAfter(to)) {
            throw new TimeFrameMismatchException("from is after to");
        }
        List<String> companies = missions
            .stream()
            .map(Mission::company)
            .distinct()
            .toList();
        Map<String, String> answer = new HashMap<>();

        for (String companyName : companies) {
            Map<String, Long> locations = missions
                .stream()
                .filter(m -> m.company().equals(companyName))
                .filter(m -> m.missionStatus() == MissionStatus.SUCCESS)
                .collect(Collectors.groupingBy(Mission::location, Collectors.counting()));
            String answerLocation =
                locations.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .limit(1).toString();
            answer.put(companyName, answerLocation);
        }
        return answer;
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return rockets;
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is <= 0");
        }
        return rockets
            .stream()
            .sorted(Comparator.comparing((Rocket r) -> r.height().orElse(Double.MIN_VALUE)).reversed())
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets
            .stream()
            .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0 || missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("n is <= 0 , missionStatus or rocketStatus are null");
        }
        List<String> rocketNames =
            missions
                .stream()
                .sorted((m1, m2) -> m2.cost().orElse(Double.MAX_VALUE).compareTo(m1.cost().orElse(Double.MAX_VALUE)))
                .limit(n)
                .map(m -> m.detail().rocketName())
                .toList();

        return rockets
            .stream()
            .filter(r -> rocketNames.contains(r))
            .map(r -> r.wiki().orElse(null))
            .filter(Objects::nonNull).toList();
    }

    @Override
    /**
     * Saves the name of the most reliable rocket in a given time period in an encrypted format.
     *
     * @param outputStream the output stream where the encrypted result is written into
     * @param from         the inclusive beginning of the time frame
     * @param to           the inclusive end of the time frame
     * @throws IllegalArgumentException   if outputStream, from or to is null
     * @throws CipherException            if the encrypt/decrypt operation cannot be completed successfully
     * @throws TimeFrameMismatchException if to is before from
     */
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (outputStream == null || from == null || to == null) {
            throw new IllegalArgumentException("outputstream is null or from or to is null");
        }

        if (from.isAfter(to)) {
            throw new TimeFrameMismatchException("from is after to");
        }
    }
}
