package project_of._dsa;

import java.util.*;

public class ParkingSlotManager {
    private List<Vehicle>[] slotVehicles;
    private int totalSlots;
    private int bikeZoneEnd;
    private int bikesPerSlot;
    private boolean allowBikeInCarSlots;
    private Map<Integer, String> reservedFor;

    private int generalZoneSize;
    private int vipZoneSize;
    private int emergencyZoneSize;
    private int generalZoneStart;
    private int vipZoneStart;
    private int emergencyZoneStart;

    @SuppressWarnings("unchecked")
    public ParkingSlotManager(int totalSlots) {
        this(totalSlots, 2, false);
    }

    @SuppressWarnings("unchecked")
    public ParkingSlotManager(int totalSlots, int bikesPerSlot, boolean allowBikeInCarSlots) {
        this.totalSlots = Math.max(1, totalSlots);
        this.slotVehicles = new ArrayList[this.totalSlots];
        for (int i = 0; i < this.totalSlots; i++) {
            this.slotVehicles[i] = new ArrayList<>();
        }
        this.reservedFor = new HashMap<>();
        this.bikesPerSlot = Math.max(1, bikesPerSlot);
        this.allowBikeInCarSlots = allowBikeInCarSlots;
        this.bikeZoneEnd = Math.max(1, (int) Math.ceil(this.totalSlots * 0.2));
        initializeZones();
    }

    private void initializeZones() {
        int remaining = totalSlots - bikeZoneEnd;
        if (remaining <= 0) {
            generalZoneSize = 0;
            vipZoneSize = 0;
            emergencyZoneSize = 0;
            generalZoneStart = bikeZoneEnd;
            vipZoneStart = totalSlots;
            emergencyZoneStart = totalSlots;
            return;
        }
        generalZoneSize = (int) Math.floor(remaining * 0.50);
        vipZoneSize = Math.max(1, (int) Math.floor(remaining * 0.15));
        emergencyZoneSize = remaining - generalZoneSize - vipZoneSize;
        if (emergencyZoneSize < 1) {
            emergencyZoneSize = 1;
            generalZoneSize = remaining - vipZoneSize - emergencyZoneSize;
        }
        generalZoneStart = bikeZoneEnd;
        vipZoneStart = generalZoneStart + generalZoneSize;
        emergencyZoneStart = vipZoneStart + vipZoneSize;
    }

    public boolean isBikeSlot(int slotNumber) {
        int index = slotNumber - 1;
        return index >= 0 && index < bikeZoneEnd;
    }

    public boolean isCarSlot(int slotNumber) {
        int index = slotNumber - 1;
        return index >= bikeZoneEnd && index < totalSlots;
    }

    public boolean canParkBikeInSlot(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        if (isBikeSlot(slotNumber)) return true;
        return allowBikeInCarSlots;
    }

    public boolean canParkCarInSlot(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        if (isCarSlot(slotNumber)) return true;
        return false;
    }

    public boolean isBikeSlotFull(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return true;
        return getBikeCount(slotNumber) >= bikesPerSlot;
    }

    public int getBikeCount(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return 0;
        int count = 0;
        for (Vehicle v : slotVehicles[index]) {
            if ("Bike".equalsIgnoreCase(v.getVehicleType())) {
                count++;
            }
        }
        return count;
    }

    public int getBikeZoneEnd() {
        return bikeZoneEnd;
    }

    public int getCarZoneStart() {
        return bikeZoneEnd + 1;
    }

    public int getBikesPerSlot() {
        return bikesPerSlot;
    }

    public boolean isAllowBikeInCarSlots() {
        return allowBikeInCarSlots;
    }

    public void setAllowBikeInCarSlots(boolean allow) {
        this.allowBikeInCarSlots = allow;
    }

    public void setBikesPerSlot(int count) {
        this.bikesPerSlot = Math.max(1, count);
    }

    public boolean isGeneralSlot(int slotNumber) {
        int index = slotNumber - 1;
        return index >= generalZoneStart && index < generalZoneStart + generalZoneSize;
    }

    public boolean isVipSlot(int slotNumber) {
        int index = slotNumber - 1;
        return index >= vipZoneStart && index < vipZoneStart + vipZoneSize;
    }

    public boolean isEmergencySlot(int slotNumber) {
        int index = slotNumber - 1;
        return index >= emergencyZoneStart && index < emergencyZoneStart + emergencyZoneSize;
    }

    public String getZoneForSlot(int slotNumber) {
        int index = slotNumber - 1;
        if (index >= 0 && index < bikeZoneEnd) return "BIKE";
        if (index >= generalZoneStart && index < generalZoneStart + generalZoneSize) return "GENERAL";
        if (index >= vipZoneStart && index < vipZoneStart + vipZoneSize) return "VIP";
        if (index >= emergencyZoneStart && index < emergencyZoneStart + emergencyZoneSize) return "EMERGENCY";
        return "UNKNOWN";
    }

    public String getSlotPrefix(int slotNumber) {
        int index = slotNumber - 1;
        if (index >= 0 && index < bikeZoneEnd) {
            return "B" + slotNumber;
        }
        if (index >= generalZoneStart && index < generalZoneStart + generalZoneSize) {
            return "G" + (index - generalZoneStart + 1);
        }
        if (index >= vipZoneStart && index < vipZoneStart + vipZoneSize) {
            return "V" + (index - vipZoneStart + 1);
        }
        if (index >= emergencyZoneStart && index < emergencyZoneStart + emergencyZoneSize) {
            return "E" + (index - emergencyZoneStart + 1);
        }
        return String.valueOf(slotNumber);
    }

    public String getSlotDisplayLabel(int slotNumber) {
        return getSlotPrefix(slotNumber) + " (" + getZoneForSlot(slotNumber) + ")";
    }

    public int getGeneralZoneSize() { return generalZoneSize; }
    public int getVipZoneSize() { return vipZoneSize; }
    public int getEmergencyZoneSize() { return emergencyZoneSize; }

    public int getGeneralZoneStart() { return generalZoneStart; }
    public int getGeneralZoneEnd() { return generalZoneStart + generalZoneSize - 1; }
    public int getVipZoneStart() { return vipZoneStart; }
    public int getVipZoneEnd() { return vipZoneStart + vipZoneSize - 1; }
    public int getEmergencyZoneStart() { return emergencyZoneStart; }
    public int getEmergencyZoneEnd() { return emergencyZoneStart + emergencyZoneSize - 1; }

    public int getGeneralOccupiedSlots() {
        int count = 0;
        for (int i = generalZoneStart; i < generalZoneStart + generalZoneSize; i++) {
            if (!slotVehicles[i].isEmpty()) count++;
        }
        return count;
    }

    public int getVipOccupiedSlots() {
        int count = 0;
        for (int i = vipZoneStart; i < vipZoneStart + vipZoneSize; i++) {
            if (!slotVehicles[i].isEmpty()) count++;
        }
        return count;
    }

    public int getEmergencyOccupiedSlots() {
        int count = 0;
        for (int i = emergencyZoneStart; i < emergencyZoneStart + emergencyZoneSize; i++) {
            if (!slotVehicles[i].isEmpty()) count++;
        }
        return count;
    }

    public int getGeneralAvailableSlots() { return generalZoneSize - getGeneralOccupiedSlots(); }
    public int getVipAvailableSlots() { return vipZoneSize - getVipOccupiedSlots(); }
    public int getEmergencyAvailableSlots() { return emergencyZoneSize - getEmergencyOccupiedSlots(); }

    public double getGeneralOccupancyPercentage() {
        if (generalZoneSize == 0) return 0.0;
        return ((double) getGeneralOccupiedSlots() / generalZoneSize) * 100.0;
    }

    public double getVipOccupancyPercentage() {
        if (vipZoneSize == 0) return 0.0;
        return ((double) getVipOccupiedSlots() / vipZoneSize) * 100.0;
    }

    public double getEmergencyOccupancyPercentage() {
        if (emergencyZoneSize == 0) return 0.0;
        return ((double) getEmergencyOccupiedSlots() / emergencyZoneSize) * 100.0;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public Vehicle getVehicleInSlot(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return null;
        List<Vehicle> vehicles = slotVehicles[index];
        return vehicles.isEmpty() ? null : vehicles.get(0);
    }

    public List<Vehicle> getAllVehiclesInSlot(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return Collections.emptyList();
        return new ArrayList<>(slotVehicles[index]);
    }

    public boolean isOccupied(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        return !slotVehicles[index].isEmpty();
    }

    public boolean isReserved(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        return reservedFor.containsKey(index);
    }

    public String getReservedFor(int slotNumber) {
        return reservedFor.get(slotNumber - 1);
    }

    public boolean reserveSlot(int slotNumber, String reservedFor) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        if (!slotVehicles[index].isEmpty()) return false;
        this.reservedFor.put(index, reservedFor);
        return true;
    }

    public boolean releaseSlot(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        if (slotVehicles[index].isEmpty()) return false;
        slotVehicles[index].clear();
        reservedFor.remove(index);
        return true;
    }

    public boolean occupySlot(int slotNumber, Vehicle vehicle) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        slotVehicles[index].clear();
        if (vehicle != null) {
            slotVehicles[index].add(vehicle);
        }
        reservedFor.remove(index);
        return true;
    }

    public boolean addVehicleToSlot(int slotNumber, Vehicle vehicle) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        if (!canVehicleUseSlot(vehicle, slotNumber)) {
            return false;
        }
        if (vehicle != null && !slotVehicles[index].contains(vehicle)) {
            slotVehicles[index].add(vehicle);
        }
        reservedFor.remove(index);
        return true;
    }

    public boolean canVehicleUseSlot(Vehicle vehicle, int slotNumber) {
        if (vehicle == null) return false;
        String type = vehicle.getVehicleType();
        String priority = vehicle.getPriority() != null ? vehicle.getPriority() : "NORMAL";
        String zone = getZoneForSlot(slotNumber);
        if ("BIKE".equalsIgnoreCase(zone)) {
            return "Bike".equalsIgnoreCase(type);
        }
        if ("VIP".equalsIgnoreCase(zone)) {
            return "VIP".equalsIgnoreCase(priority);
        }
        if ("EMERGENCY".equalsIgnoreCase(zone)) {
            return "Ambulance".equalsIgnoreCase(priority)
                    || "Fire Brigade".equalsIgnoreCase(priority)
                    || "Police".equalsIgnoreCase(priority);
        }
        if ("GENERAL".equalsIgnoreCase(zone)) {
            return !"Bike".equalsIgnoreCase(type)
                    && !"VIP".equalsIgnoreCase(priority)
                    && !"Ambulance".equalsIgnoreCase(priority)
                    && !"Fire Brigade".equalsIgnoreCase(priority)
                    && !"Police".equalsIgnoreCase(priority);
        }
        return false;
    }

    public int allocateSlot(String priority, String vehicleType) {
        if (vehicleType == null) vehicleType = "Car";

        if ("Bike".equalsIgnoreCase(vehicleType)) {
            return allocateBikeSlot(priority);
        } else if ("VIP".equalsIgnoreCase(priority)) {
            return allocateVipSlot();
        } else if ("Ambulance".equalsIgnoreCase(priority)
                || "Fire Brigade".equalsIgnoreCase(priority)
                || "Police".equalsIgnoreCase(priority)) {
            return allocateEmergencySlot();
        } else {
            return allocateGeneralSlot();
        }
    }

    private int allocateBikeSlot(String priority) {
        for (int i = 0; i < bikeZoneEnd; i++) {
            if (getBikeCount(i + 1) < bikesPerSlot) {
                return i + 1;
            }
        }
        if (allowBikeInCarSlots) {
            for (int i = bikeZoneEnd; i < totalSlots; i++) {
                if (slotVehicles[i].isEmpty()) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    private int allocateGeneralSlot() {
        for (int i = generalZoneStart; i < generalZoneStart + generalZoneSize; i++) {
            if (slotVehicles[i].isEmpty()) {
                return i + 1;
            }
        }
        return -1;
    }

    private int allocateVipSlot() {
        for (int i = vipZoneStart; i < vipZoneStart + vipZoneSize; i++) {
            if (slotVehicles[i].isEmpty()) {
                return i + 1;
            }
        }
        return -1;
    }

    private int allocateEmergencySlot() {
        for (int i = emergencyZoneStart; i < emergencyZoneStart + emergencyZoneSize; i++) {
            if (slotVehicles[i].isEmpty()) {
                return i + 1;
            }
        }
        return -1;
    }

    public AllocationResult tryAssignSlot(int requestedSlot, Vehicle incoming) {
        int index = requestedSlot - 1;
        if (index < 0 || index >= totalSlots) {
            return AllocationResult.invalid("Invalid slot number.");
        }

        if (!canVehicleUseSlot(incoming, requestedSlot)) {
            String zone = getZoneForSlot(requestedSlot);
            return AllocationResult.invalid("Vehicle type/priority is not allowed in " + zone + " zone (slot " + requestedSlot + ").");
        }

        List<Vehicle> existingVehicles = slotVehicles[index];
        if (existingVehicles.isEmpty()) {
            return AllocationResult.success(requestedSlot, null, false);
        }

        Vehicle existing = existingVehicles.get(0);
        int cmp = comparePriority(incoming.getPriority(), existing.getPriority());
        if (cmp >= 0) {
            return AllocationResult.denied("Slot " + requestedSlot + " is occupied by a vehicle with equal or higher priority (" + existing.getPriority() + ").");
        }

        Vehicle displaced = existing;
        int nearest = findNearestEmptySlot(displaced);
        if (nearest != -1) {
            return AllocationResult.success(requestedSlot, displaced, true);
        }

        return AllocationResult.success(requestedSlot, displaced, false);
    }

    public int findNearestEmptySlot(Vehicle vehicle) {
        if (vehicle == null) return -1;
        String type = vehicle.getVehicleType() != null ? vehicle.getVehicleType() : "Car";
        if ("Bike".equalsIgnoreCase(type)) {
            for (int i = 0; i < bikeZoneEnd; i++) {
                if (getBikeCount(i + 1) < bikesPerSlot) {
                    return i + 1;
                }
            }
            if (allowBikeInCarSlots) {
                return allocateGeneralSlot();
            }
            return -1;
        }
        return allocateSlot(vehicle.getPriority(), vehicle.getVehicleType());
    }

    public void relocateVehicle(Vehicle vehicle, int newSlot) {
        int oldSlot = vehicle.getSlotNumber();
        if (oldSlot > 0) {
            releaseSlot(oldSlot);
        }
        if (!canVehicleUseSlot(vehicle, newSlot)) {
            int correctSlot = allocateSlot(vehicle.getPriority(), vehicle.getVehicleType());
            if (correctSlot != -1) {
                newSlot = correctSlot;
            } else {
                newSlot = -1;
            }
        }
        if (newSlot != -1) {
            int index = newSlot - 1;
            slotVehicles[index].add(vehicle);
            vehicle.setSlotNumber(newSlot);
        }
    }

    public boolean removeVehicleFromSlot(int slotNumber, Vehicle vehicle) {
        int index = slotNumber - 1;
        if (index < 0 || index >= totalSlots) return false;
        boolean removed = slotVehicles[index].remove(vehicle);
        if (slotVehicles[index].isEmpty()) {
            reservedFor.remove(index);
        }
        return removed;
    }

    public static int comparePriority(String p1, String p2) {
        int v1 = getPriorityValue(p1);
        int v2 = getPriorityValue(p2);
        return Integer.compare(v1, v2);
    }

    private static int getPriorityValue(String priority) {
        if (priority == null) return 5;
        return switch (priority.toUpperCase()) {
            case "VIP" -> 1;
            case "AMBULANCE" -> 2;
            case "FIRE BRIGADE" -> 3;
            case "POLICE" -> 4;
            default -> 5;
        };
    }

    public int getOccupiedSlots() {
        int count = 0;
        for (List<Vehicle> list : slotVehicles) {
            if (!list.isEmpty()) count++;
        }
        return count;
    }

    public int getOccupiedBikeSlots() {
        int count = 0;
        for (int i = 0; i < bikeZoneEnd; i++) {
            if (!slotVehicles[i].isEmpty()) count++;
        }
        return count;
    }

    public int getTotalBikeCount() {
        int count = 0;
        for (int i = 0; i < bikeZoneEnd; i++) {
            count += getBikeCount(i + 1);
        }
        return count;
    }

    public int getOccupiedCarSlots() {
        int count = 0;
        for (int i = bikeZoneEnd; i < totalSlots; i++) {
            if (!slotVehicles[i].isEmpty()) count++;
        }
        return count;
    }

    public double getOccupancyPercentage() {
        if (totalSlots == 0) return 0.0;
        return ((double) getOccupiedSlots() / totalSlots) * 100.0;
    }

    public boolean[] getSlotStatuses() {
        boolean[] statuses = new boolean[totalSlots];
        for (int i = 0; i < totalSlots; i++) {
            statuses[i] = !slotVehicles[i].isEmpty();
        }
        return statuses;
    }

    public String[] getReservedStatuses() {
        String[] result = new String[totalSlots];
        for (int i = 0; i < totalSlots; i++) {
            result[i] = reservedFor.getOrDefault(i, null);
        }
        return result;
    }

    public void displaySlots() {
        System.out.println("========= PARKING SLOTS =========");
        for (int i = 0; i < totalSlots; i++) {
            String status;
            if (slotVehicles[i].isEmpty()) {
                status = reservedFor.containsKey(i) ? "Reserved (" + reservedFor.get(i) + ")" : "Available";
            } else {
                status = "Occupied (" + slotVehicles[i].get(0).getVehicleNo() + ")";
                if (isBikeSlot(i + 1)) {
                    status += " [Bikes: " + getBikeCount(i + 1) + "/" + bikesPerSlot + "]";
                }
            }
            System.out.println("Slot " + (i + 1) + " : " + status);
        }
    }

    public static class AllocationResult {
        public boolean success;
        public boolean displaced;
        public boolean canRelocate;
        public int slotNumber;
        public Vehicle displacedVehicle;
        public String message;

        private AllocationResult() {}

        static AllocationResult success(int slot, Vehicle displaced, boolean canRelocate) {
            AllocationResult r = new AllocationResult();
            r.success = true;
            r.displaced = displaced != null;
            r.canRelocate = canRelocate;
            r.slotNumber = slot;
            r.displacedVehicle = displaced;
            if (displaced != null) {
                r.message = canRelocate
                        ? "Slot " + slot + " available. Displaced vehicle (" + displaced.getVehicleNo() + ") will be relocated."
                        : "Parking is full. Displaced vehicle (" + displaced.getVehicleNo() + ") will be removed from parking.";
            } else {
                r.message = "Slot " + slot + " is available.";
            }
            return r;
        }

        static AllocationResult denied(String msg) {
            AllocationResult r = new AllocationResult();
            r.success = false;
            r.displaced = false;
            r.message = msg;
            return r;
        }

        static AllocationResult invalid(String msg) {
            AllocationResult r = new AllocationResult();
            r.success = false;
            r.displaced = false;
            r.message = msg;
            return r;
        }
    }
}
