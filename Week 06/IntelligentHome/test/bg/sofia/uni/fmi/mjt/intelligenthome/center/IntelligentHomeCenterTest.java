package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.MapDeviceStorage;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class IntelligentHomeCenterTest {

    @Test
    void testRegistrationWithAlreadyRegisteredDevice() {
        DeviceStorage storage = new MapDeviceStorage();
        IoTDevice testAlexa = new AmazonAlexa("Alexa", 220.0, LocalDateTime.now());
        IoTDevice testBulb = new RgbBulb("heg", 260.0, LocalDateTime.now());
        IoTDevice testThermostat = new WiFiThermostat("thermo", 125.25, LocalDateTime.now());
        storage.store(testAlexa.getId(), testAlexa);
        storage.store(testBulb.getId(), testBulb);
        storage.store(testThermostat.getId(), testThermostat);
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(storage);
        assertThrows(DeviceAlreadyRegisteredException.class,
                () -> intelligentHomeCenter.register(testAlexa), "Expected DeviceAlreadyRegistered exception");
        assertThrows(DeviceAlreadyRegisteredException.class,
                () -> intelligentHomeCenter.register(testAlexa), "Expected DeviceAlreadyRegistered exception");
        assertThrows(DeviceAlreadyRegisteredException.class,
                () -> intelligentHomeCenter.register(testAlexa), "Expected DeviceAlreadyRegistered exception");
    }

    @Test
    void testRegistrationWithNullDevice() {
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(new MapDeviceStorage());
        assertThrows(IllegalArgumentException.class,
                () -> intelligentHomeCenter.register(null), "IllegalArgumentException expected");
    }

    @Test
    void testSuccessfulRegistration() throws DeviceAlreadyRegisteredException, DeviceNotFoundException {
        DeviceStorage storage = new MapDeviceStorage();
        IoTDevice testAlexa = new AmazonAlexa("Alexa", 220.0, LocalDateTime.now());
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(storage);
        intelligentHomeCenter.register(testAlexa);
        assertEquals(intelligentHomeCenter.getDeviceById(testAlexa.getId()), testAlexa,
                "The registration test failed");
    }

    @Test
    void testUnregisterWithNull() {
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(new MapDeviceStorage());
        assertThrows(IllegalArgumentException.class,
                () -> intelligentHomeCenter.unregister(null), "IllegalArgumentException expected");
    }

    @Test
    void testUnregisterValidDevice() throws DeviceAlreadyRegisteredException, DeviceNotFoundException {
        DeviceStorage storage = new MapDeviceStorage();
        IoTDevice testAlexa = new AmazonAlexa("Alexa", 220.0, LocalDateTime.now());
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(storage);
        intelligentHomeCenter.register(testAlexa);
        intelligentHomeCenter.unregister(testAlexa);
        assertThrows(DeviceNotFoundException.class,
                () -> intelligentHomeCenter.getDeviceById(testAlexa.getId()), "Unregistration test failed");
    }

    @Test
    void testUnregisterForDeviceNotFound() {
        DeviceStorage storage = new MapDeviceStorage();
        IoTDevice device = new WiFiThermostat("test", 120, LocalDateTime.now());
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(storage);
        assertThrows(DeviceNotFoundException.class,
                () -> intelligentHomeCenter.unregister(device), "DeviceNotFound expected");
    }

    @Test
    void testGetDeviceByIllegalId() throws DeviceNotFoundException {
        assertThrows(IllegalArgumentException.class,
                () -> new IntelligentHomeCenter(new MapDeviceStorage()).getDeviceById(null),
                "IllegalArgumentException expected");
        assertThrows(IllegalArgumentException.class,
                () -> new IntelligentHomeCenter(new MapDeviceStorage()).getDeviceById(""),
                "IllegalArgumentException expected");
    }

    @Test
    void testGetDeviceByInvalidId() {
        IoTDevice device = new AmazonAlexa("testAlexa", 250.0, LocalDateTime.now());
        assertThrows(DeviceNotFoundException.class,
                () -> new IntelligentHomeCenter(new MapDeviceStorage()).getDeviceById(device.getId()),
                "DeviceNotFoundException expected");
    }

    @Test
    void testGetDeviceByValidId() throws DeviceNotFoundException {
        IoTDevice device = new AmazonAlexa("testAlexa", 250.0, LocalDateTime.now());
        DeviceStorage storage = new MapDeviceStorage();
        storage.store(device.getId(), device);
        assertEquals(device, new IntelligentHomeCenter(storage).getDeviceById(device.getId()),
                "The devices must be equal");
    }

    @Test
    void testGetDeviceQuantityPerTypeForNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new IntelligentHomeCenter(new MapDeviceStorage()).getDeviceQuantityPerType(null),
                "IllegalArgumentException expected");
    }

    @Test
    void testGetDeviceQuantityPerType() {
        DeviceStorage storage = new MapDeviceStorage();
        IoTDevice firstDeviceAlexa = new AmazonAlexa("alexa", 250.0, LocalDateTime.now());
        IoTDevice secondDeviceAlexa = new AmazonAlexa("alexa", 250.0, LocalDateTime.now());
        storage.store(firstDeviceAlexa.getId(), firstDeviceAlexa);
        storage.store(secondDeviceAlexa.getId(), secondDeviceAlexa);
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(storage);
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER),
                "The expected number is different");
        assertEquals(0, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.BULB),
                "The expected number is different");
        assertEquals(0, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT),
                "The expected number is different");

    }

    @Test
    void testGetTopNDevicesByPowerForInvalidN() {
        assertThrows(IllegalArgumentException.class,
                () -> new IntelligentHomeCenter(new MapDeviceStorage()).getTopNDevicesByPowerConsumption(-1),
                "IllegalArgumentException expected");

    }

    @Test
    void testGetTopNDevicesByPowerValidInput() {
        LocalDateTime firstTime = LocalDateTime.now();
        LocalDateTime secondTime = LocalDateTime.now();
        LocalDateTime thenFirst = firstTime.plusHours(7);
        LocalDateTime thenSecond = secondTime.plusHours(15);
        DeviceStorage storage = new MapDeviceStorage();
        IoTDevice firstDeviceAlexa = new AmazonAlexa("alexa1", 250.0, LocalDateTime.now());
        firstDeviceAlexa.setRegistration(thenFirst);
        IoTDevice secondDeviceAlexa = new AmazonAlexa("alexa2", 250.0, LocalDateTime.now());
        secondDeviceAlexa.setRegistration(thenSecond);
        storage.store(firstDeviceAlexa.getId(), firstDeviceAlexa);
        storage.store(secondDeviceAlexa.getId(), secondDeviceAlexa);
        Collection<String> collection = new ArrayList<>();
        collection.add(firstDeviceAlexa.getId());
        collection.add(secondDeviceAlexa.getId());
        assertIterableEquals(collection, new IntelligentHomeCenter(storage).getTopNDevicesByPowerConsumption(2),
                "The two collections must be exactly the same");
    }

    @Test
    void testGetFirstNDevicesByRegistrationForInvalidN() {
        assertThrows(IllegalArgumentException.class,
                () -> new IntelligentHomeCenter(new MapDeviceStorage()).getFirstNDevicesByRegistration(0),
                "IllegalArgumentException expected");
    }

    @Test
    void testGetFirstNDevicesByRegistrationForValidN() throws DeviceAlreadyRegisteredException {

        DeviceStorage storage = new MapDeviceStorage();
        IntelligentHomeCenter intelligentHomeCenter = new IntelligentHomeCenter(storage);
        IoTDevice firstDeviceAlexa = new AmazonAlexa("alexa1", 250.0, LocalDateTime.now());
        intelligentHomeCenter.register(firstDeviceAlexa);
        IoTDevice secondDeviceAlexa = new AmazonAlexa("alexa2", 125.0, LocalDateTime.now());
        intelligentHomeCenter.register(secondDeviceAlexa);
        Collection<IoTDevice> collection = new LinkedList<>();
        collection.add(firstDeviceAlexa);
        collection.add(secondDeviceAlexa);
        assertIterableEquals(collection, intelligentHomeCenter.getFirstNDevicesByRegistration(2),
                "The two collections must be exactly the same");
    }

}
