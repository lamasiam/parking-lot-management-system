package models.parking;

import models.vehicle.Vehicle;

/**
 * Abstract base class for all parking spot types
 * Each concrete spot type will define its own vehicle compatibility rules
 * 
 * @author Member 1 - Parking Structure Lead
 */
public abstract class ParkingSpot {
    protected String spotId;           // Format: F1-R1-S1
    protected SpotType type;
    protected SpotStatus status;
    protected Vehicle currentVehicle;
    protected double hourlyRate;
    protected int floorNumber;
    protected int rowNumber;
    protected int spotNumber;
    
    /**
     * Constructor for creating a parking spot
     * 
     * @param floorNumber Floor number (1-based)
     * @param rowNumber Row number (1-based)
     * @param spotNumber Spot number (1-based)
     * @param type Type of spot
     * @param hourlyRate Hourly rate for this spot
     */
    public ParkingSpot(int floorNumber, int rowNumber, int spotNumber, 
                       SpotType type, double hourlyRate) {
        this.floorNumber = floorNumber;
        this.rowNumber = rowNumber;
        this.spotNumber = spotNumber;
        this.type = type;
        this.hourlyRate = hourlyRate;
        this.status = SpotStatus.AVAILABLE;
        this.currentVehicle = null;
        this.spotId = generateSpotId();
    }
    
    /**
     * Generates spot ID in format: F{floor}-R{row}-S{spot}
     * Example: F1-R2-S5 means Floor 1, Row 2, Spot 5
     */
    private String generateSpotId() {
        return String.format("F%d-R%d-S%d", floorNumber, rowNumber, spotNumber);
    }
    
    /**
     * Checks if this spot is currently available
     */
    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }
    
    /**
     * Assigns a vehicle to this spot
     * 
     * @param vehicle Vehicle to assign
     * @return true if assignment successful, false if spot occupied
     */
    public boolean assignVehicle(Vehicle vehicle) {
        if (!isAvailable()) {
            System.out.println("Cannot assign vehicle - spot " + spotId + " is occupied");
            return false;
        }
        
        if (!canFitVehicle(vehicle)) {
            System.out.println("Vehicle type " + vehicle.getType() + 
                             " cannot park in " + type + " spot");
            return false;
        }
        
        this.currentVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
        System.out.println("Vehicle " + vehicle.getLicensePlate() + 
                         " assigned to spot " + spotId);
        return true;
    }
    
    /**
     * Releases the vehicle from this spot
     */
    public void releaseVehicle() {
        if (currentVehicle != null) {
            System.out.println("Releasing vehicle " + currentVehicle.getLicensePlate() + 
                             " from spot " + spotId);
        }
        this.currentVehicle = null;
        this.status = SpotStatus.AVAILABLE;
    }

    /**
     * Park a vehicle in this spot
     * Updates status to OCCUPIED
     */
    public void parkVehicle(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
    }

    /**
     * Remove vehicle from this spot
     * Updates status to AVAILABLE
     */
    public void removeVehicle() {
        this.currentVehicle = null;
        this.status = SpotStatus.AVAILABLE;
    }
    
    /**
     * Abstract method - each spot type defines which vehicles it can accept
     * 
     * @param vehicle Vehicle to check
     * @return true if vehicle can park here
     */
    public abstract boolean canFitVehicle(Vehicle vehicle);
    
    // Getters
    public String getSpotId() { return spotId; }
    public SpotType getType() { return type; }
    public SpotStatus getStatus() { return status; }
    public Vehicle getCurrentVehicle() { return currentVehicle; }
    public double getHourlyRate() { return hourlyRate; }
    public int getFloorNumber() { return floorNumber; }
    public int getRowNumber() { return rowNumber; }
    public int getSpotNumber() { return spotNumber; }
    
    // Setters
    public void setHourlyRate(double rate) { 
        this.hourlyRate = rate; 
    }
    
    @Override
    public String toString() {
        String vehicleInfo = currentVehicle != null ? 
            " [" + currentVehicle.getLicensePlate() + "]" : "";
        return String.format("%s (%s) - %s%s", 
            spotId, 
            type.getDisplayName(), 
            status.getDisplayName(),
            vehicleInfo);
    }
}
