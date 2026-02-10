package models.fine;

import java.time.LocalDateTime;

public class Fine {
    private String licensePlate;
    private double amount;
    private String reason;
    private LocalDateTime dateIssued;
    private boolean isPaid;

    public Fine(String licensePlate, double amount, String reason) {
        this.licensePlate = licensePlate;
        this.amount = amount;
        this.reason = reason;
        this.dateIssued = LocalDateTime.now();
        this.isPaid = false;
    }

    // Getters
    public String getLicensePlate() { return licensePlate; }
    public double getAmount() { return amount; }
    public String getReason() { return reason; }
    public LocalDateTime getDateIssued() { return dateIssued; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}