package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IoTDeviceBaseTest {
    @Test
    void testGetPowerConsumptionKwh() {
        IoTDevice amazon = new AmazonAlexa("alexa", 250.0, LocalDateTime.now());
        assertEquals(250.0, amazon.getPowerConsumption(),
                0.0001, "The powerConsumption must be the same");
    }

    @Test
    void testGetName() {
        IoTDevice amazon = new AmazonAlexa("alexa", 250.0, LocalDateTime.now());
        assertEquals("alexa", amazon.getName(), "The names must be the same");
    }
}
