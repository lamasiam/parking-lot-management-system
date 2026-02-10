package models.payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Payment model class
 * Represents a payment transaction for parking fees
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public class Payment {
    private String paymentId;
    private String licensePlate;
    private double parkingFee;
    private double fineAmount;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentTime;
    private String ticketId;
    
    /**
     * Creates a new payment record
     * 
     * @param licensePlate Vehicle license plate
     * @param parkingFee Parking fee amount
     * @param fineAmount Fine amount (if any)
     * @param paymentMethod Payment method used
     * @param ticketId Associated ticket ID
     */
    public Payment(String licensePlate, double parkingFee, double fineAmount, 
                   PaymentMethod paymentMethod, String ticketId) {
        this.licensePlate = licensePlate;
        this.parkingFee = parkingFee;
        this.fineAmount = fineAmount;
        this.totalAmount = parkingFee + fineAmount;
        this.paymentMethod = paymentMethod;
        this.ticketId = ticketId;
        this.paymentTime = LocalDateTime.now();
        this.paymentId = generatePaymentId();
    }
    
    /**
     * Generates payment ID in format: P-PLATE-TIMESTAMP
     * Example: P-ABC123-20250130143022
     */
    private String generatePaymentId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = paymentTime.format(formatter);
        return String.format("P-%s-%s", licensePlate, timestamp);
    }
    
    /**
     * Get formatted payment time for display
     */
    public String getFormattedPaymentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return paymentTime.format(formatter);
    }
    
    // Getters
    public String getPaymentId() {
        return paymentId;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public double getParkingFee() {
        return parkingFee;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    @Override
    public String toString() {
        return String.format("Payment[%s] - %s: RM %.2f (%s)",
            paymentId,
            licensePlate,
            totalAmount,
            paymentMethod.getDisplayName());
    }
}
