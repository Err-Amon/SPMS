
package project_of._dsa;

public class VehicleLinkedList {
    private VehicleNode head;
    private int size;

    public VehicleLinkedList() {
        head = null;
        size = 0;
    }

    public void addVehicle(Vehicle vehicle) {
        VehicleNode newNode = new VehicleNode(vehicle);
        if (head == null) {
            head = newNode;
        } else {
            VehicleNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        size++;
        System.out.println("Vehicle Added Successfully");
    }

    public void addVehicleSilent(Vehicle vehicle) {
        VehicleNode newNode = new VehicleNode(vehicle);
        if (head == null) {
            head = newNode;
        } else {
            VehicleNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        size++;
    }

    public void displayVehicle() {
        if (head == null) {
            System.out.println("No Vehicles Found.");
            return;
        }
        VehicleNode temp = head;
        while (temp != null) {
            System.out.println("-------------------------");
            System.out.println("Vehicle NO:" + temp.vehicle.getVehicleNo());
            System.out.println("Owner: " + temp.vehicle.getOwnerName());
            System.out.println("Type:" + temp.vehicle.getVehicleType());
            temp = temp.next;
        }
    }

    public Vehicle searchVehicle(String vehicleNo) {
        VehicleNode temp = head;
        while (temp != null) {
            if (temp.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNo)) {
                return temp.vehicle;
            }
            temp = temp.next;
        }
        return null;
    }

    public boolean deleteVehicle(String vehicleNo) {
        if (head == null) {
            return false;
        }
        if (head.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNo)) {
            head = head.next;
            size--;
            return true;
        }
        VehicleNode current = head;
        VehicleNode previous = null;
        while (current != null) {
            if (current.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNo)) {
                previous.next = current.next;
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public boolean updateOwnerName(String vehicleNo, String newOwner) {
        Vehicle vehicle = searchVehicle(vehicleNo);
        if (vehicle != null) {
            vehicle.setOwnerName(newOwner);
            return true;
        }
        return false;
    }

    public int geSize() {
        return size;
    }

    public int getSize() {
        return size;
    }

    public void reverse() {
        VehicleNode prev = null;
        VehicleNode current = head;
        VehicleNode next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        head = prev;
    }

    public Vehicle[] toArray() {
        Vehicle[] arr = new Vehicle[size];
        VehicleNode temp = head;
        int i = 0;
        while (temp != null) {
            arr[i++] = temp.vehicle;
            temp = temp.next;
        }
        return arr;
    }
}
