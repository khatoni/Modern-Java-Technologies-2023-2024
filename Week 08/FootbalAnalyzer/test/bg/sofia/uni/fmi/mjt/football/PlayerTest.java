package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class PlayerTest {

    @Test
    void testPlayerOfSuccessfullyCreated() {
        String data =
            "Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left";
        Player test = new Player("Messi", "Lionel Andrés Messi Cuccittini",
            LocalDate.of(1987, 6, 24), 31, 170.18, 72.1,
            List.of(), "Argentina", 94, 94,
            110500000, 565000, Foot.LEFT);

        Player myPlayer = Player.of(data);
        Assertions.assertEquals(test, myPlayer);
    }
}
