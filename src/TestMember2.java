import models.parking.*;
import models.vehicle.*;
import controllers.EntryController;
import database.VehiclesDAO;
import database.TicketsDAO;
import java.time.LocalDateTime;

/**
 * Test class for Member 2's vehicle and entry system implementation
 * 
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class TestMember2 {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  MEMBER 2: VEHICLE & ENTRY SYSTEM TEST        ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        // Test 1: Vehicle Creation
        testVehicleCreation();
        
        // Test 2: Ticket Generation
        testTicketGeneration();
        
        // Test 3: Entry Controller
        testEntryController();
        
        // Test 4: Database Integration
        testDatabaseIntegration();
        
        // Test 5: Parking Rules
        testParkingRules();
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║  ALL MEMBER 2 TESTS PASSED! ✓                 ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
    
    private static void testVehicleCreation() {
        System.out.println("TEST 1: Vehicle Creation");
        System.out.println("─────────────────────────────────────────────────");
        
        // Create each vehicle type
        Vehicle motorcycle = new Motorcycle("M123");
        Vehicle car = new Car("C456");
        Vehicle suv = new SUV("S789");
        Vehicle handicapped = new HandicappedVehicle("H999", true);
        
        // Set entry times
        LocalDateTime now = LocalDateTime.now();
        motorcycle.setEntryTime(now);
        car.setEntryTime(now);
        suv.setEntryTime(now);
        handicapped.setEntryTime(now);
        
        // Verify types
        assert motorcycle.getType() == VehicleType.MOTORCYCLE : "Motorcycle type incorrect";
        assert car.getType() == VehicleType.CAR : "Car type incorrect";
        assert suv.getType() == VehicleType.SUV : "SUV type incorrect";
        assert handicapped.getType() == VehicleType.HANDICAPPED : "Handicapped type incorrect";
        
        // Verify handicapped card
        assert !motorcycle.hasHandicappedCard() : "Motorcycle should not have card";
        assert handicapped.hasHandicappedCard() : "Handicapped vehicle should have card";
        
        System.out.println("✓ Motorcycle created: " + motorcycle.getLicensePlate());
        System.out.println("✓ Car created: " + car.getLicensePlate());
        System.out.println("✓ SUV created: " + suv.getLicensePlate());
        System.out.println("✓ Handicapped vehicle created: " + handicapped.getLicensePlate());
        System.out.println("\n✓ All vehicles created successfully\n");
    }
    
    private static void testTicketGeneration() {
        System.out.println("TEST 2: Ticket Generation");
        System.out.println("─────────────────────────────────────────────────");
        
        // Create a parking lot
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(1);
        
        // Create vehicle
        Vehicle car = new Car("ABC123");
        car.setEntryTime(LocalDateTime.now());
        
        // Get a spot
        ParkingSpot spot = parkingLot.getSpotById("F1-R1-S1");
        
        // Generate ticket
        Ticket ticket = new Ticket(car, spot);
        
        // Verify ticket format: T-PLATE-TIMESTAMP
        assert ticket.getTicketId().startsWith("T-ABC123-") : "Ticket ID format incorrect";
        assert ticket.getVehicle().equals(car) : "Ticket vehicle incorrect";
        assert ticket.getSpot().equals(spot) : "Ticket spot incorrect";
        
        System.out.println("✓ Ticket generated: " + ticket.getTicketId());
        System.out.println("✓ Ticket format verified");
        System.out.println("\n" + ticket.getTicketDetails());
        System.out.println("✓ Ticket generation working\n");
    }
    
    private static void testEntryController() {
        System.out.println("TEST 3: Entry Controller");
        System.out.println("─────────────────────────────────────────────────");
        
        // Create parking lot
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(2);
        
        // Create entry controller
        EntryController controller = new EntryController(parkingLot);
        
        // Test 3a: Find available spots
        System.out.println("\n3a. Finding available spots:");
        var motorcycleSpots = controller.findAvailableSpots(VehicleType.MOTORCYCLE);
        var carSpots = controller.findAvailableSpots(VehicleType.CAR);
        var suvSpots = controller.findAvailableSpots(VehicleType.SUV);
        
        System.out.println("  Motorcycle spots: " + motorcycleSpots.size());
        System.out.println("  Car spots: " + carSpots.size());
        System.out.println("  SUV spots: " + suvSpots.size());
        
        // Test 3b: Park vehicles
        System.out.println("\n3b. Parking vehicles:");
        
        // Park a motorcycle
        Ticket ticket1 = controller.processParkingEntry("M001", VehicleType.MOTORCYCLE, false, null);
        assert ticket1 != null : "Motorcycle parking failed";
        System.out.println("  ✓ Motorcycle parked: " + ticket1.getTicketId());
        
        // Park a car
        Ticket ticket2 = controller.processParkingEntry("C002", VehicleType.CAR, false, null);
        assert ticket2 != null : "Car parking failed";
        System.out.println("  ✓ Car parked: " + ticket2.getTicketId());
        
        // Park an SUV
        Ticket ticket3 = controller.processParkingEntry("S003", VehicleType.SUV, false, null);
        assert ticket3 != null : "SUV parking failed";
        System.out.println("  ✓ SUV parked: " + ticket3.getTicketId());
        
        // Test 3c: Validate duplicate parking
        System.out.println("\n3c. Testing duplicate parking prevention:");
        Ticket duplicateTicket = controller.processParkingEntry("M001", VehicleType.MOTORCYCLE, false, null);
        assert duplicateTicket == null : "Duplicate parking should be prevented";
        System.out.println("  ✓ Duplicate parking correctly prevented");
        
        // Test 3d: Statistics
        System.out.println("\n3d. Parking lot statistics:");
        System.out.println("  " + controller.getParkingLotStats());
        
        System.out.println("\n✓ Entry controller working correctly\n");
    }
    
    private static void testDatabaseIntegration() {
        System.out.println("TEST 4: Database Integration");
        System.out.println("─────────────────────────────────────────────────");
        
        VehiclesDAO vehiclesDAO = new VehiclesDAO();
        TicketsDAO ticketsDAO = new TicketsDAO();
        
        // Create test data
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(1);
        ParkingSpot spot = parkingLot.getSpotById("F1-R1-S1");
        
        Vehicle testVehicle = new Car("DB123");
        testVehicle.setEntryTime(LocalDateTime.now());
        
        // Test vehicle save
        boolean vehicleSaved = vehiclesDAO.saveVehicle(testVehicle, spot);
        assert vehicleSaved : "Vehicle save failed";
        System.out.println("✓ Vehicle saved to database");
        
        // Test vehicle retrieval
        Vehicle retrieved = vehiclesDAO.getVehicleByPlate("DB123");
        assert retrieved != null : "Vehicle retrieval failed";
        assert retrieved.getLicensePlate().equals("DB123") : "Retrieved wrong vehicle";
        System.out.println("✓ Vehicle retrieved from database");
        
        // Test ticket save
        Ticket testTicket = new Ticket(testVehicle, spot);
        boolean ticketSaved = ticketsDAO.saveTicket(testTicket);
        assert ticketSaved : "Ticket save failed";
        System.out.println("✓ Ticket saved to database");
        
        // Test ticket retrieval
        String[] ticketData = ticketsDAO.getTicketByLicensePlate("DB123");
        assert ticketData != null : "Ticket retrieval failed";
        assert ticketData[1].equals("DB123") : "Retrieved wrong ticket";
        System.out.println("✓ Ticket retrieved from database");
        
        System.out.println("\n✓ Database integration working\n");
    }
    
    private static void testParkingRules() {
        System.out.println("TEST 5: Parking Rules Validation");
        System.out.println("─────────────────────────────────────────────────");
        
        ParkingLot parkingLot = new ParkingLot("Test Lot");
        parkingLot.initializeDefaultLayout(1);
        
        System.out.println("\n5a. Motorcycle → Compact only:");
        var motorcycleSpots = parkingLot.findAvailableSpots(VehicleType.MOTORCYCLE);
        boolean allCompact = motorcycleSpots.stream()
            .allMatch(s -> s.getType() == SpotType.COMPACT);
        assert allCompact : "Motorcycles should only get compact spots";
        System.out.println("  ✓ Motorcycles correctly limited to Compact spots");
        
        System.out.println("\n5b. Car → Compact or Regular:");
        var carSpots = parkingLot.findAvailableSpots(VehicleType.CAR);
        boolean compactOrRegular = carSpots.stream()
            .allMatch(s -> s.getType() == SpotType.COMPACT || s.getType() == SpotType.REGULAR);
        assert compactOrRegular : "Cars should get compact or regular spots";
        System.out.println("  ✓ Cars correctly get Compact or Regular spots");
        
        System.out.println("\n5c. SUV → Regular only:");
        var suvSpots = parkingLot.findAvailableSpots(VehicleType.SUV);
        boolean allRegular = suvSpots.stream()
            .allMatch(s -> s.getType() == SpotType.REGULAR);
        assert allRegular : "SUVs should only get regular spots";
        System.out.println("  ✓ SUVs correctly limited to Regular spots");
        
        System.out.println("\n5d. Handicapped → Any spot:");
        var handicappedSpots = parkingLot.findAvailableSpots(VehicleType.HANDICAPPED);
        boolean hasAllTypes = handicappedSpots.stream()
            .map(ParkingSpot::getType)
            .distinct()
            .count() >= 3; // Should have at least 3 different types
        assert hasAllTypes : "Handicapped vehicles should access multiple spot types";
        System.out.println("  ✓ Handicapped vehicles correctly get any spots");
        
        System.out.println("\n✓ All parking rules validated correctly\n");
    }
}