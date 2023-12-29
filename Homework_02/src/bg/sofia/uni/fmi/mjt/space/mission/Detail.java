package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {

    private static final int ROCKET_NAME_INDEX = 0;
    private static final int PAYLOAD_INDEX = 1;

    public static Detail of(String line) {
        String[] tokens = line.split("|");
        String rocketName = tokens[ROCKET_NAME_INDEX];
        String payload = tokens[PAYLOAD_INDEX];

        return new Detail(rocketName, payload);
    }
}
