package models.parking;

import models.vehicle.Vehicle;

/**
 * Reserved parking spot - for VIP customers
 * Rate: RM 10/hour
 * Fine applied if parked without reservation
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class ReservedSpot extends ParkingSpot {
    private boolean hasReservation;
    private String reservedFor; // License plate of reserved vehicle
    
    public ReservedSpot(int floorNumber, int rowNumber, int spotNumber) {
        super(floorNumber, rowNumber, spotNumber, 
              SpotType.RESERVED, SpotType.RESERVED.getDefaultRate());
        this.hasReservation = false;
        this.reservedFor = null;
    }
    
    /**
     * Reserved spots can fit any vehicle type
     */
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true;
    }
    
    /**
     * Override to check reservation status
     * NOTE: Member 4 will handle fine if parked without reservation
     */
    @Override
    public boolean assignVehicle(Vehicle vehicle) {
        // Check if someone else has reservation
        if (hasReservation && !vehicle.getLicensePlate().equals(reservedFor)) {
            System.out.println("WARNING: Spot reserved for " + reservedFor + 
                             " but " + vehicle.getLicensePlate() + " is parking");
            // Still allow parking but Member 4 will add fine
        }
        
        return super.assignVehicle(vehicle);
    }
    
    /**
     * Make a reservation for a specific license plate
     */
    public void makeReservation(String licensePlate) {
        this.hasReservation = true;
        this.reservedFor = licensePlate;
        System.out.println("Reservation made for " + licensePlate + " at spot " + spotId);
    }
    
    /**
     * Cancel the reservation
     */
    public void cancelReservation() {
        this.hasReservation = false;
        this.reservedFor = null;
        System.out.println("Reservation cancelled for spot " + spotId);
    }
    
    // Getters
    public boolean hasReservation() {
        return hasReservation;
    }
    
    public String getReservedFor() {
        return reservedFor;
    }
}
