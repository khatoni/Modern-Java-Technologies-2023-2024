package bg.sofia.uni.fmi.mjt.itinerary;

import java.math.BigDecimal;

public record Location(int x, int y) {

    public static BigDecimal findDistance(Location l1, Location l2) {
        return new BigDecimal(Math.abs(l1.x() - l2.x()) + Math.abs(l1.y() - l2.y()));
    }
}