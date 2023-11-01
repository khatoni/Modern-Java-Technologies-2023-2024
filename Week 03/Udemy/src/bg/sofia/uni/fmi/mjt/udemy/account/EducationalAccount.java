package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase {


    public EducationalAccount(String username, double balance) {
        super(username, balance);
        type = AccountType.EDUCATION;

    }

    private boolean hasDiscount() {

        if (numberOfBoughtCourses < 5 && (numberOfBoughtCourses + 1) % 6 != 0) {
            return false;
        }
        double sum = 0;
        for (int i = numberOfBoughtCourses - 1; i >= numberOfBoughtCourses - 5; i--) {
            if (courses[i] != null && courses[i].isCompleted()) {
                sum += courses[i].getGrade();
            }
        }
        return Double.compare(sum / 5.0, 4.5) >= 0;
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
        double price = course.getPrice();
        if (hasDiscount()) {
            price *= 0.85;
        }
        if (!hasBeenPurchased(course)) {
            if (price > balance) {
                throw new InsufficientBalanceException("not enough money");
            }
            courses[numberOfBoughtCourses] = course;
            courses[numberOfBoughtCourses++].purchase();
            balance -= price;
        }
    }
}
