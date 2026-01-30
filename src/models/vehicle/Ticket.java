package models.vehicle;

import models.parking.ParkingSpot;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Parking Ticket class
 * Generated when a vehicle enters the parking lot
 * Format: T-{LICENSE_PLATE}-{TIMESTAMP}
 * Example: T-ABC123-20250130143022
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class Ticket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot spot;
    private LocalDateTime entryTime;
    
    /**
     * Creates a new parking ticket
     * 
     * @param vehicle Vehicle being parked
     * @param spot Parking spot assigned
     */
    public Ticket(Vehicle vehicle, ParkingSpot spot) {
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
        this.ticketId = generateTicketId();
        
        System.out.println("✓ Generated ticket: " + ticketId);
    }
    
    /**
     * Generates ticket ID in format: T-PLATE-TIMESTAMP
     * Example: T-ABC123-20250130143022
     */
    private String generateTicketId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = entryTime.format(formatter);
        return String.format("T-%s-%s", vehicle.getLicensePlate(), timestamp);
    }
    
    /**
     * Get formatted entry time for display
     */
    public String getFormattedEntryTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return entryTime.format(formatter);
    }
    
    /**
     * Get ticket details as formatted string
     */
    public String getTicketDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════\n");
        sb.append("       PARKING TICKET\n");
        sb.append("═══════════════════════════════════════\n");
        sb.append(String.format("Ticket ID    : %s\n", ticketId));
        sb.append(String.format("License Plate: %s\n", vehicle.getLicensePlate()));
        sb.append(String.format("Vehicle Type : %s\n", vehicle.getType().getDisplayName()));
        sb.append(String.format("Spot ID      : %s\n", spot.getSpotId()));
        sb.append(String.format("Spot Type    : %s\n", spot.getType().getDisplayName()));
        sb.append(String.format("Hourly Rate  : RM %.2f/hour\n", spot.getHourlyRate()));
        sb.append(String.format("Entry Time   : %s\n", getFormattedEntryTime()));
        sb.append("═══════════════════════════════════════\n");
        sb.append("Please keep this ticket for exit.\n");
        sb.append("═══════════════════════════════════════\n");
        return sb.toString();
    }
    
    // Getters
    public String getTicketId() {
        return ticketId;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public ParkingSpot getSpot() {
        return spot;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    @Override
    public String toString() {
        return String.format("Ticket[%s] - %s at %s (Entry: %s)",
            ticketId,
            vehicle.getLicensePlate(),
            spot.getSpotId(),
            getFormattedEntryTime());
    }
}