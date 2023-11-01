package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

import java.util.Objects;

public class Resource implements Completable {
    private String name;
    private ResourceDuration duration;
    private boolean isCompleted;
    private int completionPercentage;

    public Resource(String name, ResourceDuration duration) {
        this.name = name;
        this.duration = duration;
    }

    /**
     * Returns the resource name.
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the total duration of the resource.
     */
    public ResourceDuration getDuration() {

        return duration;
    }

    /**
     * Marks the resource as completed.
     */
    public void complete() {

        isCompleted = true;
        completionPercentage=100;
    }

    /**
     * Returns true if and only if the course is completed.
     */
    public boolean isCompleted() {

        return isCompleted;
    }

    /**
     * Returns the completion percentage of the resource.
     * The percentage is an integer in the range [0, 100].
     */
    public int getCompletionPercentage() {

        return completionPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }

        Resource that = (Resource) o;

        return that.completionPercentage == completionPercentage &&
                Objects.equals(that.name, name) &&
                Objects.equals(that.duration, duration) &&
                that.isCompleted == isCompleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, isCompleted, completionPercentage);
    }

}
