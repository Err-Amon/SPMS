package project_of._dsa;

public class DataStructureManager {

    public static ParkingQueue parkingQueue;
    public static VehicleStack vehicleStack;
    public static VehicleLinkedList linkedList;
    public static VehicleDoublyLinkedList doublyLinkedList;
    public static VehicleHashTable hashTable;
    public static VehicleAVLTree avlTree;
    public static CircularVehicleQueue circularQueue;
    public static ParkingMinHeap minHeap;
    public static VehicleTrie trie;
    public static DynamicArray<Vehicle> dynamicArray;
    public static ParkingSlotManager slotManager;
    public static java.util.Queue<Vehicle> waitingQueue;

    public static int CAPACITY = 50;

    public DataStructureManager() {
        initializeStructures();
    }

    public static void initializeStructures() {
        SettingsBean settings = SettingsDAO.loadSettings();
        CAPACITY = Math.max(1, settings.getTotalCapacity());
        slotManager = new ParkingSlotManager(CAPACITY, settings.getBikeSlotsPerSlot(), settings.isAllowBikeInCarSlots());
        parkingQueue = new ParkingQueue();
        vehicleStack = new VehicleStack();
        linkedList = new VehicleLinkedList();
        doublyLinkedList = new VehicleDoublyLinkedList();
        hashTable = new VehicleHashTable(Math.max(50, CAPACITY));
        avlTree = new VehicleAVLTree();
        circularQueue = new CircularVehicleQueue(CAPACITY);
        minHeap = new ParkingMinHeap(CAPACITY);
        trie = new VehicleTrie();
        dynamicArray = new DynamicArray<>();
        waitingQueue = new java.util.LinkedList<>();
    }

    public static void refreshFromDB() {
        initializeStructures();
        java.util.List<Vehicle> vehicles = VehicleDAO.getAllVehicles();
        for (Vehicle v : vehicles) {
            dynamicArray.add(v);
            linkedList.addVehicleSilent(v);
            doublyLinkedList.addVehicle(v);
            hashTable.insertVehicle(v);
            avlTree.insert(v);
            minHeap.insert(v);
            trie.insert(v.getVehicleNo());
            if (v.isParked() && v.getSlotNumber() > 0) {
                slotManager.addVehicleToSlot(v.getSlotNumber(), v);
            }
        }
    }

    public static void rebuildDSAStructures() {
        refreshFromDB();
    }

    public static void reinitializeSlots(int newCapacity) {
        if (newCapacity < 1) newCapacity = 1;
        if (newCapacity > 1000) newCapacity = 1000;
        CAPACITY = newCapacity;
        DataStructureManager old = new DataStructureManager();
        old.refreshFromDB();
    }
}
