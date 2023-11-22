package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;
import java.util.Objects;

public class AmazonAlexa extends IoTDeviceBase {
    private String alexaID;
    private DeviceType type;

    public AmazonAlexa(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        type = DeviceType.SMART_SPEAKER;
        alexaID = type.getShortName() + '-' + name + '-' + uniqueNumberDevice;
        uniqueNumberDevice++;
    }

    public AmazonAlexa(AmazonAlexa other) {
        super(other.name, other.powerConsumption, other.installationDateTime);
        type = DeviceType.SMART_SPEAKER;
        this.alexaID = other.alexaID;
        uniqueNumberDevice++;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public String getId() {
        return alexaID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AmazonAlexa that)) {
            return false;
        }

        return this.alexaID.equals(that.alexaID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alexaID);
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return super.getInstallationDateTime();
    }
}