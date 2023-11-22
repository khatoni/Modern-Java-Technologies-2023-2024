package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RgbBulbTest {

    @Test
    void testEqualsForDifferentTypes() {
        IoTDevice device = new RgbBulb("bulb122", 250.0, LocalDateTime.now());
        Assertions.assertNotEquals(device, new Object(),
                "The object should not be equal");
    }

    @Test
    void testEqualsForTheSameObject() {
        RgbBulb device = new RgbBulb("bulb123", 250.0, LocalDateTime.now());
        RgbBulb cloning = new RgbBulb(device);
        Assertions.assertTrue(device.equals(cloning),
                "The object must be equal");
        Assertions.assertTrue(device.equals(device),
                "The same object must be equal");
    }

    @Test
    void testHashCode() {
        RgbBulb device = new RgbBulb("bulb122", 250.0, LocalDateTime.now());
        RgbBulb cloning = new RgbBulb(device);
        Assertions.assertTrue(device.equals(cloning));
        Assertions.assertEquals(device.hashCode(), cloning.hashCode(),
                "The hashCode must be the same");
    }
}
