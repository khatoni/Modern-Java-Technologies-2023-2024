package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Comparator;

public class CompareMembersByName implements Comparator<GymMember> {

    @Override
    public int compare(GymMember first, GymMember second) {
        // compare the car brand names lexicographically
        return String.CASE_INSENSITIVE_ORDER.compare(first.getName(), second.getName());
    }

}