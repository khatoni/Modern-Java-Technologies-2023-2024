package bg.sofia.uni.fmi.mjt.itinerary.comparator;

import bg.sofia.uni.fmi.mjt.itinerary.MyPair;

import java.util.Comparator;

public class PairComparatorLexicographic implements Comparator<MyPair> {

    @Override
    public int compare(MyPair firstPair, MyPair secondPair) {
        return (firstPair.city().name().compareTo(secondPair.city().name()));
    }
}
