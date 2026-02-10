package models.parking;

import models.vehicle.Vehicle;
import models.vehicle.VehicleType;

/**
 * Compact parking spot - for motorcycles and small cars
 * Rate: RM 2/hour
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class CompactSpot extends ParkingSpot {
    
    public CompactSpot(int floorNumber, int rowNumber, int spotNumber) {
        super(floorNumber, rowNumber, spotNumber, 
              SpotType.COMPACT, SpotType.COMPACT.getDefaultRate());
    }
    
    /**
     * Compact spots can fit: Motorcycles and Cars only
     * SUVs are too large for compact spots
     */
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        VehicleType vType = vehicle.getType();
        return vType == VehicleType.MOTORCYCLE || vType == VehicleType.CAR;
    }
}
