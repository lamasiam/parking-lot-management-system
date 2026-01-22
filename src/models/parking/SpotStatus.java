package models.parking;

/**
 * Enum representing the status of a parking spot
 *
 * @author Member 1
 */
public enum SpotStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied");

    private final String displayName;

    SpotStatus(String displayName) {
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
