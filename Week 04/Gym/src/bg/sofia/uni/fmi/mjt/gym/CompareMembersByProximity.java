package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Comparator;

public class CompareMembersByProximity implements Comparator<GymMember> {
    private Address address;

    public CompareMembersByProximity(Address address) {
        this.address = address;
    }

    @Override
    public int compare(GymMember first, GymMember second) {
        return Double.compare(first.getAddress().getDistanceTo(address), second.getAddress().getDistanceTo(address));
    }
}