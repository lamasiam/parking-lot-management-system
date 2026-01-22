package models.parking;

/**
 * Enum representing different types of parking spots
 * Each type has a display name and default hourly rate
 *
 * @author Member 1
 */
public enum SpotType {
    COMPACT("Compact", 2.0),
    REGULAR("Regular", 5.0),
    HANDICAPPED("Handicapped", 2.0),
    RESERVED("Reserved", 10.0);

    private final String displayName;
    private final double defaultRate;

    SpotType(String displayName, double defaultRate) {
        this.displayName = displayName;
        this.defaultRate = defaultRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultRate() {
        return defaultRate;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
