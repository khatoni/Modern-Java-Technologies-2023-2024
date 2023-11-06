package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.member.Member;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.SortedSet;
import java.util.TreeSet;

public class Gym implements GymAPI {
    private int capacity;
    private Address address;
    private SortedSet<GymMember> gymMembers;

    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        gymMembers = new TreeSet<>();
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return Collections.unmodifiableSortedSet(gymMembers);
    }

    /**
     * Returns an unmodifiable copy of all members of the gym sorted by their name in lexicographic order.
     * If there are no members, return an empty collection.
     */
    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        SortedSet<GymMember> answer = new TreeSet<>(new CompareMembersByName());
        answer.addAll(gymMembers);
        return Collections.unmodifiableSortedSet(answer);
    }

    /**
     * Returns an unmodifiable copy of all members of the gym sorted by their proximity to the
     * gym in increasing order. If there are no members, return an empty collection.
     */
    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> answer = new TreeSet<>(new CompareMembersByProximity(address));
        answer.addAll(gymMembers);
        return Collections.unmodifiableSortedSet(answer);
    }

    /**
     * Adds a single member to the gym.
     *
     * @param member the member to add
     * @throws GymCapacityExceededException - if the gym is full
     * @throws IllegalArgumentException     if member is null
     */
    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null || ((Member) member).isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (gymMembers.size() + 1 >= capacity) {
            throw new GymCapacityExceededException();
        }
        double distanceToGym = address.getDistanceTo(member.getAddress());
        Member tmp = Member.of(member);
        Map<DayOfWeek, Workout> trainingPlan = member.getTrainingProgram();
        for (Map.Entry<DayOfWeek, Workout> el : trainingPlan.entrySet()) {
            tmp.setWorkout(el.getKey(), el.getValue());
        }
        tmp.setProximity(distanceToGym);
        gymMembers.add(tmp);
    }

    /**
     * Adds a group of members to the gym. If the gym does not have the capacity to accept all the
     * new members then no members are added
     *
     * @param members the members to add
     * @throws GymCapacityExceededException if the gym is full
     * @throws IllegalArgumentException     if members is null or empty
     */
    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (gymMembers.size() + members.size() >= capacity) {
            throw new GymCapacityExceededException();
        }
        for (GymMember el : members) {
            if (el == null || ((Member) el).isEmpty()) {
                throw new IllegalArgumentException("member with null PersonalIdNumber");
            }
            double distanceToGym = address.getDistanceTo(el.getAddress());
            Member tmp = Member.of(el);
            Map<DayOfWeek, Workout> trainingPlan = el.getTrainingProgram();
            for (Map.Entry<DayOfWeek, Workout> mapEntry : trainingPlan.entrySet()) {
                tmp.setWorkout(mapEntry.getKey(), mapEntry.getValue());
            }
            tmp.setProximity(distanceToGym);
            gymMembers.add(tmp);
        }
    }

    /**
     * Checks if a given member is member of the gym.
     *
     * @param member - the member
     * @throws IllegalArgumentException if member is null
     */
    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException();
        }
        for (GymMember el : gymMembers) {
            if (el.equals(member)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an Exercise is trained on a given day.
     *
     * @param exerciseName - the name of the Exercise
     * @param day          - the day for which the check is done
     * @throws IllegalArgumentException if day is null or if exerciseName is null or empty
     */
    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (exerciseName == null || exerciseName.isBlank() || day == null) {
            throw new IllegalArgumentException();
        }
        for (GymMember m : gymMembers) {
            Workout workDay = m.getTrainingProgram().get(day);
            if (workDay == null) {
                continue;
            }
            SequencedCollection<Exercise> tmpSeq = workDay.exercises();
            for (Exercise seqEx : tmpSeq) {
                if (seqEx.name().equals(exerciseName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an unmodifiable Map representing each day and the names of the members that do this exercise on it.
     *
     * @param exerciseName - the name of the exercise being done
     * @throws IllegalArgumentException if exerciseName is null or empty
     */
    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException();
        }
        Map<DayOfWeek, List<String>> answer = new HashMap<>();
        for (GymMember member : gymMembers) {
            Map<DayOfWeek, Workout> tmp = member.getTrainingProgram();
            if (tmp == null) {
                continue;
            }

            for (Map.Entry<DayOfWeek, Workout> el : tmp.entrySet()) {
                SequencedCollection<Exercise> tmpExercise = el.getValue().exercises();
                for (Exercise ex : tmpExercise) {
                    if (ex.name() != null && ex.name().equals(exerciseName)) {
                        answer.putIfAbsent(el.getKey(), new ArrayList<>());
                        List<String> names = answer.get(el.getKey());
                        names.add(member.getName());
                        answer.putIfAbsent(el.getKey(), names);

                    }
                }
            }
        }
        return Collections.unmodifiableMap(answer);
    }

}
