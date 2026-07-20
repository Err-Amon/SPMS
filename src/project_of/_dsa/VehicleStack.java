
package project_of._dsa;

public class VehicleStack {
    private StackNode head;
    private StackNode tail;
    private int size;

    public VehicleStack() {
        head = null;
        size = 0;
    }

    public void push(Vehicle vehicle) {
        StackNode newNode = new StackNode(vehicle);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
        System.out.println(vehicle.getVehicleNo() + " pushed into Stack.");
    }

    public Vehicle pop() {
        if (head == null) {
            System.out.println("Stack is Empty");
            return null;
        }
        Vehicle removedVehicle = head.vehicle;
        head = head.next;
        size--;
        System.out.println(removedVehicle.getVehicleNo() + " popped from Stack.");
        return removedVehicle;
    }

    public Vehicle peek() {
        if (head == null) {
            System.out.println("Stack is Empty");
            return null;
        }
        return head.vehicle;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void displayStack() {
        if (head == null) {
            System.out.println("Stack is Empty");
            return;
        }
        StackNode temp = head;
        int index = 1;
        while (temp != null) {
            System.out.println("----------");
            System.out.println("Index: " + index++);
            System.out.println("Vehicle: " + temp.vehicle.getVehicleNo());
            System.out.println("Owner: " + temp.vehicle.getOwnerName());
            System.out.println("Type: " + temp.vehicle.getVehicleType());
            temp = temp.next;
        }
    }

    public Vehicle[] toArray() {
        Vehicle[] arr = new Vehicle[size];
        StackNode temp = head;
        int i = 0;
        while (temp != null) {
            arr[i++] = temp.vehicle;
            temp = temp.next;
        }
        return arr;
    }
}
