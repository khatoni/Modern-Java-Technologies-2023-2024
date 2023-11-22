package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class AmazonAlexaTest {

    @Test
    void testEqualsForDifferentTypes() {
        IoTDevice device = new AmazonAlexa("alexaa", 250.0, LocalDateTime.now());
        Assertions.assertNotEquals(device, new Object(),
                "The devices should not be equal");
    }

    @Test
    void testEqualsForTheSameObject() {
        AmazonAlexa device = new AmazonAlexa("alexaa", 250.0, LocalDateTime.now());
        AmazonAlexa cloning = new AmazonAlexa(device);
        Assertions.assertTrue(device.equals(cloning),
                "The devices must be equal");
    }

    @Test
    void testHashCode() {
        AmazonAlexa device = new AmazonAlexa("alexaa", 250.0, LocalDateTime.now());
        AmazonAlexa cloning = new AmazonAlexa(device);
        Assertions.assertTrue(device.equals(cloning));
        Assertions.assertEquals(device.hashCode(), cloning.hashCode(),
                "The hashCode must be the same");
    }
}
