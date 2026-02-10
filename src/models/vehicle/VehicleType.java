package models.vehicle;

/**
 * Enum representing different types of vehicles
 * This will be used by Member 2 for Vehicle hierarchy
 *
 * @author Member 1
 */
public enum VehicleType {
    MOTORCYCLE("Motorcycle"),
    CAR("Car"),
    SUV("SUV/Truck"),
    HANDICAPPED("Handicapped Vehicle");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
