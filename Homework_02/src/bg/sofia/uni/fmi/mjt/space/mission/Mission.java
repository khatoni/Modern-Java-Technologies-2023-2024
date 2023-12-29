package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public record Mission(String id, String company, String location,
                      LocalDate date, Detail detail, RocketStatus rocketStatus,
                      Optional<Double> cost, MissionStatus missionStatus) {

    private static final int ID_INDEX = 0;
    private static final int COMPANY_INDEX = 1;
    private static final int LOCATION_INDEX = 2;
    private static final int DATE_INDEX = 3;
    private static final int DETAIL_INDEX = 4;
    private static final int ROCKET_STATUS_INDEX = 5;
    private static final int COST_INDEX = 6;
    private static final int MISSION_STATUS_INDEX = 7;

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("\"EEE\sMMM\sdd,\suuuu\"", Locale.ENGLISH);

    public static Mission of(String line) {
        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String id = tokens[ID_INDEX];
        String company = tokens[COMPANY_INDEX];
        String location = tokens[LOCATION_INDEX];
        LocalDate date = LocalDate.parse(tokens[DATE_INDEX], DATE_FORMATTER);
        Detail detail = Detail.of(tokens[DETAIL_INDEX]);
        String rocketStatusString = tokens[ROCKET_STATUS_INDEX];
        RocketStatus rocketStatus = null;
        switch (rocketStatusString) {
            case "StatisActive" -> rocketStatus = RocketStatus.STATUS_ACTIVE;
            case "StatusRetired" -> rocketStatus = RocketStatus.STATUS_RETIRED;
        }
        String costString = tokens[COST_INDEX].replaceAll("\"", "").strip();
        Optional<Double> cost;
        try {
            cost = Optional.of(Double.valueOf(costString));
        } catch (NumberFormatException e) {
            cost = Optional.empty();
        }
        String missionStatusString = tokens[MISSION_STATUS_INDEX];
        MissionStatus missionStatus = null;
        switch (missionStatusString) {
            case "Success" -> missionStatus = MissionStatus.SUCCESS;
            case "Failure" -> missionStatus = MissionStatus.FAILURE;
            case "Partial Failure" -> missionStatus = MissionStatus.PARTIAL_FAILURE;
            case "Prelaunch Failure" -> missionStatus = MissionStatus.PRELAUNCH_FAILURE;
        }
        return new Mission(id, company, location, date, detail, rocketStatus, cost, missionStatus);
    }
}
