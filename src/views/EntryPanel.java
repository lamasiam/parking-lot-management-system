package views;

import models.parking.ParkingLot;
import models.parking.ParkingSpot;
import models.vehicle.Ticket; 
import models.vehicle.*;
import controllers.EntryController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Entry Panel GUI - handles vehicle entry process
 * Member 2 responsibility
 * @author Member 2 - Vehicle & Entry Management Lead
 */
public class EntryPanel extends JPanel {
    // REMOVED: private ParkingLot parkingLot; (Not used locally)
    private EntryController entryController;
    
    // Input Components
    private JTextField licensePlateField;
    private JComboBox<VehicleType> vehicleTypeCombo;
    private JCheckBox handicappedCardCheckbox;
    
    // Action Buttons
    private JButton searchSpotsButton;
    private JButton parkVehicleButton;
    private JButton clearButton;
    
    // Display Components
    private JTable spotsTable;
    private DefaultTableModel spotsTableModel;
    private JTextArea ticketDisplay;
    private JLabel statusLabel;
    
    // Selected spot
    private String selectedSpotId;
    
    public EntryPanel(ParkingLot parkingLot) {
        this.entryController = new EntryController(parkingLot);
        this.selectedSpotId = null;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Vehicle Entry System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content split into left and right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createInputPanel());
        splitPane.setRightComponent(createDisplayPanel());
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
        
        // Status bar at bottom
        statusLabel = new JLabel("Ready to process vehicle entry");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Vehicle Information"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // License Plate
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("License Plate:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        licensePlateField = new JTextField(15);
        licensePlateField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(licensePlateField, gbc);
        
        // Vehicle Type
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Vehicle Type:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        vehicleTypeCombo = new JComboBox<>(VehicleType.values());
        vehicleTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        vehicleTypeCombo.addActionListener(e -> updateHandicappedCardVisibility());
        formPanel.add(vehicleTypeCombo, gbc);
        
        // Handicapped Card Checkbox
        gbc.gridx = 1; gbc.gridy = 2;
        handicappedCardCheckbox = new JCheckBox("Has Handicapped Card");
        handicappedCardCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        handicappedCardCheckbox.setEnabled(false);
        formPanel.add(handicappedCardCheckbox, gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        searchSpotsButton = new JButton("🔍 Search Available Spots");
        searchSpotsButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchSpotsButton.addActionListener(e -> searchAvailableSpots());
        buttonsPanel.add(searchSpotsButton);
        
        parkVehicleButton = new JButton("🚗 Park Vehicle");
        parkVehicleButton.setFont(new Font("Arial", Font.BOLD, 14));
        parkVehicleButton.setEnabled(false);
        parkVehicleButton.addActionListener(e -> parkVehicle());
        buttonsPanel.add(parkVehicleButton);
        
        clearButton = new JButton("🔄 Clear Form");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.addActionListener(e -> clearForm());
        buttonsPanel.add(clearButton);
        
        panel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Instructions
        JTextArea instructions = new JTextArea(
            "Instructions:\n" +
            "1. Enter vehicle license plate\n" +
            "2. Select vehicle type\n" +
            "3. Check handicapped card if applicable\n" +
            "4. Click 'Search Available Spots'\n" +
            "5. Select a spot from the table\n" +
            "6. Click 'Park Vehicle' to complete\n\n" +
            "Parking Rules:\n" +
            "• Motorcycle → Compact spots only\n" +
            "• Car → Compact or Regular spots\n" +
            "• SUV → Regular spots only\n" +
            "• Handicapped → Any spot\n" +
            "• Handicapped card + Handicapped spot = RM 2/hr"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Arial", Font.PLAIN, 11));
        instructions.setBackground(new Color(240, 240, 240));
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JScrollPane(instructions), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Available spots table
        JPanel spotsPanel = new JPanel(new BorderLayout());
        spotsPanel.setBorder(BorderFactory.createTitledBorder("Available Parking Spots"));
        
        String[] columns = {"Select", "Spot ID", "Type", "Rate (RM/hr)", "Floor"};      
        
        // 1. Define Model with Boolean column for Checkboxes
        spotsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Column 0 is Boolean -> Renders as Checkbox
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Prevent manual clicking of the checkbox itself. 
                // We want users to select the ROW, which will check the box.
                return false; 
            }
        };

        spotsTable = new JTable(spotsTableModel);
        spotsTable.setRowHeight(25);
        
        // 2. Set Selection Mode to Single Selection
        spotsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 3. Add Selection Listener (Handles the "Radio Button" logic)
        spotsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = spotsTable.getSelectedRow();
                
                if (selectedRow >= 0) {
                    // Get the Spot ID (Column 1)
                    selectedSpotId = (String) spotsTableModel.getValueAt(selectedRow, 1);
                    parkVehicleButton.setEnabled(true);
                    
                    // Enforce Single Checkbox Selection
                    for (int i = 0; i < spotsTableModel.getRowCount(); i++) {
                        spotsTableModel.setValueAt(i == selectedRow, i, 0);
                    }
                }
            }
        });
        
        JScrollPane spotsScrollPane = new JScrollPane(spotsTable);
        spotsPanel.add(spotsScrollPane, BorderLayout.CENTER);
        
        // Ticket display
        JPanel ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBorder(BorderFactory.createTitledBorder("Generated Ticket"));
        
        ticketDisplay = new JTextArea(12, 30);
        ticketDisplay.setEditable(false);
        ticketDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ticketDisplay.setText("No ticket generated yet.\n\nPlease park a vehicle to generate a ticket.");
        
        JScrollPane ticketScrollPane = new JScrollPane(ticketDisplay);
        ticketPanel.add(ticketScrollPane, BorderLayout.CENTER);
        
        // Split spots and ticket
        JSplitPane displaySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        displaySplit.setTopComponent(spotsPanel);
        displaySplit.setBottomComponent(ticketPanel);
        displaySplit.setDividerLocation(250);
        
        panel.add(displaySplit, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateHandicappedCardVisibility() {
        VehicleType selectedType = (VehicleType) vehicleTypeCombo.getSelectedItem();
        boolean isHandicapped = (selectedType == VehicleType.HANDICAPPED);
        handicappedCardCheckbox.setEnabled(isHandicapped);
        
        if (!isHandicapped) {
            handicappedCardCheckbox.setSelected(false);
        }
    }
    
    private void searchAvailableSpots() {
        String licensePlate = licensePlateField.getText().trim();
        if (licensePlate.isEmpty()) { showError("Please enter a license plate"); return; }
        
        if (!entryController.validateLicensePlate(licensePlate)) {
            showError("Invalid license plate format (3-10 alphanumeric characters)");
            return;
        }
        
        VehicleType vehicleType = (VehicleType) vehicleTypeCombo.getSelectedItem();
        List<ParkingSpot> availableSpots = entryController.findAvailableSpots(vehicleType);
        
        spotsTableModel.setRowCount(0);
        
        if (availableSpots.isEmpty()) {
            showError("No available spots for " + vehicleType.getDisplayName());
            parkVehicleButton.setEnabled(false);
            return;
        }
        
        for (ParkingSpot spot : availableSpots) {
            Object[] row = {
                false, // Checkbox (False by default)
                spot.getSpotId(),
                spot.getType().getDisplayName(),
                String.format("%.2f", spot.getHourlyRate()),
                "Floor " + spot.getFloorNumber()
            };
            spotsTableModel.addRow(row);
        }
        
        statusLabel.setText("Found " + availableSpots.size() + " available spots for " + vehicleType.getDisplayName());
    }
    
    private void parkVehicle() {
        // Validate inputs
        String licensePlate = licensePlateField.getText().trim().toUpperCase();
        if (licensePlate.isEmpty()) { showError("Please enter a license plate"); return; }
        
        if (selectedSpotId == null) { showError("Please select a parking spot"); return; }
        
        VehicleType vehicleType = (VehicleType) vehicleTypeCombo.getSelectedItem();
        boolean hasHandicappedCard = handicappedCardCheckbox.isSelected();
        
        // Process parking via Controller
        try {
            // ✅ RESTORED EXACTLY FROM BEFORE CODE
            Ticket ticket = entryController.processParkingEntry(
                licensePlate, vehicleType, hasHandicappedCard, selectedSpotId
            );
            
            if (ticket != null) {
                // ✅ RESTORED: Uses .getTicketDetails() instead of .toString()
                ticketDisplay.setText(ticket.getTicketDetails());
                
                JOptionPane.showMessageDialog(this,
                    "Vehicle parked successfully!\n\n" +
                    "License Plate: " + licensePlate + "\n" +
                    "Spot: " + selectedSpotId + "\n" +
                    "Ticket ID: " + ticket.getTicketId(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form for next entry
                clearForm();
                
                statusLabel.setText("✓ Vehicle parked successfully: " + licensePlate);
            } else {
                showError("Failed to park vehicle. Please try again.");
            }
            
        } catch (Exception e) {
            showError("Error parking vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        licensePlateField.setText("");
        vehicleTypeCombo.setSelectedIndex(0);
        handicappedCardCheckbox.setSelected(false);
        handicappedCardCheckbox.setEnabled(false);
        spotsTableModel.setRowCount(0);
        selectedSpotId = null;
        parkVehicleButton.setEnabled(false);
        statusLabel.setText("Form cleared - Ready for next vehicle");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("✗ Error: " + message);
    }
}