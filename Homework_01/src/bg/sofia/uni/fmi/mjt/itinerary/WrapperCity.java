package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public class WrapperCity {

    private final VehicleType vehicle;
    private final String name;
    private final BigDecimal price;
    private final Location location;

    public WrapperCity(String name, VehicleType vehicle, BigDecimal price, Location location) {
        this.name = name;
        this.vehicle = vehicle;
        this.price = price;
        this.location = location;
    }

    public BigDecimal getOriginalPrice() {
        return price;
    }

    public BigDecimal getPriceAfterFees() {
        return switch (vehicle) {
            case VehicleType.BUS -> (VehicleType.BUS.getGreenTax().add(new BigDecimal("1"))).multiply(price);
            case VehicleType.PLANE -> (VehicleType.PLANE.getGreenTax().add(new BigDecimal("1"))).multiply(price);
            case VehicleType.TRAIN -> price;
        };
    }

    public VehicleType getVehicle() {
        return vehicle;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
