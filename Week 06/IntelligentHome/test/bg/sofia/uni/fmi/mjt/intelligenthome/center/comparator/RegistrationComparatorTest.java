package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationComparatorTest {

    @Test
    void testCompare() {
        IoTDevice testBulb = new RgbBulb("bulb", 100.0, LocalDateTime.now().minusHours(5));
        testBulb.setRegistration(LocalDateTime.now().minusHours(4).minusMinutes(20));
        IoTDevice testThermostat = new WiFiThermostat("thermo", 200.0, LocalDateTime.now().minusHours(5));
        testThermostat.setRegistration(LocalDateTime.now().minusHours(2));
        assertTrue(new RegistrationComparator().compare(testBulb, testThermostat) > 0,
                "The comparison is not executed as expected");
    }
}
