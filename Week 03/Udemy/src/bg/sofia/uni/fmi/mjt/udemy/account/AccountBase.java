package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;

public abstract class AccountBase implements Account {
    protected String username;
    protected double balance;
    protected AccountType type;
    protected Course[] courses;
    protected int numberOfBoughtCourses;

    public AccountBase() {

    }

    public AccountBase(String username, double balance) {
        if (username == null || balance < 0) {
            throw new IllegalArgumentException();
        }
        this.username = username;
        this.balance = balance;
        courses = new Course[100];
    }

    /**
     * @param course is expected to be not null
     * @return boolean
     * @throws CourseAlreadyPurchasedException
     */
    protected boolean hasBeenPurchased(Course course) throws CourseAlreadyPurchasedException {
        for (int i = 0; i < numberOfBoughtCourses; i++) {
            if (courses[i] != null && courses[i].equals(course))
                throw new CourseAlreadyPurchasedException("course already purchased in hasBeenPurchased");
        }
        return false;
    }

    /**
     * Returns the username of the account.
     */

    public String getUsername() {

        return username;
    }

    /**
     * Adds the given amount of money to the account's balance.
     *
     * @param amount the amount of money which will be added to the account's balance.
     * @throws IllegalArgumentException if amount has a negative value.
     */
    public void addToBalance(double amount) {
        if (amount < 0) throw new IllegalArgumentException("illegal");
        balance += amount;
    }

    /**
     * Returns the balance of the account.
     */
    public double getBalance() {

        return balance;
    }

    /**
     * Buys the given course for the account.
     *
     * @param course the course which will be bought.
     * @throws IllegalArgumentException          if the account buyer is of type BusinessAccount and course has category which is not among the permitted for this account
     * @throws InsufficientBalanceException      if the account does not have enough funds in its balance.
     * @throws MaxCourseCapacityReachedException if the account has reached the maximum allowed course capacity.
     * @throws CourseAlreadyPurchasedException   if the course is already purchased for this account.
     */
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (courses == null) {
            courses = new Course[100];
        }
        if (course == null) throw new IllegalArgumentException("illegal argument null in buyCourse");
        if (numberOfBoughtCourses >= 100)
            throw new MaxCourseCapacityReachedException("maxCapacity reached in buyCouurse");

    }


    /**
     * Completes the given resources that belong to the given course provided that the course was previously purchased by this account.
     *
     * @param resourcesToComplete the resources which will be completed.
     * @param course              the course in which the resources will be completed.
     * @throws CourseNotPurchasedException if course is not currently purchased for this account.
     * @throws ResourceNotFoundException   if a certain resource could not be found in the course.
     */
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {

        int index = isPurchased(course);

        if (resourcesToComplete != null) {
            for (int j = 0; j < resourcesToComplete.length; j++) {
                courses[index].completeResource(resourcesToComplete[j]);
            }
        }
        if (courses[index].areAllResourcesCompleted()) {
            courses[index].complete();
        }
    }

    /**
     * Completes the whole course.
     *
     * @param course the course which will be completed.
     * @param grade  the grade with which the course will be completed.
     * @throws IllegalArgumentException    if grade is not in range [2.00, 6.00].
     * @throws CourseNotPurchasedException if course is not currently purchased for this account.
     * @throws CourseNotCompletedException if there is a resource in the course which is not completed.
     */
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (grade < 2.00 || grade > 6.00) throw new IllegalArgumentException("grade not valid in completeCourse");
        int index = isPurchased(course);
        if (!courses[index].isCompleted())
            throw new CourseNotCompletedException("Course not completed in completeCourse");
        courses[index].complete();
        courses[index].setGrade(grade);
    }

    /**
     * returns the index of the course in the array if bougth
     *
     * @param course the course which will be searched
     * @throws CourseNotPurchasedException if course is not currently purchased;
     */
    public int isPurchased(Course course) throws CourseNotPurchasedException {
        int index = -1;
        if (course == null) throw new CourseNotPurchasedException();
        for (int i = 0; i < numberOfBoughtCourses; i++) {
            if (courses[i] != null && courses[i].equals(course)) {
                index = i;
            }
        }
        if (index == -1) throw new CourseNotPurchasedException("Course not purchased in isPurchased");
        return index;
    }

    /**
     * Gets the course with the least completion percentage.
     * Returns null if the account does not have any purchased courses.
     */

    public Course getLeastCompletedCourse() {
        if (courses == null) return null;
        if (courses.length == 0) return null;
        double minCompletionPercentage = 100.00;
        int answerIndex = 0;
        for (int i = 0; i < numberOfBoughtCourses; i++) {
            if (courses[i] != null && courses[i].getCompletionPercentage() < minCompletionPercentage) {
                minCompletionPercentage = courses[i].getCompletionPercentage();
                answerIndex = i;
            }
        }
        return courses[answerIndex];


    }
}
