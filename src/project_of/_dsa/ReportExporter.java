package project_of._dsa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ReportExporter {

    public static File exportParkingHistoryCsv(File targetFile) throws IOException {
        List<Vehicle> history = VehicleDAO.getParkingHistory("", "", "", "");
        try (FileWriter writer = new FileWriter(targetFile, StandardCharsets.UTF_8)) {
            writer.write("vehicle_no,owner_name,vehicle_type,slot_number,entry_time,exit_time,parking_fee,duration_minutes,status\n");
            for (Vehicle v : history) {
                long minutes = 0;
                if (v.getEntryTime() != null && v.getExitTime() != null) {
                    minutes = java.time.Duration.between(v.getEntryTime(), v.getExitTime()).toMinutes();
                }
                writer.write(String.format(
                        "%s,%s,%s,%s,%s,%s,%.2f,%d,%s\n",
                        escapeCsv(v.getVehicleNo()),
                        escapeCsv(v.getOwnerName()),
                        escapeCsv(v.getVehicleType()),
                        v.getSlotNumber() != -1 ? v.getSlotNumber() : "",
                        v.getEntryTime() != null ? v.getEntryTime().toString() : "",
                        v.getExitTime() != null ? v.getExitTime().toString() : "",
                        v.getParkingFee(),
                        minutes,
                        v.isParked() ? "PARKED" : "EXITED"
                ));
            }
        }
        return targetFile;
    }

    public static File exportRevenuePdf(File targetFile) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append("Smart Parking Management System\n");
        content.append("Revenue Report\n");
        content.append("Today: PKR ").append(String.format("%.2f", VehicleDAO.getTodayRevenue())).append("\n");
        content.append("This Week: PKR ").append(String.format("%.2f", VehicleDAO.getWeeklyRevenue())).append("\n");
        content.append("This Month: PKR ").append(String.format("%.2f", VehicleDAO.getMonthlyRevenue())).append("\n");
        content.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n");
        String escaped = content.toString().replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
        String pdf = "%PDF-1.4\n"
                + "1 0 obj<< /Type /Catalog /Pages 2 0 R >>endobj\n"
                + "2 0 obj<< /Type /Pages /Kids [3 0 R] /Count 1 >>endobj\n"
                + "3 0 obj<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>endobj\n"
                + "4 0 obj<< /Length 0 >>stream\n"
                + "BT\n/F1 12 Tf\n72 720 Td\n(" + escaped + ") Tj\nET\n"
                + "endstream\nendobj\n"
                + "5 0 obj<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>endobj\n"
                + "xref\n0 6\n"
                + "0000000000 65535 f \n"
                + "0000000010 00000 n \n"
                + "0000000062 00000 n \n"
                + "0000000119 00000 n \n"
                + "0000000203 00000 n \n"
                + "0000000301 00000 n \n"
                + "trailer<< /Size 6 /Root 1 0 R >>\n"
                + "startxref\n0\n"
                + "%%EOF\n";
        Files.writeString(targetFile.toPath(), pdf, StandardCharsets.ISO_8859_1);
        return targetFile;
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", "\\,").replace("\n", " ").replace("\r", " ");
    }
}
