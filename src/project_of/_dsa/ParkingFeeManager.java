
package project_of._dsa;

import java.time.LocalDateTime;
import java.time.Duration;

public class ParkingFeeManager {

    public static double calculateFee(Vehicle vehicle, SettingsBean settings) {
        if (vehicle == null || vehicle.getEntryTime() == null) {
            return 0.0;
        }
        LocalDateTime exitTime = vehicle.getExitTime() != null ? vehicle.getExitTime() : LocalDateTime.now();
        long minutes = Duration.between(vehicle.getEntryTime(), exitTime).toMinutes();
        double hours = Math.ceil(minutes / 60.0);
        if (hours == 0) {
            hours = 1;
        }
        double rate = settings.getRateForVehicle(vehicle.getVehicleType(), vehicle.getPriority());
        double totalFee = hours * rate;
        vehicle.setParkingFee(totalFee);
        return totalFee;
    }

    public static double calculateFeePreview(Vehicle vehicle, SettingsBean settings) {
        if (vehicle == null || vehicle.getEntryTime() == null) {
            return 0.0;
        }
        LocalDateTime previewExit = LocalDateTime.now();
        long minutes = Duration.between(vehicle.getEntryTime(), previewExit).toMinutes();
        double hours = Math.ceil(minutes / 60.0);
        if (hours == 0) {
            hours = 1;
        }
        double rate = settings.getRateForVehicle(vehicle.getVehicleType(), vehicle.getPriority());
        return hours * rate;
    }

    public static long calculateDurationMinutes(Vehicle vehicle) {
        if (vehicle == null || vehicle.getEntryTime() == null) {
            return 0;
        }
        LocalDateTime exitTime = vehicle.getExitTime() != null ? vehicle.getExitTime() : LocalDateTime.now();
        return Duration.between(vehicle.getEntryTime(), exitTime).toMinutes();
    }

    public static String formatDuration(long minutes) {
        long hours = minutes / 60;
        long mins = minutes % 60;
        if (hours == 0) {
            return mins + " min";
        }
        return hours + "h " + mins + "m";
    }
}
