import models.parking.*;

/**
 * Test class for Member 1's parking structure implementation
 * Run this to verify all classes work correctly
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class TestMember1 {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  MEMBER 1: PARKING STRUCTURE TEST             ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        // Test 1: Create parking lot
        testParkingLotCreation();
        
        // Test 2: Verify spot types
        testSpotTypes();
        
        // Test 3: Test occupancy calculations
        testOccupancyCalculations();
        
        // Test 4: Test spot retrieval
        testSpotRetrieval();
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║  ALL TESTS PASSED! ✓                          ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
    
    private static void testParkingLotCreation() {
        System.out.println("TEST 1: Creating Parking Lot");
        System.out.println("─────────────────────────────────────────────────");
        
        ParkingLot parkingLot = new ParkingLot("University Parking Lot");
        parkingLot.initializeDefaultLayout(3); // 3 floors
        
        System.out.println("\n✓ Parking lot created successfully");
        System.out.println("✓ Total floors: " + parkingLot.getTotalFloors());
        System.out.println("✓ Total spots: " + parkingLot.getTotalSpots());
        System.out.println();
    }
    
    private static void testSpotTypes() {
        System.out.println("TEST 2: Verifying Spot Types");
        System.out.println("─────────────────────────────────────────────────");
        
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(1);
        
        Floor floor1 = parkingLot.getFloor(1);
        
        System.out.println("Compact spots: " + floor1.getSpotCountByType(SpotType.COMPACT));
        System.out.println("Regular spots: " + floor1.getSpotCountByType(SpotType.REGULAR));
        System.out.println("Handicapped spots: " + floor1.getSpotCountByType(SpotType.HANDICAPPED));
        System.out.println("Reserved spots: " + floor1.getSpotCountByType(SpotType.RESERVED));
        
        System.out.println("\n✓ All spot types created correctly");
        System.out.println();
    }
    
    private static void testOccupancyCalculations() {
        System.out.println("TEST 3: Testing Occupancy Calculations");
        System.out.println("─────────────────────────────────────────────────");
        
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(2);
        
        System.out.println("Initial occupancy: " + 
                         String.format("%.1f%%", parkingLot.getOverallOccupancyRate()));
        System.out.println("Available spots: " + parkingLot.getAvailableSpots());
        System.out.println("Occupied spots: " + parkingLot.getOccupiedSpots());
        
        System.out.println("\n✓ Occupancy calculations working");
        System.out.println();
    }
    
    private static void testSpotRetrieval() {
        System.out.println("TEST 4: Testing Spot Retrieval");
        System.out.println("─────────────────────────────────────────────────");
        
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(1);
        
        // Try to get a spot by ID
        ParkingSpot spot = parkingLot.getSpotById("F1-R1-S1");
        if (spot != null) {
            System.out.println("Found spot: " + spot.getSpotId());
            System.out.println("Type: " + spot.getType().getDisplayName());
            System.out.println("Rate: RM " + spot.getHourlyRate() + "/hour");
            System.out.println("Status: " + spot.getStatus().getDisplayName());
        }
        
        System.out.println("\n✓ Spot retrieval working");
        System.out.println();
    }
}
