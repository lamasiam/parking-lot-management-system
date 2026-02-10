package models.vehicle;

/**
 * SUV/Truck class
 * Can ONLY park in Regular spots (RM 5/hour)
 * Too large for Compact spots
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class SUV extends AbstractVehicle {
    
    /**
     * Creates a new SUV
     * 
     * @param licensePlate License plate number
     */
    public SUV(String licensePlate) {
        super(licensePlate, VehicleType.SUV);
        System.out.println("Created SUV: " + licensePlate);
    }
    
    @Override
    public String toString() {
        return "SUV " + super.toString();
    }
}