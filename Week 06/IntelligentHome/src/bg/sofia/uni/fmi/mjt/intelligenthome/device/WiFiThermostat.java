package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;
import java.util.Objects;

public class WiFiThermostat extends IoTDeviceBase {
    private String thermostatID;
    private DeviceType type;

    public WiFiThermostat(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        type = DeviceType.THERMOSTAT;
        thermostatID = type.getShortName() + '-' + name + '-' + uniqueNumberDevice;
        uniqueNumberDevice++;
    }

    public WiFiThermostat(WiFiThermostat other) {
        super(other.name, other.powerConsumption, other.installationDateTime);
        this.thermostatID = other.thermostatID;
        this.type = other.getType();
        uniqueNumberDevice++;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public String getId() {
        return thermostatID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WiFiThermostat that)) {
            return false;
        }

        return this.thermostatID.equals(that.thermostatID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.thermostatID);
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return super.getInstallationDateTime();
    }
}