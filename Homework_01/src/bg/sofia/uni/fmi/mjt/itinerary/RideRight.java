package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.comparator.PairComparatorDistance;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SequencedCollection;
import java.util.Set;
import java.util.Stack;

public class RideRight implements ItineraryPlanner {

    private final BigDecimal pricePerKilometer = new BigDecimal("0.02");
    private final Map<City, Set<WrapperCity>> myGraph;
    private final List<Journey> journeys;
    private final Set<City> allCities;

    private void setDataBeforeAStar(Map<City, BigDecimal> distances, Map<City, Journey> cameFrom, City start) {
        for (City city : allCities) {
            distances.putIfAbsent(city, new BigDecimal(Double.MAX_VALUE));
            cameFrom.putIfAbsent(city, null);
        }
        distances.put(start, new BigDecimal("0.0"));
    }

    private PriorityQueue<MyPair> setPriorityQueue(City start) {
        PriorityQueue<MyPair> pq = new PriorityQueue<>(
            new PairComparatorDistance());

        pq.add(new MyPair(0.0, start));

        return pq;
    }

    private BigDecimal calculateTentativeScore(Map<City, BigDecimal> distances, City currentCity,
                                               WrapperCity wrapperCity) {

        return distances.get(currentCity).add(wrapperCity.getPriceAfterFees()).add((
            Location.findDistance(currentCity.location(), wrapperCity.getLocation()).multiply(pricePerKilometer)));
    }

    private void validateEndCities(City start, City destination) throws CityNotKnownException {

        if (City.isCityInvalid(start) || City.isCityInvalid(destination)) {
            throw new IllegalArgumentException("The start or the destination is null");
        }

        boolean isPresentStartCity = false;
        boolean isPresentDestinationCity = false;

        for (Journey journey : journeys) {
            if (journey.to().equals(destination)) {
                isPresentStartCity = true;
            }
            if (journey.from().equals(start)) {
                isPresentDestinationCity = true;
            }
        }

        if (!isPresentStartCity) {
            throw new CityNotKnownException("The provided start city is not present in the list of schedules");
        }

        if (!isPresentDestinationCity) {
            throw new CityNotKnownException("The provided destination city is not present in the list of schedules");
        }
    }

    private Journey findDirectPath(City start, City destination) throws NoPathToDestinationException {

        Set<WrapperCity> directConnections = myGraph.get(start);
        BigDecimal currentCheapestPrice = new BigDecimal(Double.MAX_VALUE);
        Journey answer = null;

        if (directConnections == null || directConnections.isEmpty()) {
            throw new NoPathToDestinationException("There is no direct path between the cities");
        }

        for (WrapperCity wrapperCity : directConnections) {
            if (wrapperCity.getName().equals(destination.name())) {
                BigDecimal tentativePrice = wrapperCity.getPriceAfterFees().add(
                    Location.findDistance(start.location(), destination.location()).multiply(pricePerKilometer));
                if (tentativePrice.compareTo(currentCheapestPrice) < 0) {
                    currentCheapestPrice = tentativePrice;
                    answer = new Journey(wrapperCity.getVehicle(), start, destination, wrapperCity.getOriginalPrice());
                }
            }
        }

        return answer;
    }

    private SequencedCollection<Journey> findPath(City end, Map<City, Journey> cameFrom) {
        List<Journey> answer = new ArrayList<>();
        Stack<Journey> tmp = new Stack<>();
        City current = end;

        while (current != null) {
            tmp.add(cameFrom.get(current));
            if (cameFrom.get(current) != null) {
                current = cameFrom.get(current).from();
            } else {
                current = null;
            }
        }

        while (!tmp.isEmpty()) {
            if (tmp.peek() != null) {
                answer.add(tmp.peek());
            }
            tmp.pop();
        }
        return answer;
    }

    private SequencedCollection<Journey> aStarShortestPath(City start, City end) {
        Map<City, BigDecimal> distances = new HashMap<>();
        Map<City, Journey> cameFrom = new HashMap<>();
        setDataBeforeAStar(distances, cameFrom, start);
        PriorityQueue<MyPair> pq = setPriorityQueue(start);
        while (!pq.isEmpty()) {
            City currentCity = pq.peek().city();
            if (currentCity.equals(end)) {
                return findPath(end, cameFrom);
            }
            pq.remove();
            if (myGraph.get(currentCity) == null) {
                continue;
            }
            for (WrapperCity wrapperCity : myGraph.get(currentCity)) {
                BigDecimal tentativeScore = calculateTentativeScore(distances, currentCity, wrapperCity);
                if (tentativeScore.compareTo(
                    distances.get(new City(wrapperCity.getName(), wrapperCity.getLocation()))) < 0) {
                    City neighbour = new City(wrapperCity.getName(), wrapperCity.getLocation());
                    cameFrom.replace(neighbour,
                        new Journey(wrapperCity.getVehicle(), currentCity, neighbour, wrapperCity.getOriginalPrice()));
                    distances.put(neighbour, tentativeScore);
                    pq.add(new MyPair(distances.get(neighbour).doubleValue(), neighbour));
                }
            }
        }
        return null;
    }

    public RideRight(List<Journey> schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Cannot construct object with null schedule");
        }

        for (Journey journey : schedule) {
            if (Journey.isInvalidJourney(journey)) {
                throw new IllegalArgumentException("One of the journeys has invalid data");
            }
        }

        allCities = new HashSet<>();
        journeys = schedule;
        myGraph = new HashMap<>();

        for (Journey journey : journeys) {
            allCities.add(journey.from());
            allCities.add(journey.to());
            Set<WrapperCity> connections;
            if (myGraph.containsKey(journey.from())) {
                connections = myGraph.get(journey.from());
            } else {
                connections = new HashSet<>();
            }
            WrapperCity wrapperCity =
                new WrapperCity(journey.to().name(), journey.vehicleType(), journey.price(), journey.to().location());
            connections.add(wrapperCity);
            myGraph.put(journey.from(), connections);
        }
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {

        validateEndCities(start, destination);

        if (myGraph.get(start) == null || myGraph.get(start).isEmpty()) {
            throw new NoPathToDestinationException("There are no paths from the start city");
        }

        SequencedCollection<Journey> cities;
        if (!allowTransfer) {
            Journey answer = findDirectPath(start, destination);
            if (answer == null) {
                throw new NoPathToDestinationException("There is no direct path between the start and destination");
            }

            cities = new ArrayList<>();
            cities.add(answer);

        } else {
            cities = aStarShortestPath(start, destination);
            if (cities == null) {
                throw new NoPathToDestinationException("There is no path between the cities even with transfers");
            }
        }

        return cities;
    }
}
