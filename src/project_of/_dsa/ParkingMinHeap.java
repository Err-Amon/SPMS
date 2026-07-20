
package project_of._dsa;

public class ParkingMinHeap {
    private MinHeapNode[] heap;
    private int size;
    private int capacity;

    public ParkingMinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new MinHeapNode[capacity + 1];
    }

    private int getPriorityValue(String priority) {
        return switch (priority.toUpperCase()) {
            case "VIP" -> 1;
            case "AMBULANCE" -> 2;
            case "FIRE BRIGADE" -> 3;
            case "POLICE" -> 4;
            default -> 5;
        };
    }

    public void insert(Vehicle vehicle) {
        if (size >= capacity) {
            return;
        }
        int priorityValue = getPriorityValue(vehicle.getPriority() != null ? vehicle.getPriority() : "NORMAL");
        MinHeapNode node = new MinHeapNode(vehicle, priorityValue);
        heap[++size] = node;
        siftUp(size);
        System.out.println("Vehicle " + vehicle.getVehicleNo() + " inserted into Priority Queue with priority " + priorityValue);
    }

    public Vehicle extractMin() {
        if (size == 0) {
            return null;
        }
        Vehicle minVehicle = heap[1].vehicle;
        heap[1] = heap[size];
        heap[size] = null;
        size--;
        siftDown(1);
        System.out.println("Vehicle " + minVehicle.getVehicleNo() + " extracted from Priority Queue");
        return minVehicle;
    }

    public Vehicle peek() {
        if (size == 0) {
            return null;
        }
        return heap[1].vehicle;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void siftUp(int index) {
        while (index > 1) {
            int parent = index / 2;
            if (heap[parent].priority <= heap[index].priority) {
                break;
            }
            MinHeapNode temp = heap[parent];
            heap[parent] = heap[index];
            heap[index] = temp;
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (index * 2 <= size) {
            int smallest = index;
            int left = index * 2;
            int right = index * 2 + 1;

            if (left <= size && heap[left].priority < heap[smallest].priority) {
                smallest = left;
            }
            if (right <= size && heap[right].priority < heap[smallest].priority) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            MinHeapNode temp = heap[index];
            heap[index] = heap[smallest];
            heap[smallest] = temp;
            index = smallest;
        }
    }

    public void displayHeap() {
        if (size == 0) {
            System.out.println("Heap is empty");
            return;
        }
        System.out.println("----- Priority Queue Contents -----");
        for (int i = 1; i <= size; i++) {
            MinHeapNode node = heap[i];
            System.out.println("Priority: " + node.priority + " | Vehicle: " + node.vehicle);
        }
        System.out.println("-----------------------------------");
    }
}
