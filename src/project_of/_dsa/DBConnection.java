
package project_of._dsa;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static Connection con;
    private static boolean driverLoaded = false;

    public static Connection getConnection() {
        try {
            ensureDriverLoaded();
            con = DriverManager.getConnection("jdbc:sqlite:parking.db");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    private static synchronized void ensureDriverLoaded() throws Exception {
        if (driverLoaded) {
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            driverLoaded = true;
            return;
        } catch (ClassNotFoundException ignored) {
            Path jarPath = findSqliteJar();
            if (jarPath != null && Files.exists(jarPath)) {
                URLClassLoader systemLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
                addUrlMethod.invoke(systemLoader, jarPath.toUri().toURL());
                Class.forName("org.sqlite.JDBC");
                driverLoaded = true;
                return;
            }
            throw ignored;
        }
    }

    private static Path findSqliteJar() {
        String[] candidates = {
                Paths.get("lib", "sqlite-jdbc.jar").toString(),
                Paths.get(System.getProperty("user.dir"), "lib", "sqlite-jdbc.jar").toString(),
                Paths.get(System.getProperty("user.dir"), "..", "lib", "sqlite-jdbc.jar").toString()
        };

        for (String candidate : candidates) {
            Path path = Paths.get(candidate);
            if (Files.exists(path)) {
                return path;
            }
        }

        return null;
    }
}
