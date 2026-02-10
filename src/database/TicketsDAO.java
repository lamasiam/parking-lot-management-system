package database;

import models.vehicle.Ticket;
import java.sql.*;

/**
 * Data Access Object for Tickets
 * Handles all database operations for parking tickets
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class TicketsDAO {
    private Connection connection;
    
    public TicketsDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
        createTable();
    }
    
    /**
     * Create tickets table if not exists
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets (" +
                     "ticket_id VARCHAR(50) PRIMARY KEY, " +
                     "license_plate VARCHAR(20) NOT NULL, " +
                     "spot_id VARCHAR(20) NOT NULL, " +
                     "entry_time TIMESTAMP NOT NULL, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate), " +
                     "FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id)" +
                     ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Table 'tickets' ready");
        } catch (SQLException e) {
            System.err.println("✗ Error creating tickets table");
            e.printStackTrace();
        }
    }
    
    /**
     * Save a ticket to database
     * 
     * @param ticket Ticket to save
     * @return true if successful
     */
    public boolean saveTicket(Ticket ticket) {
        String sql = "INSERT OR REPLACE INTO tickets " +
                     "(ticket_id, license_plate, spot_id, entry_time) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getTicketId());
            pstmt.setString(2, ticket.getVehicle().getLicensePlate());
            pstmt.setString(3, ticket.getSpot().getSpotId());
            pstmt.setTimestamp(4, Timestamp.valueOf(ticket.getEntryTime()));
            
            pstmt.executeUpdate();
            System.out.println("✓ Saved ticket to database: " + ticket.getTicketId());
            return true;
            
        } catch (SQLException e) {
            System.err.println("✗ Error saving ticket: " + ticket.getTicketId());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get ticket by ticket ID
     * 
     * @param ticketId Ticket ID to search
     * @return Ticket data as String array [ticketId, licensePlate, spotId, entryTime]
     */
    public String[] getTicketById(String ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new String[] {
                    rs.getString("ticket_id"),
                    rs.getString("license_plate"),
                    rs.getString("spot_id"),
                    rs.getTimestamp("entry_time").toString()
                };
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting ticket: " + ticketId);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get ticket by license plate
     * 
     * @param licensePlate License plate to search
     * @return Ticket data as String array
     */
    public String[] getTicketByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM tickets WHERE license_plate = ? " +
                     "ORDER BY entry_time DESC LIMIT 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new String[] {
                    rs.getString("ticket_id"),
                    rs.getString("license_plate"),
                    rs.getString("spot_id"),
                    rs.getTimestamp("entry_time").toString()
                };
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting ticket for: " + licensePlate);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Delete ticket (after vehicle exits)
     * 
     * @param ticketId Ticket ID to delete
     * @return true if successful
     */
    public boolean deleteTicket(String ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ticketId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Deleted ticket: " + ticketId);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error deleting ticket: " + ticketId);
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get total tickets issued (all time)
     * 
     * @return Total count
     */
    public int getTotalTicketsIssued() {
        String sql = "SELECT COUNT(*) as total FROM tickets";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error counting tickets");
            e.printStackTrace();
        }
        
        return 0;
    }
}