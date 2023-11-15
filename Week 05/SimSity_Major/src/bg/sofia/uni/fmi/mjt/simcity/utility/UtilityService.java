package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {

    private Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = new HashMap<>(taxRates);
    }

    /**
     * Retrieves the costs of a specific utility for a given billable building.
     *
     * @param utilityType The utility type used for the costs' calculation.
     * @param billable    The billable building for which the utility costs will be calculated.
     * @return The cost of the specified utility for the billable building, rounded up to two decimal places.
     * @throws IllegalArgumentException if the utility or billable is null.
     */
    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("The utility type or the billable is null");
        }

        double rate = taxRates.get(utilityType);
        double consumption = switch (utilityType) {
            case WATER -> billable.getWaterConsumption();
            case ELECTRICITY -> billable.getElectricityConsumption();
            case NATURAL_GAS -> billable.getNaturalGasConsumption();
        };
        return rate * consumption;
    }

    /**
     * Calculates the total utility costs for a given billable building.
     *
     * @param billable The billable building for which total utility costs are calculated.
     * @return The total cost of all utilities for the billable building, rounded up to two decimal places.
     * @throws IllegalArgumentException if the billable is null.
     */
    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("The provided billable is null");
        }

        double electricityPrice = taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption();
        double gasPrice = taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
        double waterPrice = taxRates.get(UtilityType.WATER) * billable.getWaterConsumption();
        return electricityPrice + gasPrice + waterPrice;
    }

    /**
     * Computes the absolute difference in utility costs between two billable buildings for each utility type.
     *
     * @param firstBillable  The first billable building used for the cost comparison.
     * @param secondBillable The second billable building used for the cost comparison.
     * @return An unmodifiable map containing the absolute difference in costs between the buildings for each
     * utility. Each cost difference is rounded up to two decimal places.
     * @throws IllegalArgumentException if any billable is null.
     */
    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("The first or the second billable is null");
        }

        Map<UtilityType, Double> answer = new HashMap<>();
        for (Map.Entry<UtilityType, Double> element : taxRates.entrySet()) {
            double firstBillableCost = getUtilityCosts(element.getKey(), firstBillable);
            double secondBillableCost = getUtilityCosts(element.getKey(), secondBillable);
            answer.putIfAbsent(element.getKey(), Math.abs(firstBillableCost - secondBillableCost));
        }
        
        return Collections.unmodifiableMap(answer);
    }

}
