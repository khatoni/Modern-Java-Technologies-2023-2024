package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;
import java.util.Arrays;


public class Udemy implements LearningPlatform {
    private Account[] accounts;
    private Course[] courses;

    private int accountsSize;
    private int coursesSize;

    public Udemy(Account[] accounts, Course[] courses) {
        this.accounts = accounts;
        this.courses = courses;
    }

    /**
     * Returns the course with the given name.
     *
     * @param name the exact name of the course.
     * @throws IllegalArgumentException if name is null or blank.
     * @throws CourseNotFoundException  if course with the specified name is not present in the platform.
     */
    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("illegal");
        if (courses == null || courses.length == 0) throw new CourseNotFoundException("course not found");
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null && courses[i].getName() != null && courses[i].getName().equals(name)) {
                return courses[i];
            }
        }
        throw new CourseNotFoundException("not found");
    }

    private boolean isValidKeyword(String keyword) {
        String tmp = keyword.toLowerCase();
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) < 'a' || tmp.charAt(i) > 'z') return false;
        }
        return true;
    }

    /**
     * Returns all courses which name or description containing keyword.
     * A keyword is a word that consists of only small and capital latin letters.
     *
     * @param keyword the exact keyword for which we will search.
     * @throws IllegalArgumentException if keyword is null, blank or not a keyword.
     */
    @Override
    public Course[] findByKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("illegal");
        if (isValidKeyword(keyword) == false) throw new IllegalArgumentException("illegal");
        Course[] answer = new Course[100];
        int currSize = 0;
        for (int i = 0; i < courses.length; i++) {
            if ((courses[i] != null && courses[i].getDescription() != null && courses[i].getDescription().contains(keyword)) ||
                    (courses[i].getName() != null && courses[i].getName().contains(keyword))) {
                answer[currSize++] = courses[i];
            }
        }
        if(currSize==0) {
            return new Course[0];
        }
        Course[] finalAnswer = Arrays.copyOf(answer, currSize);
        return finalAnswer;
    }

    /**
     * Returns all courses from a given category.
     *
     * @param category the exact category the courses for which we want to get.
     * @throws IllegalArgumentException if category is null.
     */
    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) throw new IllegalArgumentException();
        if (courses.length == 0) return null;
        Course[] answer = new Course[courses.length];
        int currSize = 0;
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null && courses[i].getCategory() == category) {
                answer[currSize++] = courses[i];
            }
        }

        Course[] finalAnswer = Arrays.copyOf(answer, currSize);
        return finalAnswer;
    }

    /**
     * Returns the account with the given name.
     *
     * @param name the exact name of the account.
     * @throws IllegalArgumentException if name is null or blank.
     * @throws AccountNotFoundException if account with such a name does not exist in the platform.
     */
    @Override
    public Account getAccount(String name) throws AccountNotFoundException {

        if (name == null || name.isBlank()) throw new IllegalArgumentException("illegal name in getAccount");
        if (accounts == null || accounts.length == 0) throw new AccountNotFoundException("accont not found in getAccount1");
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i] != null && accounts[i].getUsername() != null && accounts[i].getUsername().equals(name)) {
                return accounts[i];
            }
        }
        throw new AccountNotFoundException("account not found in getAccount2");
    }

    /**
     * Returns the course with the longest duration in the platform.
     * Returns null if the platform has no courses.
     */
    @Override
    public Course getLongestCourse() {
        if (courses == null) return null;
        if (courses.length == 0) return null;
        int index = 0;
        double longestDuration = 0.0;
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null && courses[i].getTotalTime() != null && courses[i].getTotalTime().hours() * 60 + courses[i].getTotalTime().minutes() > longestDuration) {
                longestDuration = courses[i].getTotalTime().hours() * 60 + courses[i].getTotalTime().minutes();
                index = i;
            }
        }
        return courses[index];
    }

    /**
     * Returns the cheapest course from the given category.
     * Returns null if the platform has no courses.
     *
     * @param category the exact category for which we want to find the cheapest course.
     * @throws IllegalArgumentException if category is null.
     */
    @Override
    public Course getCheapestByCategory(Category category) {

        if (category == null) throw new IllegalArgumentException("illegal category passed");
        if (courses == null || courses.length == 0) return null;
        if (courses.length == 0) return null;
        double minPrice = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null && courses[i].getCategory() == category && courses[i].getPrice() < minPrice) {
                minPrice = courses[i].getPrice();
                index = i;
            }
        }
        if (index == -1) return null;
        return courses[index];
    }

}
