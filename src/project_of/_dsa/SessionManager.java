
package project_of._dsa;

import java.sql.*;
import java.time.LocalDateTime;

public class SessionManager {

    private static String currentUser = null;
    private static LocalDateTime loginTime;

    public static boolean login(String username, String password) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT id, password_hash, role FROM login WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (PasswordUtils.verifyPassword(password, storedHash)) {
                    currentUser = username;
                    loginTime = LocalDateTime.now();
                    rs.close();
                    ps.close();
                    return true;
                }
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
        loginTime = null;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
