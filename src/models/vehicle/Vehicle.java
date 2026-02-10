package models.vehicle;

import java.time.LocalDateTime;

/**
 * Interface for Vehicle - Member 2 will implement concrete classes
 * This interface allows Member 1 to work independently
 *
 * @author Member 1
 */
public interface Vehicle {

    String getLicensePlate();

    VehicleType getType();

    LocalDateTime getEntryTime();

    void setEntryTime(LocalDateTime entryTime);

    LocalDateTime getExitTime();

    void setExitTime(LocalDateTime exitTime);

    boolean hasHandicappedCard();
}
