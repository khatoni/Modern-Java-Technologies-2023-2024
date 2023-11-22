package bg.sofia.uni.fmi.mjt.intelligenthome.storage;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

public class MapDeviceStorageTest {

    @Test
    void testGetId() {
        IoTDevice device = new AmazonAlexa("alexa1", 100.0, LocalDateTime.now());
        MapDeviceStorage storage = mock();
        Mockito.when(storage.get(device.getId())).thenReturn(device);
        assertEquals(device, storage.get(device.getId()));
    }

    @Test
    void testDelete() {
        MapDeviceStorage mapDeviceStorage = mock();
        Mockito.when(mapDeviceStorage.delete("bulb123")).thenReturn(false);
        assertFalse(mapDeviceStorage.delete("bulb123"));
    }
}
