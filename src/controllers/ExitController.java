package controllers;

import models.parking.ParkingLot;
import models.parking.ParkingSpot;
import models.parking.SpotStatus;
import models.vehicle.Vehicle;
import models.payment.Payment;
import models.payment.PaymentMethod;
import models.payment.Receipt;
import database.TicketsDAO;
import database.VehiclesDAO;
import database.ParkingSpotsDAO;
import database.PaymentsDAO;
import services.FineCalculator;

import java.time.LocalDateTime;
import java.sql.Timestamp;

/**
 * Exit Controller - INTEGRATED WITH STRATEGY PATTERN
 * Handles vehicle exit, fine calculation, and payment process
 * * @author Member 3 - Exit & Payment Management Lead
 */
public class ExitController {
    private ParkingLot parkingLot;
    private TicketsDAO ticketsDAO;
    private VehiclesDAO vehiclesDAO;
    private ParkingSpotsDAO spotsDAO;
    private PaymentsDAO paymentsDAO;
    private PaymentProcessor paymentProcessor;
    
    public ExitController(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.ticketsDAO = new TicketsDAO();
        this.vehiclesDAO = new VehiclesDAO();
        this.spotsDAO = new ParkingSpotsDAO();
        this.paymentsDAO = new PaymentsDAO();
        this.paymentProcessor = new PaymentProcessor();
        System.out.println("✓ ExitController initialized");
    }
    
    public Vehicle findVehicle(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return null;
        }
        return parkingLot.findVehicleByLicensePlate(licensePlate.trim().toUpperCase());
    }
    
    public ParkingSpot getVehicleSpot(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return parkingLot.findSpotByVehicle(vehicle.getLicensePlate());
    }
    
    public double[] calculateBill(String licensePlate) {
        Vehicle vehicle = findVehicle(licensePlate);
        if (vehicle == null) {
            System.err.println("✗ Vehicle not found: " + licensePlate);
            return null;
        }
        
        ParkingSpot spot = getVehicleSpot(vehicle);
        if (spot == null) {
            System.err.println("✗ Parking spot not found for vehicle: " + licensePlate);
            return null;
        }


        
        // --- FIX START: RELOAD ENTRY TIME FROM DATABASE ---
        // We trust the database (which Admin Panel updated) over the stale Java object
LocalDateTime entryTime;
        String[] ticketData = ticketsDAO.getTicketByLicensePlate(licensePlate);
        
        if (ticketData != null) {
            try {
                // Parse time from DB
                entryTime = java.sql.Timestamp.valueOf(ticketData[3]).toLocalDateTime();
                
                // ✅ FIX: Update the memory object so the UI updates too!
                // (This fixes the "Today/Yesterday" display bug)
                vehicle.setEntryTime(entryTime); 
                
            } catch (Exception e) {
                entryTime = vehicle.getEntryTime();
            }
        } else {
            entryTime = vehicle.getEntryTime();
        }
        
        // --- FIX END ---
        
        if (entryTime == null) {
            System.err.println("✗ Entry time not found for vehicle: " + licensePlate);
            return null;
        }
        
        LocalDateTime exitTime = LocalDateTime.now();
        
        // 1. Calculate Duration
        long durationHours = BillingCalculator.calculateDuration(entryTime, exitTime);
        
        // 2. Calculate Standard Parking Fee
        double hourlyRate = spot.getHourlyRate();
        double parkingFee = BillingCalculator.calculateParkingFee(durationHours, hourlyRate);
        
        // 3. STRATEGY PATTERN: Calculate Overstay Fine (Current Session)
        double overstayFine = FineCalculator.calculate(durationHours);
        
        // 4. Get Previous Unpaid Fines (from Database)
        double previousFines = paymentsDAO.getUnpaidFines(licensePlate);
        
        // 5. Total Fine & Total Bill
        double totalFine = overstayFine + previousFines;
        double totalBill = BillingCalculator.calculateTotalBill(parkingFee, totalFine);
        
        System.out.println("✓ Bill calculated for " + licensePlate);
        System.out.println("  Duration: " + durationHours + " hours");
        System.out.println("  Parking Fee: RM " + String.format("%.2f", parkingFee));
        System.out.println("  Overstay Fine: RM " + String.format("%.2f", overstayFine));
        System.out.println("  Previous Fines: RM " + String.format("%.2f", previousFines));
        System.out.println("  Total Due: RM " + String.format("%.2f", totalBill));
        
        return new double[]{durationHours, parkingFee, totalFine, totalBill};
    }

    public Receipt processExit(String licensePlate, PaymentMethod paymentMethod) {
        try {
            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                System.err.println("✗ License plate is required");
                return null;
            }
            
            licensePlate = licensePlate.trim().toUpperCase();
            
            Vehicle vehicle = findVehicle(licensePlate);
            if (vehicle == null) {
                System.err.println("✗ Vehicle not found: " + licensePlate);
                return null;
            }
            
            ParkingSpot spot = getVehicleSpot(vehicle);
            if (spot == null) {
                System.err.println("✗ Parking spot not found");
                return null;
            }
            
            String[] ticketData = ticketsDAO.getTicketByLicensePlate(licensePlate);
            if (ticketData == null) {
                System.err.println("✗ Ticket not found for vehicle: " + licensePlate);
                return null;
            }
            
            String ticketId = ticketData[0];
            String spotId = ticketData[2];
            Timestamp entryTimestamp = Timestamp.valueOf(ticketData[3]);
            LocalDateTime entryTime = entryTimestamp.toLocalDateTime();
            LocalDateTime exitTime = LocalDateTime.now();
            
            // --- RE-CALCULATE EVERYTHING TO BE SAFE ---
            long durationHours = BillingCalculator.calculateDuration(entryTime, exitTime);
            double hourlyRate = spot.getHourlyRate();
            double parkingFee = BillingCalculator.calculateParkingFee(durationHours, hourlyRate);
            
            // STRATEGY PATTERN INTEGRATION
            double overstayFine = FineCalculator.calculate(durationHours);
            double previousFines = paymentsDAO.getUnpaidFines(licensePlate);
            double totalFine = overstayFine + previousFines;
            
            Payment payment = paymentProcessor.processPayment(
                licensePlate,
                parkingFee,
                totalFine, // Pass the total fine (new + old)
                paymentMethod,
                ticketId
            );
            
            if (payment == null) {
                System.err.println("✗ Payment processing failed");
                return null;
            }
            
            paymentsDAO.savePayment(payment);
            
            Receipt receipt = new Receipt(
                payment,
                spotId,
                spot.getType().getDisplayName(),
                entryTime,
                exitTime,
                durationHours,
                hourlyRate,
                ticketId
            );
            
            spot.releaseVehicle();
            
            // Update Spot Status in Database
            spotsDAO.updateSpotStatus(spotId, SpotStatus.AVAILABLE, null);
            
            vehiclesDAO.deleteVehicle(licensePlate);
            ticketsDAO.deleteTicket(ticketId);
            
            System.out.println("✓ Vehicle exit successful!");
            System.out.println("  Vehicle: " + licensePlate);
            System.out.println("  Spot released: " + spotId);
            System.out.println("  Receipt: " + receipt.getReceiptId());
            
            return receipt;
            
        } catch (Exception e) {
            System.err.println("✗ Exit processing failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String getParkingLotStats() {
        return String.format(
            "Total Spots: %d | Available: %d | Occupied: %d | Occupancy: %.1f%%",
            parkingLot.getTotalSpots(),
            parkingLot.getAvailableSpots(),
            parkingLot.getOccupiedSpots(),
            parkingLot.getOverallOccupancyRate()
        );
    }
    
    public boolean validateLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        String cleaned = licensePlate.trim().replaceAll("[^A-Za-z0-9]", "");
        return cleaned.length() >= 3 && cleaned.length() <= 10;
    }
}