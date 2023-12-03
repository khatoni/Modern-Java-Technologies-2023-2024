package bg.sofia.uni.fmi.mjt.football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class FootballPlayerAnalyzer {

    private final List<Player> players;

    public FootballPlayerAnalyzer(Reader reader) {

        try (var dataReader = new BufferedReader(reader)) {
            players = dataReader.lines().map(Player::of).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while reading from the file", e);
        }
    }

    public List<Player> getAllPlayers() {
        return players;
    }

    public Set<String> getAllNationalities() {
        List<String> nationalities = players.stream()
            .map(Player::nationality)
            .distinct()
            .toList();
        return Collections.unmodifiableSet(new LinkedHashSet<>(nationalities));
    }

    public Player getHighestPaidPlayerByNationality(String nationality) {
        if (nationality == null || nationality.isBlank()) {
            throw new IllegalArgumentException("The provided nationality is invalid");
        }
        return players.stream()
            .filter(player -> player.nationality().equals(nationality))
            .max(Comparator.comparing(Player::wageEuro))
            .orElseThrow(() -> new NoSuchElementException("There is not such player"));
    }

    public Map<Position, Set<Player>> groupByPosition() {

        List<List<Position>> validPosition = players.stream().map(Player::positions).toList();
        Set<Position> answerPositions = new HashSet<>();
        for (List<Position> element : validPosition) {
            answerPositions.addAll(element);
        }
        Map<Position, Set<Player>> answer = new HashMap<>();
        for (Position position : answerPositions) {
            List<Player> answerPlayers = players.stream()
                .filter(player -> player.positions().contains(position))
                .toList();
            answer.putIfAbsent(position, new HashSet<>(answerPlayers));
        }

        return answer;
    }

    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if (position == null || budget < 0) {
            throw new IllegalArgumentException("The provided position or budget is invalid");
        }
        return players.stream().filter(player -> player.valueEuro() <= budget)
            .max(Comparator.comparing(player -> (player.potential() + player.overallRating()) / player.age()));
    }

    public Set<Player> getSimilarPlayers(Player player) {
        final int positiveMaximum = 3;
        final int negativeMaximum = -3;
        List<Player> answer = players.stream().filter(p -> p.preferredFoot() == player.preferredFoot())
            .filter(p -> p.overallRating() - player.overallRating() <= positiveMaximum &&
                p.overallRating() - player.overallRating() >= negativeMaximum).toList();

        return Collections.unmodifiableSet(new LinkedHashSet<>(answer));
    }

    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("The provided keyword is not valid");
        }

        List<Player> answer = players.stream().filter(player -> player.fullName().contains(keyword)).toList();

        return Collections.unmodifiableSet(new LinkedHashSet<>(answer));
    }
}