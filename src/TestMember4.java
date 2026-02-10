import database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TestMember4 {
    public static void main(String[] args) {
        System.out.println("=== MEMBER 4: FINE SYSTEM TESTER (SQLite Compatible) ===");
        System.out.println("This tool modifies a vehicle's entry time to simulate overstaying.");
        
        // Use try-with-resources to automatically close the scanner
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter License Plate to Time-Travel (e.g., ABC1234): ");
            String plate = scanner.nextLine().toUpperCase();
            
            System.out.print("How many hours back should we send it? (e.g., 25): ");
            int hours = scanner.nextInt();
            
            simulateTimeTravel(plate, hours);
        } // Scanner is automatically closed here
    }

    private static void simulateTimeTravel(String licensePlate, int hours) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        
        // SQLite Syntax: datetime('now', '-25 hours')
        String timeModifier = "-" + hours + " hours";
        
        // Update BOTH 'vehicles' and 'tickets' tables
        // If we don't update 'tickets', the ExitController won't see the new time!
        String sqlVehicle = "UPDATE vehicles SET entry_time = datetime('now', 'localtime', ?) WHERE license_plate = ?";
        String sqlTicket  = "UPDATE tickets SET entry_time = datetime('now', 'localtime', ?) WHERE license_plate = ?";
        
        try {
            conn.setAutoCommit(false); // Start Transaction

            int rowsV = 0;
            int rowsT = 0;

            // Update Vehicle Table
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlVehicle)) {
                pstmt1.setString(1, timeModifier);
                pstmt1.setString(2, licensePlate);
                rowsV = pstmt1.executeUpdate();
            }

            // Update Ticket Table
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlTicket)) {
                pstmt2.setString(1, timeModifier);
                pstmt2.setString(2, licensePlate);
                rowsT = pstmt2.executeUpdate();
            }
            
            if (rowsV > 0 || rowsT > 0) {
                conn.commit(); // Save changes
                System.out.println("✅ SUCCESS! Vehicle " + licensePlate + " is now parked " + hours + " hours ago.");
                System.out.println("   - Vehicles Table Updated: " + (rowsV > 0 ? "Yes" : "No"));
                System.out.println("   - Tickets Table Updated:  " + (rowsT > 0 ? "Yes" : "No"));
                System.out.println("Go to 'Vehicle Exit' panel now to see the fine!");
            } else {
                conn.rollback();
                System.out.println("❌ ERROR: Vehicle not found or not currently parked.");
            }
            
            conn.setAutoCommit(true); // Reset

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error. Ensure your database is connected.");
        }
    }
}