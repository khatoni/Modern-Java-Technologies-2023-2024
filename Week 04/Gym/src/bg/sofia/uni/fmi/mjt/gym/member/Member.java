package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Member implements GymMember, Comparable<Member> {

    private final String name;
    private final int age;
    private final String pesonalIdNumber;
    private final Gender gender;
    private final Address address;
    private double proximity;

    private final Map<DayOfWeek, Workout> trainingPlan;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.pesonalIdNumber = personalIdNumber;
        this.gender = gender;
        trainingPlan = new HashMap<>();
    }

    public static Member of(GymMember member) {
        return new Member(member.getAddress(), member.getName(),
                member.getAge(), member.getPersonalIdNumber(), member.getGender());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return pesonalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public double getProximity() {
        return proximity;
    }

    public void setProximity(double distanceToGym) {
        this.proximity = distanceToGym;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Map.copyOf(trainingPlan);
    }

    @Override
    public int compareTo(Member other) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.pesonalIdNumber, other.pesonalIdNumber);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Day or workout is null in the function setWorkout");
        }
        trainingPlan.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException("exerciseName is null in the function getDaysFinishingWith");
        }
        Collection<DayOfWeek> answer = new HashSet<>();
        for (Map.Entry<DayOfWeek, Workout> element : trainingPlan.entrySet()) {
            if (element.getValue().exercises().getLast().name().equals(exerciseName)) {
                answer.add(element.getKey());
            }
        }
        return answer;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null || exercise == null || exercise.name() == null) {
            throw new IllegalArgumentException("Day or exercise is null in the function addExercise");
        }

        if (trainingPlan == null || (trainingPlan != null && trainingPlan.get(day) == null)) {
            throw new DayOffException("the workout on a particular day is null");
        }

        Workout tmp = trainingPlan.get(day);
        if (tmp != null) {
            tmp.exercises().addLast(exercise);
            trainingPlan.put(day, tmp);
        }

    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null || exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Day or exercise is null in the function addExercise");
        }
        for (Exercise it : exercises) {
            if (it == null || it.name() == null) {
                throw new IllegalArgumentException("exercise name is null");
            }
        }
        if (trainingPlan == null || (trainingPlan != null && trainingPlan.get(day) == null)) {
            throw new DayOffException("the workout on a particular day is null");
        }

        for (Exercise it : exercises) {
            Workout tmp = trainingPlan.get(day);
            tmp.exercises().addLast(it);
            trainingPlan.put(day, tmp);
        }
    }

    public boolean isEmpty() {
        return name == null || name.isBlank() || pesonalIdNumber == null || pesonalIdNumber.isBlank()
                || gender == null || address == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Member that)) {
            return false;
        }

        return that.pesonalIdNumber.equals(this.pesonalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pesonalIdNumber);
    }

}
