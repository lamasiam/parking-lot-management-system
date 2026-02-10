package views;

import controllers.ExitController;
import models.parking.ParkingLot;
import models.vehicle.Vehicle;
import models.parking.ParkingSpot;
import models.payment.PaymentMethod;
import models.payment.Receipt;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Exit Panel GUI - Handles vehicle exit, billing, and payment
 * Integrated with Strategy Pattern for Fines
 */
public class ExitPanel extends JPanel {
    // REMOVED: private ParkingLot parkingLot; (Not used locally)
    private ExitController exitController;
    
    // UI Components
    private JTextField licensePlateField;
    private JButton searchButton;
    private JButton calculateBillButton;
    private JButton processExitButton;
    private JButton clearButton;
    
    private JTextArea billDetailsArea;
    private JTextArea receiptArea;
    
    private JRadioButton cashRadio;
    private JRadioButton cardRadio;
    private ButtonGroup paymentMethodGroup;
    
    private JLabel statusLabel;
    
    // State Variables
    private String currentLicensePlate;
    private double[] currentBill; // [Duration, ParkingFee, Fine, Total]
    
    public ExitPanel(ParkingLot parkingLot) {
        this.exitController = new ExitController(parkingLot);
        
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        licensePlateField = new JTextField(15);
        licensePlateField.setFont(new Font("Monospaced", Font.BOLD, 14));
        
        searchButton = new JButton("🔍 Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        searchButton.setToolTipText("Search for a parked vehicle");
        searchButton.addActionListener(e -> searchVehicle());
        
        calculateBillButton = new JButton("💰 Calculate Bill");
        calculateBillButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        calculateBillButton.setBackground(new Color(52, 152, 219));
        calculateBillButton.setForeground(Color.WHITE);
        calculateBillButton.setFocusPainted(false);
        calculateBillButton.setEnabled(false);
        calculateBillButton.addActionListener(e -> calculateBill());
        
        processExitButton = new JButton("✅ Pay & Exit");
        processExitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        processExitButton.setBackground(new Color(46, 204, 113)); // Green
        processExitButton.setForeground(Color.WHITE);
        processExitButton.setFocusPainted(false);
        processExitButton.setEnabled(false);
        processExitButton.addActionListener(e -> processExit());
        
        clearButton = new JButton("🔄 Clear");
        clearButton.addActionListener(e -> clearForm());
        
        billDetailsArea = new JTextArea(12, 40);
        billDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        billDetailsArea.setEditable(false);
        billDetailsArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        receiptArea = new JTextArea(20, 40);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(250, 255, 240)); // Light yellowish for receipt
        receiptArea.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113)));
        
        cashRadio = new JRadioButton("💵 Cash");
        cashRadio.setSelected(true);
        cardRadio = new JRadioButton("💳 Card");
        
        paymentMethodGroup = new ButtonGroup();
        paymentMethodGroup.add(cashRadio);
        paymentMethodGroup.add(cardRadio);
        
        statusLabel = new JLabel("Enter license plate to begin.");
        statusLabel.setForeground(Color.GRAY);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // --- TOP: Search Bar ---
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(createTitledBorder("🚗 Vehicle Exit", new Color(52, 152, 219)));
        
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBox.add(new JLabel("License Plate: "));
        searchBox.add(licensePlateField);
        searchBox.add(searchButton);
        searchBox.add(calculateBillButton);
        searchBox.add(clearButton);
        
        topPanel.add(searchBox, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // --- CENTER: Split Pane (Bill vs Receipt) ---
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        
        // Bill Section
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBorder(createTitledBorder("💰 Bill Details", Color.DARK_GRAY));
        billPanel.add(new JScrollPane(billDetailsArea), BorderLayout.CENTER);
        
        // Payment Section
        JPanel payPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        payPanel.setBorder(createTitledBorder("💳 Payment Method", Color.DARK_GRAY));
        payPanel.add(cashRadio);
        payPanel.add(cardRadio);
        payPanel.add(processExitButton); // Put the big button here
        
        leftPanel.add(billPanel, BorderLayout.CENTER);
        leftPanel.add(payPanel, BorderLayout.SOUTH);
        
        // Receipt Section
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(createTitledBorder("🧾 Receipt", new Color(46, 204, 113)));
        receiptPanel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, receiptPanel);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5);
        
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    // --- ACTIONS ---
    
    private void searchVehicle() {
        String plate = licensePlateField.getText().trim().toUpperCase();
        if (plate.isEmpty()) {
            showMsg("Please enter a license plate.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Vehicle v = exitController.findVehicle(plate);
        if (v == null) {
            statusLabel.setText("❌ Vehicle not found.");
            statusLabel.setForeground(Color.RED);
            showMsg("Vehicle " + plate + " is not currently parked.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ParkingSpot spot = exitController.getVehicleSpot(v);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Display initial info
        billDetailsArea.setText(
            "✓ VEHICLE FOUND\n" +
            "──────────────────────\n" +
            "Plate: " + v.getLicensePlate() + "\n" +
            "Type:  " + v.getType() + "\n" +
            "Spot:  " + (spot != null ? spot.getSpotId() : "Unknown") + "\n" +
            "Entry: " + v.getEntryTime().format(fmt) + "\n\n" +
            "Click 'Calculate Bill' to proceed."
        );
        
        currentLicensePlate = plate;
        calculateBillButton.setEnabled(true);
        statusLabel.setText("✓ Vehicle found: " + plate);
        statusLabel.setForeground(new Color(46, 204, 113));
    }
    
    private void calculateBill() {
        if (currentLicensePlate == null) return;
        
        // Call Controller (Returns: [Duration, ParkingFee, TotalFine, TotalBill])
        currentBill = exitController.calculateBill(currentLicensePlate);
        
        if (currentBill == null) {
            showMsg("Error calculating bill. Please check logs.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        long duration = (long) currentBill[0];
        double parkingFee = currentBill[1];
        double fines = currentBill[2];
        double total = currentBill[3];
        
        Vehicle v = exitController.findVehicle(currentLicensePlate);
        ParkingSpot spot = exitController.getVehicleSpot(v);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime exitTime = LocalDateTime.now();
        
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════\n");
        sb.append("         PARKING BILL         \n");
        sb.append("══════════════════════════════\n");
        sb.append(" Plate      : ").append(currentLicensePlate).append("\n");
        sb.append(" Spot       : ").append(spot.getSpotId()).append("\n");
        sb.append(" Entry Time : ").append(v.getEntryTime().format(fmt)).append("\n");
        sb.append(" Exit Time  : ").append(exitTime.format(fmt)).append("\n");
        sb.append(" Duration   : ").append(duration).append(" hours\n");
        sb.append("──────────────────────────────\n");
        sb.append(String.format(" Parking Fee: RM %7.2f\n", parkingFee));
        
        if (fines > 0) {
            sb.append(String.format(" Fines      : RM %7.2f\n", fines));
            sb.append(" (Includes Overstay/Previous)\n");
        }
        
        sb.append("──────────────────────────────\n");
        sb.append(String.format(" TOTAL DUE  : RM %7.2f\n", total));
        sb.append("══════════════════════════════\n");
        
        billDetailsArea.setText(sb.toString());
        statusLabel.setText(String.format("💰 Total Due: RM %.2f", total));
        
        processExitButton.setEnabled(true);
    }
    
    private void processExit() {
        if (currentLicensePlate == null) return;
        
        PaymentMethod method = cashRadio.isSelected() ? PaymentMethod.CASH : PaymentMethod.CARD;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Confirm payment of RM " + String.format("%.2f", currentBill[3]) + " via " + method + "?",
            "Confirm Payment", JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) return;
        
        Receipt receipt = exitController.processExit(currentLicensePlate, method);
        
        if (receipt != null) {
            receiptArea.setText(receipt.getFormattedReceipt());
            receiptArea.setCaretPosition(0); // Scroll to top
            
            showMsg("Payment Successful!\nVehicle exit processed.", JOptionPane.INFORMATION_MESSAGE);
            
            // Disable buttons to prevent double-payment
            processExitButton.setEnabled(false);
            calculateBillButton.setEnabled(false);
            statusLabel.setText("✓ Exit Complete.");
        } else {
            showMsg("Payment Failed. Please try again.", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        licensePlateField.setText("");
        billDetailsArea.setText("");
        receiptArea.setText("");
        statusLabel.setText("Enter license plate to begin.");
        statusLabel.setForeground(Color.GRAY);
        
        currentLicensePlate = null;
        currentBill = null;
        
        calculateBillButton.setEnabled(false);
        processExitButton.setEnabled(false);
        licensePlateField.requestFocus();
    }
    
    // --- HELPER ---
    private Border createTitledBorder(String title, Color color) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(color, 2),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12), color
        );
    }
    
    private void showMsg(String msg, int type) {
        JOptionPane.showMessageDialog(this, msg, "System Message", type);
    }
}