package controllers;

import models.parking.ParkingLot;
import models.parking.ParkingSpot;
import models.parking.SpotStatus; // Added import
import models.vehicle.*;
import database.VehiclesDAO;
import database.TicketsDAO;
import database.ParkingSpotsDAO; // Added import
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entry Controller - handles vehicle entry and parking process
 * Integrates with Member 1's ParkingLot class
 * Fixed: Now updates parking_spots table in database
 * * @author Member 2 - Vehicle & Entry Management Lead
 */
public class EntryController {
    private ParkingLot parkingLot;
    private VehiclesDAO vehiclesDAO;
    private TicketsDAO ticketsDAO;
    private ParkingSpotsDAO spotsDAO; // Added DAO
    
    /**
     * Creates entry controller
     * * @param parkingLot The parking lot to manage
     */
    public EntryController(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.vehiclesDAO = new VehiclesDAO();
        this.ticketsDAO = new TicketsDAO();
        this.spotsDAO = new ParkingSpotsDAO(); // Initialize DAO
        System.out.println("✓ EntryController initialized");
    }
    
    /**
     * Creates a vehicle based on type
     * * @param licensePlate License plate number
     * @param type Vehicle type
     * @param hasHandicappedCard Whether vehicle has handicapped card (for HANDICAPPED type)
     * @return Created vehicle
     */
    public Vehicle createVehicle(String licensePlate, VehicleType type, boolean hasHandicappedCard) {
        // Validate license plate
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be empty");
        }
        
        licensePlate = licensePlate.trim().toUpperCase();
        
        // Check if vehicle already parked
        Vehicle existingVehicle = parkingLot.findVehicleByLicensePlate(licensePlate);
        if (existingVehicle != null) {
            throw new IllegalStateException("Vehicle " + licensePlate + " is already parked!");
        }
        
        // Create appropriate vehicle type
        Vehicle vehicle;
        switch (type) {
            case MOTORCYCLE:
                vehicle = new Motorcycle(licensePlate);
                break;
            case CAR:
                vehicle = new Car(licensePlate);
                break;
            case SUV:
                vehicle = new SUV(licensePlate);
                break;
            case HANDICAPPED:
                vehicle = new HandicappedVehicle(licensePlate, hasHandicappedCard);
                break;
            default:
                throw new IllegalArgumentException("Invalid vehicle type: " + type);
        }
        
        return vehicle;
    }
    
    /**
     * Find available spots for a vehicle type
     * * @param vehicleType Type of vehicle
     * @return List of available suitable spots
     */
    public List<ParkingSpot> findAvailableSpots(VehicleType vehicleType) {
        return parkingLot.findAvailableSpots(vehicleType);
    }
    
    /**
     * Park a vehicle in a specific spot
     * * @param vehicle Vehicle to park
     * @param spotId ID of the spot to park in
     * @return Generated parking ticket, or null if failed
     */
    public Ticket parkVehicle(Vehicle vehicle, String spotId) {
        // Get the spot
        ParkingSpot spot = parkingLot.getSpotById(spotId);
        if (spot == null) {
            System.err.println("✗ Spot not found: " + spotId);
            return null;
        }
        
        // Check if spot is available
        if (!spot.isAvailable()) {
            System.err.println("✗ Spot " + spotId + " is already occupied");
            return null;
        }
        
        // Set entry time
        LocalDateTime entryTime = LocalDateTime.now();
        vehicle.setEntryTime(entryTime);
        
        // Assign vehicle to spot (uses Member 1's method - Updates Memory)
        boolean assigned = spot.assignVehicle(vehicle);
        
        if (!assigned) {
            System.err.println("✗ Failed to assign vehicle to spot");
            return null;
        }
        
        // Generate ticket
        Ticket ticket = new Ticket(vehicle, spot);
        
        // Save to database (Updates DB)
        vehiclesDAO.saveVehicle(vehicle, spot);
        ticketsDAO.saveTicket(ticket);
        
        // --- FIX START: Update the Spot Status in Database ---
        spotsDAO.updateSpotStatus(spot.getSpotId(), SpotStatus.OCCUPIED, vehicle.getLicensePlate());
        // --- FIX END ---
        
        System.out.println("✓ Vehicle parked successfully!");
        System.out.println("  Vehicle: " + vehicle.getLicensePlate());
        System.out.println("  Spot: " + spot.getSpotId());
        System.out.println("  Ticket: " + ticket.getTicketId());
        
        return ticket;
    }
    
    /**
     * Complete parking process - find spot and park
     * * @param licensePlate License plate number
     * @param vehicleType Type of vehicle
     * @param hasHandicappedCard Whether has handicapped card
     * @param preferredSpotId Optional preferred spot ID (can be null)
     * @return Generated ticket, or null if failed
     */
    public Ticket processParkingEntry(String licensePlate, VehicleType vehicleType, 
                                     boolean hasHandicappedCard, String preferredSpotId) {
        try {
            // Create vehicle
            Vehicle vehicle = createVehicle(licensePlate, vehicleType, hasHandicappedCard);
            
            // Determine spot to use
            String spotId = preferredSpotId;
            
            if (spotId == null || spotId.trim().isEmpty()) {
                // Find first available spot
                List<ParkingSpot> availableSpots = findAvailableSpots(vehicleType);
                if (availableSpots.isEmpty()) {
                    System.err.println("✗ No available spots for " + vehicleType);
                    return null;
                }
                spotId = availableSpots.get(0).getSpotId();
            }
            
            // Park vehicle
            return parkVehicle(vehicle, spotId);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("✗ Parking entry failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get parking lot statistics
     * * @return Statistics string
     */
    public String getParkingLotStats() {
        return String.format(
            "Total Spots: %d | Available: %d | Occupied: %d | Occupancy: %.1f%%",
            parkingLot.getTotalSpots(),
            parkingLot.getAvailableSpots(),
            parkingLot.getOccupiedSpots(),
            parkingLot.getOverallOccupancyRate()
        );
    }
    
    /**
     * Validate license plate format
     * * @param licensePlate License plate to validate
     * @return true if valid
     */
    public boolean validateLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation: 3-10 alphanumeric characters
        String cleaned = licensePlate.trim().replaceAll("[^A-Za-z0-9]", "");
        return cleaned.length() >= 3 && cleaned.length() <= 10;
    }
}