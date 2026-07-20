package project_of._dsa;

import java.sql.*;

public class DBInitializer {

    public static void initialize() {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("Cannot initialize database: connection is null");
                return;
            }

            Statement stmt = con.createStatement();

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS login (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password_hash TEXT NOT NULL, " +
                "role TEXT DEFAULT 'admin', " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS vehicles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "vehicle_no TEXT NOT NULL, " +
                "owner_name TEXT NOT NULL, " +
                "vehicle_type TEXT NOT NULL, " +
                "slot_number INTEGER DEFAULT -1, " +
                "entry_time TEXT, " +
                "exit_time TEXT, " +
                "parking_fee REAL DEFAULT 0.0, " +
                "parked INTEGER DEFAULT 0, " +
                "priority TEXT DEFAULT 'NORMAL', " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            migrateVehiclesTable(con);

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS parking_slots (" +
                "slot_number INTEGER PRIMARY KEY, " +
                "status TEXT DEFAULT 'AVAILABLE', " +
                "vehicle_no TEXT, " +
                "reserved_for TEXT DEFAULT NULL, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS parking_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "vehicle_no TEXT NOT NULL, " +
                "owner_name TEXT NOT NULL, " +
                "vehicle_type TEXT NOT NULL, " +
                "slot_number INTEGER, " +
                "entry_time TEXT, " +
                "exit_time TEXT, " +
                "parking_fee REAL DEFAULT 0.0, " +
                "duration_minutes INTEGER DEFAULT 0, " +
                "status TEXT DEFAULT 'EXITED', " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS settings (" +
                "id INTEGER PRIMARY KEY CHECK (id = 1), " +
                "parking_fee_car REAL DEFAULT 50.0, " +
                "parking_fee_bike REAL DEFAULT 30.0, " +
                "parking_fee_van REAL DEFAULT 80.0, " +
                "parking_fee_bus REAL DEFAULT 100.0, " +
                "parking_fee_suv REAL DEFAULT 60.0, " +
                "parking_fee_truck REAL DEFAULT 120.0, " +
                "parking_fee_vip REAL DEFAULT 150.0, " +
                "total_capacity INTEGER DEFAULT 50, " +
                "bike_slots_per_slot INTEGER DEFAULT 2, " +
                "allow_bike_in_car_slots INTEGER DEFAULT 0, " +
                "hourly_rate REAL DEFAULT 50.0, " +
                "theme TEXT DEFAULT 'LIGHT', " +
                "long_parking_threshold_minutes INTEGER DEFAULT 720, " +
                "updated_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            migrateSettingsTable(con);

            stmt.executeUpdate(
                "INSERT OR IGNORE INTO settings (id, total_capacity) VALUES (1, 50)"
            );

            stmt.executeUpdate(
                "INSERT OR IGNORE INTO login (id, username, password_hash, role) " +
                "VALUES (1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin')"
            );

            stmt.close();
            System.out.println("Database initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void migrateVehiclesTable(Connection con) {
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet rs = meta.getTables(null, null, "vehicles", null);
            if (!rs.next()) {
                rs.close();
                return;
            }
            rs.close();

            Statement stmt = con.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS vehicles_new");

            stmt.executeUpdate(
                "CREATE TABLE vehicles_new (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "vehicle_no TEXT NOT NULL, " +
                "owner_name TEXT NOT NULL, " +
                "vehicle_type TEXT NOT NULL, " +
                "slot_number INTEGER DEFAULT -1, " +
                "entry_time TEXT, " +
                "exit_time TEXT, " +
                "parking_fee REAL DEFAULT 0.0, " +
                "parked INTEGER DEFAULT 0, " +
                "priority TEXT DEFAULT 'NORMAL', " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            stmt.executeUpdate("INSERT INTO vehicles_new SELECT * FROM vehicles");
            stmt.executeUpdate("DROP TABLE vehicles");
            stmt.executeUpdate("ALTER TABLE vehicles_new RENAME TO vehicles");
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void migrateSettingsTable(Connection con) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("ALTER TABLE settings ADD COLUMN parking_fee_suv REAL DEFAULT 60.0");
            stmt.executeUpdate("ALTER TABLE settings ADD COLUMN parking_fee_truck REAL DEFAULT 120.0");
            stmt.executeUpdate("ALTER TABLE settings ADD COLUMN parking_fee_vip REAL DEFAULT 150.0");
            stmt.executeUpdate("ALTER TABLE settings ADD COLUMN bike_slots_per_slot INTEGER DEFAULT 2");
            stmt.executeUpdate("ALTER TABLE settings ADD COLUMN allow_bike_in_car_slots INTEGER DEFAULT 0");
            stmt.close();
        } catch (Exception e) {
        }
    }
}
