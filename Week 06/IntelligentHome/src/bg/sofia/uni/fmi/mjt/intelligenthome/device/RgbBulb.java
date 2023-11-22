package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;
import java.util.Objects;

public class RgbBulb extends IoTDeviceBase {
    private String bulbID;
    private DeviceType type;

    public RgbBulb(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        type = DeviceType.BULB;
        bulbID = type.getShortName() + '-' + name + '-' + uniqueNumberDevice;
        uniqueNumberDevice++;
    }

    public RgbBulb(RgbBulb other) {
        super(other.name, other.powerConsumption, other.installationDateTime);
        this.bulbID = other.bulbID;
        this.type = other.getType();
        uniqueNumberDevice++;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public String getId() {
        return bulbID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RgbBulb that)) {
            return false;
        }

        return this.bulbID.equals(that.bulbID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bulbID);
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return super.getInstallationDateTime();
    }
}