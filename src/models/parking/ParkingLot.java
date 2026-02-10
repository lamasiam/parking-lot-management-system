package models.parking;

import models.vehicle.Vehicle;
import models.vehicle.VehicleType;
import java.util.ArrayList;
import java.util.List;

/**
 * Main parking lot class - contains multiple floors
 * Provides methods to find spots, assign vehicles, and track occupancy
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class ParkingLot {
    private String name;
    private List<Floor> floors;
    private int totalFloors;
    
    /**
     * Creates a new parking lot
     * 
     * @param name Name of the parking lot
     */
    public ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.totalFloors = 0;
        System.out.println("Created parking lot: " + name);
    }
    
    /**
     * Adds a new floor to the parking lot
     * 
     * @return The newly created floor
     */
    public Floor addFloor() {
        totalFloors++;
        Floor floor = new Floor(totalFloors);
        floors.add(floor);
        System.out.println("Added Floor " + totalFloors + " to " + name);
        return floor;
    }
    
    /**
     * Initializes parking lot with default layout
     * Creates multiple floors with standard spot distribution
     * 
     * @param numFloors Number of floors to create
     */
    public void initializeDefaultLayout(int numFloors) {
        System.out.println("\n=== Initializing " + name + " with " + numFloors + " floors ===");
        
        for (int f = 0; f < numFloors; f++) {
            Floor floor = addFloor();
            int spotCounter = 1;
            
            // Row 1: 5 Compact spots
            for (int i = 1; i <= 5; i++) {
                floor.addSpot(new CompactSpot(floor.getFloorNumber(), 1, spotCounter++));
            }
            
            // Row 2: 8 Regular spots
            for (int i = 1; i <= 8; i++) {
                floor.addSpot(new RegularSpot(floor.getFloorNumber(), 2, spotCounter++));
            }
            
            // Row 3: 2 Handicapped spots
            for (int i = 1; i <= 2; i++) {
                floor.addSpot(new HandicappedSpot(floor.getFloorNumber(), 3, spotCounter++));
            }
            
            // Row 4: 3 Reserved spots
            for (int i = 1; i <= 3; i++) {
                floor.addSpot(new ReservedSpot(floor.getFloorNumber(), 4, spotCounter++));
            }
        }
        
        System.out.println("=== Initialization complete ===");
        System.out.println(this);
        System.out.println();
    }
    
    /**
     * Finds available spots for a specific vehicle type
     * Implements the parking rules:
     * - Motorcycle: Compact only
     * - Car: Compact or Regular
     * - SUV: Regular only
     * - Handicapped: Any spot
     * 
     * @param vehicleType Type of vehicle
     * @return List of available suitable spots
     */
    public List<ParkingSpot> findAvailableSpots(VehicleType vehicleType) {
        List<ParkingSpot> availableSpots = new ArrayList<>();
        
        for (Floor floor : floors) {
            for (ParkingSpot spot : floor.getAvailableSpots()) {
                if (canParkHere(vehicleType, spot)) {
                    availableSpots.add(spot);
                }
            }
        }
        
        System.out.println("Found " + availableSpots.size() + 
                         " available spots for " + vehicleType);
        return availableSpots;
    }
    
    /**
     * Checks if a vehicle type can park in a spot type
     * Based on assignment requirements
     */
    private boolean canParkHere(VehicleType vehicleType, ParkingSpot spot) {
        SpotType spotType = spot.getType();
        
        switch (vehicleType) {
            case MOTORCYCLE:
                // Motorcycles can ONLY park in compact spots
                return spotType == SpotType.COMPACT;
            
            case CAR:
                // Cars can park in compact OR regular spots
                return spotType == SpotType.COMPACT || spotType == SpotType.REGULAR;
            
            case SUV:
                // SUVs can ONLY park in regular spots
                return spotType == SpotType.REGULAR;
            
            case HANDICAPPED:
                // Handicapped vehicles can park ANYWHERE
                return true;
            
            default:
                return false;
        }
    }
    
    /**
     * Gets a spot by its ID
     * Searches across all floors
     */
    public ParkingSpot getSpotById(String spotId) {
        for (Floor floor : floors) {
            ParkingSpot spot = floor.getSpotById(spotId);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }
    
    /**
     * Finds a vehicle by license plate
     * Used during exit process (Member 3 will call this)
     */
    public Vehicle findVehicleByLicensePlate(String licensePlate) {
        for (Floor floor : floors) {
            for (ParkingSpot spot : floor.getAllSpots()) {
                if (!spot.isAvailable() && 
                    spot.getCurrentVehicle() != null &&
                    spot.getCurrentVehicle().getLicensePlate().equals(licensePlate)) {
                    return spot.getCurrentVehicle();
                }
            }
        }
        return null;
    }
    
    /**
     * Finds the spot where a vehicle is parked
     * Used during exit process (Member 3 will call this)
     */
    public ParkingSpot findSpotByVehicle(String licensePlate) {
        for (Floor floor : floors) {
            for (ParkingSpot spot : floor.getAllSpots()) {
                if (!spot.isAvailable() && 
                    spot.getCurrentVehicle() != null &&
                    spot.getCurrentVehicle().getLicensePlate().equals(licensePlate)) {
                    return spot;
                }
            }
        }
        return null;
    }
    
    /**
     * Gets all currently parked vehicles
     * Used for reports (Member 4 will call this)
     */
    public List<Vehicle> getCurrentlyParkedVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        
        for (Floor floor : floors) {
            for (ParkingSpot spot : floor.getAllSpots()) {
                if (!spot.isAvailable() && spot.getCurrentVehicle() != null) {
                    vehicles.add(spot.getCurrentVehicle());
                }
            }
        }
        
        return vehicles;
    }
    
    /**
     * Calculates overall occupancy rate across all floors
     */
    public double getOverallOccupancyRate() {
        int totalSpots = getTotalSpots();
        if (totalSpots == 0) return 0.0;
        
        int occupiedSpots = getOccupiedSpots();
        return (occupiedSpots * 100.0) / totalSpots;
    }
    
    /**
     * Gets total number of spots across all floors
     */
    public int getTotalSpots() {
        return floors.stream()
                     .mapToInt(Floor::getTotalSpots)
                     .sum();
    }
    
    /**
     * Gets number of occupied spots
     */
    public int getOccupiedSpots() {
        return floors.stream()
                     .mapToInt(Floor::getOccupiedCount)
                     .sum();
    }
    
    /**
     * Gets number of available spots
     */
    public int getAvailableSpots() {
        return floors.stream()
                     .mapToInt(Floor::getAvailableCount)
                     .sum();
    }
    
    /**
     * Gets all floors
     */
    public List<Floor> getFloors() {
        return new ArrayList<>(floors);
    }
    
    /**
     * Gets a specific floor by number
     */
    public Floor getFloor(int floorNumber) {
        if (floorNumber < 1 || floorNumber > floors.size()) {
            return null;
        }
        return floors.get(floorNumber - 1);
    }
    
    // Getters
    public String getName() { return name; }
    public int getTotalFloors() { return totalFloors; }
    
    @Override
    public String toString() {
        return String.format("%s: %d floors, %d/%d spots occupied (%.1f%%)", 
            name, 
            totalFloors, 
            getOccupiedSpots(), 
            getTotalSpots(), 
            getOverallOccupancyRate());
    }
}
