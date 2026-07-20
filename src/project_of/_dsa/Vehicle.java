
package project_of._dsa;

import java.time.LocalDateTime;

public class Vehicle {

    private String vehicleNo;
    private String ownerName;
    private String vehicleType;
    private String priority;
    private int slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double parkingFee;
    private boolean parked;

    public Vehicle(String vehicleNo, String ownerName, String vehicleType) {
        this.vehicleNo = vehicleNo;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.priority = "NORMAL";
        this.slotNumber = -1;
        this.entryTime = null;
        this.exitTime = null;
        this.parkingFee = 0;
        this.parked = false;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getParkingFee() {
        return parkingFee;
    }

    public boolean isParked() {
        return parked;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setParkingFee(double parkingFee) {
        this.parkingFee = parkingFee;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    @Override
    public String toString() {
        return vehicleNo + " - " + ownerName + " [" + vehicleType + "]";
    }
}
