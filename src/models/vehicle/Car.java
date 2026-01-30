package models.vehicle;

/**
 * Car class
 * Can park in Compact (RM 2/hour) OR Regular (RM 5/hour) spots
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class Car extends AbstractVehicle {
    
    /**
     * Creates a new car
     * 
     * @param licensePlate License plate number
     */
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
        System.out.println("Created Car: " + licensePlate);
    }
    
    @Override
    public String toString() {
        return "Car " + super.toString();
    }
}