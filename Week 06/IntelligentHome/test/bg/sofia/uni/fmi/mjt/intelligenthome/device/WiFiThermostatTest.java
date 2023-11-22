package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class WiFiThermostatTest {

    @Test
    void testEqualsForDifferentTypes() {
        IoTDevice device = new WiFiThermostat("wifi122", 250.0, LocalDateTime.now());
        Assertions.assertNotEquals(device, new Object(),
                "The object should not be equal");
    }

    @Test
    void testEqualsForTheSameObject() {
        WiFiThermostat device = new WiFiThermostat("wifi123", 250.0, LocalDateTime.now());
        WiFiThermostat cloning = new WiFiThermostat(device);
        Assertions.assertTrue(device.equals(cloning),
                "The object should be equal");
        Assertions.assertTrue(device.equals(device),
                "The same object must be equal");
    }

    @Test
    void testHashCode() {
        WiFiThermostat device = new WiFiThermostat("wifi122", 250.0, LocalDateTime.now());
        WiFiThermostat cloning = new WiFiThermostat(device);
        Assertions.assertTrue(device.equals(cloning));
        Assertions.assertEquals(device.hashCode(), cloning.hashCode(),
                "The hashCode must be the same");
    }
}
