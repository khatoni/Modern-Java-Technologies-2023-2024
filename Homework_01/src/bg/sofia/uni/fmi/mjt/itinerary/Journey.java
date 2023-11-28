package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) {

    public static boolean isInvalidJourney(Journey journey) {
        return journey == null || City.isCityInvalid(journey.from()) || City.isCityInvalid(journey.to())
                || journey.vehicleType() == null || journey.price().compareTo(new BigDecimal("0.0")) <= 0;
    }
}