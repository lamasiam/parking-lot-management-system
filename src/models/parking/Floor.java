package models.parking;

import models.vehicle.VehicleType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents one floor in the parking lot
 * Contains multiple parking spots arranged in rows
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class Floor {
    private int floorNumber;
    private List<ParkingSpot> spots;
    private int totalSpots;
    
    /**
     * Creates a new floor
     * 
     * @param floorNumber Floor number (1-based)
     */
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        this.totalSpots = 0;
    }
    
    /**
     * Adds a parking spot to this floor
     */
    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
        totalSpots++;
        System.out.println("Added spot " + spot.getSpotId() + " to Floor " + floorNumber);
    }
    
    /**
     * Removes a parking spot by ID
     */
    public boolean removeSpot(String spotId) {
        boolean removed = spots.removeIf(spot -> spot.getSpotId().equals(spotId));
        if (removed) {
            totalSpots--;
            System.out.println("Removed spot " + spotId + " from Floor " + floorNumber);
        }
        return removed;
    }
    
    /**
     * Gets all spots on this floor
     */
    public List<ParkingSpot> getAllSpots() {
        return new ArrayList<>(spots); // Return copy to prevent modification
    }
    
    /**
     * Gets all available spots on this floor
     */
    public List<ParkingSpot> getAvailableSpots() {
        return spots.stream()
                    .filter(ParkingSpot::isAvailable)
                    .collect(Collectors.toList());
    }
    
    /**
     * Gets available spots suitable for a specific vehicle type
     */
    public List<ParkingSpot> getAvailableSpotsForVehicle(VehicleType vehicleType) {
        return spots.stream()
                    .filter(spot -> spot.isAvailable())
                    .collect(Collectors.toList());
    }
    
    /**
     * Gets a spot by its ID
     */
    public ParkingSpot getSpotById(String spotId) {
        return spots.stream()
                    .filter(spot -> spot.getSpotId().equals(spotId))
                    .findFirst()
                    .orElse(null);
    }
    
    /**
     * Calculates occupancy rate for this floor
     */
    public double getOccupancyRate() {
        if (totalSpots == 0) return 0.0;
        
        long occupiedCount = spots.stream()
                                   .filter(spot -> !spot.isAvailable())
                                   .count();
        
        return (occupiedCount * 100.0) / totalSpots;
    }
    
    /**
     * Gets count of spots by type
     */
    public int getSpotCountByType(SpotType type) {
        return (int) spots.stream()
                          .filter(spot -> spot.getType() == type)
                          .count();
    }
    
    /**
     * Gets available count by type
     */
    public int getAvailableCountByType(SpotType type) {
        return (int) spots.stream()
                          .filter(spot -> spot.getType() == type && spot.isAvailable())
                          .count();
    }
    
    /**
     * Gets occupied count by type
     */
    public int getOccupiedCountByType(SpotType type) {
        return (int) spots.stream()
                          .filter(spot -> spot.getType() == type && !spot.isAvailable())
                          .count();
    }
    
    // Getters
    public int getFloorNumber() { return floorNumber; }
    public int getTotalSpots() { return totalSpots; }
    
    public int getOccupiedCount() { 
        return (int) spots.stream().filter(s -> !s.isAvailable()).count(); 
    }
    
    public int getAvailableCount() { 
        return (int) spots.stream().filter(ParkingSpot::isAvailable).count(); 
    }
    
    @Override
    public String toString() {
        return String.format("Floor %d: %d/%d spots occupied (%.1f%%)", 
            floorNumber, 
            getOccupiedCount(), 
            totalSpots, 
            getOccupancyRate());
    }
}
