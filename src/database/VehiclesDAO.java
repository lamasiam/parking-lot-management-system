package database;

import models.vehicle.*;
import models.parking.ParkingSpot;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Vehicles
 * Handles all database operations for vehicles
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class VehiclesDAO {
    private Connection connection;
    
    public VehiclesDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
        createTable();
    }
    
    /**
     * Create vehicles table if not exists
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS vehicles (" +
                     "license_plate VARCHAR(20) PRIMARY KEY, " +
                     "vehicle_type VARCHAR(20) NOT NULL, " +
                     "has_handicapped_card BOOLEAN DEFAULT 0, " +
                     "entry_time TIMESTAMP NOT NULL, " +
                     "exit_time TIMESTAMP, " +
                     "spot_id VARCHAR(20), " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id)" +
                     ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Table 'vehicles' ready");
        } catch (SQLException e) {
            System.err.println("✗ Error creating vehicles table");
            e.printStackTrace();
        }
    }
    
    /**
     * Save a vehicle to database
     * 
     * @param vehicle Vehicle to save
     * @param spot Parking spot assigned
     * @return true if successful
     */
    public boolean saveVehicle(Vehicle vehicle, ParkingSpot spot) {
        String sql = "INSERT OR REPLACE INTO vehicles " +
                     "(license_plate, vehicle_type, has_handicapped_card, " +
                     "entry_time, spot_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getLicensePlate());
            pstmt.setString(2, vehicle.getType().name());
            pstmt.setBoolean(3, vehicle.hasHandicappedCard());
            pstmt.setTimestamp(4, Timestamp.valueOf(vehicle.getEntryTime()));
            pstmt.setString(5, spot.getSpotId());
            
            pstmt.executeUpdate();
            System.out.println("✓ Saved vehicle to database: " + vehicle.getLicensePlate());
            return true;
            
        } catch (SQLException e) {
            System.err.println("✗ Error saving vehicle: " + vehicle.getLicensePlate());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get vehicle by license plate
     * 
     * @param licensePlate License plate to search
     * @return Vehicle object or null
     */
    public Vehicle getVehicleByPlate(String licensePlate) {
        String sql = "SELECT * FROM vehicles WHERE license_plate = ? AND exit_time IS NULL";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createVehicleFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting vehicle: " + licensePlate);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update vehicle exit time
     * 
     * @param licensePlate License plate
     * @param exitTime Exit time
     * @return true if successful
     */
    public boolean updateExitTime(String licensePlate, LocalDateTime exitTime) {
        String sql = "UPDATE vehicles SET exit_time = ? WHERE license_plate = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(exitTime));
            pstmt.setString(2, licensePlate);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Updated exit time for: " + licensePlate);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error updating exit time: " + licensePlate);
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete vehicle record (after exit and payment)
     * 
     * @param licensePlate License plate
     * @return true if successful
     */
    public boolean deleteVehicle(String licensePlate) {
        String sql = "DELETE FROM vehicles WHERE license_plate = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Deleted vehicle record: " + licensePlate);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error deleting vehicle: " + licensePlate);
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get total currently parked vehicles
     * 
     * @return Count of vehicles
     */
    public int getCurrentVehicleCount() {
        String sql = "SELECT COUNT(*) as total FROM vehicles WHERE exit_time IS NULL";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error counting vehicles");
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Helper: Create Vehicle object from ResultSet
     */
    private Vehicle createVehicleFromResultSet(ResultSet rs) throws SQLException {
        String licensePlate = rs.getString("license_plate");
        String typeStr = rs.getString("vehicle_type");
        boolean hasCard = rs.getBoolean("has_handicapped_card");
        Timestamp entryTs = rs.getTimestamp("entry_time");
        Timestamp exitTs = rs.getTimestamp("exit_time");
        
        VehicleType type = VehicleType.valueOf(typeStr);
        
        Vehicle vehicle;
        switch (type) {
            case MOTORCYCLE:
                vehicle = new Motorcycle(licensePlate);
                break;
            case CAR:
                vehicle = new Car(licensePlate);
                break;
            case SUV:
                vehicle = new SUV(licensePlate);
                break;
            case HANDICAPPED:
                vehicle = new HandicappedVehicle(licensePlate, hasCard);
                break;
            default:
                return null;
        }
        
        if (entryTs != null) {
            vehicle.setEntryTime(entryTs.toLocalDateTime());
        }
        
        if (exitTs != null) {
            vehicle.setExitTime(exitTs.toLocalDateTime());
        }
        
        return vehicle;
    }

    /**
 * Get all currently parked vehicles with their spot info
 * Member 4 - Used for the Reporting Panel
 */
public List<String[]> getAllCurrentVehicles() {
    List<String[]> vehicles = new ArrayList<>();
    String sql = "SELECT v.license_plate, v.vehicle_type, v.entry_time, v.spot_id " + 
                 "FROM vehicles v WHERE v.exit_time IS NULL";
    
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            String[] row = new String[] {
                rs.getString("license_plate"),
                rs.getString("vehicle_type"),
                rs.getString("spot_id"),
                rs.getTimestamp("entry_time").toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            };
            vehicles.add(row);
        }
    } catch (SQLException e) {
        System.err.println("✗ Error getting all current vehicles");
        e.printStackTrace();
    }
    return vehicles;
}


}