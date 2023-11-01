package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {
    public CourseDuration {
        if (hours < 0 || hours > 24) throw new IllegalArgumentException("hours not valid in CourseDuration compact constructor");
        if (minutes < 0 || minutes > 60) throw new IllegalArgumentException("minutes not valid in CourseDuration compact constr");
    }

    public static CourseDuration of(Resource[] content) {
        int sum = 0;
        if (content == null) throw new IllegalArgumentException("content is null in CourseDuration of");
        for (int i = 0; i < content.length; i++) {
            if (content[i] != null) {
                sum += content[i].getDuration().minutes();
            }
        }
        return new CourseDuration(sum / 60, sum % 60);

    }
}
