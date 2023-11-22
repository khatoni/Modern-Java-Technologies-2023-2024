package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KWhComparatorTest {

    @Test
    void testCompare() {
        IoTDevice testBulb = new RgbBulb("bulb", 100.0, LocalDateTime.now().minusHours(5));
        IoTDevice testThermostat = new WiFiThermostat("thermo", 200.0, LocalDateTime.now().minusHours(5));
        assertTrue(new KWhComparator().compare(testBulb, testThermostat) < 0,
                "The comparison is not executed as expected");
    }
}
