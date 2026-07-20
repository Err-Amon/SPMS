
package project_of._dsa;

public class DLLNode {
    Vehicle vehicle;
    DLLNode prev;
    DLLNode next;

    public DLLNode(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.prev = null;
        this.next = null;
    }
}
