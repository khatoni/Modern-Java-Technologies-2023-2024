package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class BusinessAccount extends AccountBase {
    private Category[] permittedCategories;

    public BusinessAccount(String username, double balance, Category[] allowedCategories) {
        super(username, balance);
        type = AccountType.BUSINESS;
        if (allowedCategories == null || allowedCategories.length == 0) throw new IllegalArgumentException();
        permittedCategories = allowedCategories;
    }

    private boolean permitCategory(Category category) {
        if (category == null) return false;
        for (int i = 0; i < permittedCategories.length; i++) {
            if (permittedCategories[i] != null && permittedCategories[i] == category) {
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Buys the given course for the account.
     *
     * @param course the course which will be bought.
     * @throws IllegalArgumentException if the account buyer is of type BusinessAccount and course has category which is not among the permitted for this account
     * @throws InsufficientBalanceException if the account does not have enough funds in its balance.
     * @throws CourseAlreadyPurchasedException if the course is already purchased for this account.
     * @throws MaxCourseCapacityReachedException if the account has reached the maximum allowed course capacity.
     */
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (course == null) throw new IllegalArgumentException("illegal");
        if (permitCategory(course.getCategory()) == false) throw new IllegalArgumentException("illegal");
        super.buyCourse(course);
        if (hasBeenPurchased(course) == false) {
            if (course.getPrice() * 0.8 > this.balance) {
                throw new InsufficientBalanceException("not money");
            }
            courses[numberOfBoughtCourses] = course;
            courses[numberOfBoughtCourses].purchase();
            numberOfBoughtCourses++;
            balance -= course.getPrice() * 0.8;
        }

    }
}
