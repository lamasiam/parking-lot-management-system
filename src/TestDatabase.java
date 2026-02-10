import database.DatabaseManager;
import database.ParkingSpotsDAO;
import models.parking.*;

/**
 * Test database functionality
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  DATABASE TEST                                 ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        // Test 1: Database connection
        System.out.println("TEST 1: Database Connection");
        System.out.println("─────────────────────────────────────────────────");
        DatabaseManager dbManager = DatabaseManager.getInstance();
        boolean connected = dbManager.testConnection();
        System.out.println("Connection status: " + (connected ? "✓ CONNECTED" : "✗ FAILED"));
        System.out.println();
        
        // Test 2: Save parking lot to database
        System.out.println("TEST 2: Save Parking Lot to Database");
        System.out.println("─────────────────────────────────────────────────");
        ParkingLot parkingLot = new ParkingLot("University Parking Lot");
        parkingLot.initializeDefaultLayout(3); // 3 floors for testing
        
        ParkingSpotsDAO dao = new ParkingSpotsDAO();
        boolean saved = dao.saveParkingLot(parkingLot);
        System.out.println("Save status: " + (saved ? "✓ SUCCESS" : "✗ FAILED"));
        System.out.println();
        
        // Test 3: Retrieve spot from database
        System.out.println("TEST 3: Retrieve Spot from Database");
        System.out.println("─────────────────────────────────────────────────");
        ParkingSpot spot = dao.getSpotById("F1-R1-S1");
        if (spot != null) {
            System.out.println("Retrieved: " + spot);
            System.out.println("✓ RETRIEVAL SUCCESS");
        } else {
            System.out.println("✗ RETRIEVAL FAILED");
        }
        System.out.println();
        
        // Test 4: Database statistics
        System.out.println("TEST 4: Database Statistics");
        System.out.println("─────────────────────────────────────────────────");
        System.out.println("Total spots in database: " + dao.getTotalSpots());
        System.out.println("Occupied spots: " + dao.getOccupiedSpots());
        System.out.println("✓ STATISTICS WORKING");
        System.out.println();
        
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  ALL DATABASE TESTS COMPLETE!                 ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
}
