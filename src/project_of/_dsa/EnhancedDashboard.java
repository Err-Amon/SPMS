package project_of._dsa;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class EnhancedDashboard extends JFrame {

    private static final Color PRIMARY = Theme.PRIMARY;
    private static final Color BG = Theme.BG;
    private static final Color CARD = Theme.CARD;
    private static final Color TEXT = Theme.TEXT;
    private static final Color MUTED = Theme.MUTED;
    private static final Color SIDEBAR = Theme.SIDEBAR;
    private static final Color SIDEBAR_HOVER = Theme.SIDEBAR_HOVER;
    private static final Color SIDEBAR_ACTIVE = Theme.SIDEBAR_ACTIVE;
    private static final Color BORDER = Theme.BORDER;

    private static final Font FONT = Theme.FONT_BODY;
    private static final Font FONT_BOLD = Theme.FONT_LABEL;
    private static final Font FONT_TITLE = Theme.FONT_TITLE;
    private static final Font FONT_STAT_VALUE = Theme.FONT_STAT_VALUE;
    private static final Font FONT_STAT_TITLE = Theme.FONT_STAT_TITLE;

    private JLabel lblUser;
    private Timer refreshTimer;
    private JPanel contentArea;
    private CardLayout contentCards;
    private JFrame currentFrame;
    private String currentPage = "dashboard";

    public EnhancedDashboard() {
        setTitle("Smart Parking Management System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);

        contentArea = new JPanel();
        contentCards = new CardLayout();
        contentArea.setLayout(contentCards);
        contentArea.setBackground(BG);
        contentArea.add(buildDashboardCard(), "dashboard");
        add(contentArea, BorderLayout.CENTER);

        startAutoRefresh();
        setVisible(true);
        DataStructureManager.rebuildDSAStructures();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));

        JLabel title = new JLabel("Smart Parking Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        lblUser = new JLabel("User: " + (SessionManager.getCurrentUser() != null ? SessionManager.getCurrentUser() : "Guest"));
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(new Color(200, 210, 230));
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 14, 20, 14));

        JLabel brand = new JLabel("Parking System");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setBorder(BorderFactory.createEmptyBorder(0, 12, 24, 12));
        sidebar.add(brand);

        String[][] items = {
            {"Dashboard", "dashboard", "\u25C9"},
            {"Vehicle Entry", "entry", "\u25B8"},
            {"Vehicle Exit", "exit", "\u25C2"},
            {"Search Vehicle", "search", "\u2315"},
            {"Parking Slots", "slots", "\u25A6"},
            {"Reports", "reports", "\u25A4"},
            {"History", "history", "\u25F7"},
            {"Settings", "settings", "\u2699"},
            {"Logout", "logout", "\u238B"}
        };

        for (String[] item : items) {
            Theme.RoundedButton btn = new Theme.RoundedButton(item[2] + "  " + item[0], SIDEBAR, Theme.RADIUS_MEDIUM);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btn.setForeground(new Color(190, 200, 220));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            btn.setPreferredSize(new Dimension(200, 44));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            final String action = item[1];
            btn.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    if (!action.equals(currentPage)) {
                        btn.setBackground(SIDEBAR_HOVER);
                    }
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (!action.equals(currentPage)) {
                        btn.setBackground(SIDEBAR);
                    }
                }
            });
            btn.addActionListener(e -> handleMenu(action));
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        }
        return sidebar;
    }

    private void handleMenu(String action) {
        if (currentFrame != null) {
            currentFrame.dispose();
            currentFrame = null;
        }
        currentPage = action;
        if ("dashboard".equals(action)) {
            refreshDashboard();
        } else {
            switch (action) {
                case "entry": currentFrame = new VehicleEntryFrame(this); break;
                case "exit": currentFrame = new VehicleExitFrame(this); break;
                case "search": currentFrame = new SearchVehicleFrame(this); break;
                case "slots": currentFrame = new ParkingSlotsFrame(this); break;
                case "reports": currentFrame = new ReportsFrame(this); break;
                case "history": currentFrame = new HistoryFrame(this); break;
                case "settings": currentFrame = new SettingsFrame(this); break;
                case "logout": doLogout(); break;
            }
        }
    }

    private JPanel buildDashboardCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        SettingsBean s = SettingsDAO.loadSettings();
        int total = s.getTotalCapacity();
        int occupied = DataStructureManager.slotManager.getOccupiedSlots();
        int available = total - occupied;
        int bikeBikes = DataStructureManager.slotManager.getTotalBikeCount();
        int carOccupied = DataStructureManager.slotManager.getOccupiedCarSlots();
        int vehicles = VehicleDAO.getTotalVehiclesCount();
        double revenue = VehicleDAO.getTodayRevenue();

        String[] labels = {"Total Slots", "Available", "Occupied", "Bikes", "Car Zone", "Vehicles", "Revenue"};
        String[] values = {String.valueOf(total), String.valueOf(available), String.valueOf(occupied),
            String.valueOf(bikeBikes), String.valueOf(carOccupied), String.valueOf(vehicles), String.format("PKR %.2f", revenue)};
        Color[] colors = {PRIMARY, Theme.SUCCESS, Theme.DANGER, Theme.ACCENT, new Color(155, 89, 182), PRIMARY, new Color(22, 160, 133)};

        JPanel statsRow = new JPanel(new GridLayout(1, labels.length, 16, 0));
        statsRow.setBackground(BG);
        for (int i = 0; i < labels.length; i++) {
            statsRow.add(buildStatCard(labels[i], values[i], colors[i]));
        }
        card.add(statsRow, BorderLayout.NORTH);
        return card;
    }

    private JPanel buildStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS_LARGE, Theme.RADIUS_LARGE);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(Theme.SHADOW_SIZE, Theme.SHADOW_SIZE, getWidth() - Theme.SHADOW_SIZE, getHeight() - Theme.SHADOW_SIZE, Theme.RADIUS_LARGE, Theme.RADIUS_LARGE);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        card.setPreferredSize(new Dimension(170, 120));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(Theme.FONT_STAT_VALUE);
        val.setForeground(color);

        JLabel ttl = new JLabel(title, SwingConstants.CENTER);
        ttl.setFont(Theme.FONT_STAT_TITLE);
        ttl.setForeground(MUTED);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(CARD);
        text.add(val);
        text.add(Box.createRigidArea(new Dimension(0, 4)));
        text.add(ttl);

        card.add(text, BorderLayout.CENTER);
        return card;
    }

    public void refreshDashboard() {
        contentArea.removeAll();
        contentArea.add(buildDashboardCard(), "dashboard");
        contentCards.show(contentArea, "dashboard");
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(30000, e -> {
            DataStructureManager.rebuildDSAStructures();
            refreshDashboard();
        });
        refreshTimer.start();
    }

    private void doLogout() {
        int r = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            SessionManager.logout();
            if (refreshTimer != null) refreshTimer.stop();
            dispose();
            new EnhancedLoginFrame();
        }
    }
}

