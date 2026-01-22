package views;

import models.parking.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Admin Panel GUI - displays parking lot information
 * Member 1 responsibility
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class AdminPanel extends JPanel {
    private ParkingLot parkingLot;
    
    // GUI Components
    private JLabel totalSpotsLabel;
    private JLabel occupiedSpotsLabel;
    private JLabel availableSpotsLabel;
    private JLabel occupancyRateLabel;
    private JTable floorsTable;
    private JTable spotsTable;
    private JComboBox<Integer> floorSelector;
    private JButton refreshButton;
    
    public AdminPanel(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        initComponents();
        refreshData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top Panel - Summary Statistics
        add(createSummaryPanel(), BorderLayout.NORTH);
        
        // Center Panel - Tabbed view
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Floors Overview", createFloorsPanel());
        tabbedPane.addTab("Spot Details", createSpotsPanel());
        add(tabbedPane, BorderLayout.CENTER);
        
        // Bottom Panel - Refresh button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> refreshData());
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Parking Lot Summary"));
        
        // Total Spots
        JPanel totalPanel = createStatCard("Total Spots", Color.GRAY);
        totalSpotsLabel = new JLabel("0", SwingConstants.CENTER);
        totalSpotsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalPanel.add(totalSpotsLabel, BorderLayout.CENTER);
        
        // Occupied Spots
        JPanel occupiedPanel = createStatCard("Occupied", Color.RED);
        occupiedSpotsLabel = new JLabel("0", SwingConstants.CENTER);
        occupiedSpotsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        occupiedSpotsLabel.setForeground(Color.RED);
        occupiedPanel.add(occupiedSpotsLabel, BorderLayout.CENTER);
        
        // Available Spots
        JPanel availablePanel = createStatCard("Available", Color.GREEN);
        availableSpotsLabel = new JLabel("0", SwingConstants.CENTER);
        availableSpotsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        availableSpotsLabel.setForeground(Color.GREEN);
        availablePanel.add(availableSpotsLabel, BorderLayout.CENTER);
        
        // Occupancy Rate
        JPanel ratePanel = createStatCard("Occupancy Rate", Color.BLUE);
        occupancyRateLabel = new JLabel("0%", SwingConstants.CENTER);
        occupancyRateLabel.setFont(new Font("Arial", Font.BOLD, 24));
        occupancyRateLabel.setForeground(Color.BLUE);
        ratePanel.add(occupancyRateLabel, BorderLayout.CENTER);
        
        panel.add(totalPanel);
        panel.add(occupiedPanel);
        panel.add(availablePanel);
        panel.add(ratePanel);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        panel.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        return panel;
    }
    
    private JPanel createFloorsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Table for floors
        String[] columns = {"Floor", "Total Spots", "Occupied", "Available", "Occupancy %"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        floorsTable = new JTable(model);
        floorsTable.getTableHeader().setReorderingAllowed(false);
        floorsTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(floorsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSpotsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Floor selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Floor:"));
        
        floorSelector = new JComboBox<>();
        for (int i = 1; i <= parkingLot.getTotalFloors(); i++) {
            floorSelector.addItem(i);
        }
        floorSelector.addActionListener(e -> updateSpotsTable());
        topPanel.add(floorSelector);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table for spots
        String[] columns = {"Spot ID", "Type", "Status", "Rate (RM/hr)", "Vehicle"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        spotsTable = new JTable(model);
        spotsTable.getTableHeader().setReorderingAllowed(false);
        spotsTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(spotsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void refreshData() {
        // Update summary labels
        totalSpotsLabel.setText(String.valueOf(parkingLot.getTotalSpots()));
        occupiedSpotsLabel.setText(String.valueOf(parkingLot.getOccupiedSpots()));
        availableSpotsLabel.setText(String.valueOf(parkingLot.getAvailableSpots()));
        occupancyRateLabel.setText(String.format("%.1f%%", parkingLot.getOverallOccupancyRate()));
        
        // Update floors table
        updateFloorsTable();
        
        // Update spots table
        updateSpotsTable();
    }
    
    private void updateFloorsTable() {
        DefaultTableModel model = (DefaultTableModel) floorsTable.getModel();
        model.setRowCount(0);
        
        for (Floor floor : parkingLot.getFloors()) {
            Object[] row = {
                "Floor " + floor.getFloorNumber(),
                floor.getTotalSpots(),
                floor.getOccupiedCount(),
                floor.getAvailableCount(),
                String.format("%.1f%%", floor.getOccupancyRate())
            };
            model.addRow(row);
        }
    }
    
    private void updateSpotsTable() {
        DefaultTableModel model = (DefaultTableModel) spotsTable.getModel();
        model.setRowCount(0);
        
        if (floorSelector.getSelectedItem() != null) {
            int floorNum = (Integer) floorSelector.getSelectedItem();
            Floor floor = parkingLot.getFloor(floorNum);
            
            if (floor != null) {
                for (ParkingSpot spot : floor.getAllSpots()) {
                    Object[] row = {
                        spot.getSpotId(),
                        spot.getType().getDisplayName(),
                        spot.getStatus().getDisplayName(),
                        String.format("%.2f", spot.getHourlyRate()),
                        spot.getCurrentVehicle() != null ? 
                            spot.getCurrentVehicle().getLicensePlate() : "-"
                    };
                    model.addRow(row);
                }
            }
        }
    }
}
