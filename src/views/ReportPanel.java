package views;

import controllers.ReportController;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Minimalist Report Panel
 * Matches the "Vehicle Exit" aesthetic: Light gray background, blue titled borders.
 * @author Member 4 - Report Module Lead
 */
public class ReportPanel extends JPanel {
    private ReportController reportController;
    
    // UI Constants
    private final Color ACCENT_BLUE = new Color(0, 102, 204);
    private final Color BG_COLOR = new Color(238, 238, 238);
    private final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 12);
    
    // UI Components
    private JLabel lblTotalRevenue;
    private JLabel lblOccupancy;
    private JLabel lblTotalCars;
    private JTable vehiclesTable;
    private DefaultTableModel tableModel;
    private JProgressBar occupancyBar;
    private SimpleBarChart revenueChart;

    public ReportPanel() {
        this.reportController = new ReportController();
        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Header ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(BG_COLOR);
        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> refreshData());
        topPanel.add(refreshBtn);
        add(topPanel, BorderLayout.NORTH);

        // --- Tabs ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard Summary", createDashboardTab());
        tabbedPane.addTab("Live Vehicle List", createVehicleListTab());
        tabbedPane.addTab("Outstanding Fines", createFinesTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==========================================
    // TAB 1: DASHBOARD (Fixed NPE Error)
    // ==========================================
    private JPanel createDashboardTab() {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(BG_COLOR);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BG_COLOR);
        statsPanel.setBorder(createBlueBorder("📊 System Overview"));
        statsPanel.setPreferredSize(new Dimension(800, 120));

        // 1. Revenue Card (With Details Button)
        JPanel revCard = new JPanel(new BorderLayout());
        revCard.setBackground(BG_COLOR);
        revCard.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        lblTotalRevenue = new JLabel("RM 0.00", SwingConstants.CENTER);
        lblTotalRevenue.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTotalRevenue.setForeground(new Color(46, 204, 113));
        
        JButton btnDetails = new JButton("View Details 🔍");
        btnDetails.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnDetails.setFocusPainted(false);
        btnDetails.addActionListener(e -> showRevenueDetailsDialog());
        
        JPanel revHeader = new JPanel(new BorderLayout());
        revHeader.setBackground(BG_COLOR);
        JLabel lblRevTitle = new JLabel("Total Revenue", SwingConstants.CENTER);
        lblRevTitle.setForeground(Color.DARK_GRAY);
        revHeader.add(lblRevTitle, BorderLayout.CENTER);
        
        revCard.add(revHeader, BorderLayout.NORTH);
        revCard.add(lblTotalRevenue, BorderLayout.CENTER);
        revCard.add(btnDetails, BorderLayout.SOUTH);

        // 2. Initialize the other labels (THIS WAS MISSING causing NPE)
        lblOccupancy = new JLabel("0 / 0");
        lblTotalCars = new JLabel("0");

        statsPanel.add(revCard);
        statsPanel.add(createSimpleStat("Current Occupancy", lblOccupancy));
        statsPanel.add(createSimpleStat("Active Vehicles", lblTotalCars));

        container.add(statsPanel, BorderLayout.NORTH);

        // 3. Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        chartsPanel.setBackground(BG_COLOR);

        JPanel occPanel = new JPanel(new BorderLayout());
        occPanel.setBackground(BG_COLOR);
        occPanel.setBorder(createBlueBorder("🅿️ Occupancy Rate"));
        
        occupancyBar = new JProgressBar(0, 100);
        occupancyBar.setStringPainted(true);
        occupancyBar.setForeground(ACCENT_BLUE);
        
        JPanel barWrapper = new JPanel(new GridBagLayout());
        barWrapper.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        barWrapper.add(occupancyBar, gbc);
        occPanel.add(barWrapper, BorderLayout.CENTER);

        JPanel revPanel = new JPanel(new BorderLayout());
        revPanel.setBackground(BG_COLOR);
        revPanel.setBorder(createBlueBorder("💰 Revenue Breakdown"));
        
        revenueChart = new SimpleBarChart();
        revPanel.add(revenueChart, BorderLayout.CENTER);

        chartsPanel.add(occPanel);
        chartsPanel.add(revPanel);
        
        container.add(chartsPanel, BorderLayout.CENTER);

        return container;
    }

    // ==========================================
    // TAB 2 & 3 (Vehicle List & Fines)
    // ==========================================
    private JPanel createVehicleListTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(createBlueBorder("🚗 Current Vehicles"));
        
        String[] columns = {"License Plate", "Vehicle Type", "Spot ID", "Entry Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        vehiclesTable = new JTable(tableModel);
        vehiclesTable.setRowHeight(24);
        vehiclesTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(vehiclesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFinesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(createBlueBorder("⚠️ Outstanding Fines"));

        String[] columns = {"License Plate", "Fine Amount", "Violation", "Date"};
        JTable fineTable = new JTable(new DefaultTableModel(columns, 0));
        fineTable.setRowHeight(24);
        
        DefaultTableModel model = (DefaultTableModel) fineTable.getModel();
        model.addRow(new Object[]{"No Data", "-", "-", "-"}); // Placeholder
        
        panel.add(new JScrollPane(fineTable), BorderLayout.CENTER);
        return panel;
    }

    // ==========================================
    // HELPERS
    // ==========================================
    private Border createBlueBorder(String title) {
        Border lineBorder = BorderFactory.createLineBorder(ACCENT_BLUE, 1);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, title);
        titledBorder.setTitleColor(ACCENT_BLUE);
        titledBorder.setTitleFont(TITLE_FONT);
        return titledBorder;
    }

    private JPanel createSimpleStat(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLbl.setForeground(Color.DARK_GRAY);
        
        // This line caused NPE because valueLabel was null
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20)); 
        valueLabel.setForeground(Color.BLACK);
        
        panel.add(titleLbl, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private void showRevenueDetailsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Revenue Transaction Log", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String[] columns = {"Date & Time", "License Plate", "Method", "Fee (RM)", "Fine (RM)", "Total (RM)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        List<String[]> logs = reportController.getTransactionHistory();
        double totalSum = 0;
        for (String[] row : logs) {
            model.addRow(row);
            try { totalSum += Double.parseDouble(row[5]); } catch (Exception ignored) {}
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JLabel lblTotal = new JLabel(" Total Collected: RM " + String.format("%.2f", totalSum) + "   ");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(lblTotal, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void refreshData() {
        Map<String, Object> stats = reportController.getDashboardStats();
        Map<String, Double> revenueBreakdown = reportController.getRevenueBreakdown();
        
        double revenue = (double) stats.get("total_revenue");
        lblTotalRevenue.setText(String.format("RM %.2f", revenue));
        
        int occupied = (int) stats.get("occupied_spots");
        int total = (int) stats.get("total_spots");
        lblOccupancy.setText(occupied + " / " + total);

        JScrollPane scroll = (JScrollPane) ((JPanel) ((JTabbedPane) getComponent(1)).getComponentAt(2)).getComponent(0);
        JTable fineTable = (JTable) scroll.getViewport().getView();
        DefaultTableModel fineModel = (DefaultTableModel) fineTable.getModel();
        
        int activeVehicles = (int) stats.get("current_vehicles");
        lblTotalCars.setText(String.valueOf(activeVehicles));

        fineModel.setRowCount(0);

        List<String[]> fines = reportController.getOutstandingFines();

        if (fines.isEmpty()) {
            fineModel.addRow(new Object[]{"No Overstays", "-", "-", "-"});
        } else {
            for (String[] row : fines) {
                // Row: [Plate, Fine Amount, Duration, Entry Time]
                fineModel.addRow(row);
            }
        }
        
        if (total > 0) {
            int percentage = (int) (((double) occupied / total) * 100);
            occupancyBar.setValue(percentage);
        }

        double cash = revenueBreakdown.getOrDefault("CASH", 0.0);
        double card = revenueBreakdown.getOrDefault("CARD", 0.0);
        revenueChart.setData(cash, card);

        tableModel.setRowCount(0);
        List<String[]> vehicles = reportController.getCurrentVehicleReport();
        for (String[] row : vehicles) {
            tableModel.addRow(row);
        }
    }

    // ==========================================
    // INNER CLASS: CUSTOM CHART
    // ==========================================
    private class SimpleBarChart extends JPanel {
        private double cashVal = 0;
        private double cardVal = 0;

        public SimpleBarChart() {
            setBackground(BG_COLOR);
        }

        public void setData(double cash, double card) {
            this.cashVal = cash;
            this.cardVal = card;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int pad = 20;
            double max = Math.max(cashVal, cardVal);
            if (max == 0) max = 1;

            int barH = 30;
            int availableWidth = w - 120; 
            if (availableWidth < 0) availableWidth = 10;

            int cashW = (int) ((cashVal / max) * availableWidth);
            int cardW = (int) ((cardVal / max) * availableWidth);

            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Cash: RM " + cashVal, 10, pad + 20);
            g2.setColor(new Color(46, 204, 113));
            g2.fillRoundRect(110, pad, Math.max(cashW, 5), barH, 5, 5);

            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Card: RM " + cardVal, 10, pad + barH + 40);
            g2.setColor(new Color(52, 152, 219));
            g2.fillRoundRect(110, pad + barH + 20, Math.max(cardW, 5), barH, 5, 5);
        }
    }
}