package models.vehicle;

import java.time.LocalDateTime;

/**
 * Abstract base class for all vehicle types
 * Implements common vehicle functionality
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public abstract class AbstractVehicle implements Vehicle {
    protected String licensePlate;
    protected VehicleType type;
    protected LocalDateTime entryTime;
    protected LocalDateTime exitTime;
    
    /**
     * Constructor for creating a vehicle
     * 
     * @param licensePlate Vehicle's license plate number
     * @param type Type of vehicle
     */
    public AbstractVehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.entryTime = null;
        this.exitTime = null;
    }
    
    @Override
    public String getLicensePlate() {
        return licensePlate;
    }
    
    @Override
    public VehicleType getType() {
        return type;
    }
    
    @Override
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    @Override
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    
    @Override
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    
    @Override
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    
    /**
     * Default implementation - override in HandicappedVehicle
     */
    @Override
    public boolean hasHandicappedCard() {
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - Entry: %s", 
            type.getDisplayName(),
            licensePlate,
            entryTime != null ? entryTime.toString() : "Not parked");
    }
}