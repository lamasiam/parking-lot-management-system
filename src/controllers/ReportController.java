package controllers;

import database.ParkingSpotsDAO;
import database.PaymentsDAO;
import database.VehiclesDAO;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller for generating reports
 * Aggregates data from multiple DAOs to present to the Admin View
 * * @author Hong - Report Module Lead
 */
public class ReportController {
    private ParkingSpotsDAO spotsDAO;
    private PaymentsDAO paymentsDAO;
    private VehiclesDAO vehiclesDAO;

    public ReportController() {
        this.spotsDAO = new ParkingSpotsDAO();
        this.paymentsDAO = new PaymentsDAO();
        this.vehiclesDAO = new VehiclesDAO();
    }

/**
     * Get summary statistics for the dashboard
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. Get accurate counts
        int totalSpots = spotsDAO.getTotalSpots();
        
        // FIX: Use vehicle count as the source of truth for occupancy
        int currentVehicles = vehiclesDAO.getCurrentVehicleCount(); 
        int occupiedSpots = currentVehicles; 
        
        // Calculate available based on that
        int availableSpots = totalSpots - occupiedSpots;
        
        // 2. Revenue Data
        double totalRevenue = paymentsDAO.getTotalRevenue();
        int totalTransactions = paymentsDAO.getTotalPaymentsCount();

        stats.put("total_spots", totalSpots);
        stats.put("occupied_spots", occupiedSpots);
        stats.put("available_spots", availableSpots);
        stats.put("total_revenue", totalRevenue);
        stats.put("total_transactions", totalTransactions);
        stats.put("current_vehicles", currentVehicles);
        
        return stats;
    }

    /**
     * Get list of all currently parked vehicles
     * Returns list of String arrays: [Plate, Type, Spot, EntryTime]
     */
    public List<String[]> getCurrentVehicleReport() {
        return vehiclesDAO.getAllCurrentVehicles();
    }

    /**
     * Get revenue breakdown by payment method
     */
    public Map<String, Double> getRevenueBreakdown() {
        Map<String, Double> breakdown = new HashMap<>();
        breakdown.put("CASH", paymentsDAO.getRevenueByMethod("CASH"));
        breakdown.put("CARD", paymentsDAO.getRevenueByMethod("CARD"));
        return breakdown;
    }

    /**
     * Get detailed transaction log
     * Returns: [Date, Plate, Method, Fee, Fine, Total]
     */
    public List<String[]> getTransactionHistory() {
        return paymentsDAO.getAllPayments();
    }

    /**
     * Get list of currently parked vehicles that have overstayed
     * Returns: [License Plate, Potential Fine, Overstay Duration, Entry Time]
     */
    public List<String[]> getOutstandingFines() {
        List<String[]> list = new ArrayList<>();
        List<String[]> activeVehicles = vehiclesDAO.getAllCurrentVehicles();
        
        // We need the strategy to calculate the fine
        models.fine.FineStrategy strategy = services.FineCalculator.getStrategy();
        
        for (String[] row : activeVehicles) {
            String plate = row[0];
            String entryTimeStr = row[3]; // "2023-10-27 10:00:00"
            
            try {
                // Parse Database String to LocalDateTime
                // Note: format might vary, so we use a safe parser or Timestamp
                java.sql.Timestamp ts = java.sql.Timestamp.valueOf(entryTimeStr);
                java.time.LocalDateTime entryTime = ts.toLocalDateTime();
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                
                // Calculate Duration
                long duration = java.time.Duration.between(entryTime, now).toHours();
                if (duration == 0) duration = 1;
                
                // Calculate Fine
                double fine = strategy.calculateFine(duration);
                
                // Only add if there IS a fine
                if (fine > 0) {
                    String durationStr = duration + " hours";
                    String fineStr = String.format("RM %.2f", fine);
                    
                    list.add(new String[]{plate, fineStr, durationStr, entryTimeStr});
                }
                
            } catch (Exception e) {
                System.err.println("Error parsing date for " + plate + ": " + e.getMessage());
            }
        }
        return list;
    }
}