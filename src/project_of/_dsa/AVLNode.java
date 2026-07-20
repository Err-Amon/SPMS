
package project_of._dsa;

public class AVLNode {
    Vehicle vehicle;
    AVLNode left;
    AVLNode right;
    int height;

    public AVLNode(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
