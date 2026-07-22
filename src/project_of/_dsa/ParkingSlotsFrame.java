package project_of._dsa;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ParkingSlotsFrame extends JFrame {

    private final Color SLOT_AVAILABLE = Theme.SLOT_AVAILABLE;
    private final Color SLOT_OCCUPIED = Theme.SLOT_OCCUPIED;
    private final Color SLOT_RESERVED = Theme.SLOT_RESERVED;
    private final Color BG_COLOR = Theme.BG;
    private final Color CARD_BG = Theme.CARD;
    private EnhancedDashboard parentDashboard;
    private JPanel slotsGridPanel;
    private SettingsBean settings;
    private Timer refreshTimer;

    public ParkingSlotsFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        this.settings = SettingsDAO.loadSettings();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Parking Slots - Smart Parking System");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        JLabel title = Theme.createTitle("Parking Slots");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel legendPanel = createLegendPanel();
        mainPanel.add(legendPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        slotsGridPanel = new JPanel(new GridLayout(0, 10, 8, 8));
        slotsGridPanel.setBackground(CARD_BG);
        JScrollPane scrollPane = new JScrollPane(slotsGridPanel);
        scrollPane.setBorder(Theme.createCardBorder());
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 480));
        Theme.styleScrollPane(scrollPane);
        mainPanel.add(scrollPane);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setFont(Theme.FONT_BUTTON);
        btnBack.setBackground(Theme.PRIMARY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(180, 42));
        btnBack.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(Theme.PRIMARY_LIGHT);
            }
            @Override public void mouseExited(MouseEvent e) {
                btnBack.setBackground(Theme.PRIMARY);
            }
        });
        btnBack.addActionListener(e -> {
            if (refreshTimer != null) refreshTimer.stop();
            dispose();
            if (parentDashboard != null) parentDashboard.toFront();
        });
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(BG_COLOR);
        backPanel.add(btnBack);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(backPanel);

        JScrollPane outerScrollPane = new JScrollPane(mainPanel);
        outerScrollPane.setBorder(null);
        outerScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(outerScrollPane);
        add(outerScrollPane, BorderLayout.CENTER);
        renderSlots();
        startAutoRefresh();
        setVisible(true);
    }

    private JPanel createLegendPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        panel.add(createLegendItem("Available", SLOT_AVAILABLE));
        panel.add(createLegendItem("Occupied", SLOT_OCCUPIED));
        panel.add(createLegendItem("Reserved", SLOT_RESERVED));
        return panel;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setBackground(CARD_BG);
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(22, 22));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true));
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BODY);
        item.add(colorBox);
        item.add(label);
        return item;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 0));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        int total = settings.getTotalCapacity();
        int occupied = DataStructureManager.slotManager.getOccupiedSlots();
        int available = total - occupied;
        double occupancy = DataStructureManager.slotManager.getOccupancyPercentage();
        int bikeOccupied = DataStructureManager.slotManager.getOccupiedBikeSlots();
        int generalOccupied = DataStructureManager.slotManager.getGeneralOccupiedSlots();
        int vipOccupied = DataStructureManager.slotManager.getVipOccupiedSlots();
        int emergencyOccupied = DataStructureManager.slotManager.getEmergencyOccupiedSlots();

        panel.add(createStatBadge("Total", String.valueOf(total)));
        panel.add(createStatBadge("Available", String.valueOf(available)));
        panel.add(createStatBadge("Occupied", String.valueOf(occupied)));
        panel.add(createStatBadge("Bikes", bikeOccupied + "/" + DataStructureManager.slotManager.getBikeZoneEnd()));
        panel.add(createStatBadge("General", generalOccupied + "/" + DataStructureManager.slotManager.getGeneralZoneSize()));
        panel.add(createStatBadge("VIP", vipOccupied + "/" + DataStructureManager.slotManager.getVipZoneSize()));
        panel.add(createStatBadge("Emergency", emergencyOccupied + "/" + DataStructureManager.slotManager.getEmergencyZoneSize()));

        return panel;
    }

    private JPanel createStatBadge(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Theme.PRIMARY);
        JLabel labelText = new JLabel(label, SwingConstants.CENTER);
        labelText.setFont(Theme.FONT_STAT_TITLE);
        labelText.setForeground(Theme.MUTED);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(labelText, BorderLayout.SOUTH);
        return panel;
    }

    private void renderSlots() {
        slotsGridPanel.removeAll();
        int total = settings.getTotalCapacity();
        for (int i = 1; i <= total; i++) {
            final int slotNumber = i;
            String slotPrefix = DataStructureManager.slotManager.getSlotPrefix(slotNumber);
            SlotButton slotBtn = new SlotButton(slotNumber);
            slotBtn.setText(slotPrefix);
            slotBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            slotBtn.setFocusPainted(false);
            slotBtn.setBorderPainted(false);
            slotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            slotBtn.setPreferredSize(new Dimension(100, 80));
            slotBtn.setMaximumSize(new Dimension(100, 80));
            slotBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
            slotBtn.setHorizontalTextPosition(SwingConstants.CENTER);
            slotBtn.setIconTextGap(4);

            boolean occupied = DataStructureManager.slotManager.isOccupied(slotNumber);
            boolean reserved = DataStructureManager.slotManager.isReserved(slotNumber);

            if (occupied) {
                slotBtn.setBackground(SLOT_OCCUPIED);
                slotBtn.setForeground(Color.WHITE);
                java.util.List<Vehicle> vehicles = DataStructureManager.slotManager.getAllVehiclesInSlot(slotNumber);
                if (!vehicles.isEmpty()) {
                    Vehicle v = vehicles.get(0);
                    slotBtn.setIcon(Theme.getVehicleIcon(v.getVehicleType(), 52, 52));
                    String tooltip = "Slot: " + DataStructureManager.slotManager.getSlotDisplayLabel(slotNumber) + "\n" +
                            "Vehicle: " + v.getVehicleNo() + "\n" +
                            "Owner: " + v.getOwnerName() + "\n" +
                            "Type: " + v.getVehicleType() + "\n" +
                            "Priority: " + v.getPriority();
                    if (DataStructureManager.slotManager.isBikeSlot(slotNumber)) {
                        int bikeCount = DataStructureManager.slotManager.getBikeCount(slotNumber);
                        tooltip += "\nBikes: " + bikeCount + "/" + DataStructureManager.slotManager.getBikesPerSlot();
                    }
                    slotBtn.setToolTipText(tooltip);
                }
            } else if (reserved) {
                slotBtn.setBackground(SLOT_RESERVED);
                slotBtn.setForeground(Color.BLACK);
                slotBtn.setToolTipText("Slot " + slotNumber + " - Reserved");
            } else {
                slotBtn.setBackground(SLOT_AVAILABLE);
                slotBtn.setForeground(Theme.PRIMARY);
                slotBtn.setToolTipText("Slot " + slotNumber + " - Available");
            }

            slotBtn.addActionListener(e -> showSlotDetails(slotNumber));
            slotsGridPanel.add(slotBtn);
        }
        slotsGridPanel.revalidate();
        slotsGridPanel.repaint();
    }

    private void showSlotDetails(int slotNumber) {
        java.util.List<Vehicle> vehicles = DataStructureManager.slotManager.getAllVehiclesInSlot(slotNumber);
        if (vehicles.isEmpty() && DataStructureManager.slotManager.isReserved(slotNumber)) {
            JOptionPane.showMessageDialog(this,
                    "Slot: " + slotNumber + "\nReserved for: " + DataStructureManager.slotManager.getReservedFor(slotNumber),
                    "Slot Details", JOptionPane.INFORMATION_MESSAGE);
        } else if (vehicles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Slot: " + slotNumber + "\nStatus: Available",
                    "Slot Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Slot: ").append(slotNumber).append("\n");
            for (int idx = 0; idx < vehicles.size(); idx++) {
                Vehicle v = vehicles.get(idx);
                sb.append("\nVehicle ").append(idx + 1).append(":\n");
                sb.append("  No: ").append(v.getVehicleNo()).append("\n");
                sb.append("  Owner: ").append(v.getOwnerName()).append("\n");
                sb.append("  Type: ").append(v.getVehicleType()).append("\n");
                sb.append("  Priority: ").append(v.getPriority()).append("\n");
                sb.append("  Entry: ").append(v.getEntryTime() != null ? v.getEntryTime().toString() : "N/A").append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Slot Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, e -> renderSlots());
        refreshTimer.start();
    }

    private static class SlotButton extends JButton {
        public SlotButton(int slot) {
            super(String.valueOf(slot));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS_MEDIUM, Theme.RADIUS_MEDIUM);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
