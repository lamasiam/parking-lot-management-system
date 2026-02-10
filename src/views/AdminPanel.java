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
        tabbedPane.addTab("Settings", createSettingsPanel());
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

// ==========================================
    // TAB 3: SETTINGS (Mac OS Compatible)
    // ==========================================
    private JPanel createSettingsPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(245, 247, 250)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- CARD 1: STRATEGY SETTINGS ---
        JPanel strategyCard = createCard("💰 Fine Strategy Configuration");
        
        JLabel lblStrat = new JLabel("Select Billing Scheme:");
        lblStrat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] strategies = {"Fixed Fine (RM 50)", "Progressive Scheme", "Hourly Scheme (RM 20/hr)"};
        JComboBox<String> strategyCombo = new JComboBox<>(strategies);
        strategyCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        strategyCombo.addActionListener(e -> {
            String selected = (String) strategyCombo.getSelectedItem();
            if (selected.contains("Fixed")) {
                services.FineCalculator.setStrategy(new models.fine.FixedFineStrategy());
            } else if (selected.contains("Progressive")) {
                services.FineCalculator.setStrategy(new models.fine.ProgressiveFineStrategy());
            } else {
                services.FineCalculator.setStrategy(new models.fine.HourlyFineStrategy());
            }
            JOptionPane.showMessageDialog(this, "Strategy Updated: " + selected);
        });

        JPanel stratContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        stratContent.setOpaque(false);
        stratContent.add(lblStrat);
        stratContent.add(strategyCombo);
        
        strategyCard.add(stratContent, BorderLayout.CENTER);
        container.add(strategyCard, gbc);

        // --- CARD 2: SIMULATION TOOLS ---
        gbc.gridy++;
        JPanel simCard = createCard("🛠️ Simulation & Testing Tools");
        
        JPanel simContent = new JPanel(new GridBagLayout());
        simContent.setOpaque(false);
        GridBagConstraints simGbc = new GridBagConstraints();
        simGbc.insets = new Insets(10, 10, 10, 10);
        simGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Input Fields
        JTextField txtPlate = createStyledField("License Plate (e.g. ABC1234)");
        JTextField txtHours = createStyledField("Rewind Hours (e.g. 25)");
        
        // --- BUTTON FIX FOR MAC ---
        JButton btnSimulate = new JButton("Simulate Overstay Entry");
        btnSimulate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSimulate.setForeground(Color.WHITE);
        btnSimulate.setBackground(new Color(220, 53, 69)); // Red
        
        // THESE 2 LINES FIX THE INVISIBLE BUTTON ON MAC
        btnSimulate.setOpaque(true); 
        btnSimulate.setBorderPainted(false);
        
        btnSimulate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSimulate.addActionListener(e -> {
            String plate = txtPlate.getText().trim().toUpperCase();
            String hoursStr = txtHours.getText().trim();
            if (plate.isEmpty() || hoursStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Plate and Hours.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                performTimeTravel(plate, Integer.parseInt(hoursStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Hours must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Assemble Layout
        simGbc.gridx = 0; simGbc.gridy = 0; simGbc.weightx = 0.4;
        simContent.add(new JLabel("Target Vehicle:"), simGbc);
        
        simGbc.gridx = 1; simGbc.weightx = 0.6;
        simContent.add(txtPlate, simGbc);
        
        simGbc.gridx = 0; simGbc.gridy = 1; simGbc.weightx = 0.4;
        simContent.add(new JLabel("Simulate Time Passed:"), simGbc);
        
        simGbc.gridx = 1; simGbc.weightx = 0.6;
        simContent.add(txtHours, simGbc);
        
        // Button Row
        simGbc.gridx = 0; simGbc.gridy = 2; simGbc.gridwidth = 2; simGbc.weightx = 1.0;
        simGbc.fill = GridBagConstraints.NONE; 
        simGbc.anchor = GridBagConstraints.EAST; // Aligns button to right
        simContent.add(btnSimulate, simGbc);
        
        simCard.add(simContent, BorderLayout.CENTER);
        container.add(simCard, gbc);
        
        // Spacer
        gbc.gridy++;
        gbc.weighty = 1.0;
        container.add(Box.createVerticalGlue(), gbc);

        // Scroll Pane logic
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        
        // Wrap in a JPanel to match return type
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }
    // --- HELPER UI METHODS ---
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        card.add(lblTitle, BorderLayout.NORTH);
        return card;
    }

    private JTextField createStyledField(String tooltip) {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setToolTipText(tooltip);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

/**
     * Helper method to execute the Time Travel SQL update
     * FIX: Now updates BOTH 'vehicles' and 'tickets' tables to ensure consistency
     */
    private void performTimeTravel(String plate, int hours) {
        java.sql.Connection conn = database.DatabaseManager.getInstance().getConnection();
        
        // SQLite Syntax: datetime('now', 'localtime', '-X hours')
        String timeModifier = "-" + hours + " hours";
        
        // Query 1: Update Vehicle Record
        String sqlVehicle = "UPDATE vehicles SET entry_time = datetime('now', 'localtime', ?) WHERE license_plate = ?";
        
        // Query 2: Update Ticket Record (Crucial for Exit Controller!)
        String sqlTicket = "UPDATE tickets SET entry_time = datetime('now', 'localtime', ?) WHERE license_plate = ?";
        
        try {
            // Disable auto-commit to handle transaction
            conn.setAutoCommit(false);
            
            int rowsV = 0;
            int rowsT = 0;
            
            // Run Update 1 (Vehicles)
            try (java.sql.PreparedStatement pstmt1 = conn.prepareStatement(sqlVehicle)) {
                pstmt1.setString(1, timeModifier);
                pstmt1.setString(2, plate);
                rowsV = pstmt1.executeUpdate();
            }
            
            // Run Update 2 (Tickets)
            try (java.sql.PreparedStatement pstmt2 = conn.prepareStatement(sqlTicket)) {
                pstmt2.setString(1, timeModifier);
                pstmt2.setString(2, plate);
                rowsT = pstmt2.executeUpdate();
            }
            
            if (rowsV > 0 || rowsT > 0) {
                conn.commit(); // Save changes
                JOptionPane.showMessageDialog(this, 
                    "✅ Success! Time travel applied.\n" +
                    "Vehicle Table Updated: " + (rowsV > 0 ? "Yes" : "No") + "\n" +
                    "Ticket Table Updated: " + (rowsT > 0 ? "Yes" : "No") + "\n\n" +
                    "Go to 'Vehicle Exit' now!", 
                    "Simulation Active", JOptionPane.INFORMATION_MESSAGE);
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this, 
                    "❌ Vehicle " + plate + " not found currently parked.", 
                    "Simulation Failed", JOptionPane.ERROR_MESSAGE);
            }
            
            conn.setAutoCommit(true); // Reset to default
            
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
