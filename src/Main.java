import database.DatabaseManager;
import database.ParkingSpotsDAO;
import java.awt.*;
import javax.swing.*;
import models.parking.ParkingLot;
import views.AdminPanel;
import views.EntryPanel;
import views.ExitPanel;
import views.ReportPanel;

/**
 * Main application entry point
 * Integrated with all Members (1, 2, 3, 4)
 * Fixed: Now loads existing data from database on startup
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database first
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Parking Lot Management System - Starting...    ");
        System.out.println("═══════════════════════════════════════════════════\n");
        
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        if (!dbManager.testConnection()) {
            System.err.println("✗ Database connection failed! Exiting...");
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            ParkingLot parkingLot;
            ParkingSpotsDAO dao = new ParkingSpotsDAO();
            
            // ---------------------------------------------------------
            // FIX: Try to load existing data from database first
            // ---------------------------------------------------------
            System.out.println("Checking for existing data...");
            ParkingLot existingLot = dao.loadParkingLot("University Parking Lot");
            
            if (existingLot != null) {
                // CASE 1: Data exists! Use it.
                parkingLot = existingLot;
                System.out.println("✓ RESTORED previous state: " + parkingLot.getOccupiedSpots() + " vehicles found.");
            } else {
                // CASE 2: New installation (First run)
                System.out.println("No existing data found. Initializing new lot.");
                parkingLot = new ParkingLot("University Parking Lot");
                parkingLot.initializeDefaultLayout(5); 
                dao.saveParkingLot(parkingLot);
            }
            // ---------------------------------------------------------
            
            // Create main frame
            JFrame frame = new JFrame("Parking Lot Management System - vFinal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            // Create tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Add Member 1's Admin Panel
            AdminPanel adminPanel = new AdminPanel(parkingLot);
            tabbedPane.addTab("📊 Admin Panel", adminPanel);
            
            // Add Member 2's Entry Panel
            EntryPanel entryPanel = new EntryPanel(parkingLot);
            tabbedPane.addTab("🚗 Vehicle Entry", entryPanel);
            
            // Add Member 3's Exit Panel
            ExitPanel exitPanel = new ExitPanel(parkingLot);
            tabbedPane.addTab("🚪 Vehicle Exit", exitPanel);
            
            // Add Member 4's Report Panel
            ReportPanel reportPanel = new ReportPanel();
            tabbedPane.addTab("📈 Reports", reportPanel);
            
            // Set Reports as default tab
            tabbedPane.setSelectedIndex(3);
            
            frame.add(tabbedPane);
            frame.setVisible(true);
            
            System.out.println("\n═══════════════════════════════════════════════════");
            System.out.println("  ✓ System Started Successfully!                   ");
            System.out.println("═══════════════════════════════════════════════════");
            System.out.println("✓ Member 1 (Admin) : INTEGRATED");
            System.out.println("✓ Member 2 (Entry) : INTEGRATED");
            System.out.println("✓ Member 3 (Exit)  : INTEGRATED");
            System.out.println("✓ Member 4 (Report): INTEGRATED");
            System.out.println("═══════════════════════════════════════════════════\n");
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            dbManager.closeConnection();
        }));
    }
}