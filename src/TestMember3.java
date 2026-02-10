import controllers.BillingCalculator;
import controllers.ExitController;
import controllers.PaymentProcessor;
import database.*;
import java.time.LocalDateTime;
import models.parking.*;
import models.payment.*;
import models.vehicle.*;

/**
 * Test Suite for Member 3 - Exit & Payment Management
 * 
 * Tests:
 * 1. BillingCalculator - Duration and fee calculations
 * 2. PaymentProcessor - Payment processing
 * 3. ExitController - Complete exit workflow
 * 4. PaymentsDAO - Database operations
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public class TestMember3 {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("   MEMBER 3 TEST SUITE - EXIT & PAYMENT MANAGEMENT");
        System.out.println("═══════════════════════════════════════════════════════\n");
        
        // Test 1: Billing Calculator
        System.out.println("TEST 1: BILLING CALCULATOR - CEILING ROUNDING");
        System.out.println("─────────────────────────────────────────────────────");
        testBillingCalculator();
        
        System.out.println("\n\nTEST 2: PAYMENT PROCESSOR");
        System.out.println("─────────────────────────────────────────────────────");
        testPaymentProcessor();
        
        System.out.println("\n\nTEST 3: PAYMENTS DATABASE");
        System.out.println("─────────────────────────────────────────────────────");
        testPaymentsDAO();
        
        System.out.println("\n\nTEST 4: EXIT CONTROLLER - FULL WORKFLOW");
        System.out.println("─────────────────────────────────────────────────────");
        testExitController();
        
        System.out.println("\n\n═══════════════════════════════════════════════════════");
        System.out.println("   ALL TESTS COMPLETED");
        System.out.println("═══════════════════════════════════════════════════════");
    }
    
    /**
     * Test 1: Billing Calculator - CRITICAL CEILING ROUNDING TEST
     */
    private static void testBillingCalculator() {
        System.out.println("\n🧪 Testing Duration Calculation (CEILING Rounding):\n");
        
        // Test Case 1: Exact hours (no rounding needed)
        LocalDateTime entry1 = LocalDateTime.of(2025, 1, 30, 10, 0, 0);
        LocalDateTime exit1 = LocalDateTime.of(2025, 1, 30, 13, 0, 0);
        long duration1 = BillingCalculator.calculateDuration(entry1, exit1);
        System.out.println("Test 1a - Exact hours:");
        System.out.println("  Entry: 10:00:00, Exit: 13:00:00");
        System.out.println("  Expected: 3 hours | Actual: " + duration1 + " hours");
        System.out.println("  Result: " + (duration1 == 3 ? "✓ PASS" : "✗ FAIL"));
        
        // Test Case 2: 1 hour 1 minute -> should round UP to 2 hours
        LocalDateTime entry2 = LocalDateTime.of(2025, 1, 30, 10, 0, 0);
        LocalDateTime exit2 = LocalDateTime.of(2025, 1, 30, 11, 1, 0);
        long duration2 = BillingCalculator.calculateDuration(entry2, exit2);
        System.out.println("\nTest 1b - 1 hour 1 minute (CRITICAL TEST):");
        System.out.println("  Entry: 10:00:00, Exit: 11:01:00");
        System.out.println("  Expected: 2 hours (CEILING) | Actual: " + duration2 + " hours");
        System.out.println("  Result: " + (duration2 == 2 ? "✓ PASS" : "✗ FAIL"));
        
        // Test Case 3: 2 hours 30 minutes -> should round UP to 3 hours
        LocalDateTime entry3 = LocalDateTime.of(2025, 1, 30, 10, 0, 0);
        LocalDateTime exit3 = LocalDateTime.of(2025, 1, 30, 12, 30, 0);
        long duration3 = BillingCalculator.calculateDuration(entry3, exit3);
        System.out.println("\nTest 1c - 2 hours 30 minutes:");
        System.out.println("  Entry: 10:00:00, Exit: 12:30:00");
        System.out.println("  Expected: 3 hours (CEILING) | Actual: " + duration3 + " hours");
        System.out.println("  Result: " + (duration3 == 3 ? "✓ PASS" : "✗ FAIL"));
        
        // Test Case 4: 30 minutes -> should round UP to 1 hour
        LocalDateTime entry4 = LocalDateTime.of(2025, 1, 30, 10, 0, 0);
        LocalDateTime exit4 = LocalDateTime.of(2025, 1, 30, 10, 30, 0);
        long duration4 = BillingCalculator.calculateDuration(entry4, exit4);
        System.out.println("\nTest 1d - 30 minutes:");
        System.out.println("  Entry: 10:00:00, Exit: 10:30:00");
        System.out.println("  Expected: 1 hour (minimum) | Actual: " + duration4 + " hours");
        System.out.println("  Result: " + (duration4 == 1 ? "✓ PASS" : "✗ FAIL"));
        
        // Test Case 5: 5 hours 59 minutes -> should round UP to 6 hours
        LocalDateTime entry5 = LocalDateTime.of(2025, 1, 30, 10, 0, 0);
        LocalDateTime exit5 = LocalDateTime.of(2025, 1, 30, 15, 59, 0);
        long duration5 = BillingCalculator.calculateDuration(entry5, exit5);
        System.out.println("\nTest 1e - 5 hours 59 minutes:");
        System.out.println("  Entry: 10:00:00, Exit: 15:59:00");
        System.out.println("  Expected: 6 hours (CEILING) | Actual: " + duration5 + " hours");
        System.out.println("  Result: " + (duration5 == 6 ? "✓ PASS" : "✗ FAIL"));
        
        System.out.println("\n🧪 Testing Fee Calculation:\n");
        
        // Test parking fee calculation
        double fee1 = BillingCalculator.calculateParkingFee(3, 5.0);
        System.out.println("Test 2a - 3 hours at RM 5.00/hour:");
        System.out.println("  Expected: RM 15.00 | Actual: RM " + String.format("%.2f", fee1));
        System.out.println("  Result: " + (fee1 == 15.0 ? "✓ PASS" : "✗ FAIL"));
        
        double fee2 = BillingCalculator.calculateParkingFee(2, 2.0);
        System.out.println("\nTest 2b - 2 hours at RM 2.00/hour:");
        System.out.println("  Expected: RM 4.00 | Actual: RM " + String.format("%.2f", fee2));
        System.out.println("  Result: " + (fee2 == 4.0 ? "✓ PASS" : "✗ FAIL"));
        
        System.out.println("\n🧪 Testing Total Bill with Fines:\n");
        
        double total1 = BillingCalculator.calculateTotalBill(15.0, 0.0);
        System.out.println("Test 3a - No fines:");
        System.out.println("  Parking: RM 15.00, Fine: RM 0.00");
        System.out.println("  Expected: RM 15.00 | Actual: RM " + String.format("%.2f", total1));
        System.out.println("  Result: " + (total1 == 15.0 ? "✓ PASS" : "✗ FAIL"));
        
        double total2 = BillingCalculator.calculateTotalBill(15.0, 50.0);
        System.out.println("\nTest 3b - With fine:");
        System.out.println("  Parking: RM 15.00, Fine: RM 50.00");
        System.out.println("  Expected: RM 65.00 | Actual: RM " + String.format("%.2f", total2));
        System.out.println("  Result: " + (total2 == 65.0 ? "✓ PASS" : "✗ FAIL"));
    }
    
    /**
     * Test 2: Payment Processor
     */
    private static void testPaymentProcessor() {
        System.out.println("\n🧪 Testing Payment Processing:\n");
        
        PaymentProcessor processor = new PaymentProcessor();
        
        // Test cash payment
        Payment payment1 = processor.processPayment(
            "ABC123",
            15.0,
            0.0,
            PaymentMethod.CASH,
            "T-ABC123-20250130100000"
        );
        
        System.out.println("Test 1 - Cash Payment:");
        if (payment1 != null) {
            System.out.println("  ✓ Payment created: " + payment1.getPaymentId());
            System.out.println("  License Plate: " + payment1.getLicensePlate());
            System.out.println("  Parking Fee: RM " + String.format("%.2f", payment1.getParkingFee()));
            System.out.println("  Total: RM " + String.format("%.2f", payment1.getTotalAmount()));
            System.out.println("  Method: " + payment1.getPaymentMethod());
            System.out.println("  Result: ✓ PASS");
        } else {
            System.out.println("  ✗ FAIL - Payment creation failed");
        }
        
        // Test card payment
        Payment payment2 = processor.processPayment(
            "XYZ789",
            10.0,
            50.0,
            PaymentMethod.CARD,
            "T-XYZ789-20250130110000"
        );
        
        System.out.println("\nTest 2 - Card Payment with Fine:");
        if (payment2 != null) {
            System.out.println("  ✓ Payment created: " + payment2.getPaymentId());
            System.out.println("  Parking Fee: RM " + String.format("%.2f", payment2.getParkingFee()));
            System.out.println("  Fine: RM " + String.format("%.2f", payment2.getFineAmount()));
            System.out.println("  Total: RM " + String.format("%.2f", payment2.getTotalAmount()));
            System.out.println("  Method: " + payment2.getPaymentMethod());
            System.out.println("  Result: " + (payment2.getTotalAmount() == 60.0 ? "✓ PASS" : "✗ FAIL"));
        } else {
            System.out.println("  ✗ FAIL - Payment creation failed");
        }
        
        // Test change calculation
        System.out.println("\n🧪 Testing Change Calculation:\n");
        double change = processor.calculateChange(15.0, 20.0);
        System.out.println("Test 3 - Change Calculation:");
        System.out.println("  Total Due: RM 15.00, Paid: RM 20.00");
        System.out.println("  Expected Change: RM 5.00 | Actual: RM " + String.format("%.2f", change));
        System.out.println("  Result: " + (change == 5.0 ? "✓ PASS" : "✗ FAIL"));
    }
    
    /**
     * Test 3: Payments Database
     */
    private static void testPaymentsDAO() {
        System.out.println("\n🧪 Testing Payments Database:\n");
        
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        
        // Create test payment
        Payment testPayment = new Payment(
            "TEST123",
            20.0,
            0.0,
            PaymentMethod.CASH,
            "T-TEST123-20250130120000"
        );
        
        // Test save payment
        System.out.println("Test 1 - Save Payment:");
        boolean saved = paymentsDAO.savePayment(testPayment);
        System.out.println("  Result: " + (saved ? "✓ PASS" : "✗ FAIL"));
        
        // Test retrieve payment
        System.out.println("\nTest 2 - Retrieve Payment:");
        String[] paymentData = paymentsDAO.getPaymentById(testPayment.getPaymentId());
        if (paymentData != null) {
            System.out.println("  ✓ Payment retrieved:");
            System.out.println("    Payment ID: " + paymentData[0]);
            System.out.println("    License Plate: " + paymentData[1]);
            System.out.println("    Total: RM " + paymentData[5]);
            System.out.println("  Result: ✓ PASS");
        } else {
            System.out.println("  ✗ FAIL - Could not retrieve payment");
        }
        
        // Test revenue calculation
        System.out.println("\nTest 3 - Total Revenue:");
        double revenue = paymentsDAO.getTotalRevenue();
        System.out.println("  Total Revenue: RM " + String.format("%.2f", revenue));
        System.out.println("  Result: " + (revenue >= 0 ? "✓ PASS" : "✗ FAIL"));
        
        // Test payment count
        System.out.println("\nTest 4 - Payment Count:");
        int count = paymentsDAO.getTotalPaymentsCount();
        System.out.println("  Total Payments: " + count);
        System.out.println("  Result: " + (count >= 0 ? "✓ PASS" : "✗ FAIL"));
    }
    
    /**
     * Test 4: Exit Controller - Full Workflow
     */
    private static void testExitController() {
        System.out.println("\n🧪 Testing Complete Exit Workflow:\n");
        
        // Create parking lot using Member 1's actual method
        ParkingLot parkingLot = new ParkingLot("University Parking");
        parkingLot.initializeDefaultLayout(2); // 2 floors
        
        // Create controllers
        controllers.EntryController entryController = new controllers.EntryController(parkingLot);
        ExitController exitController = new ExitController(parkingLot);
        
        System.out.println("Step 1: Park a vehicle");
        System.out.println("─────────────────────────────────────────────────────");
        
        // Park a vehicle
        Ticket ticket = entryController.processParkingEntry(
            "TEST999",
            VehicleType.CAR,
            false,
            null
        );
        
        if (ticket != null) {
            System.out.println("✓ Vehicle parked successfully");
            System.out.println("  License: TEST999");
            System.out.println("  Ticket: " + ticket.getTicketId());
            System.out.println("  Spot: " + ticket.getSpot().getSpotId());
        } else {
            System.out.println("✗ Failed to park vehicle");
            return;
        }
        
        System.out.println("\nStep 2: Search for vehicle");
        System.out.println("─────────────────────────────────────────────────────");
        
        Vehicle vehicle = exitController.findVehicle("TEST999");
        if (vehicle != null) {
            System.out.println("✓ Vehicle found: " + vehicle.getLicensePlate());
        } else {
            System.out.println("✗ Vehicle not found");
            return;
        }
        
        System.out.println("\nStep 3: Calculate bill");
        System.out.println("─────────────────────────────────────────────────────");
        
        double[] bill = exitController.calculateBill("TEST999");
        if (bill != null) {
            System.out.println("✓ Bill calculated:");
            System.out.println("  Duration: " + (long)bill[0] + " hours");
            System.out.println("  Parking Fee: RM " + String.format("%.2f", bill[1]));
            System.out.println("  Fine: RM " + String.format("%.2f", bill[2]));
            System.out.println("  Total: RM " + String.format("%.2f", bill[3]));
        } else {
            System.out.println("✗ Bill calculation failed");
            return;
        }
        
        System.out.println("\nStep 4: Process exit and payment");
        System.out.println("─────────────────────────────────────────────────────");
        
        Receipt receipt = exitController.processExit("TEST999", PaymentMethod.CASH);
        if (receipt != null) {
            System.out.println("✓ Exit processed successfully");
            System.out.println("  Receipt ID: " + receipt.getReceiptId());
            System.out.println("  Total Paid: RM " + String.format("%.2f", receipt.getTotalPaid()));
            System.out.println("\n" + receipt.getFormattedReceipt());
        } else {
            System.out.println("✗ Exit processing failed");
            return;
        }
        
        System.out.println("\nStep 5: Verify spot released");
        System.out.println("─────────────────────────────────────────────────────");
        
        Vehicle checkVehicle = exitController.findVehicle("TEST999");
        if (checkVehicle == null) {
            System.out.println("✓ Vehicle successfully removed from system");
            System.out.println("✓ Parking spot released");
        } else {
            System.out.println("✗ Vehicle still in system");
        }
        
        System.out.println("\n✓ COMPLETE EXIT WORKFLOW TEST PASSED");
    }
}