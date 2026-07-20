
package project_of._dsa;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class VehicleDAO {

    private static LocalDateTime parseDateTimeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (value instanceof Number number) {
            return Instant.ofEpochMilli(number.longValue())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        String text = value.toString().trim();
        if (text.isEmpty()) {
            return null;
        }
        if (text.matches("\\d+")) {
            try {
                return Instant.ofEpochMilli(Long.parseLong(text))
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
            } catch (Exception ignored) {
            }
        }
        try {
            return LocalDateTime.parse(text);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return Instant.parse(text).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (DateTimeParseException ignored) {
        }
        try {
            return Timestamp.valueOf(text).toLocalDateTime();
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static void setDateTimeParameter(PreparedStatement ps, int index, LocalDateTime value) throws SQLException {
        if (value != null) {
            ps.setString(index, value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            ps.setNull(index, Types.VARCHAR);
        }
    }

    public static boolean insertVehicle(Vehicle vehicle) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO vehicles (vehicle_no, owner_name, vehicle_type, slot_number, entry_time, parked, priority) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, vehicle.getVehicleNo());
            ps.setString(2, vehicle.getOwnerName());
            ps.setString(3, vehicle.getVehicleType());
            ps.setInt(4, vehicle.getSlotNumber());
            setDateTimeParameter(ps, 5, vehicle.getEntryTime());
            ps.setInt(6, vehicle.isParked() ? 1 : 0);
            ps.setString(7, vehicle.getPriority() != null ? vehicle.getPriority() : "NORMAL");
            int rows = ps.executeUpdate();
            ps.close();
            con.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Vehicle searchVehicle(String vehicleNo) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM vehicles WHERE vehicle_no = ? ORDER BY parked DESC, entry_time DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, vehicleNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setExitTime(parseDateTimeValue(rs.getObject("exit_time")));
                vehicle.setParkingFee(rs.getDouble("parking_fee"));
                vehicle.setParked(rs.getInt("parked") == 1);
                vehicle.setPriority(rs.getString("priority"));
                rs.close();
                ps.close();
                con.close();
                return vehicle;
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateOwner(String vehicleNo, String ownerName) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE vehicles SET owner_name = ? WHERE vehicle_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ownerName);
            ps.setString(2, vehicleNo);
            int rows = ps.executeUpdate();
            ps.close();
            con.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteVehicle(String vehicleNo) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM vehicles WHERE vehicle_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, vehicleNo);
            int rows = ps.executeUpdate();
            ps.close();
            con.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM vehicles";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setExitTime(parseDateTimeValue(rs.getObject("exit_time")));
                vehicle.setParkingFee(rs.getDouble("parking_fee"));
                vehicle.setParked(rs.getInt("parked") == 1);
                vehicle.setPriority(rs.getString("priority"));
                list.add(vehicle);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean exitVehicle(String vehicleNo, LocalDateTime exitTime, double parkingFee) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE vehicles SET exit_time = ?, parking_fee = ?, parked = 0 WHERE vehicle_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            setDateTimeParameter(ps, 1, exitTime);
            ps.setDouble(2, parkingFee);
            ps.setString(3, vehicleNo);
            int rows = ps.executeUpdate();
            ps.close();
            con.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateVehicleSlot(String vehicleNo, int newSlot) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE vehicles SET slot_number = ? WHERE vehicle_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, newSlot);
            ps.setString(2, vehicleNo);
            int rows = ps.executeUpdate();
            ps.close();
            con.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getTotalVehiclesCount() {
        int count = 0;
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM vehicles");
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Vehicle> searchByOwner(String ownerName) {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM vehicles WHERE owner_name LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + ownerName + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setExitTime(parseDateTimeValue(rs.getObject("exit_time")));
                vehicle.setParkingFee(rs.getDouble("parking_fee"));
                vehicle.setParked(rs.getInt("parked") == 1);
                vehicle.setPriority(rs.getString("priority"));
                list.add(vehicle);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Vehicle> searchByType(String vehicleType) {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM vehicles WHERE vehicle_type = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, vehicleType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setExitTime(parseDateTimeValue(rs.getObject("exit_time")));
                vehicle.setParkingFee(rs.getDouble("parking_fee"));
                vehicle.setParked(rs.getInt("parked") == 1);
                vehicle.setPriority(rs.getString("priority"));
                list.add(vehicle);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static double getTodayRevenue() {
        double revenue = 0;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT SUM(parking_fee) FROM vehicles WHERE date(exit_time) = date('now') AND exit_time IS NOT NULL";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                revenue = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public static double getWeeklyRevenue() {
        double revenue = 0;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT SUM(parking_fee) FROM vehicles WHERE exit_time >= date('now', '-7 days') AND exit_time IS NOT NULL";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                revenue = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public static double getMonthlyRevenue() {
        double revenue = 0;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT SUM(parking_fee) FROM vehicles WHERE strftime('%m', exit_time) = strftime('%m', 'now') AND exit_time IS NOT NULL";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                revenue = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public static List<Vehicle> getParkedVehicles() {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM vehicles WHERE parked = 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                list.add(vehicle);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertParkingHistory(Vehicle vehicle) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO parking_history (vehicle_no, owner_name, vehicle_type, slot_number, entry_time, exit_time, parking_fee, duration_minutes, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, vehicle.getVehicleNo());
            ps.setString(2, vehicle.getOwnerName());
            ps.setString(3, vehicle.getVehicleType());
            ps.setInt(4, vehicle.getSlotNumber());
            setDateTimeParameter(ps, 5, vehicle.getEntryTime());
            setDateTimeParameter(ps, 6, vehicle.getExitTime());
            ps.setDouble(7, vehicle.getParkingFee());
            long duration = 0;
            if (vehicle.getEntryTime() != null && vehicle.getExitTime() != null) {
                duration = java.time.Duration.between(vehicle.getEntryTime(), vehicle.getExitTime()).toMinutes();
            }
            ps.setLong(8, duration);
            ps.setString(9, "EXITED");
            int rows = ps.executeUpdate();
            ps.close();
            con.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Vehicle> getParkingHistoryWithParked() {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String parkedSql = "SELECT * FROM vehicles WHERE parked = 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(parkedSql);
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setParked(true);
                list.add(vehicle);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Vehicle> getParkingHistory(String vehicleNo, String ownerName, String vehicleType, String status) {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM parking_history WHERE 1=1");
            List<Object> params = new ArrayList<>();
            if (vehicleNo != null && !vehicleNo.isEmpty()) {
                sql.append(" AND vehicle_no LIKE ?");
                params.add("%" + vehicleNo + "%");
            }
            if (ownerName != null && !ownerName.isEmpty()) {
                sql.append(" AND owner_name LIKE ?");
                params.add("%" + ownerName + "%");
            }
            if (vehicleType != null && !vehicleType.isEmpty()) {
                sql.append(" AND vehicle_type = ?");
                params.add(vehicleType);
            }
            if (status != null && !status.isEmpty()) {
                sql.append(" AND status = ?");
                params.add(status);
            }
            sql.append(" ORDER BY created_at DESC LIMIT 100");
            PreparedStatement ps = con.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i).toString());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setExitTime(parseDateTimeValue(rs.getObject("exit_time")));
                vehicle.setParkingFee(rs.getDouble("parking_fee"));
                list.add(vehicle);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Vehicle> getRecentActivities(int limit) {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM parking_history ORDER BY created_at DESC LIMIT ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("vehicle_no"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                );
                vehicle.setSlotNumber(rs.getInt("slot_number"));
                vehicle.setEntryTime(parseDateTimeValue(rs.getObject("entry_time")));
                vehicle.setExitTime(parseDateTimeValue(rs.getObject("exit_time")));
                vehicle.setParkingFee(rs.getDouble("parking_fee"));
                list.add(vehicle);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Vehicle> getPeakHours() {
        List<Vehicle> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT strftime('%H', entry_time) as hour, COUNT(*) as count FROM vehicles WHERE entry_time IS NOT NULL GROUP BY hour ORDER BY count DESC LIMIT 5";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(rs.getString("hour"), "Vehicles", "Count");
                vehicle.setParkingFee(rs.getInt("count"));
                list.add(vehicle);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
