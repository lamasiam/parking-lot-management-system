package database;

import java.sql.*;
import models.payment.Payment;

/**
 * Data Access Object for Payments
 * Handles all database operations for payments and fines
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public class PaymentsDAO {
    private Connection connection;
    
    public PaymentsDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
        createTable();
    }
    
    /**
     * Create payments table if not exists
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS payments (" +
                     "payment_id VARCHAR(50) PRIMARY KEY, " +
                     "license_plate VARCHAR(20) NOT NULL, " +
                     "ticket_id VARCHAR(50) NOT NULL, " +
                     "parking_fee DECIMAL(10,2) NOT NULL, " +
                     "fine_amount DECIMAL(10,2) DEFAULT 0, " +
                     "total_amount DECIMAL(10,2) NOT NULL, " +
                     "payment_method VARCHAR(20) NOT NULL, " +
                     "payment_time TIMESTAMP NOT NULL, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                     ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Table 'payments' ready");
        } catch (SQLException e) {
            System.err.println("✗ Error creating payments table");
            e.printStackTrace();
        }
    }
    
    /**
     * Save a payment to database
     * 
     * @param payment Payment to save
     * @return true if successful
     */
    public boolean savePayment(Payment payment) {
        String sql = "INSERT INTO payments " +
                     "(payment_id, license_plate, ticket_id, parking_fee, " +
                     "fine_amount, total_amount, payment_method, payment_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, payment.getPaymentId());
            pstmt.setString(2, payment.getLicensePlate());
            pstmt.setString(3, payment.getTicketId());
            pstmt.setDouble(4, payment.getParkingFee());
            pstmt.setDouble(5, payment.getFineAmount());
            pstmt.setDouble(6, payment.getTotalAmount());
            pstmt.setString(7, payment.getPaymentMethod().name());
            pstmt.setTimestamp(8, Timestamp.valueOf(payment.getPaymentTime()));
            
            pstmt.executeUpdate();
            System.out.println("✓ Saved payment to database: " + payment.getPaymentId());
            return true;
            
        } catch (SQLException e) {
            System.err.println("✗ Error saving payment: " + payment.getPaymentId());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get payment by payment ID
     * 
     * @param paymentId Payment ID to search
     * @return Payment data as array, or null if not found
     */
    public String[] getPaymentById(String paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new String[] {
                    rs.getString("payment_id"),
                    rs.getString("license_plate"),
                    rs.getString("ticket_id"),
                    String.valueOf(rs.getDouble("parking_fee")),
                    String.valueOf(rs.getDouble("fine_amount")),
                    String.valueOf(rs.getDouble("total_amount")),
                    rs.getString("payment_method"),
                    rs.getTimestamp("payment_time").toString()
                };
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting payment: " + paymentId);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all payments for a license plate
     * 
     * @param licensePlate License plate to search
     * @return Total amount paid by this vehicle
     */
    public double getTotalPaidByVehicle(String licensePlate) {
        String sql = "SELECT SUM(total_amount) as total FROM payments " +
                     "WHERE license_plate = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting total paid for: " + licensePlate);
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Get unpaid fines for a license plate
     * For now, returns 0 (fine system can be implemented later)
     * 
     * @param licensePlate License plate to check
     * @return Unpaid fine amount
     */
    public double getUnpaidFines(String licensePlate) {
        // For now, return 0 - fine system can be implemented in future
        // This is where you would check for overstaying fines or other penalties
        return 0.0;
    }
    
    /**
     * Get total revenue collected
     * 
     * @return Total revenue
     */
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) as total FROM payments";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error calculating total revenue");
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Get total payments count
     * 
     * @return Total number of payments
     */
    public int getTotalPaymentsCount() {
        String sql = "SELECT COUNT(*) as total FROM payments";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error counting payments");
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get revenue by payment method
     * 
     * @param method Payment method (CASH or CARD)
     * @return Revenue for that method
     */
    public double getRevenueByMethod(String method) {
        String sql = "SELECT SUM(total_amount) as total FROM payments " +
                     "WHERE payment_method = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, method);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting revenue by method: " + method);
            e.printStackTrace();
        }
        
        return 0.0;
    }

    /**
     * Get all payment records for the detailed report
     * Orders by newest first
     */
    public java.util.List<String[]> getAllPayments() {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_time DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String[] row = {
                    rs.getTimestamp("payment_time").toLocalDateTime().toString().replace("T", " "), // Date
                    rs.getString("license_plate"),          // Plate
                    rs.getString("payment_method"),         // Method (Cash/Card)
                    String.format("%.2f", rs.getDouble("parking_fee")), // Fee
                    String.format("%.2f", rs.getDouble("fine_amount")), // Fine
                    String.format("%.2f", rs.getDouble("total_amount")) // Total
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
