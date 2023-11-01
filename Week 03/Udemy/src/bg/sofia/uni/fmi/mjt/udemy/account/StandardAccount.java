package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;

public class StandardAccount extends AccountBase {

    public StandardAccount(String username, double balance) {
        super(username, balance);
        type = AccountType.STANDARD;
    }

    @Override
    /**
     * Buys the given course for the account.
     *
     * @param course the course which will be bought.
     * @throws IllegalArgumentException if the account buyer is of type BusinessAccount and course has category which is not among the permitted for this account
     * @throws InsufficientBalanceException if the account does not have enough funds in its balance.
     * @throws MaxCourseCapacityReachedException if the account has reached the maximum allowed course capacity.
     * @throws CourseAlreadyPurchasedException if the course is already purchased for this account.
     */
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
       super.buyCourse(course);
        if(hasBeenPurchased(course)==false) {
            if (course.getPrice() > this.balance) throw new InsufficientBalanceException("not money");
            courses[numberOfBoughtCourses] = course;
            courses[numberOfBoughtCourses++].purchase();
            balance -= course.getPrice();
        }

    }
}
