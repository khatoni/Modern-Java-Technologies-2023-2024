package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    private final int initialBuildableArea;
    private int currentBuildableArea;
    private Map<String, E> buildings;

    public Plot(int buildableArea) {
        this.initialBuildableArea = buildableArea;
        this.currentBuildableArea = initialBuildableArea;
        buildings = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank() || buildable == null) {
            throw new IllegalArgumentException(
                    "The address is null or blank or the buildable provided is null");
        }

        if (buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException(
                    "The address is already occupied");
        }

        if (currentBuildableArea < buildable.getArea()) {
            throw new InsufficientPlotAreaException(
                    "Insufficient amount of area left to build the provided buildable");
        }

        buildings.putIfAbsent(address, buildable);
        currentBuildableArea -= buildable.getArea();
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("The provided map is null or empty");
        }

        int sum = 0;
        for (Map.Entry<String, E> element : buildables.entrySet()) {
            if (buildings.containsKey(element.getKey())) {
                throw new BuildableAlreadyExistsException("This building is already built");
            }
            sum += element.getValue().getArea();
        }

        if (sum > currentBuildableArea) {
            throw new InsufficientPlotAreaException("Not enough space");
        }

        buildings.putAll(buildables);
        currentBuildableArea -= sum;
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("The address provided for demolition is null or blank");
        }

        if (!buildings.containsKey(address)) {
            throw new BuildableNotFoundException("The address provided for demolitions is not builded yet");
        }

        currentBuildableArea += buildings.get(address).getArea();
        buildings.remove(address);

    }

    @Override
    public void demolishAll() {
        buildings.clear();
        currentBuildableArea = initialBuildableArea;
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        return currentBuildableArea;
    }
}
