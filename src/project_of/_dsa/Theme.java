package project_of._dsa;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

public class Theme {

    public static final Color PRIMARY = new Color(25, 42, 86);
    public static final Color PRIMARY_LIGHT = new Color(52, 73, 94);
    public static final Color ACCENT = new Color(52, 152, 219);
    public static final Color SUCCESS = new Color(39, 174, 96);
    public static final Color DANGER = new Color(231, 76, 60);
    public static final Color WARNING = new Color(243, 156, 18);
    public static final Color BG = new Color(236, 240, 241);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(44, 62, 80);
    public static final Color MUTED = new Color(127, 140, 141);
    public static final Color SIDEBAR = new Color(30, 45, 80);
    public static final Color SIDEBAR_HOVER = new Color(45, 65, 110);
    public static final Color SIDEBAR_ACTIVE = new Color(55, 80, 130);
    public static final Color BORDER = new Color(220, 220, 220);
    public static final Color SLOT_AVAILABLE = new Color(236, 240, 241);
    public static final Color SLOT_OCCUPIED = new Color(142, 68, 173);
    public static final Color SLOT_RESERVED = new Color(189, 195, 199);
    public static final Color INPUT_BG = Color.WHITE;
    public static final Color SHADOW = new Color(0, 0, 0, 25);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_STAT_VALUE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_STAT_TITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_CELL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    public static final int RADIUS_SMALL = 10;
    public static final int RADIUS_MEDIUM = 14;
    public static final int RADIUS_LARGE = 18;
    public static final int SHADOW_SIZE = 8;

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER, 1, RADIUS_MEDIUM),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        );
    }

    public static Border createPanelBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(230, 230, 230), 1, RADIUS_MEDIUM),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        );
    }

    public static Border createInputBorder() {
        return BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(189, 195, 199), 1, RADIUS_SMALL),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }

    public static Border createRoundedBorder(Color color, int thickness, int radius) {
        return new RoundedBorder(color, thickness, radius);
    }

    public static JButton createButton(String text) {
        JButton btn = new RoundedButton(text, PRIMARY, RADIUS_MEDIUM);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 44));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        return btn;
    }

    public static JButton createAccentButton(String text) {
        JButton btn = new RoundedButton(text, ACCENT, RADIUS_MEDIUM);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT.darker());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT);
            }
        });
        return btn;
    }

    public static JButton createDangerButton(String text) {
        JButton btn = new RoundedButton(text, DANGER, RADIUS_MEDIUM);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(192, 57, 43));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(DANGER);
            }
        });
        return btn;
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_INPUT);
        field.setPreferredSize(new Dimension(0, 46));
        field.setBorder(createInputBorder());
        field.setBackground(INPUT_BG);
        return field;
    }

    public static JTextField createReadOnlyField() {
        JTextField field = createTextField();
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240));
        return field;
    }

    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(FONT_INPUT);
        combo.setPreferredSize(new Dimension(0, 40));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setBackground(INPUT_BG);
        return combo;
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT);
        return lbl;
    }

    public static JLabel createTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(PRIMARY);
        return lbl;
    }

    public static JLabel createSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SECTION);
        lbl.setForeground(PRIMARY);
        return lbl;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE_CELL);
        table.setRowHeight(32);
        table.getTableHeader().setFont(FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setPreferredSize(new Dimension(12, 0));
        vBar.setUnitIncrement(16);
        JScrollBar hBar = scrollPane.getHorizontalScrollBar();
        hBar.setPreferredSize(new Dimension(0, 12));
        hBar.setUnitIncrement(16);
    }

    public static Color getSlotColor(boolean occupied, boolean reserved) {
        if (occupied) return SLOT_OCCUPIED;
        if (reserved) return SLOT_RESERVED;
        return SLOT_AVAILABLE;
    }

    public static Icon getVehicleIcon(String vehicleType, int width, int height) {
        return new VehicleIcon(vehicleType, width, height);
    }

    public static class VehicleIcon implements Icon {
        private static Image carImage;
        private static Image motorcycleImage;
        private final String type;
        private final int width;
        private final int height;

        static {
            carImage = loadIcon("/project_of/_dsa/icons/car.png");
            motorcycleImage = loadIcon("/project_of/_dsa/icons/motorcycle.png");
        }

        private static Image loadIcon(String path) {
            try {
                java.net.URL url = Theme.class.getResource(path);
                if (url != null) {
                    Image img = new ImageIcon(url).getImage();
                    if (img.getWidth(null) > 0 && img.getHeight(null) > 0) {
                        return img;
                    }
                }
            } catch (Exception e) {
            }
            return null;
        }

        public VehicleIcon(String type, int width, int height) {
            this.type = type != null ? type : "Car";
            this.width = width;
            this.height = height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Image img = "Bike".equalsIgnoreCase(type) ? motorcycleImage : carImage;
            if (img != null) {
                g.drawImage(img, x, y, width, height, null);
            }
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }

    public static class RoundedBorder implements Border {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                    width - thickness, height - thickness,
                    radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static class RoundedButton extends JButton {
        private final Color baseColor;
        private final int radius;

        public RoundedButton(String text, Color baseColor, int radius) {
            super(text);
            this.baseColor = baseColor;
            this.radius = radius;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setBackground(baseColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
        }
    }
}
