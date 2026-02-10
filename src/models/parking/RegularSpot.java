package models.parking;

import models.vehicle.Vehicle;
import models.vehicle.VehicleType;

/**
 * Regular parking spot - for regular cars and SUVs
 * Rate: RM 5/hour
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class RegularSpot extends ParkingSpot {
    
    public RegularSpot(int floorNumber, int rowNumber, int spotNumber) {
        super(floorNumber, rowNumber, spotNumber, 
              SpotType.REGULAR, SpotType.REGULAR.getDefaultRate());
    }
    
    /**
     * Regular spots can fit: Cars and SUVs
     * Motorcycles CAN park here but it's not optimal (they should use compact)
     */
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        VehicleType vType = vehicle.getType();
        return vType == VehicleType.CAR || vType == VehicleType.SUV;
    }
}
