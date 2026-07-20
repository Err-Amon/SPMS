
package project_of._dsa;

public class CircularVehicleQueue {
    private Vehicle[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public CircularVehicleQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new Vehicle[capacity];
        this.front = -1;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(Vehicle vehicle) {
        if (isFull()) {
            System.out.println("Queue is Full");
            return;
        }
        if (front == -1) {
            front = 0;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = vehicle;
        size++;
        System.out.println(vehicle.getVehicleNo() + " enqueued in Circular Queue");
    }

    public Vehicle dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return null;
        }
        Vehicle removed = queue[front];
        queue[front] = null;
        if (front == rear) {
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % capacity;
        }
        size--;
        System.out.println(removed.getVehicleNo() + " dequeued from Circular Queue");
        return removed;
    }

    public Vehicle peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int getSize() {
        return size;
    }

    public void displayQueue() {
        if (isEmpty()) {
            System.out.println("Queue Empty");
            return;
        }
        int i = front;
        int count = 0;
        while (count < size) {
            System.out.println(queue[i].getVehicleNo());
            i = (i + 1) % capacity;
            count++;
        }
    }
}
