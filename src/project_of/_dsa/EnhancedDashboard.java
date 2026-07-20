package project_of._dsa;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EnhancedDashboard extends JFrame {

    // Core palette
    private static final Color PRIMARY      = new Color(30, 41, 82);
    private static final Color TEXT         = new Color(220, 230, 255);
    private static final Color MUTED        = new Color(160, 180, 215);
    private static final Color SIDEBAR_BG   = new Color(15, 22, 50);
    private static final Color SIDEBAR_HOVER  = new Color(35, 52, 100);
    private static final Color SIDEBAR_ACTIVE = new Color(50, 80, 180);

    // Glassmorphism card colours
    // White-tinted glass: ~18% white over the dark background
    private static final Color GLASS_FILL      = new Color(255, 255, 255, 46);   // ~18% white
    private static final Color GLASS_FILL2     = new Color(180, 210, 255, 28);   // subtle blue tint at bottom
    private static final Color GLASS_BORDER    = new Color(255, 255, 255, 55);   // thin top/left highlight
    private static final Color GLASS_BORDER2   = new Color(120, 160, 220, 30);   // subtle bottom/right
    private static final Color GLASS_SHADOW    = new Color(0, 0, 0, 60);

    private static final Font FONT_VALUE  = new Font("Segoe UI", Font.BOLD, 34);
    private static final Font FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_BRAND  = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_NAV    = new Font("Segoe UI", Font.PLAIN, 14);

    private JLabel lblUser;
    private Timer refreshTimer;
    private JPanel contentArea;
    private CardLayout contentCards;
    private JFrame currentFrame;
    private String currentPage = "dashboard";
    private Image backgroundImage;
    private JButton selectedButton;

    public EnhancedDashboard() {
        setTitle("Smart Parking Management System");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLayout(new BorderLayout());
        getContentPane().setBackground(SIDEBAR_BG);

        try {
            java.net.URL url = getClass().getResource("/project_of/_dsa/icons/bg.png");
            if (url != null) backgroundImage = new ImageIcon(url).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);

        contentArea = new JPanel();
        contentCards = new CardLayout();
        contentArea.setLayout(contentCards);
        contentArea.setBackground(SIDEBAR_BG);
        contentArea.add(buildDashboardCard(), "dashboard");
        add(contentArea, BorderLayout.CENTER);

        startAutoRefresh();
        setVisible(true);
        DataStructureManager.rebuildDSAStructures();
    }

    // ─────────────────────────── HEADER ────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Very dark semi-transparent bar — matches sidebar depth
                g2.setColor(new Color(15, 22, 50, 230));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Subtle bottom separator line
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // LEFT — system title, vertically centred
        JLabel title = new JLabel("Smart Parking Management System");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT);
        header.add(title, BorderLayout.WEST);

        // CENTRE — empty placeholder so title stays left-anchored cleanly
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        header.add(centre, BorderLayout.CENTER);

        // RIGHT — avatar pill: coloured circle + username + subtle capsule background
        String userName = SessionManager.getCurrentUser() != null ? SessionManager.getCurrentUser() : "Guest";
        String initial  = userName.isEmpty() ? "?" : String.valueOf(Character.toUpperCase(userName.charAt(0)));

        // Avatar circle — letter perfectly centred using glyph bounds
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(60, 100, 220));
                g2.fillOval(0, 0, getWidth(), getHeight());
                // Use getStringBounds for pixel-perfect centering
                g2.setColor(Color.WHITE);
                Font f = new Font("Segoe UI", Font.BOLD, 14);
                g2.setFont(f);
                java.awt.geom.Rectangle2D bounds = g2.getFontMetrics(f).getStringBounds(initial, g2);
                int tx = (int) Math.round((getWidth()  - bounds.getWidth())  / 2.0);
                int ty = (int) Math.round((getHeight() - bounds.getHeight()) / 2.0 - bounds.getY());
                g2.drawString(initial, tx, ty);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        avatar.setPreferredSize(new Dimension(32, 32));
        avatar.setMinimumSize(new Dimension(32, 32));
        avatar.setMaximumSize(new Dimension(32, 32));

        lblUser = new JLabel(userName);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(TEXT);

        // Pill — uses BoxLayout so we can truly centre children vertically
        JPanel pill = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(new Color(255, 255, 255, 28));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
            @Override public boolean isOpaque() { return false; }
        };
        pill.setOpaque(false);
        pill.setLayout(new BoxLayout(pill, BoxLayout.X_AXIS));
        pill.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 16));
        pill.setPreferredSize(new Dimension(148, 40));
        pill.setMaximumSize(new Dimension(148, 40));
        avatar.setAlignmentY(Component.CENTER_ALIGNMENT);
        lblUser.setAlignmentY(Component.CENTER_ALIGNMENT);
        pill.add(avatar);
        pill.add(Box.createRigidArea(new Dimension(10, 0)));
        pill.add(lblUser);

        // Outer right panel — FlowLayout with vertical gap so pill sits centred in the 64px header
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        right.setOpaque(false);
        right.add(pill);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ─────────────────────────── SIDEBAR ───────────────────────────────────

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(SIDEBAR_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Subtle right edge divider
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillRect(getWidth() - 1, 0, 1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(28, 14, 28, 14));

        // Brand
        JLabel brand = new JLabel("Parking System");
        brand.setFont(FONT_BRAND);
        brand.setForeground(TEXT);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setBorder(BorderFactory.createEmptyBorder(0, 14, 32, 14));
        sidebar.add(brand);

        String[][] items = {
            {"Dashboard",     "dashboard"},
            {"Vehicle Entry", "entry"},
            {"Vehicle Exit",  "exit"},
            {"Search Vehicle","search"},
            {"Parking Slots", "slots"},
            {"Reports",       "reports"},
            {"History",       "history"},
            {"Settings",      "settings"},
            {"Logout",        "logout"}
        };

        JButton dashboardBtn = null;
        for (String[] item : items) {
            JButton btn = createNavButton(item[0], item[1]);
            if ("dashboard".equals(item[1])) {
                dashboardBtn = btn;
            }
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        }
        if (dashboardBtn != null) {
            selectedButton = dashboardBtn;
            selectedButton.setForeground(Color.WHITE);
        }
        return sidebar;
    }

    private JButton createNavButton(String label, String action) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = this == selectedButton;
                boolean hovered = getModel().isRollover();
                if (active) {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(60, 100, 220, 200),
                                                         getWidth(), 0, new Color(40, 70, 170, 200));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth(), 1, 12, 12);
                } else if (hovered) {
                    g2.setColor(new Color(255, 255, 255, 14));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_NAV);
        btn.setForeground(new Color(180, 200, 235));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(192, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        btn.addActionListener(e -> {
            if (selectedButton != null && selectedButton != btn) {
                selectedButton.setForeground(new Color(180, 200, 235));
                selectedButton.repaint();
            }
            selectedButton = btn;
            btn.setForeground(Color.WHITE);
            btn.repaint();
            handleMenu(action);
        });
        return btn;
    }

    // ─────────────────────────── NAV ───────────────────────────────────────

    private void handleMenu(String action) {
        if (currentFrame != null) { currentFrame.dispose(); currentFrame = null; }
        currentPage = action;
        if ("dashboard".equals(action)) {
            refreshDashboard();
        } else {
            switch (action) {
                case "entry":   currentFrame = new VehicleEntryFrame(this);  break;
                case "exit":    currentFrame = new VehicleExitFrame(this);   break;
                case "search":  currentFrame = new SearchVehicleFrame(this); break;
                case "slots":   currentFrame = new ParkingSlotsFrame(this);  break;
                case "reports": currentFrame = new ReportsFrame(this);       break;
                case "history": currentFrame = new HistoryFrame(this);       break;
                case "settings":currentFrame = new SettingsFrame(this);      break;
                case "logout":  doLogout();                                   break;
            }
        }
    }

    // ─────────────────────────── DASHBOARD ─────────────────────────────────

    private JPanel buildDashboardCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                int w = getWidth(), h = getHeight();

                // 1. Background image, cover-fill
                if (backgroundImage != null) {
                    int iw = backgroundImage.getWidth(null);
                    int ih = backgroundImage.getHeight(null);
                    if (iw > 0 && ih > 0) {
                        double scale = Math.max((double) w / iw, (double) h / ih);
                        int dw = (int)(iw * scale), dh = (int)(ih * scale);
                        int dx = (w - dw) / 2,      dy = (h - dh) / 2;
                        g2.drawImage(backgroundImage, dx, dy, dw, dh, null);
                    }
                }

                // 2. Dark blue overlay — makes bg read as evening/night parking, not overpowering
                g2.setColor(new Color(10, 18, 48, 155));
                g2.fillRect(0, 0, w, h);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // ── Stats ──────────────────────────────────────────
        SettingsBean s   = SettingsDAO.loadSettings();
        int total        = s.getTotalCapacity();
        int occupied     = DataStructureManager.slotManager.getOccupiedSlots();
        int available    = total - occupied;
        int bikes        = DataStructureManager.slotManager.getTotalBikeCount();
        int carOccupied  = DataStructureManager.slotManager.getOccupiedCarSlots();
        int vehicles     = VehicleDAO.getTotalVehiclesCount();
        double revenue   = VehicleDAO.getTodayRevenue();

        String[] labels = {"Total Slots", "Available", "Occupied", "Bikes", "Car Zone", "Vehicles", "Revenue"};
        String[] values = {
            String.valueOf(total),
            String.valueOf(available),
            String.valueOf(occupied),
            String.valueOf(bikes),
            String.valueOf(carOccupied),
            String.valueOf(vehicles),
            String.format("PKR %.0f", revenue)
        };
        Color[] accents = {
            new Color(200, 215, 255),   // Total — soft white-blue
            new Color(52, 211, 153),    // Available — emerald
            new Color(248, 113, 113),   // Occupied — red
            new Color(96, 165, 250),    // Bikes — blue
            new Color(167, 139, 250),   // Car Zone — violet
            new Color(200, 215, 255),   // Vehicles — soft white-blue
            new Color(52, 211, 153),    // Revenue — emerald
        };

        JPanel statsRow = new JPanel(new GridLayout(1, labels.length, 16, 0));
        statsRow.setOpaque(false);
        for (int i = 0; i < labels.length; i++) {
            statsRow.add(buildGlassCard(labels[i], values[i], accents[i]));
        }
        card.add(statsRow, BorderLayout.NORTH);

        // Spacer
        JPanel center = new JPanel();
        center.setOpaque(false);
        card.add(center, BorderLayout.CENTER);

        return card;
    }

    // ─────────────────────────── GLASS CARD ────────────────────────────────

    private JPanel buildGlassCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int r = 22;

                // ── Drop shadow (painted before the card shape) ──────────
                for (int i = 8; i > 0; i--) {
                    int alpha = (int)(GLASS_SHADOW.getAlpha() * (i / 8.0) * 0.5);
                    g2.setColor(new Color(0, 0, 0, alpha));
                    g2.fillRoundRect(i / 2, i, w - i, h - i / 2, r + 4, r + 4);
                }

                // ── Frosted glass fill — gradient top-to-bottom ──────────
                GradientPaint glassFill = new GradientPaint(
                    0, 0,    new Color(255, 255, 255, 52),   // top: brighter white tint
                    0, h,    new Color(160, 190, 240, 30)    // bottom: cool blue tint
                );
                g2.setPaint(glassFill);
                g2.fillRoundRect(0, 0, w, h, r, r);

                // ── Inner top-highlight (simulates frosted glass light catch) ──
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(1, 1, w - 2, h / 2, r, r);
                // Clip bottom half of that highlight so it fades away
                g2.setColor(new Color(160, 190, 240, 0));
                g2.fillRect(1, h / 4, w - 2, h / 4);

                // ── Border: thin white top/left, subtle blue bottom/right ──
                // Outer frame
                g2.setColor(GLASS_BORDER);
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, r, r);

                // Extra bright top-edge catch
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine(r / 2, 1, w - r / 2, 1);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override public boolean isOpaque() { return false; }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(160, 120));
        card.setLayout(new GridBagLayout());

        // Center content vertically + horizontally
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(FONT_VALUE);
        valLabel.setForeground(accentColor);
        valLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Small spacer
        inner.add(Box.createVerticalGlue());
        inner.add(valLabel);
        inner.add(Box.createRigidArea(new Dimension(0, 7)));

        JLabel ttlLabel = new JLabel(title, SwingConstants.CENTER);
        ttlLabel.setFont(FONT_LABEL);
        ttlLabel.setForeground(new Color(190, 210, 245));  // light muted blue-white
        ttlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(ttlLabel);
        inner.add(Box.createVerticalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        gbc.insets = new Insets(20, 16, 20, 16);
        card.add(inner, gbc);

        return card;
    }

    // ─────────────────────────── REFRESH ───────────────────────────────────

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