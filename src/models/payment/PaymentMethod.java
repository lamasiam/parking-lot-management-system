package models.payment;

/**
 * Payment Method enum
 * Defines available payment methods for parking fees
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public enum PaymentMethod {
    CASH("Cash"),
    CARD("Card");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
