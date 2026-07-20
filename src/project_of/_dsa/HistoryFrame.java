package project_of._dsa;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HistoryFrame extends JFrame {

    private EnhancedDashboard parentDashboard;
    private JTable historyTable;
    private DefaultTableModel historyModel;
    private JTextField txtFilterVehicleNo;
    private JComboBox<String> cbFilterType;
    private JComboBox<String> cbFilterStatus;
    private JTextField txtFilterOwner;

    public HistoryFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Parking History - Smart Parking System");
        setSize(1280, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel title = Theme.createTitle("Parking History");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        createFilterPanel(mainPanel);
        createHistoryTable(mainPanel);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(Theme.BG);
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
        backPanel.add(btnBack);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        mainPanel.add(backPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(scrollPane);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        loadHistory();
    }

    private void createFilterPanel(JPanel mainPanel) {
        JPanel filterCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        filterCard.setBackground(Theme.CARD);
        filterCard.setBorder(Theme.createCardBorder());
        filterCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));

        txtFilterVehicleNo = new JTextField();
        txtFilterVehicleNo.setPreferredSize(new Dimension(160, 36));
        txtFilterVehicleNo.setFont(Theme.FONT_INPUT);
        txtFilterVehicleNo.setBorder(Theme.createInputBorder());
        txtFilterVehicleNo.setBackground(Theme.INPUT_BG);

        txtFilterOwner = new JTextField();
        txtFilterOwner.setPreferredSize(new Dimension(160, 36));
        txtFilterOwner.setFont(Theme.FONT_INPUT);
        txtFilterOwner.setBorder(Theme.createInputBorder());
        txtFilterOwner.setBackground(Theme.INPUT_BG);

        cbFilterType = new JComboBox<>(new String[]{"All Types", "Car", "Bike", "Van", "Bus"});
        cbFilterType.setFont(Theme.FONT_INPUT);
        cbFilterType.setPreferredSize(new Dimension(130, 36));
        cbFilterType.setBackground(Theme.INPUT_BG);

        cbFilterStatus = new JComboBox<>(new String[]{"All Status", "EXITED", "PARKED"});
        cbFilterStatus.setFont(Theme.FONT_INPUT);
        cbFilterStatus.setPreferredSize(new Dimension(130, 36));
        cbFilterStatus.setBackground(Theme.INPUT_BG);

        JButton btnFilter = Theme.createAccentButton("Apply Filter");
        btnFilter.setPreferredSize(new Dimension(140, 38));
        btnFilter.setMaximumSize(new Dimension(160, 38));
        btnFilter.addActionListener(e -> loadHistory());

        filterCard.add(Theme.createLabel("Vehicle:"));
        filterCard.add(txtFilterVehicleNo);
        filterCard.add(Theme.createLabel("Owner:"));
        filterCard.add(txtFilterOwner);
        filterCard.add(cbFilterType);
        filterCard.add(cbFilterStatus);
        filterCard.add(btnFilter);

        mainPanel.add(filterCard);
    }

    private void createHistoryTable(JPanel mainPanel) {
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Theme.CARD);
        tableCard.setBorder(Theme.createCardBorder());
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 480));

        String[] cols = {"Vehicle No", "Owner", "Type", "Slot", "Entry Time", "Exit Time", "Duration", "Fee (PKR)", "Status"};
        historyModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        historyTable = new JTable(historyModel);
        Theme.styleTable(historyTable);
        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(null);
        tableCard.add(scroll, BorderLayout.CENTER);
        mainPanel.add(tableCard);
    }

    private void loadHistory() {
        historyModel.setRowCount(0);
        String vehicleNo = txtFilterVehicleNo.getText().trim();
        String owner = txtFilterOwner.getText().trim();
        String type = cbFilterType.getSelectedItem().toString().equals("All Types") ? "" : cbFilterType.getSelectedItem().toString();
        String status = cbFilterStatus.getSelectedItem().toString().equals("All Status") ? "" : cbFilterStatus.getSelectedItem().toString();

        java.util.List<Vehicle> allRecords = new ArrayList<>();
        allRecords.addAll(VehicleDAO.getParkingHistoryWithParked());
        allRecords.addAll(VehicleDAO.getParkingHistory(vehicleNo, owner, type, status));

        for (Vehicle v : allRecords) {
            boolean matchesVehicleNo = vehicleNo.isEmpty() || v.getVehicleNo().toLowerCase().contains(vehicleNo.toLowerCase());
            boolean matchesOwner = owner.isEmpty() || (v.getOwnerName() != null && v.getOwnerName().toLowerCase().contains(owner.toLowerCase()));
            boolean matchesType = type.isEmpty() || v.getVehicleType().equalsIgnoreCase(type);
            boolean matchesStatus = status.isEmpty() || (v.isParked() ? "PARKED" : "EXITED").equalsIgnoreCase(status);

            if (matchesVehicleNo && matchesOwner && matchesType && matchesStatus) {
                long minutes = 0;
                if (v.getEntryTime() != null && v.getExitTime() != null) {
                    minutes = java.time.Duration.between(v.getEntryTime(), v.getExitTime()).toMinutes();
                }
                String duration = minutes > 0 ? ParkingFeeManager.formatDuration(minutes) : "N/A";
                historyModel.addRow(new Object[]{
                        v.getVehicleNo(), v.getOwnerName(), v.getVehicleType(),
                        v.getSlotNumber() != -1 ? v.getSlotNumber() : "N/A",
                        v.getEntryTime() != null ? v.getEntryTime().toString().substring(0, 16) : "N/A",
                        v.getExitTime() != null ? v.getExitTime().toString().substring(0, 16) : "N/A",
                        duration,
                        String.format("%.2f", v.getParkingFee()),
                        v.isParked() ? "PARKED" : "EXITED"
                });
            }
        }
    }
}
