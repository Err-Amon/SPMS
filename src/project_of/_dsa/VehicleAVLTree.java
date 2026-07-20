
package project_of._dsa;

public class VehicleAVLTree {
    private AVLNode root;

    public VehicleAVLTree() {
        root = null;
    }

    public void insert(Vehicle vehicle) {
        root = insertNode(root, vehicle);
    }

    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int balanceFactor(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private AVLNode insertNode(AVLNode current, Vehicle vehicle) {
        if (current == null) {
            return new AVLNode(vehicle);
        }
        if (vehicle.getVehicleNo().compareToIgnoreCase(current.vehicle.getVehicleNo()) < 0) {
            current.left = insertNode(current.left, vehicle);
        } else if (vehicle.getVehicleNo().compareToIgnoreCase(current.vehicle.getVehicleNo()) > 0) {
            current.right = insertNode(current.right, vehicle);
        } else {
            return current;
        }

        current.height = 1 + Math.max(height(current.left), height(current.right));
        int balance = balanceFactor(current);

        if (balance > 1) {
            if (vehicle.getVehicleNo().compareToIgnoreCase(current.left.vehicle.getVehicleNo()) < 0) {
                return rightRotate(current);
            } else {
                current.left = leftRotate(current.left);
                return rightRotate(current);
            }
        }
        if (balance < -1) {
            if (vehicle.getVehicleNo().compareToIgnoreCase(current.right.vehicle.getVehicleNo()) > 0) {
                return leftRotate(current);
            } else {
                current.right = rightRotate(current.right);
                return leftRotate(current);
            }
        }
        return current;
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public Vehicle search(String vehicleNo) {
        AVLNode node = searchNode(root, vehicleNo);
        if (node == null) {
            return null;
        }
        return node.vehicle;
    }

    private AVLNode searchNode(AVLNode current, String vehicleNo) {
        if (current == null) {
            return null;
        }
        if (vehicleNo.equalsIgnoreCase(current.vehicle.getVehicleNo())) {
            return current;
        }
        if (vehicleNo.compareToIgnoreCase(current.vehicle.getVehicleNo()) < 0) {
            return searchNode(current.left, vehicleNo);
        }
        return searchNode(current.right, vehicleNo);
    }

    public void inOrder() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(AVLNode node) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left);
        System.out.println(node.vehicle);
        inOrderTraversal(node.right);
    }

    public void preOrder() {
        preOrderTraversal(root);
    }

    private void preOrderTraversal(AVLNode node) {
        if (node == null) {
            return;
        }
        System.out.println(node.vehicle);
        preOrderTraversal(node.left);
        preOrderTraversal(node.right);
    }

    public void postOrder() {
        postOrderTraversal(root);
    }

    private void postOrderTraversal(AVLNode node) {
        if (node == null) {
            return;
        }
        postOrderTraversal(node.left);
        postOrderTraversal(node.right);
        System.out.println(node.vehicle);
    }

    public void reverse() {
        reverseTree(root);
    }

    private void reverseTree(AVLNode node) {
        if (node == null) {
            return;
        }
        reverseTree(node.left);
        reverseTree(node.right);
        AVLNode temp = node.left;
        node.left = node.right;
        node.right = temp;
    }

    public int getHeight() {
        return root != null ? root.height : 0;
    }

    public void rangeSearch(String min, String max) {
        rangeSearchUtil(root, min, max);
    }

    private void rangeSearchUtil(AVLNode node, String min, String max) {
        if (node == null) {
            return;
        }
        if (min.compareToIgnoreCase(node.vehicle.getVehicleNo()) < 0) {
            rangeSearchUtil(node.left, min, max);
        }
        if (min.compareToIgnoreCase(node.vehicle.getVehicleNo()) <= 0
                && max.compareToIgnoreCase(node.vehicle.getVehicleNo()) >= 0) {
            System.out.println(node.vehicle);
        }
        if (max.compareToIgnoreCase(node.vehicle.getVehicleNo()) > 0) {
            rangeSearchUtil(node.right, min, max);
        }
    }

    public int getSize() {
        return countNodes(root);
    }

    private int countNodes(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
}
