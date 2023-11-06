package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.Objects;

public record Exercise(String name, int sets, int repetitions) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Exercise)) {
            return false;
        }

        Exercise that = (Exercise) obj;

        return that.name().equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
