package project_of._dsa;

public class Project_of_DSA {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            // Initialize SQLite database and default data
            DBInitializer.initialize();

            // Initialize DSA structures
            new DataStructureManager();

            // Open Enhanced Login Screen
            new EnhancedLoginFrame();
        });
    }
}