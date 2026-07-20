
package project_of._dsa;
import java.util.LinkedList;
import java.util.Queue;

public class ParkingQueue {
    public static class QueueNode{
        Vehicle vehicle;
        QueueNode next;
        
        public QueueNode(Vehicle vehicle){
            this.vehicle=vehicle;
            this.next=null;
        }
       
    }
    private QueueNode front;
    private QueueNode rear;
    private int Size;
    public ParkingQueue(){
        
    }
    public ParkingQueue(Vehicle vehicle){
        front = null;
        rear = null;
        Size = 0;
    }
    
    public void Addvehicle(Vehicle vehicle){
        QueueNode newNode = new QueueNode(vehicle);
        if(rear == null){
            front = newNode;
            rear = newNode;
            
        }else{
            rear.next = newNode;
            rear = newNode;
        }
        Size++;
        System.out.println(vehicle.getVehicleNo()+"Add Successfully");
    }
    public Vehicle removeVehicle(){
        if(front == null){
            System.out.println("Queue is Empty");
            return null;
        }
        Vehicle removeVehicle=front.vehicle;
        front = front.next;
        if(front == null){
            rear = null;
        }
        Size--;
        return removeVehicle;
    }
    
    public Vehicle getFront(){
                if(front == null){
            System.out.println("Queue is Empty");
            return null;
        }
        return front.vehicle;
    }
    public boolean isEmpty() {

        return front == null;
    }

   
    public int getSize() {

        return Size;
    }

    
    public void displayQueue() {

        if (front == null) {

            System.out.println(
                    "Queue Empty");

            return;
        }

        QueueNode temp = front;

        while (temp != null) {

            System.out.println(
                    temp.vehicle.getVehicleNo()
            );

            temp = temp.next;
        }
    }
    
}
