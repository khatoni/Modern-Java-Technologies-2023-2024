package bg.sofia.uni.fmi.mjt.itinerary.comparator;

import bg.sofia.uni.fmi.mjt.itinerary.MyPair;

import java.util.Comparator;

public class PairComparatorDistance implements Comparator<MyPair> {

    @Override
    public int compare(MyPair firstPair, MyPair secondPair) {
        return (int)(firstPair.heuristic() - secondPair.heuristic());
    }
}