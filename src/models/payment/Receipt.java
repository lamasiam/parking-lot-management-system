package models.payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Receipt class
 * Generates detailed payment receipt for parking exit
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public class Receipt {
    private String receiptId;
    private String licensePlate;
    private String spotId;
    private String spotType;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long durationHours;
    private double hourlyRate;
    private double parkingFee;
    private double fineAmount;
    private double totalPaid;
    private PaymentMethod paymentMethod;
    private String ticketId;
    
    /**
     * Creates a receipt from payment details
     * 
     * @param payment Payment transaction
     * @param spotId Parking spot ID
     * @param spotType Parking spot type
     * @param entryTime Vehicle entry time
     * @param exitTime Vehicle exit time
     * @param durationHours Duration in hours (ceiling rounded)
     * @param hourlyRate Hourly rate of the spot
     * @param ticketId Ticket ID
     */
    public Receipt(Payment payment, String spotId, String spotType, 
                   LocalDateTime entryTime, LocalDateTime exitTime, 
                   long durationHours, double hourlyRate, String ticketId) {
        this.licensePlate = payment.getLicensePlate();
        this.spotId = spotId;
        this.spotType = spotType;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationHours = durationHours;
        this.hourlyRate = hourlyRate;
        this.parkingFee = payment.getParkingFee();
        this.fineAmount = payment.getFineAmount();
        this.totalPaid = payment.getTotalAmount();
        this.paymentMethod = payment.getPaymentMethod();
        this.ticketId = ticketId;
        this.receiptId = payment.getPaymentId().replace("P-", "R-");
    }
    
    /**
     * Get formatted receipt as string
     */
    public String getFormattedReceipt() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("              PARKING EXIT RECEIPT\n");
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append(String.format("Receipt ID      : %s\n", receiptId));
        sb.append(String.format("Ticket ID       : %s\n", ticketId));
        sb.append(String.format("License Plate   : %s\n", licensePlate));
        sb.append(String.format("Parking Spot    : %s (%s)\n", spotId, spotType));
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(String.format("Entry Time      : %s\n", entryTime.format(formatter)));
        sb.append(String.format("Exit Time       : %s\n", exitTime.format(formatter)));
        sb.append(String.format("Duration        : %d hour%s\n", durationHours, durationHours > 1 ? "s" : ""));
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(String.format("Hourly Rate     : RM %.2f/hour\n", hourlyRate));
        sb.append(String.format("Parking Fee     : RM %.2f (%d × %.2f)\n", 
            parkingFee, durationHours, hourlyRate));
        
        if (fineAmount > 0) {
            sb.append(String.format("Fine Amount     : RM %.2f\n", fineAmount));
        }
        
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(String.format("TOTAL PAID      : RM %.2f\n", totalPaid));
        sb.append(String.format("Payment Method  : %s\n", paymentMethod.getDisplayName()));
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("          Thank you for parking with us!\n");
        sb.append("═══════════════════════════════════════════════════════\n");
        
        return sb.toString();
    }
    
    // Getters
    public String getReceiptId() {
        return receiptId;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public String getSpotId() {
        return spotId;
    }
    
    public String getSpotType() {
        return spotType;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    
    public long getDurationHours() {
        return durationHours;
    }
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public double getParkingFee() {
        return parkingFee;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public double getTotalPaid() {
        return totalPaid;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    @Override
    public String toString() {
        return String.format("Receipt[%s] - %s: RM %.2f",
            receiptId,
            licensePlate,
            totalPaid);
    }
}
