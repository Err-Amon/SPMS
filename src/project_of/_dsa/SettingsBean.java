package project_of._dsa;

public class SettingsBean {

    private double parkingFeeCar;
    private double parkingFeeBike;
    private double parkingFeeVan;
    private double parkingFeeBus;
    private double parkingFeeSUV;
    private double parkingFeeTruck;
    private double parkingFeeVIP;
    private int totalCapacity;
    private int bikeSlotsPerSlot;
    private boolean allowBikeInCarSlots;

    public SettingsBean() {
        this.parkingFeeCar = 50.0;
        this.parkingFeeBike = 30.0;
        this.parkingFeeVan = 80.0;
        this.parkingFeeBus = 100.0;
        this.parkingFeeSUV = 60.0;
        this.parkingFeeTruck = 120.0;
        this.parkingFeeVIP = 150.0;
        this.totalCapacity = 50;
        this.bikeSlotsPerSlot = 2;
        this.allowBikeInCarSlots = false;
    }

    public double getParkingFeeCar() {
        return parkingFeeCar;
    }

    public void setParkingFeeCar(double parkingFeeCar) {
        this.parkingFeeCar = parkingFeeCar;
    }

    public double getParkingFeeBike() {
        return parkingFeeBike;
    }

    public void setParkingFeeBike(double parkingFeeBike) {
        this.parkingFeeBike = parkingFeeBike;
    }

    public double getParkingFeeVan() {
        return parkingFeeVan;
    }

    public void setParkingFeeVan(double parkingFeeVan) {
        this.parkingFeeVan = parkingFeeVan;
    }

    public double getParkingFeeBus() {
        return parkingFeeBus;
    }

    public void setParkingFeeBus(double parkingFeeBus) {
        this.parkingFeeBus = parkingFeeBus;
    }

    public double getParkingFeeSUV() {
        return parkingFeeSUV;
    }

    public void setParkingFeeSUV(double parkingFeeSUV) {
        this.parkingFeeSUV = parkingFeeSUV;
    }

    public double getParkingFeeTruck() {
        return parkingFeeTruck;
    }

    public void setParkingFeeTruck(double parkingFeeTruck) {
        this.parkingFeeTruck = parkingFeeTruck;
    }

    public double getParkingFeeVIP() {
        return parkingFeeVIP;
    }

    public void setParkingFeeVIP(double parkingFeeVIP) {
        this.parkingFeeVIP = parkingFeeVIP;
    }

    public int getBikeSlotsPerSlot() {
        return bikeSlotsPerSlot;
    }

    public void setBikeSlotsPerSlot(int bikeSlotsPerSlot) {
        if (bikeSlotsPerSlot < 1) bikeSlotsPerSlot = 1;
        if (bikeSlotsPerSlot > 10) bikeSlotsPerSlot = 10;
        this.bikeSlotsPerSlot = bikeSlotsPerSlot;
    }

    public boolean isAllowBikeInCarSlots() {
        return allowBikeInCarSlots;
    }

    public void setAllowBikeInCarSlots(boolean allowBikeInCarSlots) {
        this.allowBikeInCarSlots = allowBikeInCarSlots;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        if (totalCapacity < 1) totalCapacity = 1;
        if (totalCapacity > 1000) totalCapacity = 1000;
        this.totalCapacity = totalCapacity;
    }

    public double getRateForType(String type) {
        return getRateForVehicle(type, null);
    }

    public double getRateForVehicle(String type, String priority) {
        if (priority != null && priority.equalsIgnoreCase("VIP")) {
            return parkingFeeVIP;
        }
        return switch (type.toLowerCase()) {
            case "bike" -> parkingFeeBike;
            case "van" -> parkingFeeVan;
            case "bus" -> parkingFeeBus;
            case "suv" -> parkingFeeSUV;
            case "truck" -> parkingFeeTruck;
            default -> parkingFeeCar;
        };
    }
}
