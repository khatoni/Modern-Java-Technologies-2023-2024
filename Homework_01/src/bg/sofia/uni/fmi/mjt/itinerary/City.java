package bg.sofia.uni.fmi.mjt.itinerary;

import java.util.Objects;

public record City(String name, Location location) {

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;

        if (other == null) return false;

        if (!(other instanceof City city)) return false;

        return this.name.equals(city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static boolean isCityInvalid(City city) {
        return city == null || city.name() == null || city.name().isBlank() || city.location() == null;
    }
}