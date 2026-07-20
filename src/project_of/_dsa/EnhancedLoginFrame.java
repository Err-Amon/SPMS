package project_of._dsa;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class EnhancedLoginFrame extends JFrame {

    private static final Color PRIMARY = Theme.PRIMARY;
    private static final Color BG = Theme.BG;
    private static final Color CARD = Theme.CARD;
    private static final Color TEXT = Theme.TEXT;
    private static final Color MUTED = Theme.MUTED;
    private static final Color ACCENT = Theme.ACCENT;

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public EnhancedLoginFrame() {
        setTitle("Smart Parking System - Login");
        setSize(520, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));

        JLabel title = new JLabel("Smart Parking System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 6)));
        JLabel subtitle = new JLabel("Please sign in to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(subtitle);
        main.add(Box.createRigidArea(new Dimension(0, 32)));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(28, 32, 28, 32)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(Theme.createLabel("Username"), gbc);
        gbc.gridy = 1;
        txtUser = Theme.createTextField();
        card.add(txtUser, gbc);

        gbc.gridy = 2;
        card.add(Theme.createLabel("Password"), gbc);
        gbc.gridy = 3;
        txtPass = new JPasswordField();
        txtPass.setFont(Theme.FONT_INPUT);
        txtPass.setPreferredSize(new Dimension(0, 40));
        txtPass.setBorder(Theme.createInputBorder());
        txtPass.setBackground(Theme.INPUT_BG);
        card.add(txtPass, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(22, 8, 8, 8);
        btnLogin = new Theme.RoundedButton("Sign In", Theme.ACCENT, Theme.RADIUS_MEDIUM);
        btnLogin.setFont(Theme.FONT_BUTTON);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        card.add(btnLogin, gbc);

        main.add(card);
        add(main, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> performLogin());
        txtPass.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performLogin();
            }
        });

        setVisible(true);
    }

    private void performLogin() {
        String username = txtUser.getText().trim();
        String password = String.valueOf(txtPass.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Authenticating...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override protected Boolean doInBackground() {
                return SessionManager.login(username, password);
            }
            @Override protected void done() {
                try {
                    if (get()) {
                        dispose();
                        new EnhancedDashboard();
                    } else {
                        JOptionPane.showMessageDialog(EnhancedLoginFrame.this, "Invalid Username or Password!", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Sign In");
                }
            }
        };
        worker.execute();
    }
}
