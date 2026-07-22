package project_of._dsa;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.border.*;

public class VehicleEntryFrame extends JFrame {

    private JTextField txtVehicleNo;
    private JComboBox<String> cbVehicleType;
    private JComboBox<String> cbPriority;
    private JTextField txtSlot;
    private JTextField txtEntryTime;
    private JTextField txtFeePreview;
    private JTextField txtOwnerName;
    private EnhancedDashboard parentDashboard;
    private SettingsBean settings;

    public VehicleEntryFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        this.settings = SettingsDAO.loadSettings();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Vehicle Entry - Smart Parking System");
        setSize(780, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel title = Theme.createTitle("Vehicle Entry");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 22)));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(Theme.createCardBorder());
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 560));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(Theme.createLabel("Vehicle Number *"), gbc);
        gbc.gridy = 1;
        txtVehicleNo = Theme.createTextField();
        card.add(txtVehicleNo, gbc);

        gbc.gridy = 2;
        card.add(Theme.createLabel("Owner Name *"), gbc);
        gbc.gridy = 3;
        txtOwnerName = Theme.createTextField();
        card.add(txtOwnerName, gbc);

        gbc.gridy = 4;
        card.add(Theme.createLabel("Vehicle Type *"), gbc);
        gbc.gridy = 5;
        cbVehicleType = Theme.createComboBox(new String[]{"Car", "Bike", "Van", "Bus", "SUV", "Truck"});
        card.add(cbVehicleType, gbc);

        gbc.gridy = 6;
        card.add(Theme.createLabel("Priority *"), gbc);
        gbc.gridy = 7;
        cbPriority = Theme.createComboBox(new String[]{"NORMAL", "VIP", "AMBULANCE", "FIRE BRIGADE", "POLICE"});
        card.add(cbPriority, gbc);

        gbc.gridy = 8;
        card.add(Theme.createLabel("Allocated Slot (leave empty for auto)"), gbc);
        gbc.gridy = 9;
        txtSlot = Theme.createTextField();
        txtSlot.setToolTipText("Enter a slot number to request a specific slot, or leave empty for automatic allocation.");
        card.add(txtSlot, gbc);

        gbc.gridy = 10;
        card.add(Theme.createLabel("Entry Time"), gbc);
        gbc.gridy = 11;
        txtEntryTime = Theme.createReadOnlyField();
        txtEntryTime.setText(LocalDateTime.now().toString());
        card.add(txtEntryTime, gbc);

        gbc.gridy = 12;
        card.add(Theme.createLabel("Estimated Fee (per hour)"), gbc);
        gbc.gridy = 13;
        txtFeePreview = Theme.createReadOnlyField();
        settingsChanged();
        card.add(txtFeePreview, gbc);

        mainPanel.add(card);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 22)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonPanel.setBackground(Theme.BG);
        JButton btnSave = Theme.createAccentButton("Save");
        JButton btnClear = Theme.createButton("Clear");
        JButton btnBack = Theme.createButton("Back");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);
        mainPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        btnSave.addActionListener(e -> saveVehicle());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            dispose();
            if (parentDashboard != null) {
                parentDashboard.toFront();
                parentDashboard.setVisible(true);
            }
        });

        cbVehicleType.addActionListener(e -> {
            settingsChanged();
            txtSlot.setText("");
        });

        cbPriority.addActionListener(e -> settingsChanged());

        setVisible(true);
    }

    private void settingsChanged() {
        String type = cbVehicleType != null ? cbVehicleType.getSelectedItem().toString() : "Car";
        String priority = cbPriority != null ? cbPriority.getSelectedItem().toString() : "NORMAL";
        txtFeePreview.setText(String.format("%.2f PKR/hr", settings.getRateForVehicle(type, priority)));
    }

    private void clearFields() {
        txtVehicleNo.setText("");
        txtOwnerName.setText("");
        cbVehicleType.setSelectedIndex(0);
        cbPriority.setSelectedIndex(0);
        txtSlot.setText("");
        txtEntryTime.setText(LocalDateTime.now().toString());
        settingsChanged();
    }

    private void saveVehicle() {
        String vehicleNo = txtVehicleNo.getText().trim();
        String ownerName = txtOwnerName.getText().trim();
        String vehicleType = cbVehicleType.getSelectedItem().toString();
        String priority = cbPriority.getSelectedItem().toString();
        String slotText = txtSlot.getText().trim();

        if (vehicleNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Vehicle Number", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!vehicleNo.matches("^[A-Za-z0-9-]{2,15}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid vehicle registration number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ownerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Owner Name", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ownerName.matches("^[A-Za-z .'-]{2,50}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid owner name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vehicle existing = VehicleDAO.searchVehicle(vehicleNo);
        if (existing != null && existing.isParked()) {
            JOptionPane.showMessageDialog(this,
                    "Vehicle '" + vehicleNo + "' is already parked in slot " + existing.getSlotNumber(),
                    "Duplicate Alert",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int chosenSlot = -1;
        boolean preferredSlotUsed = !slotText.isEmpty();
        Vehicle displacedVehicle = null;

        LocalDateTime entryTime = LocalDateTime.now();
        Vehicle vehicle = new Vehicle(vehicleNo, ownerName, vehicleType);
        vehicle.setEntryTime(entryTime);
        vehicle.setParked(true);
        vehicle.setPriority(priority);
        SettingsBean s = SettingsDAO.loadSettings();
        vehicle.setParkingFee(s.getRateForVehicle(vehicleType, priority));

        if (preferredSlotUsed) {
            if (!slotText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Slot number must be numeric.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            chosenSlot = Integer.parseInt(slotText);
            if (chosenSlot < 1 || chosenSlot > DataStructureManager.CAPACITY) {
                JOptionPane.showMessageDialog(this, "Slot number out of range. Valid range: 1 - " + DataStructureManager.CAPACITY, "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ParkingSlotManager.AllocationResult result = DataStructureManager.slotManager.tryAssignSlot(chosenSlot, vehicle);

            if (!result.success) {
                JOptionPane.showMessageDialog(this, result.message, "Slot Allocation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (result.displaced && result.displacedVehicle != null) {
                displacedVehicle = result.displacedVehicle;
                if (result.canRelocate) {
                    int nearest = DataStructureManager.slotManager.findNearestEmptySlot(displacedVehicle);
                    if (nearest != -1) {
                        DataStructureManager.slotManager.relocateVehicle(displacedVehicle, nearest);
                        VehicleDAO.updateVehicleSlot(displacedVehicle.getVehicleNo(), nearest);
                    } else {
                        displacedVehicle.setParked(false);
                        LocalDateTime now = LocalDateTime.now();
                        displacedVehicle.setExitTime(now);
                        double fee = ParkingFeeManager.calculateFee(displacedVehicle, settings);
                        VehicleDAO.exitVehicle(displacedVehicle.getVehicleNo(), now, fee);
                        VehicleDAO.insertParkingHistory(displacedVehicle);
                        DataStructureManager.waitingQueue.offer(displacedVehicle);
                    }
                } else {
                    displacedVehicle.setParked(false);
                    LocalDateTime now = LocalDateTime.now();
                    displacedVehicle.setExitTime(now);
                    double fee = ParkingFeeManager.calculateFee(displacedVehicle, settings);
                    VehicleDAO.exitVehicle(displacedVehicle.getVehicleNo(), now, fee);
                    VehicleDAO.insertParkingHistory(displacedVehicle);
                    DataStructureManager.waitingQueue.offer(displacedVehicle);
                }
            }
        } else {
            chosenSlot = DataStructureManager.slotManager.allocateSlot(priority, vehicleType);
            if (chosenSlot == -1) {
                JOptionPane.showMessageDialog(this, "Parking Full! No slots available.", "Parking Full", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        String zone = DataStructureManager.slotManager.getZoneForSlot(chosenSlot);
        if (!isVehicleAllowedInZone(vehicleType, priority, zone)) {
            JOptionPane.showMessageDialog(this,
                    "Zone Mismatch! " + vehicleType + " / " + priority + " is not allowed in " + zone + " zone.",
                    "Allocation Error", JOptionPane.ERROR_MESSAGE);
            DataStructureManager.slotManager.releaseSlot(chosenSlot);
            return;
        }

        vehicle.setSlotNumber(chosenSlot);

        String slotPrefix = DataStructureManager.slotManager.getSlotPrefix(chosenSlot);
        txtSlot.setText(slotPrefix);

        DataStructureManager.parkingQueue.Addvehicle(vehicle);
        DataStructureManager.vehicleStack.push(vehicle);
        DataStructureManager.linkedList.addVehicleSilent(vehicle);
        DataStructureManager.doublyLinkedList.addVehicle(vehicle);
        DataStructureManager.hashTable.insertVehicle(vehicle);
        DataStructureManager.avlTree.insert(vehicle);
        DataStructureManager.minHeap.insert(vehicle);
        DataStructureManager.trie.insert(vehicle.getVehicleNo());
        DataStructureManager.dynamicArray.add(vehicle);
        DataStructureManager.circularQueue.enqueue(vehicle);
        DataStructureManager.vehicleStack.push(vehicle);
        DataStructureManager.linkedList.addVehicleSilent(vehicle);
        DataStructureManager.doublyLinkedList.addVehicle(vehicle);
        DataStructureManager.hashTable.insertVehicle(vehicle);
        DataStructureManager.avlTree.insert(vehicle);
        DataStructureManager.minHeap.insert(vehicle);
        DataStructureManager.trie.insert(vehicle.getVehicleNo());
        DataStructureManager.dynamicArray.add(vehicle);
        DataStructureManager.circularQueue.enqueue(vehicle);

        boolean result = VehicleDAO.insertVehicle(vehicle);
        if (result) {
            txtSlot.setText(String.valueOf(chosenSlot));
            txtEntryTime.setText(entryTime.toString());
            txtFeePreview.setText(String.format("%.2f PKR/hr", settings.getRateForVehicle(vehicleType, priority)));
            DataStructureManager.rebuildDSAStructures();
            String msg = "Vehicle Added Successfully!\n\nSlot: " + chosenSlot + "\nEntry Time: " + entryTime + "\nPriority: " + priority;
            if (displacedVehicle != null) {
                msg += "\nNote: A lower-priority vehicle was displaced.";
            }
            JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            if (parentDashboard != null) {
                parentDashboard.refreshDashboard();
            }
        } else {
            DataStructureManager.slotManager.releaseSlot(chosenSlot);
            JOptionPane.showMessageDialog(this, "Database Error! Slot released.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isVehicleAllowedInZone(String vehicleType, String priority, String zone) {
        if ("BIKE".equalsIgnoreCase(zone)) {
            return "Bike".equalsIgnoreCase(vehicleType);
        }
        if ("VIP".equalsIgnoreCase(zone)) {
            return "VIP".equalsIgnoreCase(priority);
        }
        if ("EMERGENCY".equalsIgnoreCase(zone)) {
            return "Ambulance".equalsIgnoreCase(priority)
                    || "Fire Brigade".equalsIgnoreCase(priority)
                    || "Police".equalsIgnoreCase(priority);
        }
        if ("GENERAL".equalsIgnoreCase(zone)) {
            return !"Bike".equalsIgnoreCase(vehicleType)
                    && !"VIP".equalsIgnoreCase(priority)
                    && !"Ambulance".equalsIgnoreCase(priority)
                    && !"Fire Brigade".equalsIgnoreCase(priority)
                    && !"Police".equalsIgnoreCase(priority);
        }
        return true;
    }
}
