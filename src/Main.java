import models.parking.ParkingLot;
import views.AdminPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point
 * Temporary version for testing Member 1's work
 * 
 * @author Member 1 - Team Leader
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create parking lot
            ParkingLot parkingLot = new ParkingLot("University Parking Lot");
            parkingLot.initializeDefaultLayout(5); // 5 floors as required
            
            // Create main frame
            JFrame frame = new JFrame("Parking Lot Management System - Member 1 Demo");
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
            
            System.out.println("\n=== Parking Lot Management System Started ===");
            System.out.println("Member 1 implementation: COMPLETE");
            System.out.println("Ready for Member 2 to start!");
        });
    }
    
    private static JPanel createPlaceholder(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
