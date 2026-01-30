import models.parking.ParkingLot;
import views.AdminPanel;
import views.EntryPanel;
import database.DatabaseManager;
import database.ParkingSpotsDAO;
import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point with Member 2 integration
 * 
 * @author Member 1 - Team Leader
 * @author Member 2 - Vehicle & Entry Management Lead
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
            // Create parking lot
            ParkingLot parkingLot = new ParkingLot("University Parking Lot");
            parkingLot.initializeDefaultLayout(5); // 5 floors as required
            
            // Save to database
            ParkingSpotsDAO dao = new ParkingSpotsDAO();
            System.out.println("\nSaving parking lot structure to database...");
            dao.saveParkingLot(parkingLot);
            
            // Create main frame
            JFrame frame = new JFrame("Parking Lot Management System - v2.0");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            // Create tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Add Member 1's Admin Panel
            AdminPanel adminPanel = new AdminPanel(parkingLot);
            tabbedPane.addTab("📊 Admin Panel", adminPanel);
            
            // Add Member 2's Entry Panel ✓ INTEGRATED
            EntryPanel entryPanel = new EntryPanel(parkingLot);
            tabbedPane.addTab("🚗 Vehicle Entry", entryPanel);
            
            // Placeholders for other members
            tabbedPane.addTab("🚪 Exit Panel", createPlaceholder("Member 3: Exit Panel"));
            tabbedPane.addTab("📈 Reports", createPlaceholder("Member 4: Reports Panel"));
            
            // Set Entry Panel as default tab
            tabbedPane.setSelectedIndex(1);
            
            frame.add(tabbedPane);
            frame.setVisible(true);
            
            System.out.println("\n═══════════════════════════════════════════════════");
            System.out.println("  ✓ System Started Successfully!                   ");
            System.out.println("═══════════════════════════════════════════════════");
            System.out.println("✓ Database: Connected");
            System.out.println("✓ Parking Lot: 5 floors, 90 spots");
            System.out.println("✓ GUI: Running");
            System.out.println("✓ Member 1 implementation: COMPLETE");
            System.out.println("✓ Member 2 implementation: COMPLETE ← NEW!");
            System.out.println("\n→ Entry Panel ready to use!");
            System.out.println("→ Ready for Member 3 to start!");
            System.out.println("═══════════════════════════════════════════════════\n");
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down system...");
            dbManager.closeConnection();
            System.out.println("✓ System shutdown complete");
        }));
    }
    
    private static JPanel createPlaceholder(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        
        JLabel statusLabel = new JLabel("Coming soon...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(Color.GRAY);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
}