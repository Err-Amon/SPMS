package project_of._dsa;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class SettingsFrame extends JFrame {

    private EnhancedDashboard parentDashboard;
    private SettingsBean settings;
    private JTextField txtTotalCapacity;
    private JTextField txtFeeCar;
    private JTextField txtFeeBike;
    private JTextField txtFeeVan;
    private JTextField txtFeeBus;
    private JTextField txtFeeSUV;
    private JTextField txtFeeTruck;
    private JTextField txtFeeVIP;
    private JTextField txtBikeSlotsPerSlot;
    private JCheckBox chkAllowBikeInCarSlots;

    public SettingsFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        this.settings = SettingsDAO.loadSettings();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Settings - Smart Parking System");
        setSize(640, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel title = Theme.createTitle("Settings");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 22)));

        mainPanel.add(createGeneralSection());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnPassword = new Theme.RoundedButton("Change Password", Theme.PRIMARY, Theme.RADIUS_MEDIUM);
        btnPassword.setFont(Theme.FONT_BUTTON);
        btnPassword.setForeground(Color.WHITE);
        btnPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnPassword.addActionListener(e -> openChangePasswordDialog());
        mainPanel.add(btnPassword);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnSave = new Theme.RoundedButton("Save Settings", Theme.ACCENT, Theme.RADIUS_MEDIUM);
        btnSave.setFont(Theme.FONT_BUTTON);
        btnSave.setForeground(Color.WHITE);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnSave.addActionListener(e -> saveSettings());
        mainPanel.add(btnSave);

        JButton btnBack = new Theme.RoundedButton("Back to Dashboard", Theme.PRIMARY, Theme.RADIUS_MEDIUM);
        btnBack.setFont(Theme.FONT_BUTTON);
        btnBack.setForeground(Color.WHITE);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnBack.addActionListener(e -> {
            dispose();
            if (parentDashboard != null) parentDashboard.toFront();
        });
        mainPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        mainPanel.add(btnBack);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createGeneralSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Theme.CARD);
        section.setBorder(Theme.createCardBorder());
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 720));

        JLabel lbl = new JLabel("General Settings");
        lbl.setFont(Theme.FONT_SECTION);
        lbl.setForeground(Theme.PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        section.add(lbl, BorderLayout.NORTH);

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(Theme.CARD);
        inner.setBorder(BorderFactory.createEmptyBorder(6, 20, 12, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        inner.add(Theme.createLabel("Total Capacity"), gbc);
        gbc.gridx = 1;
        txtTotalCapacity = Theme.createTextField();
        txtTotalCapacity.setText(String.valueOf(settings.getTotalCapacity()));
        inner.add(txtTotalCapacity, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inner.add(Theme.createLabel("Car Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeCar = Theme.createTextField();
        txtFeeCar.setText(String.valueOf(settings.getParkingFeeCar()));
        inner.add(txtFeeCar, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inner.add(Theme.createLabel("Bike Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeBike = Theme.createTextField();
        txtFeeBike.setText(String.valueOf(settings.getParkingFeeBike()));
        inner.add(txtFeeBike, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inner.add(Theme.createLabel("Van Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeVan = Theme.createTextField();
        txtFeeVan.setText(String.valueOf(settings.getParkingFeeVan()));
        inner.add(txtFeeVan, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        inner.add(Theme.createLabel("Bus Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeBus = Theme.createTextField();
        txtFeeBus.setText(String.valueOf(settings.getParkingFeeBus()));
        inner.add(txtFeeBus, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        inner.add(Theme.createLabel("SUV Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeSUV = Theme.createTextField();
        txtFeeSUV.setText(String.valueOf(settings.getParkingFeeSUV()));
        inner.add(txtFeeSUV, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        inner.add(Theme.createLabel("Truck Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeTruck = Theme.createTextField();
        txtFeeTruck.setText(String.valueOf(settings.getParkingFeeTruck()));
        inner.add(txtFeeTruck, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        inner.add(Theme.createLabel("VIP Vehicle Fee (PKR/hr)"), gbc);
        gbc.gridx = 1;
        txtFeeVIP = Theme.createTextField();
        txtFeeVIP.setText(String.valueOf(settings.getParkingFeeVIP()));
        inner.add(txtFeeVIP, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        inner.add(Theme.createLabel("Bikes Per Bike Slot"), gbc);
        gbc.gridx = 1;
        txtBikeSlotsPerSlot = Theme.createTextField();
        txtBikeSlotsPerSlot.setText(String.valueOf(settings.getBikeSlotsPerSlot()));
        inner.add(txtBikeSlotsPerSlot, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        chkAllowBikeInCarSlots = new JCheckBox("Allow Bikes in Car Slots");
        chkAllowBikeInCarSlots.setSelected(settings.isAllowBikeInCarSlots());
        chkAllowBikeInCarSlots.setBackground(Theme.CARD);
        chkAllowBikeInCarSlots.setFont(Theme.FONT_BODY);
        gbc.gridx = 1;
        inner.add(chkAllowBikeInCarSlots, gbc);

        section.add(inner, BorderLayout.CENTER);
        return section;
    }

    private void openChangePasswordDialog() {
        JDialog dialog = new JDialog(this, "Change Password", true);
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Theme.BG);
        dialog.setLayout(new BorderLayout());

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Theme.BG);
        main.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARD);
        form.setBorder(Theme.createCardBorder());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPasswordField txtCurrent = new JPasswordField();
        txtCurrent.setFont(Theme.FONT_INPUT);
        txtCurrent.setPreferredSize(new Dimension(0, 38));
        txtCurrent.setBorder(Theme.createInputBorder());
        txtCurrent.setBackground(Theme.INPUT_BG);

        JPasswordField txtNew = new JPasswordField();
        txtNew.setFont(Theme.FONT_INPUT);
        txtNew.setPreferredSize(new Dimension(0, 38));
        txtNew.setBorder(Theme.createInputBorder());
        txtNew.setBackground(Theme.INPUT_BG);

        JPasswordField txtConfirm = new JPasswordField();
        txtConfirm.setFont(Theme.FONT_INPUT);
        txtConfirm.setPreferredSize(new Dimension(0, 38));
        txtConfirm.setBorder(Theme.createInputBorder());
        txtConfirm.setBackground(Theme.INPUT_BG);

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(Theme.createLabel("Current Password"), gbc);
        gbc.gridx = 1;
        form.add(txtCurrent, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(Theme.createLabel("New Password"), gbc);
        gbc.gridx = 1;
        form.add(txtNew, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(Theme.createLabel("Confirm New Password"), gbc);
        gbc.gridx = 1;
        form.add(txtConfirm, gbc);

        main.add(form);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnSave = new Theme.RoundedButton("Change Password", Theme.ACCENT, Theme.RADIUS_MEDIUM);
        btnSave.setFont(Theme.FONT_BUTTON);
        btnSave.setForeground(Color.WHITE);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        main.add(btnSave);

        dialog.add(main, BorderLayout.CENTER);

        btnSave.addActionListener(e -> {
            String current = String.valueOf(txtCurrent.getPassword());
            String newPass = String.valueOf(txtNew.getPassword());
            String confirm = String.valueOf(txtConfirm.getPassword());

            if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!PasswordUtils.verifyPassword(current, "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918")) {
                JOptionPane.showMessageDialog(dialog, "Current password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog, "New passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String newHash = PasswordUtils.hashPassword(newPass);
            try (java.sql.Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("UPDATE login SET password_hash=? WHERE username=?");
                ps.setString(1, newHash);
                ps.setString(2, SessionManager.getCurrentUser());
                ps.executeUpdate();
                ps.close();
                JOptionPane.showMessageDialog(dialog, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtCurrent.setText("");
                txtNew.setText("");
                txtConfirm.setText("");
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void saveSettings() {
        try {
            int newCapacity = Integer.parseInt(txtTotalCapacity.getText().trim());
            if (newCapacity < 1) {
                JOptionPane.showMessageDialog(this, "Total capacity must be at least 1.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (newCapacity > 1000) {
                JOptionPane.showMessageDialog(this, "Total capacity cannot exceed 1000.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            settings.setTotalCapacity(newCapacity);
            settings.setParkingFeeCar(Double.parseDouble(txtFeeCar.getText().trim()));
            settings.setParkingFeeBike(Double.parseDouble(txtFeeBike.getText().trim()));
            settings.setParkingFeeVan(Double.parseDouble(txtFeeVan.getText().trim()));
            settings.setParkingFeeBus(Double.parseDouble(txtFeeBus.getText().trim()));
            settings.setParkingFeeSUV(Double.parseDouble(txtFeeSUV.getText().trim()));
            settings.setParkingFeeTruck(Double.parseDouble(txtFeeTruck.getText().trim()));
            settings.setParkingFeeVIP(Double.parseDouble(txtFeeVIP.getText().trim()));
            settings.setBikeSlotsPerSlot(Integer.parseInt(txtBikeSlotsPerSlot.getText().trim()));
            settings.setAllowBikeInCarSlots(chkAllowBikeInCarSlots.isSelected());

            boolean saved = SettingsDAO.saveSettings(settings);
            if (saved) {
                DataStructureManager.reinitializeSlots(settings.getTotalCapacity());
                JOptionPane.showMessageDialog(this, "Settings saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save settings", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values", "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
