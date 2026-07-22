package project_of._dsa;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.border.*;

public class VehicleExitFrame extends JFrame {

    private EnhancedDashboard parentDashboard;
    private JTextField txtVehicleNo;
    private JTextField txtOwner;
    private JTextField txtVehicleType;
    private JTextField txtPriority;
    private JTextField txtSlot;
    private JTextField txtEntryTime;
    private JTextField txtDuration;
    private JTextField txtFee;
    private JTextField txtFeePreview;
    private JTextArea receiptArea;
    private Vehicle vehicle;
    private SettingsBean settings;

    public VehicleExitFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        this.settings = SettingsDAO.loadSettings();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Vehicle Exit - Smart Parking System");
        setSize(720, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel title = Theme.createTitle("Vehicle Exit");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 22)));

        createSearchSection(mainPanel);
        createDetailsSection(mainPanel);
        createReceiptSection(mainPanel);
        createActionButtons(mainPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void createSearchSection(JPanel mainPanel) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(Theme.createCardBorder());
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(Theme.createLabel("Vehicle Number *"), gbc);
        gbc.gridy = 1;
        txtVehicleNo = Theme.createTextField();
        card.add(txtVehicleNo, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        card.add(Theme.createLabel(""), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.2;
        JButton btnSearch = Theme.createAccentButton("Search");
        btnSearch.setPreferredSize(new Dimension(120, 42));
        btnSearch.setMaximumSize(new Dimension(140, 42));
        card.add(btnSearch, gbc);

        mainPanel.add(card);

        btnSearch.addActionListener(e -> searchVehicle());
    }

    private void createDetailsSection(JPanel mainPanel) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(Theme.createCardBorder());
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        row(card, gbc, 0, "Owner Name", txtOwner = Theme.createReadOnlyField());
        row(card, gbc, 2, "Vehicle Type", txtVehicleType = Theme.createReadOnlyField());
        row(card, gbc, 4, "Priority", txtPriority = Theme.createReadOnlyField());
        row(card, gbc, 6, "Slot Number", txtSlot = Theme.createReadOnlyField());
        row(card, gbc, 8, "Entry Time", txtEntryTime = Theme.createReadOnlyField());
        row(card, gbc, 10, "Duration", txtDuration = Theme.createReadOnlyField());
        row(card, gbc, 12, "Parking Fee", txtFee = Theme.createReadOnlyField());

        mainPanel.add(card);
    }

    private void row(JPanel card, GridBagConstraints gbc, int y, String label, JTextField field) {
        gbc.gridy = y;
        card.add(Theme.createLabel(label), gbc);
        gbc.gridy = y + 1;
        card.add(field, gbc);
    }

    private void createReceiptSection(JPanel mainPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(Theme.createCardBorder());
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JLabel label = Theme.createSectionTitle("Receipt Preview");
        receiptArea = new JTextArea(4, 20);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(240, 240, 240));
        receiptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(receiptArea);
        scroll.setBorder(null);
        card.add(label, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        mainPanel.add(card);
    }

    private void createActionButtons(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonPanel.setBackground(Theme.BG);
        JButton btnExit = Theme.createAccentButton("Process Exit");
        JButton btnPrint = Theme.createButton("Print Receipt");
        JButton btnRefresh = Theme.createButton("Refresh");
        JButton btnBack = Theme.createButton("Back");
        btnExit.setPreferredSize(new Dimension(160, 42));
        btnPrint.setPreferredSize(new Dimension(160, 42));
        btnRefresh.setPreferredSize(new Dimension(140, 42));
        btnBack.setPreferredSize(new Dimension(120, 42));
        buttonPanel.add(btnExit);
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        mainPanel.add(buttonPanel);

        btnExit.addActionListener(e -> processExit());
        btnPrint.addActionListener(e -> printReceipt());
        btnRefresh.addActionListener(e -> refreshFeePreview());
        btnBack.addActionListener(e -> {
            dispose();
            if (parentDashboard != null) {
                parentDashboard.toFront();
            }
        });

        txtFeePreview = new JTextField();
        txtFeePreview.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtFeePreview.setEditable(false);
        txtFeePreview.setHorizontalAlignment(JTextField.CENTER);
        txtFeePreview.setBackground(new Color(240, 240, 240));
        txtFeePreview.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    private void searchVehicle() {
        String vehicleNo = txtVehicleNo.getText().trim();
        if (vehicleNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Vehicle Number", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        vehicle = VehicleDAO.searchVehicle(vehicleNo);
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, "Vehicle Not Found", "Search Result", JOptionPane.ERROR_MESSAGE);
            clearDetails();
            return;
        }
        if (!vehicle.isParked()) {
            JOptionPane.showMessageDialog(this, "Vehicle has already exited", "Information", JOptionPane.INFORMATION_MESSAGE);
            clearDetails();
            return;
        }
        txtOwner.setText(vehicle.getOwnerName());
        txtVehicleType.setText(vehicle.getVehicleType());
        txtPriority.setText(vehicle.getPriority());
        String slotDisplay = DataStructureManager.slotManager.getSlotDisplayLabel(vehicle.getSlotNumber());
        txtSlot.setText(slotDisplay);
        txtEntryTime.setText(vehicle.getEntryTime().toString());
        refreshFeePreview();
    }

    private void refreshFeePreview() {
        if (vehicle == null || vehicle.getEntryTime() == null) {
            return;
        }
        double previewFee = ParkingFeeManager.calculateFeePreview(vehicle, settings);
        long duration = ParkingFeeManager.calculateDurationMinutes(vehicle);
        txtDuration.setText(ParkingFeeManager.formatDuration(duration));
        txtFee.setText(String.format("%.2f PKR", previewFee));
        txtFeePreview.setText(String.format("%.2f PKR", previewFee));
        updateReceipt();
    }

    private void updateReceipt() {
        if (vehicle == null) {
            receiptArea.setText("Search a vehicle to see receipt preview.");
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(vehicle.getEntryTime(), now).toMinutes();
        double fee = ParkingFeeManager.calculateFeePreview(vehicle, settings);
        String slotDisplay = DataStructureManager.slotManager.getSlotDisplayLabel(vehicle.getSlotNumber());
        receiptArea.setText(
                "=== PARKING RECEIPT PREVIEW ===\n" +
                        "Vehicle:   " + vehicle.getVehicleNo() + "\n" +
                        "Owner:     " + vehicle.getOwnerName() + "\n" +
                        "Slot:      " + slotDisplay + "\n" +
                        "Entry:     " + vehicle.getEntryTime() + "\n" +
                        "Exit:      " + now + "\n" +
                        "Duration:  " + ParkingFeeManager.formatDuration(minutes) + "\n" +
                        "Fee:       PKR " + String.format("%.2f", fee) + "\n" +
                        "==============================="
        );
    }

    private void processExit() {
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, "Search vehicle first", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Process exit for vehicle: " + vehicle.getVehicleNo() + "?\nFee: " + txtFeePreview.getText(),
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double fee = ParkingFeeManager.calculateFee(vehicle, settings);
        vehicle.setExitTime(exitTime);
        vehicle.setParkingFee(fee);
        vehicle.setParked(false);

        boolean updated = VehicleDAO.exitVehicle(vehicle.getVehicleNo(), exitTime, fee);
        if (updated) {
            VehicleDAO.insertParkingHistory(vehicle);
            DataStructureManager.slotManager.removeVehicleFromSlot(vehicle.getSlotNumber(), vehicle);

            JOptionPane.showMessageDialog(this,
                    "Vehicle Exit Successful!\n\nParking Fee: PKR " + String.format("%.2f", fee) +
                            "\nDuration: " + ParkingFeeManager.formatDuration(ParkingFeeManager.calculateDurationMinutes(vehicle)) +
                            "\nSlot: " + DataStructureManager.slotManager.getSlotDisplayLabel(vehicle.getSlotNumber()),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            DataStructureManager.rebuildDSAStructures();
            dispose();
            if (parentDashboard != null) {
                parentDashboard.refreshDashboard();
                parentDashboard.toFront();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database Update Failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printReceipt() {
        if (receiptArea != null && receiptArea.getText().length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Receipt sent to printer.\n\n" + receiptArea.getText(),
                    "Receipt Printed",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void clearDetails() {
        vehicle = null;
        txtOwner.setText("");
        txtVehicleType.setText("");
        txtPriority.setText("");
        txtSlot.setText("");
        txtEntryTime.setText("");
        txtDuration.setText("");
        txtFee.setText("");
        txtFeePreview.setText("");
        receiptArea.setText("");
    }
}
