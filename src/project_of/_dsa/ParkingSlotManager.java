package project_of._dsa;

import java.util.*;

public class ParkingSlotManager {
    private List<Vehicle>[] slotVehicles;
    private int totalSlots;
    private int bikeZoneEnd;
    private int bikesPerSlot;
    private boolean allowBikeInCarSlots;
    private Map<Integer, String> reservedFor;

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
        if (vehicle != null && !slotVehicles[index].contains(vehicle)) {
            slotVehicles[index].add(vehicle);
        }
        reservedFor.remove(index);
        return true;
    }

    public int allocateSlot(String priority, String vehicleType) {
        if (vehicleType == null) vehicleType = "Car";

        if ("Bike".equalsIgnoreCase(vehicleType)) {
            return allocateBikeSlot(priority);
        } else {
            return allocateCarSlot(priority);
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

    private int allocateCarSlot(String priority) {
        for (int i = bikeZoneEnd; i < totalSlots; i++) {
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

        String incomingType = incoming.getVehicleType() != null ? incoming.getVehicleType() : "Car";
        boolean incomingIsBike = "Bike".equalsIgnoreCase(incomingType);

        if (incomingIsBike && !canParkBikeInSlot(requestedSlot)) {
            return AllocationResult.invalid("Bikes are not allowed in this zone.");
        }
        if (!incomingIsBike && !canParkCarInSlot(requestedSlot)) {
            return AllocationResult.invalid("Cars are not allowed in bike-only slots.");
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
            for (int i = 0; i < totalSlots; i++) {
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
        } else {
            for (int i = bikeZoneEnd; i < totalSlots; i++) {
                if (slotVehicles[i].isEmpty()) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    public void relocateVehicle(Vehicle vehicle, int newSlot) {
        int oldSlot = vehicle.getSlotNumber();
        if (oldSlot > 0) {
            releaseSlot(oldSlot);
        }
        int index = newSlot - 1;
        slotVehicles[index].add(vehicle);
        vehicle.setSlotNumber(newSlot);
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
