package project_of._dsa;

import java.sql.*;

public class SettingsDAO {

    public static SettingsBean loadSettings() {
        SettingsBean settings = new SettingsBean();
        try (Connection con = DBConnection.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM settings WHERE id = 1");
            if (rs.next()) {
                settings.setParkingFeeCar(rs.getDouble("parking_fee_car"));
                settings.setParkingFeeBike(rs.getDouble("parking_fee_bike"));
                settings.setParkingFeeVan(rs.getDouble("parking_fee_van"));
                settings.setParkingFeeBus(rs.getDouble("parking_fee_bus"));
                settings.setParkingFeeSUV(rs.getDouble("parking_fee_suv"));
                settings.setParkingFeeTruck(rs.getDouble("parking_fee_truck"));
                settings.setParkingFeeVIP(rs.getDouble("parking_fee_vip"));
                settings.setTotalCapacity(rs.getInt("total_capacity"));
                settings.setBikeSlotsPerSlot(rs.getInt("bike_slots_per_slot"));
                settings.setAllowBikeInCarSlots(rs.getInt("allow_bike_in_car_slots") == 1);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return settings;
    }

    public static boolean saveSettings(SettingsBean settings) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE settings SET "
                    + "parking_fee_car = ?, "
                    + "parking_fee_bike = ?, "
                    + "parking_fee_van = ?, "
                    + "parking_fee_bus = ?, "
                    + "parking_fee_suv = ?, "
                    + "parking_fee_truck = ?, "
                    + "parking_fee_vip = ?, "
                    + "total_capacity = ?, "
                    + "bike_slots_per_slot = ?, "
                    + "allow_bike_in_car_slots = ?, "
                    + "updated_at = CURRENT_TIMESTAMP "
                    + "WHERE id = 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, settings.getParkingFeeCar());
            ps.setDouble(2, settings.getParkingFeeBike());
            ps.setDouble(3, settings.getParkingFeeVan());
            ps.setDouble(4, settings.getParkingFeeBus());
            ps.setDouble(5, settings.getParkingFeeSUV());
            ps.setDouble(6, settings.getParkingFeeTruck());
            ps.setDouble(7, settings.getParkingFeeVIP());
            ps.setInt(8, settings.getTotalCapacity());
            ps.setInt(9, settings.getBikeSlotsPerSlot());
            ps.setInt(10, settings.isAllowBikeInCarSlots() ? 1 : 0);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
