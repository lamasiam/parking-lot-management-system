package database;

import models.parking.*;
import models.vehicle.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Parking Spots
 * Handles all database operations for parking spots
 * * @author Member 1 - Parking Structure Lead
 */
public class ParkingSpotsDAO {
    private Connection connection;
    
    public ParkingSpotsDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    /**
     * Save a parking spot to database
     */
    public boolean saveSpot(ParkingSpot spot) {
        String sql = "INSERT OR REPLACE INTO parking_spots " +
                     "(spot_id, floor_number, row_number, spot_number, " +
                     "spot_type, hourly_rate, status, current_vehicle) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, spot.getSpotId());
            pstmt.setInt(2, spot.getFloorNumber());
            pstmt.setInt(3, spot.getRowNumber());
            pstmt.setInt(4, spot.getSpotNumber());
            pstmt.setString(5, spot.getType().name());
            pstmt.setDouble(6, spot.getHourlyRate());
            pstmt.setString(7, spot.getStatus().name());
            
            // Save vehicle license plate if occupied
            String vehiclePlate = null;
            if (spot.getCurrentVehicle() != null) {
                vehiclePlate = spot.getCurrentVehicle().getLicensePlate();
            }
            pstmt.setString(8, vehiclePlate);
            
            pstmt.executeUpdate();
            System.out.println("✓ Saved spot: " + spot.getSpotId());
            return true;
            
        } catch (SQLException e) {
            System.err.println("✗ Error saving spot: " + spot.getSpotId());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update spot status (when vehicle parks/leaves)
     */
    public boolean updateSpotStatus(String spotId, SpotStatus status, String vehiclePlate) {
        String sql = "UPDATE parking_spots SET status = ?, current_vehicle = ? " +
                     "WHERE spot_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setString(2, vehiclePlate);
            pstmt.setString(3, spotId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Updated spot status: " + spotId + " -> " + status);
                return true;
            } else {
                System.err.println("✗ Spot not found: " + spotId);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error updating spot status: " + spotId);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a spot by ID from database
     */
    public ParkingSpot getSpotById(String spotId) {
        String sql = "SELECT * FROM parking_spots WHERE spot_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, spotId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createSpotFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error getting spot: " + spotId);
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Load all spots for a specific floor
     */
    public List<ParkingSpot> getSpotsByFloor(int floorNumber) {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE floor_number = ? " +
                     "ORDER BY row_number, spot_number";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, floorNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ParkingSpot spot = createSpotFromResultSet(rs);
                if (spot != null) {
                    spots.add(spot);
                }
            }
            
            System.out.println("✓ Loaded " + spots.size() + " spots for Floor " + floorNumber);
            
        } catch (SQLException e) {
            System.err.println("✗ Error loading spots for floor: " + floorNumber);
            e.printStackTrace();
        }
        
        return spots;
    }
    
    /**
     * Save entire parking lot structure to database
     */
    public boolean saveParkingLot(ParkingLot parkingLot) {
        System.out.println("\n=== Saving parking lot to database ===");
        int successCount = 0;
        int totalSpots = 0;
        
        for (Floor floor : parkingLot.getFloors()) {
            for (ParkingSpot spot : floor.getAllSpots()) {
                totalSpots++;
                if (saveSpot(spot)) {
                    successCount++;
                }
            }
        }
        
        System.out.println("✓ Saved " + successCount + "/" + totalSpots + " spots to database");
        return successCount == totalSpots;
    }
    
    /**
     * Delete a spot from database
     */
    public boolean deleteSpot(String spotId) {
        String sql = "DELETE FROM parking_spots WHERE spot_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, spotId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Deleted spot: " + spotId);
                return true;
            } else {
                System.err.println("✗ Spot not found: " + spotId);
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error deleting spot: " + spotId);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get total number of spots in database
     */
    public int getTotalSpots() {
        String sql = "SELECT COUNT(*) as total FROM parking_spots";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error counting total spots");
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get number of occupied spots
     */
    public int getOccupiedSpots() {
        String sql = "SELECT COUNT(*) as total FROM parking_spots WHERE status = 'OCCUPIED'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error counting occupied spots");
            e.printStackTrace();
        }
        
        return 0;
    }

/**
     * Load the entire Parking Lot structure and Sync with Active Vehicles
     * FIX: Trusts the 'vehicles' table as the Source of Truth to ensure 
     * Report Panel and Admin Panel are 100% synchronized.
     */
    public ParkingLot loadParkingLot(String name) {
        // 1. Check if we have data
        if (getTotalSpots() == 0) {
            return null; 
        }

        System.out.println("Loading existing parking lot data...");
        ParkingLot lot = new ParkingLot(name);
        VehiclesDAO vehiclesDAO = new VehiclesDAO();

        // 2. Load the Layout (Floors and Spots)
        String sql = "SELECT * FROM parking_spots ORDER BY floor_number, row_number, spot_number";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int floorNum = rs.getInt("floor_number");
                
                // Create floors as we find them
                while (lot.getTotalFloors() < floorNum) {
                    lot.addFloor();
                }
                
                // Create the spot 
                ParkingSpot spot = createSpotFromResultSet(rs);
                
                // Add spot to floor
                Floor targetFloor = lot.getFloor(floorNum);
                if (targetFloor != null) {
                    targetFloor.addSpot(spot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // 3. Force-Sync Active Vehicles from the Vehicles Table
        System.out.println("Syncing active vehicles...");
        List<String[]> activeVehicles = vehiclesDAO.getAllCurrentVehicles(); // The method we added earlier
        int restoredCount = 0;

        for (String[] row : activeVehicles) {
            String licensePlate = row[0];
            String spotId = row[2];
            
            // Get the full vehicle object
            Vehicle vehicle = vehiclesDAO.getVehicleByPlate(licensePlate);
            ParkingSpot spot = lot.getSpotById(spotId);
            
            if (vehicle != null && spot != null) {
                // FORCE the car into the spot
                spot.parkVehicle(vehicle);
                restoredCount++;
                
                // If the spot table was wrong, fix it now
                updateSpotStatus(spotId, SpotStatus.OCCUPIED, licensePlate);
            }
        }

        System.out.println("✓ Sync Complete. Restored " + restoredCount + " active vehicles.");
        return lot;
    }
    /**
     * Helper method: Create ParkingSpot object from database ResultSet
     */
    private ParkingSpot createSpotFromResultSet(ResultSet rs) throws SQLException {
        int floorNum = rs.getInt("floor_number");
        int rowNum = rs.getInt("row_number");
        int spotNum = rs.getInt("spot_number");
        String typeStr = rs.getString("spot_type");
        
        SpotType spotType = SpotType.valueOf(typeStr);
        
        // Create appropriate spot type
        ParkingSpot spot;
        switch (spotType) {
            case COMPACT:
                spot = new CompactSpot(floorNum, rowNum, spotNum);
                break;
            case REGULAR:
                spot = new RegularSpot(floorNum, rowNum, spotNum);
                break;
            case HANDICAPPED:
                spot = new HandicappedSpot(floorNum, rowNum, spotNum);
                break;
            case RESERVED:
                spot = new ReservedSpot(floorNum, rowNum, spotNum);
                break;
            default:
                return null;
        }
        
        return spot;
    }
    
    /**
     * Clear all spots from database (for testing)
     */
    public boolean clearAllSpots() {
        String sql = "DELETE FROM parking_spots";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Cleared all spots from database");
            return true;
            
        } catch (SQLException e) {
            System.err.println("✗ Error clearing spots");
            e.printStackTrace();
            return false;
        }
    }
}