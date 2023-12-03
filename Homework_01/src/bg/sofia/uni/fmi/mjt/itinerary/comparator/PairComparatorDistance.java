package bg.sofia.uni.fmi.mjt.itinerary.comparator;

import bg.sofia.uni.fmi.mjt.itinerary.MyPair;

import java.util.Comparator;

public class PairComparatorDistance implements Comparator<MyPair> {

    @Override
    public int compare(MyPair firstPair, MyPair secondPair) {
        if (Double.compare(firstPair.heuristic(), secondPair.heuristic()) == 0) {
            return firstPair.city().name().compareTo(secondPair.city().name());
        } else {
            return Double.compare(firstPair.heuristic(), secondPair.heuristic());
        }
    }
}