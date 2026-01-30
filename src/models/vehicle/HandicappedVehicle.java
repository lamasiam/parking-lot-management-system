package models.vehicle;

/**
 * Handicapped Vehicle class
 * Can park in ANY spot type
 * If has handicapped card and parks in handicapped spot: RM 2/hour
 * Otherwise: normal spot rates apply
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class HandicappedVehicle extends AbstractVehicle {
    private boolean hasHandicappedCard;
    
    /**
     * Creates a new handicapped vehicle
     * 
     * @param licensePlate License plate number
     * @param hasHandicappedCard Whether driver has a handicapped card
     */
    public HandicappedVehicle(String licensePlate, boolean hasHandicappedCard) {
        super(licensePlate, VehicleType.HANDICAPPED);
        this.hasHandicappedCard = hasHandicappedCard;
        System.out.println("Created Handicapped Vehicle: " + licensePlate + 
                         " (Card: " + (hasHandicappedCard ? "Yes" : "No") + ")");
    }
    
    /**
     * Override to return actual handicapped card status
     */
    @Override
    public boolean hasHandicappedCard() {
        return hasHandicappedCard;
    }
    
    /**
     * Set handicapped card status
     */
    public void setHandicappedCard(boolean hasCard) {
        this.hasHandicappedCard = hasCard;
    }
    
    @Override
    public String toString() {
        return "Handicapped Vehicle " + super.toString() + 
               " [Card: " + (hasHandicappedCard ? "Yes" : "No") + "]";
    }
}