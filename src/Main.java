import models.parking.ParkingLot;
import views.AdminPanel;
import database.DatabaseManager;
import database.ParkingSpotsDAO;
import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point with database integration
 * 
 * @author Member 1 - Team Leader
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database first
        System.out.println("=== Initializing Parking Lot Management System ===\n");
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        if (!dbManager.testConnection()) {
            System.err.println("✗ Database connection failed! Exiting...");
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            // Create parking lot
            ParkingLot parkingLot = new ParkingLot("University Parking Lot");
            parkingLot.initializeDefaultLayout(5); // 5 floors as required
            
            // Save to database
            ParkingSpotsDAO dao = new ParkingSpotsDAO();
            System.out.println("\nSaving parking lot structure to database...");
            dao.saveParkingLot(parkingLot);
            
            // Create main frame
            JFrame frame = new JFrame("Parking Lot Management System - v1.0");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            
            // Create tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
            
            // Add Admin Panel (Member 1's work)
            AdminPanel adminPanel = new AdminPanel(parkingLot);
            tabbedPane.addTab("Admin Panel", adminPanel);
            
            // Placeholders for other members
            tabbedPane.addTab("Entry Panel", createPlaceholder("Member 2: Entry Panel"));
            tabbedPane.addTab("Exit Panel", createPlaceholder("Member 3: Exit Panel"));
            tabbedPane.addTab("Reports", createPlaceholder("Member 4: Reports Panel"));
            
            frame.add(tabbedPane);
            frame.setVisible(true);
            
            System.out.println("\n=== System Started Successfully ===");
            System.out.println("✓ Database: Connected");
            System.out.println("✓ Parking Lot: 5 floors, 90 spots");
            System.out.println("✓ GUI: Running");
            System.out.println("✓ Member 1 implementation: COMPLETE");
            System.out.println("\n→ Ready for Member 2 to start!");
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down system...");
            dbManager.closeConnection();
        }));
    }
    
    private static JPanel createPlaceholder(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
