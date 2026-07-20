
package project_of._dsa;


public class VehicleHashTable {
    private HashNode[] table;
    private int tableSize;
    public VehicleHashTable(int size){
        tableSize = size;
        table = new HashNode[size];
    }
    
    private int hashFunction(String vehicleNO){
        int sum =0;
        for(int i=0; i<vehicleNO.length();i++){
            sum = sum + vehicleNO.charAt(i);
        }
        return sum % tableSize;
    }
    
    public void insertVehicle(Vehicle vehicle){
        int index = hashFunction(vehicle.getVehicleNo());
         HashNode newNode = new HashNode(vehicle);
         
         if(table[index]==null){
             table[index]= newNode;
         }else{
             HashNode temp = table[index];
             while(temp.next != null){
                 temp = temp.next;
             }
             temp.next=newNode;
         }      
    }
    
    
    public Vehicle searchVehicle(String vehicleNO){
        int index = hashFunction(vehicleNO);
        
        HashNode temp = table[index];
        while(temp != null){
            if(temp.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNO)){
                return temp.vehicle;
            }
            temp = temp.next;
        }
     return null;
    }
    
    public boolean deleteVehicle(String vehicleNO){
        int index = hashFunction(vehicleNO);
        HashNode current = table[index];
        HashNode previous = null;
        
        while(current != null){
            if(current.vehicle.getVehicleNo().equalsIgnoreCase(vehicleNO)){
            if(previous==null){
                table[index]=current.next;
            }
            else{
                previous.next=current.next;
            }
            return true;
        }
        previous = current;
        current = current.next;
    }
        return false;
   }
    
    public void displayTable(){
        for (int i = 0; i<tableSize;i++){
            System.out.println("\nBucket"+i);
            HashNode temp = table[i];
            
            while(temp != null){
                System.out.println(temp.vehicle.getVehicleNo());
                temp = temp.next;
            }
        }
    }
}
