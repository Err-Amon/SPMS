package project_of._dsa;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.*;

public class ReportsFrame extends JFrame {

    private EnhancedDashboard parentDashboard;
    private JTabbedPane tabbedPane;
    private SettingsBean settings;

    public ReportsFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        this.settings = SettingsDAO.loadSettings();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Reports - Smart Parking System");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));

        JLabel title = Theme.createTitle("Reports");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_LABEL);
        tabbedPane.setBackground(Theme.CARD);

        tabbedPane.addTab("Revenue", createRevenueReportPanel());
        tabbedPane.addTab("Occupancy", createOccupancyReportPanel());
        tabbedPane.addTab("Parking History", createHistoryReportPanel());
        tabbedPane.addTab("Export", createExportPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JButton btnBack = new Theme.RoundedButton("Back", Theme.PRIMARY, Theme.RADIUS_MEDIUM);
        btnBack.setFont(Theme.FONT_BUTTON);
        btnBack.setForeground(Color.WHITE);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(120, 42));
        btnBack.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(Theme.PRIMARY_LIGHT);
            }
            @Override public void mouseExited(MouseEvent e) {
                btnBack.setBackground(Theme.PRIMARY);
            }
        });
        btnBack.addActionListener(e -> {
            dispose();
            if (parentDashboard != null) parentDashboard.toFront();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Theme.BG);
        bottomPanel.add(btnBack);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createRevenueReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.CARD);
        panel.setBorder(Theme.createCardBorder());

        double todayRev = VehicleDAO.getTodayRevenue();
        double weekRev = VehicleDAO.getWeeklyRevenue();
        double monthRev = VehicleDAO.getMonthlyRevenue();

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        summaryPanel.setBackground(Theme.CARD);
        summaryPanel.add(createRevenueCard("Today's Revenue", String.format("PKR %.2f", todayRev), Theme.PRIMARY));
        summaryPanel.add(createRevenueCard("Weekly Revenue", String.format("PKR %.2f", weekRev), Theme.ACCENT));
        summaryPanel.add(createRevenueCard("Monthly Revenue", String.format("PKR %.2f", monthRev), Theme.SUCCESS));
        panel.add(summaryPanel, BorderLayout.NORTH);

        String[] cols = {"Period", "Revenue (PKR)", "Avg. Per Vehicle"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        model.addRow(new Object[]{"Today", String.format("%.2f", todayRev), "-"});
        model.addRow(new Object[]{"This Week", String.format("%.2f", weekRev), "-"});
        model.addRow(new Object[]{"This Month", String.format("%.2f", monthRev), "-"});

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        Theme.styleScrollPane(scroll);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRevenueCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS_MEDIUM, Theme.RADIUS_MEDIUM);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        card.setPreferredSize(new Dimension(220, 90));
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.WHITE);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Theme.FONT_BODY);
        titleLabel.setForeground(new Color(255, 255, 255, 190));
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createOccupancyReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.CARD);
        panel.setBorder(Theme.createCardBorder());

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        summaryPanel.setBackground(Theme.CARD);
        int total = settings.getTotalCapacity();
        int occupied = DataStructureManager.slotManager.getOccupiedSlots();
        int available = total - occupied;
        int bikeBikes = DataStructureManager.slotManager.getTotalBikeCount();
        int carOccupied = DataStructureManager.slotManager.getOccupiedCarSlots();
        summaryPanel.add(createOccupancyCard("Total Slots", String.valueOf(total), Theme.PRIMARY));
        summaryPanel.add(createOccupancyCard("Available", String.valueOf(available), Theme.SUCCESS));
        summaryPanel.add(createOccupancyCard("Occupied", String.valueOf(occupied), Theme.DANGER));
        summaryPanel.add(createOccupancyCard("Bikes", String.valueOf(bikeBikes), Theme.ACCENT));
        summaryPanel.add(createOccupancyCard("Car Zone", String.valueOf(carOccupied), new Color(155, 89, 182)));
        summaryPanel.add(createOccupancyCard("Occupancy", String.format("%.1f%%", DataStructureManager.slotManager.getOccupancyPercentage()), Theme.WARNING));
        panel.add(summaryPanel, BorderLayout.NORTH);

        String[] cols = {"Metric", "Value"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        model.addRow(new Object[]{"Total Capacity", total});
        model.addRow(new Object[]{"Occupied Slots", occupied});
        model.addRow(new Object[]{"Available Slots", available});
        model.addRow(new Object[]{"Bike Zone Bikes", bikeBikes});
        model.addRow(new Object[]{"Car Zone Occupied", carOccupied});
        model.addRow(new Object[]{"Bikes Per Slot", settings.getBikeSlotsPerSlot()});
        model.addRow(new Object[]{"Bikes Allowed in Car Slots", settings.isAllowBikeInCarSlots() ? "Yes" : "No"});
        model.addRow(new Object[]{"Occupancy Rate", String.format("%.1f%%", DataStructureManager.slotManager.getOccupancyPercentage())});
        model.addRow(new Object[]{"Total Vehicles All Time", VehicleDAO.getTotalVehiclesCount()});

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        Theme.styleScrollPane(scroll);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOccupancyCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS_MEDIUM, Theme.RADIUS_MEDIUM);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        card.setPreferredSize(new Dimension(150, 80));
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(Color.WHITE);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Theme.FONT_BODY);
        titleLabel.setForeground(new Color(255, 255, 255, 190));
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createHistoryReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.CARD);
        panel.setBorder(Theme.createCardBorder());

        String[] cols = {"Vehicle No", "Owner", "Type", "Slot", "Entry Time", "Exit Time", "Duration", "Fee (PKR)", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        java.util.List<Vehicle> history = VehicleDAO.getRecentActivities(50);
        for (Vehicle v : history) {
            long minutes = 0;
            if (v.getEntryTime() != null && v.getExitTime() != null) {
                minutes = java.time.Duration.between(v.getEntryTime(), v.getExitTime()).toMinutes();
            }
            String duration = minutes > 0 ? ParkingFeeManager.formatDuration(minutes) : "N/A";
            model.addRow(new Object[]{
                    v.getVehicleNo(), v.getOwnerName(), v.getVehicleType(),
                    v.getSlotNumber() != -1 ? v.getSlotNumber() : "N/A",
                    v.getEntryTime() != null ? v.getEntryTime().toString().substring(0, 16) : "N/A",
                    v.getExitTime() != null ? v.getExitTime().toString().substring(0, 16) : "N/A",
                    duration,
                    String.format("%.2f", v.getParkingFee()),
                    v.isParked() ? "PARKED" : "EXITED"
            });
        }

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        Theme.styleScrollPane(scroll);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Theme.CARD);
        panel.setBorder(Theme.createCardBorder());

        JButton btnPDF = Theme.createButton("Export to PDF");
        JButton btnCSV = Theme.createButton("Export to CSV");
        btnPDF.setPreferredSize(new Dimension(180, 44));
        btnCSV.setPreferredSize(new Dimension(180, 44));
        panel.add(btnPDF);
        panel.add(btnCSV);

        btnPDF.addActionListener(e -> exportToPDF());
        btnCSV.addActionListener(e -> exportToCSV());
        return panel;
    }

    private void exportToPDF() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Revenue Report PDF");
            chooser.setSelectedFile(new File("parking_revenue_report.pdf"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                ReportExporter.exportRevenuePdf(file);
                JOptionPane.showMessageDialog(this, "Revenue report exported to:\n" + file.getAbsolutePath(), "PDF Export", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "PDF export failed: " + ex.getMessage(), "PDF Export", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Parking History CSV");
            chooser.setSelectedFile(new File("parking_history.csv"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                ReportExporter.exportParkingHistoryCsv(file);
                JOptionPane.showMessageDialog(this, "Parking history exported to:\n" + file.getAbsolutePath(), "CSV Export", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "CSV export failed: " + ex.getMessage(), "CSV Export", JOptionPane.ERROR_MESSAGE);
        }
    }
}
