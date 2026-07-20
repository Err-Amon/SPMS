package project_of._dsa;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class SearchVehicleFrame extends JFrame {

    private EnhancedDashboard parentDashboard;
    private JTextField txtSearch;
    private JComboBox<String> cbSearchType;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private VehicleTrie trie;

    public SearchVehicleFrame(EnhancedDashboard parent) {
        this.parentDashboard = parent;
        this.trie = DataStructureManager.trie;
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Search Vehicle - Smart Parking System");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel title = Theme.createTitle("Vehicle Search");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel searchCard = new JPanel(new GridBagLayout());
        searchCard.setBackground(Theme.CARD);
        searchCard.setBorder(Theme.createCardBorder());
        searchCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        searchCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        searchCard.add(Theme.createLabel("Search By"), gbc);
        gbc.gridy = 1;
        cbSearchType = new JComboBox<>(new String[]{"Vehicle Number", "Owner Name"});
        cbSearchType.setFont(Theme.FONT_INPUT);
        cbSearchType.setPreferredSize(new Dimension(0, 40));
        cbSearchType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbSearchType.setBackground(Theme.INPUT_BG);
        searchCard.add(cbSearchType, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        searchCard.add(Theme.createLabel("Search Query"), gbc);
        gbc.gridy = 1;
        txtSearch = new JTextField();
        txtSearch.setFont(Theme.FONT_INPUT);
        txtSearch.setPreferredSize(new Dimension(0, 40));
        txtSearch.setBorder(Theme.createInputBorder());
        txtSearch.setBackground(Theme.INPUT_BG);
        searchCard.add(txtSearch, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        gbc.gridy = 1;
        JButton btnSearch = Theme.createAccentButton("Search");
        btnSearch.setPreferredSize(new Dimension(120, 42));
        btnSearch.setMaximumSize(new Dimension(140, 42));
        searchCard.add(btnSearch, gbc);

        mainPanel.add(searchCard);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        createResultsPanel(mainPanel);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        Theme.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> performSearch());
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                showLiveSuggestions(txtSearch.getText().trim());
            }
        });

        setVisible(true);
    }

    private void createResultsPanel(JPanel mainPanel) {
        JPanel resultsCard = new JPanel(new BorderLayout());
        resultsCard.setBackground(Theme.CARD);
        resultsCard.setBorder(Theme.createCardBorder());
        resultsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));

        JLabel resultsTitle = Theme.createSectionTitle("Search Results");
        resultsCard.add(resultsTitle, BorderLayout.NORTH);

        String[] columns = {"Vehicle No", "Owner", "Type", "Slot", "Entry Time", "Status", "Fee"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        Theme.styleTable(resultTable);
        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBorder(null);
        resultsCard.add(tableScroll, BorderLayout.CENTER);

        JLabel countLabel = new JLabel("0 results");
        countLabel.setFont(Theme.FONT_BODY);
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        resultsCard.add(countLabel, BorderLayout.SOUTH);

        mainPanel.add(resultsCard);
    }

    private void performSearch() {
        String query = txtSearch.getText().trim();
        String searchType = cbSearchType.getSelectedItem().toString();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search query", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        tableModel.setRowCount(0);
        java.util.List<Vehicle> results = new ArrayList<>();

        switch (searchType) {
            case "Vehicle Number":
                Vehicle byNo = trie.search(query) ? DataStructureManager.hashTable.searchVehicle(query) : null;
                if (byNo == null) {
                    byNo = VehicleDAO.searchVehicle(query);
                }
                if (byNo != null) {
                    results.add(byNo);
                }
                break;
            case "Owner Name":
                results = VehicleDAO.searchByOwner(query);
                break;
        }

        for (Vehicle v : results) {
            tableModel.addRow(new Object[]{
                    v.getVehicleNo(),
                    v.getOwnerName(),
                    v.getVehicleType(),
                    v.getSlotNumber() != -1 ? v.getSlotNumber() : "N/A",
                    v.getEntryTime() != null ? v.getEntryTime().toString().substring(0, 16) : "N/A",
                    v.isParked() ? "Parked" : "Exited",
                    String.format("%.2f PKR", v.getParkingFee())
            });
        }
    }

    private void showLiveSuggestions(String prefix) {
        if (prefix.length() < 1) return;
        java.util.List<String> suggestions = trie.searchPrefix(prefix);
        if (!suggestions.isEmpty()) {
            txtSearch.setToolTipText("Suggestions: " + String.join(", ", suggestions));
        }
    }
}
