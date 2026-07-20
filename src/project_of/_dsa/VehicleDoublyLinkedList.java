
package project_of._dsa;

public class VehicleDoublyLinkedList {
    private DLLNode head;
    private DLLNode tail;
    private int size;

    public VehicleDoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void addVehicle(Vehicle vehicle) {
        DLLNode newNode = new DLLNode(vehicle);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        System.out.println("Vehicle added to Doubly Linked List");
    }

    public void displayForward() {
        if (head == null) {
            System.out.println("No Vehicles Found.");
            return;
        }
        DLLNode temp = head;
        while (temp != null) {
            System.out.println(temp.vehicle);
            temp = temp.next;
        }
    }

    public void displayBackward() {
        if (tail == null) {
            System.out.println("No Vehicles Found.");
            return;
        }
        DLLNode temp = tail;
        while (temp != null) {
            System.out.println(temp.vehicle);
            temp = temp.prev;
        }
    }

    public Vehicle searchVehicle(String vehicleNo) {
        DLLNode temp = head;
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
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
            size--;
            return true;
        }
        DLLNode temp = head;
        while (temp != null) {
            if (temp.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNo)) {
                temp.prev.next = temp.next;
                if (temp.next != null) {
                    temp.next.prev = temp.prev;
                } else {
                    tail = temp.prev;
                }
                size--;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public boolean updateOwnerName(String vehicleNo, String newOwner) {
        DLLNode temp = head;
        while (temp != null) {
            if (temp.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNo)) {
                temp.vehicle.setOwnerName(newOwner);
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public Vehicle getFirst() {
        return head != null ? head.vehicle : null;
    }

    public Vehicle getLast() {
        return tail != null ? tail.vehicle : null;
    }

    public void reverse() {
        DLLNode current = head;
        DLLNode temp = null;
        while (current != null) {
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            current = current.prev;
        }
        if (temp != null) {
            tail = head;
            head = temp.prev;
        }
    }
}
