package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;
import java.util.Objects;
import java.util.Arrays;

public class Course implements Completable, Purchasable {

    private String name;
    private String description;
    private double price;
    private Category category;
    private Resource[] content;
    private CourseDuration totalTime;
    private boolean isPurchased;
    private boolean isCompleted;
    private double grade;

    public Course() {

    }

    public Course(String name, String description, double price, Resource[] content, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.content = content;
        totalTime = CourseDuration.of(content);

    }

    /**
     * Returns the name of the course.
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the description of the course.
     */
    public String getDescription() {

        return description;
    }

    /**
     * Returns the price of the course.
     */
    public double getPrice() {

        return price;
    }

    /**
     * Returns the category of the course.
     */
    public Category getCategory() {

        return category;
    }

    /**
     * Returns the content of the course.
     */
    public Resource[] getContent() {

        return content;
    }

    /**
     * Returns the total duration of the course.
     */
    public CourseDuration getTotalTime() {

        return totalTime;
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     * @throws IllegalArgumentException
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        boolean found = false;
        if (resourceToComplete == null) throw new IllegalArgumentException("illegal argument null in completeResource");
        if (content == null)
            throw new ResourceNotFoundException("resource not found in completeResource because null content");
        for (int i = 0; i < content.length; i++) {
            if (content[i] != null && content[i].equals(resourceToComplete)) {
                content[i].complete();
                found = true;
            }
        }
        if (!found) throw new ResourceNotFoundException("not found resource in completeResource");
    }

    public boolean areAllResourcesCompleted() {
        for (int i = 0; i < content.length; i++) {
            if (!content[i].isCompleted()) return false;
        }
        return true;
    }

    /**
     * Marks the object as purchased.
     */
    public void purchase() {

        isPurchased = true;
    }

    /**
     * Returns true if and only if the object is purchased.
     */
    public boolean isPurchased() {

        return isPurchased;
    }

    /**
     * Returns true if and only if the course is completed.
     */
    public boolean isCompleted() {

        return areAllResourcesCompleted();
    }

    /**
     * Returns the completion percentage of the resource.
     * The percentage is an integer in the range [0, 100].
     */
    public int getCompletionPercentage() {

        if (content == null || content.length == 0) return 0;
        double sum = 0;
        for (int i = 0; i < content.length; i++) {
            if (content[i] != null && content[i].isCompleted()) {
                sum += content[i].getCompletionPercentage();
            }
        }
        if (content.length == 0) return 0;
        return (int) Math.ceil((double) (sum / content.length));
    }

    /**
     * @param grade does not throw
     */
    public void setGrade(double grade) {

        this.grade = grade;
    }

    public double getGrade() {

        return grade;
    }

    public void complete() {

        isCompleted = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Course)) {
            return false;
        }

        Course that = (Course) obj;

        return Double.compare(that.price, price) == 0 &&
                Objects.equals(that.name, name) &&
                Objects.equals(that.description, description) &&
                Arrays.equals(that.content, content) &&
                Objects.equals(that.totalTime, totalTime) &&
                that.category == category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, category, Arrays.hashCode(content), totalTime);
    }
}
