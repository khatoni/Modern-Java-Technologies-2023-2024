# Gym 🏋️‍♂️
Задачата за тази седмица ще моделира фитнес, с неговите потребители и тренировките им.

## Gym
В пакета `bg.sofia.uni.fmi.mjt.gym` създайте публичен клас `Gym`, който има конструктор

```public Gym(int capacity, Address address)```
и имплементира интерфейса `GymAPI`:
```java
package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.GymMember;

import java.util.Collection;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public interface GymAPI {

    /**
     * Returns an unmodifiable copy of all members of the gym.
     * If there are no members, return an empty collection.
     */
    SortedSet<GymMember> getMembers();

    /**
     * Returns an unmodifiable copy of all members of the gym sorted by their name in lexicographic order.
     * If there are no members, return an empty collection.
     */
    SortedSet<GymMember> getMembersSortedByName();

    /**
     * Returns an unmodifiable copy of all members of the gym sorted by their proximity to the
     * gym in increasing order. If there are no members, return an empty collection.
     */
    SortedSet<GymMember> getMembersSortedByProximityToGym();

    /**
     * Adds a single member to the gym.
     *
     * @param member the member to add
     * @throws GymCapacityExceededException - if the gym is full
     * @throws IllegalArgumentException     if member is null
     */
    void addMember(GymMember member) throws GymCapacityExceededException;

    /**
     * Adds a group of members to the gym. If the gym does not have the capacity to accept all the
     * new members then no members are added
     *
     * @param members the members to add
     * @throws GymCapacityExceededException if the gym is full
     * @throws IllegalArgumentException     if members is null or empty
     */
    void addMembers(Collection<GymMember> members) throws GymCapacityExceededException;

    /**
     * Checks if a given member is member of the gym.
     *
     * @param member - the member
     * @throws IllegalArgumentException if member is null
     */
    boolean isMember(GymMember member);

    /**
     * Checks if an Exercise is trained on a given day.
     *
     * @param exerciseName - the name of the Exercise
     * @param day          - the day for which the check is done
     * @throws IllegalArgumentException if day is null or if exerciseName is null or empty
     */
    boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day);

    /**
     * Returns an unmodifiable Map representing each day and the names of the members that do this exercise on it.
     *
     * @param exerciseName - the name of the exercise being done
     * @throws IllegalArgumentException if exerciseName is null or empty
     */
    Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName);
}
```
## Member
В пакетa `bg.sofia.uni.fmi.mjt.gym.member` създайте публичен клас `Member`, чрез който се моделират потребителите на всеки `Gym`. Той трябва да имплементира интерфейса `GymMember`:
```java
package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GymMember {

    /**
     * Returns the member's name.
     */
    String getName();

    /**
     * Returns the member's age.
     */
    int getAge();

    /**
     * Returns the member's id number.
     */
    String getPersonalIdNumber();

    /**
     * Returns the member's gender.
     */
    Gender getGender();

    /**
     * Returns the member's address.
     */
    Address getAddress();

    /**
     * Returns an immutable Map representing the workout a member does on the DayOfWeek.
     */
    Map<DayOfWeek, Workout> getTrainingProgram();

    /**
     * Sets the workout for a specific day.
     *
     * @param day     - DayOfWeek on which the workout will be trained
     * @param workout - the workout to be trained
     * @throws IllegalArgumentException if day or workout is null.
     */
    void setWorkout(DayOfWeek day, Workout workout);

    /**
     * Returns a collection of days in undefined order on which the workout finishes with a specific exercise.
     *
     * @param exerciseName - the name of the exercise.
     * @throws IllegalArgumentException if exerciseName is null or empty.
     */
    Collection<DayOfWeek> getDaysFinishingWith(String exerciseName);

    /**
     * Adds an Exercise to the Workout trained on the given day. If there is no workout set for the day,
     * the day is considered a day off and no exercise can be added.
     *
     * @param day      - DayOfWeek to train the exercise.
     * @param exercise - the trained Exercise.
     * @throws DayOffException          if the Workout on this day is null.
     * @throws IllegalArgumentException if day or exercise is null
     */
    void addExercise(DayOfWeek day, Exercise exercise);

    /**
     * Adds Exercises to the Workout trained on the given day. If there is no workout set for the day, the day is
     * considered a day off and no exercise can be added.
     *
     * @param day       - DayOfWeek to train the exercise.
     * @param exercises - list of the trained Exercises
     * @throws DayOffException          if the Workout on this day is null or the exercises list is empty.
     * @throws IllegalArgumentException if day is null or exercises is null or empty
     */
    void addExercises(DayOfWeek day, List<Exercise> exercises);
}
```
и да има следния конструктор:

```
public Member(Address address, String name, int age, String personalIdNumber, Gender gender)
```
Полът на даден потребител се моделира от следния `enum`:
```
package bg.sofia.uni.fmi.mjt.gym.member;

public enum Gender {
    MALE, FEMALE, OTHER
}
```
Адресът на потребителите се моделира от следния `record`:
```
public record Address(double longitude, double latitude)
```
В него имплементирайте метод, който връща разстоянието до друг адрес и има следнат сигнатура:
```
public double getDistanceTo(Address other)
```
Членовете на фитнеса/`Member`-ите се идентифицират еднозначно по `PersonalIdNumber`.
## Workout
Всеки потребител има тренировъчна програма, при която на всеки ден от седмицата съответства тренировка. Самата тренировка е последователност от упражнения.
Упражнение и тренировка се моделират чрез `record`-и, намиращи се в пакета `bg.sofia.uni.fmi.mjt.gym.workout`:
```
public record Exercise(String name, int sets, int repetitions)
```
```
public record Workout(SequencedCollection<Exercise> exercises)
```
Упражненията се идентифицират еднозначно по тяхното име.
## Пакети
Спазвайте имената на пакетите на всички по-горе описани типове, тъй като в противен случай, решението ви няма да може да бъде автоматично тествано.
```
src
└── bg.sofia.uni.fmi.mjt.gym
    ├── member
    │      ├── Address.java
    │      ├── DayOffException.java
    │      ├── Gender.java
    │      ├── GymMember.java
    │      ├── Member.java
    │      └── (...)
    ├── workout
    │      ├── Exercise.java
    │      ├── Workout.java
    │      └── (...)
    ├── Gym.java
    ├── GymAPI.java
    ├── GymCapacityExceededException.java
    └── (...)
```
## ⚠️ Забележки
* Не променяйте по никакъв начин интерфейсите, дадени в условието.
