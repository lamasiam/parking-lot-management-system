package models.vehicle;

/**
 * Motorcycle class
 * Can ONLY park in Compact spots (RM 2/hour)
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class Motorcycle extends AbstractVehicle {
    
    /**
     * Creates a new motorcycle
     * 
     * @param licensePlate License plate number
     */
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
        System.out.println("Created Motorcycle: " + licensePlate);
    }
    
    @Override
    public String toString() {
        return "Motorcycle " + super.toString();
    }
}