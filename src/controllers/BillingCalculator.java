package controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Billing Calculator
 * Calculates parking fees with CEILING rounding for duration
 * 
 * IMPORTANT: Duration is rounded UP to the nearest hour
 * Example: 1 hour 1 minute = 2 hours
 * Example: 2 hours 30 minutes = 3 hours
 * Example: 3 hours 0 minutes = 3 hours (exact)
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public class BillingCalculator {
    
    /**
     * Calculate parking duration in hours (CEILING rounding)
     * Rounds UP to the nearest hour
     * 
     * @param entryTime Vehicle entry time
     * @param exitTime Vehicle exit time
     * @return Duration in hours (rounded UP)
     */
    public static long calculateDuration(LocalDateTime entryTime, LocalDateTime exitTime) {
        if (entryTime == null || exitTime == null) {
            throw new IllegalArgumentException("Entry time and exit time cannot be null");
        }
        
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }
        
        // Calculate total minutes
        long totalMinutes = ChronoUnit.MINUTES.between(entryTime, exitTime);
        
        // Convert to hours with CEILING rounding
        // If there are any remaining minutes, round up to next hour
        long hours = totalMinutes / 60;
        long remainingMinutes = totalMinutes % 60;
        
        if (remainingMinutes > 0) {
            hours++; // Round UP
        }
        
        // Minimum 1 hour
        return Math.max(1, hours);
    }
    
    /**
     * Calculate parking fee
     * 
     * @param durationHours Duration in hours
     * @param hourlyRate Hourly rate of the spot
     * @return Total parking fee
     */
    public static double calculateParkingFee(long durationHours, double hourlyRate) {
        if (durationHours < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        
        return durationHours * hourlyRate;
    }
    
    /**
     * Calculate total bill including fines
     * 
     * @param parkingFee Parking fee amount
     * @param fineAmount Fine amount (0 if no fines)
     * @return Total amount due
     */
    public static double calculateTotalBill(double parkingFee, double fineAmount) {
        return parkingFee + fineAmount;
    }
    
    /**
     * Check if vehicle has overstayed (more than 24 hours)
     * 
     * @param durationHours Duration in hours
     * @return true if overstayed
     */
    public static boolean hasOverstayed(long durationHours) {
        return durationHours > 24;
    }
    
    /**
     * Format duration as human-readable string
     * 
     * @param entryTime Entry time
     * @param exitTime Exit time
     * @return Formatted duration string
     */
    public static String formatDuration(LocalDateTime entryTime, LocalDateTime exitTime) {
        long hours = calculateDuration(entryTime, exitTime);
        return hours + (hours == 1 ? " hour" : " hours");
    }
}
