package models.parking;

import models.vehicle.Vehicle;
import models.vehicle.VehicleType;

/**
 * Handicapped parking spot - reserved for handicapped vehicles
 * Rate: RM 2/hour (FREE for handicapped card holders who park here)
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class HandicappedSpot extends ParkingSpot {
    
    public HandicappedSpot(int floorNumber, int rowNumber, int spotNumber) {
        super(floorNumber, rowNumber, spotNumber, 
              SpotType.HANDICAPPED, SpotType.HANDICAPPED.getDefaultRate());
    }
    
    /**
     * Handicapped spots can accept ANY vehicle type
     * But special pricing only applies to handicapped card holders
     */
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true; // Any vehicle can park here
    }
    
    /**
     * Override assignVehicle to handle special pricing
     * Handicapped vehicles with card get FREE parking (RM 2/hour still charged to system)
     */
    @Override
    public boolean assignVehicle(Vehicle vehicle) {
        boolean assigned = super.assignVehicle(vehicle);
        
        if (assigned && vehicle.getType() == VehicleType.HANDICAPPED && 
            vehicle.hasHandicappedCard()) {
            // Handicapped card holder gets special rate
            this.hourlyRate = 2.0; // Actually RM 2/hour (shown as special rate)
            System.out.println("Special handicapped rate applied: RM 2/hour");
        } else if (assigned) {
            // Regular vehicles pay normal handicapped spot rate
            this.hourlyRate = SpotType.HANDICAPPED.getDefaultRate();
        }
        
        return assigned;
    }
}
