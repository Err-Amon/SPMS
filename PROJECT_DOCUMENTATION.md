# Smart Parking Management System - Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Project Architecture](#project-architecture)
3. [Technology Stack](#technology-stack)
4. [Data Structures](#data-structures)
5. [Java Files - Detailed Explanation](#java-files---detailed-explanation)
   - [Project_of_DSA.java](#project_of_dsajava)
   - [EnhancedLoginFrame.java](#enhancedloginframejava)
   - [EnhancedDashboard.java](#enhanceddashboardjava)
   - [VehicleEntryFrame.java](#vehicleentryframejava)
   - [VehicleExitFrame.java](#vehicleexitframejava)
   - [ParkingSlotsFrame.java](#parkingslotsframejava)
   - [SearchVehicleFrame.java](#searchvehicleframejava)
   - [ReportsFrame.java](#reportsframejava)
   - [HistoryFrame.java](#historyframejava)
   - [SettingsFrame.java](#settingsframejava)
   - [VehicleDAO.java](#vehicledaojava)
   - [SettingsDAO.java](#settingsdaojava)
   - [DBConnection.java](#dbconnectionjava)
   - [DBInitializer.java](#dbinitializerjava)
   - [SessionManager.java](#sessionmanagerjava)
   - [PasswordUtils.java](#passwordutilsjava)
   - [Theme.java](#themejava)
   - [ParkingFeeManager.java](#parkingfeemanagerjava)
   - [SettingsBean.java](#settingsbeanjava)
   - [ReportExporter.java](#reportexporterjava)
   - [GreedyAllocator.java](#greedyallocatorjava)
   - [AVLNode.java](#avlnodejava)
   - [CircularVehicleQueue.java](#circularvehiclequeuejava)
   - [DataStructureManager.java](#datastructuremanagerjava)
   - [DLLNode.java](#dllnodejava)
   - [DynamicArray.java](#dynamicarrayjava)
   - [HashNode.java](#hashnodejava)
   - [MinHeapNode.java](#minheapnodejava)
   - [ParkingGraph.java](#parkinggraphjava)
   - [ParkingMinHeap.java](#parkingminheapjava)
   - [ParkingQueue.java](#parkingqueuejava)
   - [ParkingSlotManager.java](#parkingslotmanagerjava)
   - [StackNode.java](#stacknodejava)
   - [TrieNode.java](#trienodejava)
   - [Vehicle.java](#vehiclejava)
   - [VehicleAVLTree.java](#vehicleavltreejava)
   - [VehicleDoublyLinkedList.java](#vehicledoublylinkedlistjava)
   - [VehicleHashTable.java](#vehiclehashtablejava)
   - [VehicleLinkedList.java](#vehiclelinkedlistjava)
   - [VehicleNode.java](#vehiclenodejava)
   - [VehicleStack.java](#vehiclestackjava)
   - [VehicleTrie.java](#vehicletriejava)
6. [Database Schema](#database-schema)
7. [Key Methods and Interfaces](#key-methods-and-interfaces)
8. [Project Workflow](#project-workflow)

---

## Project Overview

**Project Name:** Smart Parking Management System  
**Type:** Desktop Application (Java Swing)  
**Purpose:** A comprehensive parking management system that uses Data Structures and Algorithms (DSA) to efficiently manage vehicle parking, allocation, billing, and reporting.

### Core Features
- User authentication and session management
- Vehicle entry/exit with priority-based parking allocation
- Multiple vehicle types: Car, Bike, Van, Bus, SUV, Truck, VIP Vehicle
- Priority levels: VIP > Ambulance > Fire Brigade > Police > Normal
- Dedicated bike parking zones (first 20% of slots)
- Dynamic parking fee calculation based on vehicle type and settings
- Real-time parking slot visualization
- Search functionality using Trie data structure
- Parking history and reporting with PDF/CSV export
- Settings management for fees, capacity, and bike slot configuration

---

## Project Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────┐
│         Presentation Layer (UI)             │
│  (Swing Frames - Login, Dashboard, etc.)   │
├─────────────────────────────────────────────┤
│         Business Logic Layer                │
│  (ParkingSlotManager, Fee Calculation,     │
│   Priority Handling, Allocation)            │
├─────────────────────────────────────────────┤
│         Data Structures Layer               │
│  (HashTable, AVLTree, Trie, LinkedList,    │
│   Stack, Queue, MinHeap, DynamicArray)      │
├─────────────────────────────────────────────┤
│         Data Access Layer                   │
│  (VehicleDAO, SettingsDAO, DBConnection)   │
├─────────────────────────────────────────────┤
│         Database Layer                      │
│         (SQLite Database)                   │
└─────────────────────────────────────────────┘
```

### Component Interaction Flow

```
User Input (UI)
     │
     ▼
VehicleDAO / SettingsDAO
     │
     ▼
DataStructureManager
     │
     ├──► ParkingSlotManager (Slot Allocation)
     ├──► VehicleHashTable (O(1) Search)
     ├──► VehicleAVLTree (Sorted Operations)
     ├──► VehicleTrie (Prefix Search)
     ├──► ParkingMinHeap (Priority Queue)
     ├──► VehicleLinkedList (Sequential Access)
     ├──► VehicleDoublyLinkedList (Bidirectional)
     ├──► VehicleStack (LIFO - History)
     ├──► ParkingQueue (FIFO - Waiting)
     ├──► CircularVehicleQueue (Circular Buffer)
     └──► DynamicArray (Dynamic Array)
     │
     ▼
Database (SQLite)
```

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java |
| UI Framework | Java Swing |
| Database | SQLite |
| Build | javac (manual compilation) |
| Icons | PNG (Flaticon/Icons8) |
| Architecture | MVC-inspired (DataStructureManager as central hub) |

---

## Data Structures

### 1. ParkingSlotManager
**Type:** Array of Lists with Zone Management  
**Purpose:** Core parking slot allocation and management system

**Key Characteristics:**
- Uses `ArrayList<Vehicle>[]` to represent parking slots
- Each slot can hold multiple bikes (configurable capacity)
- Implements zone-based allocation: first 20% = bike zone, rest = car zone
- Supports priority-based slot replacement
- Thread-safe slot state management

**How it differs from similar structures:**
- Unlike a simple array, each slot is a list allowing multiple vehicles (bikes)
- Unlike a HashMap, slot numbers are sequential and used directly as indices
- Unlike a Queue, it supports random access and priority-based eviction
- Unlike a Stack, it doesn't follow LIFO; instead follows spatial allocation rules

**Key Methods:**
- `allocateSlot(priority, vehicleType)` - Auto-allocate based on type and priority
- `tryAssignSlot(requestedSlot, incoming)` - Attempt preferred slot assignment with priority rules
- `findNearestEmptySlot(vehicle)` - Find closest available slot for relocation
- `relocateVehicle(vehicle, newSlot)` - Move vehicle to new slot
- `occupySlot(slotNumber, vehicle)` - Mark slot as occupied
- `releaseSlot(slotNumber)` - Free a slot
- `isBikeSlot(slotNumber)` - Check if slot is in bike zone
- `isCarSlot(slotNumber)` - Check if slot is in car zone
- `canParkBikeInSlot(slotNumber)` - Validate bike parking permission
- `canParkCarInSlot(slotNumber)` - Validate car parking permission
- `getBikeCount(slotNumber)` - Count bikes in a slot
- `isBikeSlotFull(slotNumber)` - Check bike slot capacity
- `getOccupiedSlots()` - Count total occupied slots
- `getOccupiedBikeSlots()` - Count occupied bike slots
- `getOccupiedCarSlots()` - Count occupied car slots
- `getTotalBikeCount()` - Total bikes across all bike slots
- `getOccupancyPercentage()` - Overall occupancy rate
- `getSlotStatuses()` - Get all slot occupancy states
- `getReservedStatuses()` - Get reservation info
- `displaySlots()` - Console display of all slots
- `comparePriority(p1, p2)` - Compare priority levels
- `getPriorityValue(priority)` - Convert priority to numeric value

---

### 2. VehicleHashTable
**Type:** Hash Table with Separate Chaining  
**Purpose:** O(1) average-case vehicle lookup by registration number

**Key Characteristics:**
- Custom hash function: sum of character codes modulo table size
- Uses linked list chaining for collision resolution
- Supports insert, search, and delete operations
- Case-insensitive vehicle number search

**How it differs from similar structures:**
- **vs HashMap:** Custom implementation for learning; uses separate chaining instead of Java's built-in hashing
- **vs VehicleAVLTree:** O(1) average search vs O(log n); no ordering maintained; simpler but no sorted operations
- **vs VehicleTrie:** No prefix search capability; direct key-based lookup only
- **vs Linear Search:** Much faster for large datasets; O(1) vs O(n)

**Key Methods:**
- `insertVehicle(vehicle)` - Add vehicle to hash table
- `searchVehicle(vehicleNo)` - Find vehicle by registration number
- `deleteVehicle(vehicleNo)` - Remove vehicle from hash table
- `displayTable()` - Console display of all buckets

---

### 3. VehicleAVLTree
**Type:** Self-Balancing Binary Search Tree (AVL Tree)  
**Purpose:** Maintain sorted vehicle records by registration number with O(log n) operations

**Key Characteristics:**
- Self-balancing using height difference (balance factor)
- Supports four rotation types: Left, Right, Left-Right, Right-Left
- Maintains sorted order by vehicle registration number
- Supports in-order, pre-order, post-order, and reverse traversals
- Supports range search operations

**How it differs from similar structures:**
- **vs Binary Search Tree:** Automatically balances to prevent O(n) worst case; guaranteed O(log n)
- **vs VehicleHashTable:** Maintains sorted order; supports range queries and traversals; O(log n) vs O(1) average
- **vs VehicleTrie:** Different use case; AVL for general sorting, Trie for string prefix matching
- **vs DynamicArray:** Dynamic size, sorted, linked structure vs contiguous memory

**Key Methods:**
- `insert(vehicle)` - Insert vehicle while maintaining balance
- `search(vehicleNo)` - Search by registration number
- `inOrder()` - In-order traversal (sorted)
- `preOrder()` - Pre-order traversal
- `postOrder()` - Post-order traversal
- `reverse()` - Reverse the tree
- `rangeSearch(min, max)` - Find vehicles in registration number range
- `getHeight()` - Get tree height
- `getSize()` - Count total nodes
- `rightRotate(y)` - Right rotation for balancing
- `leftRotate(x)` - Left rotation for balancing
- `balanceFactor(node)` - Calculate balance factor
- `insertNode(current, vehicle)` - Recursive insertion with balancing

---

### 4. VehicleTrie
**Type:** Prefix Tree (Trie)  
**Purpose:** Fast vehicle registration number prefix search and autocomplete

**Key Characteristics:**
- 36-child nodes per node (A-Z + 0-9 + special)
- Supports prefix search for autocomplete functionality
- Case-insensitive operations (converts to uppercase)
- Supports insert, search, and delete operations
- Collects all words with given prefix

**How it differs from similar structures:**
- **vs VehicleHashTable:** Specialized for prefix matching; hash table only supports exact match
- **vs VehicleAVLTree:** String-based prefix search vs numeric/alphabetic sorting; different use case
- **vs Linear Search:** O(m) where m = prefix length, vs O(n) for scanning all entries
- **vs Binary Search:** Can find all prefixes efficiently; binary search only finds exact or range

**Key Methods:**
- `insert(vehicleNo)` - Insert vehicle registration number
- `search(vehicleNo)` - Exact match search
- `searchPrefix(prefix)` - Find all registrations starting with prefix
- `delete(vehicleNo)` - Remove registration from trie
- `collectAllWords(node, results)` - Recursive collection of all words from node

---

### 5. VehicleLinkedList
**Type:** Singly Linked List  
**Purpose:** Sequential vehicle storage with simple traversal

**Key Characteristics:**
- Forward-only traversal
- O(1) insertion at head, O(n) at tail
- O(n) search, O(1) deletion at head, O(n) at tail
- Maintains insertion order
- Supports reverse operation

**How it differs from similar structures:**
- **vs VehicleDoublyLinkedList:** No backward traversal; simpler, less memory; no prev pointer
- **vs DynamicArray:** No random access by index; dynamic memory allocation; easier insertion/deletion
- **vs VehicleStack:** No LIFO constraint; can access any element; FIFO or any order
- **vs ParkingQueue:** Not limited to FIFO; can insert/delete anywhere

**Key Methods:**
- `addVehicle(vehicle)` - Add vehicle with confirmation message
- `addVehicleSilent(vehicle)` - Add vehicle without message
- `displayVehicle()` - Print all vehicles
- `searchVehicle(vehicleNo)` - Linear search by registration number
- `deleteVehicle(vehicleNo)` - Remove vehicle by registration number
- `updateOwnerName(vehicleNo, newOwner)` - Update owner information
- `reverse()` - Reverse the linked list
- `toArray()` - Convert to array
- `getSize()` / `geSize()` - Get list size

---

### 6. VehicleDoublyLinkedList
**Type:** Doubly Linked List  
**Purpose:** Bidirectional vehicle traversal and efficient deletion

**Key Characteristics:**
- Forward and backward traversal
- O(1) insertion/deletion at both ends (head/tail)
- Each node has prev and next pointers
- Maintains head and tail references
- Supports reverse operation

**How it differs from similar structures:**
- **vs VehicleLinkedList:** Can traverse backward; more memory for prev pointers; O(1) deletion at tail
- **vs DynamicArray:** No random access; better for frequent insertions/deletions; bidirectional traversal
- **vs CircularVehicleQueue:** Not circular; no fixed capacity; bidirectional vs unidirectional
- **vs VehicleStack:** Not LIFO constrained; can access middle elements

**Key Methods:**
- `addVehicle(vehicle)` - Add to end of list
- `displayForward()` - Traverse from head to tail
- `displayBackward()` - Traverse from tail to head
- `searchVehicle(vehicleNo)` - Linear search
- `deleteVehicle(vehicleNo)` - Delete with bidirectional pointer update
- `updateOwnerName(vehicleNo, newOwner)` - Update owner
- `getFirst()` - Get head vehicle
- `getLast()` - Get tail vehicle
- `reverse()` - Reverse the list
- `getSize()` - Get list size

---

### 7. VehicleStack
**Type:** Stack (LIFO - Last In First Out)  
**Purpose:** Track most recent parking operations

**Key Characteristics:**
- LIFO access pattern
- O(1) push and pop operations
- Uses linked list implementation
- Tracks most recently parked vehicles

**How it differs from similar structures:**
- **vs ParkingQueue:** LIFO vs FIFO; stack for undo/recent, queue for waiting list
- **vs VehicleLinkedList:** Access restricted to top only; enforces LIFO discipline
- **vs ParkingMinHeap:** No priority; only recency matters
- **vs CircularVehicleQueue:** No circular buffer; no fixed size; LIFO vs FIFO

**Key Methods:**
- `push(vehicle)` - Add vehicle to top of stack
- `pop()` - Remove and return top vehicle
- `peek()` - View top vehicle without removing
- `isEmpty()` - Check if stack is empty
- `getSize()` - Get stack size
- `displayStack()` - Print all stack elements
- `toArray()` - Convert to array

---

### 8. ParkingQueue
**Type:** Queue (FIFO - First In First Out)  
**Purpose:** Manage vehicles waiting for parking slots

**Key Characteristics:**
- FIFO access pattern
- O(1) enqueue and dequeue operations
- Uses linked list implementation
- Handles waiting vehicles when parking is full

**How it differs from similar structures:**
- **vs VehicleStack:** FIFO vs LIFO; queue for waiting list, stack for recent operations
- **vs CircularVehicleQueue:** No fixed capacity; linked list vs array implementation
- **vs ParkingMinHeap:** No priority ordering; pure FIFO based on arrival time
- **vs VehicleLinkedList:** Restricted to front insertion/rear deletion; enforces FIFO

**Key Methods:**
- `Addvehicle(vehicle)` - Add vehicle to queue rear
- `removeVehicle()` - Remove vehicle from queue front
- `getFront()` - View front vehicle
- `isEmpty()` - Check if queue is empty
- `getSize()` - Get queue size
- `displayQueue()` - Print all vehicles in queue

---

### 9. CircularVehicleQueue
**Type:** Circular Queue (Array-based)  
**Purpose:** Fixed-capacity circular buffer for vehicle management

**Key Characteristics:**
- Fixed capacity array
- Front and rear pointers with modular arithmetic
- O(1) enqueue and dequeue
- Efficient memory reuse
- Automatically overwrites oldest when full (if not checked)

**How it differs from similar structures:**
- **vs ParkingQueue:** Fixed capacity vs dynamic; array vs linked list; circular vs linear
- **vs DynamicArray:** Fixed size; circular indexing; FIFO vs random access
- **vs VehicleStack:** FIFO vs LIFO; circular buffer vs linked list
- **vs ParkingMinHeap:** No priority; FIFO order; fixed capacity

**Key Methods:**
- `enqueue(vehicle)` - Add vehicle to circular queue
- `dequeue()` - Remove vehicle from circular queue
- `peek()` - View front vehicle
- `isEmpty()` - Check if empty
- `isFull()` - Check if full
- `getSize()` - Get current size
- `displayQueue()` - Print all vehicles

---

### 10. ParkingMinHeap
**Type:** Min-Heap (Binary Heap)  
**Purpose:** Priority-based vehicle management (lower numeric value = higher priority)

**Key Characteristics:**
- Array-based complete binary tree
- O(log n) insertion and extraction
- Priority order: VIP(1) > Ambulance(2) > Fire Brigade(3) > Police(4) > Normal(5)
- Min-heap property: parent priority <= children priority
- 1-indexed array for easier parent/child calculation

**How it differs from similar structures:**
- **vs ParkingQueue:** Priority-based vs FIFO; O(log n) vs O(1) operations
- **vs VehicleStack:** Priority vs recency; different ordering criteria
- **vs VehicleAVLTree:** No sorted traversal; only min extraction; simpler implementation
- **vs DynamicArray:** Tree structure vs array; maintains heap property vs arbitrary order

**Key Methods:**
- `insert(vehicle)` - Insert vehicle with priority
- `extractMin()` - Remove and return highest priority vehicle
- `peek()` - View highest priority vehicle
- `siftUp(index)` - Bubble up to maintain heap property
- `siftDown(index)` - Bubble down to maintain heap property
- `getPriorityValue(priority)` - Convert priority string to numeric value
- `displayHeap()` - Print heap contents
- `isEmpty()` - Check if heap is empty
- `getSize()` - Get heap size

**Priority Mapping:**
```
VIP        -> 1 (Highest)
AMBULANCE  -> 2
FIRE BRIGADE -> 3
POLICE     -> 4
NORMAL     -> 5 (Lowest)
```

---

### 11. DynamicArray
**Type:** Dynamic Array (Resizable Array)  
**Purpose:** Generic dynamic array with automatic resizing

**Key Characteristics:**
- Generic type support `<E>`
- Automatic doubling when full
- Automatic halving when 25% full
- O(1) random access by index
- O(n) insertion/deletion at arbitrary index
- Includes quick sort implementation

**How it differs from similar structures:**
- **vs ArrayList:** Custom implementation for learning; same concept but manual resizing
- **vs VehicleLinkedList:** Random access vs sequential; contiguous memory vs linked nodes
- **vs VehicleStack:** Random access vs LIFO; no access restrictions
- **vs ParkingQueue:** Random access vs FIFO; dynamic size vs fixed capacity

**Key Methods:**
- `add(element)` - Add element to end (auto-resize)
- `insertAt(index, element)` - Insert at specific index
- `get(index)` - Get element by index
- `remove(index)` - Remove element by index
- `remove(element)` - Remove first occurrence of element
- `indexOf(element)` - Find index of element
- `contains(element)` - Check if element exists
- `size()` - Get current size
- `isEmpty()` - Check if empty
- `clear()` - Remove all elements
- `toArray()` - Convert to array
- `quickSort(comparator)` - Quick sort implementation
- `resize(newCapacity)` - Internal resize method

---

### 12. VehicleNode / DLLNode / StackNode / HashNode / MinHeapNode / TrieNode / AVLNode
**Type:** Node Classes  
**Purpose:** Building blocks for various data structures

**Node Types:**
- **VehicleNode:** Singly linked list node (vehicle + next pointer)
- **DLLNode:** Doubly linked list node (vehicle + prev + next pointers)
- **StackNode:** Stack node (vehicle + next pointer)
- **HashNode:** Hash table bucket node (vehicle + next pointer for chaining)
- **MinHeapNode:** Heap node (vehicle + priority value)
- **TrieNode:** Trie node (36 children array + isEndOfWord flag + vehiclePrefix)
- **AVLNode:** AVL tree node (vehicle + height + left + right)

---

### 13. ParkingGraph
**Type:** Weighted Graph (Adjacency List)  
**Purpose:** Represent parking lot layout for shortest path calculations

**Key Characteristics:**
- Adjacency list representation
- Supports Dijkstra's shortest path algorithm
- Supports BFS and DFS traversals
- Models parking slots as graph nodes
- Edge weights represent distance between slots

**How it differs from similar structures:**
- **vs Simple Array:** Represents relationships between slots; not just individual storage
- **vs Tree:** Can have cycles; multiple connections per node
- **vs ParkingMinHeap:** Different purpose; graph for pathfinding, heap for priority

**Key Methods:**
- `addEdge(source, destination, weight)` - Add connection between slots
- `dijkstraShortestPath(source)` - Find shortest paths from entrance
- `bfs(start)` - Breadth-first search
- `dfs(start)` - Depth-first search
- `getAdjacencyList(slot)` - Get connections for a slot

---

## Java Files - Detailed Explanation

### Core Application Files

#### 1. Project_of_DSA.java
**Purpose:** Application entry point  
**Responsibilities:**
- Initialize SQLite database
- Initialize DSA structures via DataStructureManager
- Launch login frame on EDT (Event Dispatch Thread)

**Key Methods:**
- `main(String[] args)` - Application entry point

---

#### 2. EnhancedLoginFrame.java
**Purpose:** Modern login interface  
**Responsibilities:**
- Username/password authentication
- Password hashing verification
- Session creation on successful login
- Transition to dashboard on success

**Key Methods:**
- `performLogin()` - Validate credentials and create session
- `setupFrame()` - Initialize UI components

---

#### 3. EnhancedDashboard.java
**Purpose:** Main dashboard with navigation sidebar  
**Responsibilities:**
- Display parking statistics
- Provide navigation to all modules
- Auto-refresh dashboard data
- Manage session state
- Handle logout

**Key Methods:**
- `buildDashboardCard()` - Create statistics cards
- `buildStatCard(title, value, color)` - Create individual stat card
- `buildSidebar()` - Create navigation sidebar
- `buildHeader()` - Create top header bar
- `refreshDashboard()` - Update dashboard with latest data
- `handleMenu(action)` - Navigate to different screens
- `startAutoRefresh()` - Periodic data refresh
- `doLogout()` - End user session

**Statistics Displayed:**
- Total Slots
- Available Slots
- Occupied Slots
- Total Bikes
- Car Zone Occupancy
- Total Vehicles
- Today's Revenue

---

#### 4. VehicleEntryFrame.java
**Purpose:** Vehicle registration and parking entry  
**Responsibilities:**
- Collect vehicle information (number, owner, type, priority)
- Support preferred slot selection via "Allocated Slot" field
- Validate vehicle data and slot availability
- Handle priority-based slot replacement
- Calculate parking fee based on vehicle type and settings
- Insert vehicle into database and all DSA structures
- Update UI and dashboard after parking

**Key Methods:**
- `saveVehicle()` - Main parking logic with priority handling
- `clearFields()` - Reset form fields
- `settingsChanged()` - Update fee preview on type/priority change
- `setupFrame()` - Initialize UI

**Parking Logic:**
1. If slot field empty → auto-allocate based on type and priority
2. If slot specified → validate and attempt assignment
3. If slot occupied by lower priority → relocate or displace
4. If slot occupied by equal/higher priority → reject
5. Update all DSA structures and database
6. Refresh UI

---

#### 5. VehicleExitFrame.java
**Purpose:** Vehicle exit processing and billing  
**Responsibilities:**
- Search for parked vehicle
- Calculate parking duration and fee
- Generate receipt preview
- Process exit and update records
- Free parking slot

**Key Methods:**
- `searchVehicle()` - Find vehicle by registration number
- `processExit()` - Handle vehicle exit and billing
- `refreshFeePreview()` - Update fee based on duration
- `updateReceipt()` - Generate receipt preview
- `printReceipt()` - Output receipt

**Fee Calculation:**
- Uses `ParkingFeeManager.calculateFee()`
- Based on duration and vehicle type rate from settings
- Updates database and DSA structures on exit

---

#### 6. ParkingSlotsFrame.java
**Purpose:** Visual parking slot grid display  
**Responsibilities:**
- Display all parking slots in grid layout
- Show slot status (available, occupied, reserved)
- Display vehicle icons in occupied slots
- Show bike slot capacity
- Provide slot details on click
- Auto-refresh slot visualization

**Key Methods:**
- `renderSlots()` - Draw all parking slots
- `showSlotDetails(slotNumber)` - Display slot information dialog
- `createLegendPanel()` - Create color legend
- `createStatsPanel()` - Create statistics badges
- `startAutoRefresh()` - Periodic grid updates

**Visual Features:**
- Color-coded slots (white=available, purple=occupied, gray=reserved)
- Vehicle icons for occupied slots
- Tooltip with vehicle details
- Bike zone indicators
- Responsive grid layout

---

#### 7. SearchVehicleFrame.java
**Purpose:** Vehicle search with live suggestions  
**Responsibilities:**
- Search by vehicle number or owner name
- Live prefix suggestions using Trie
- Display results in table format
- Support case-insensitive search

**Key Methods:**
- `performSearch()` - Execute search query
- `showLiveSuggestions(prefix)` - Show Trie-based autocomplete
- `createResultsPanel()` - Setup results table

**Search Strategy:**
- Vehicle number: Trie prefix search + HashTable exact lookup
- Owner name: Linear search through all records

---

#### 8. ReportsFrame.java
**Purpose:** Generate and export parking reports  
**Responsibilities:**
- Revenue reports (today, week, month)
- Occupancy reports with bike/car breakdown
- Parking history with filtering
- PDF and CSV export functionality

**Key Methods:**
- `createRevenueReportPanel()` - Revenue summary and table
- `createOccupancyReportPanel()` - Occupancy statistics
- `createHistoryReportPanel()` - Detailed parking history
- `createExportPanel()` - Export buttons
- `exportToPDF()` - Generate PDF report
- `exportToCSV()` - Generate CSV export
- `createRevenueCard()` - Create colored revenue card
- `createOccupancyCard()` - Create colored occupancy card

---

#### 9. HistoryFrame.java
**Purpose:** View and filter parking history  
**Responsibilities:**
- Display all parking records
- Filter by vehicle number, owner, type, status
- Show duration and fees
- Support clearing filters

**Key Methods:**
- `loadHistory()` - Load and filter history records
- `createFilterPanel()` - Setup filter controls
- `createHistoryTable()` - Setup history table

---

#### 10. SettingsFrame.java
**Purpose:** Configure parking system settings  
**Responsibilities:**
- Set total parking capacity
- Configure parking fees for each vehicle type
- Set bike slots per slot capacity
- Toggle bike-in-car-slots setting
- Change user password
- Save settings to database

**Key Methods:**
- `saveSettings()` - Persist settings to database
- `createGeneralSection()` - Setup settings form
- `openChangePasswordDialog()` - Password change dialog

**Settings Managed:**
- Total capacity
- Car/Bike/Van/Bus/SUV/Truck/VIP fees
- Bikes per bike slot
- Allow bikes in car slots

---

### Data Access Layer Files

#### 11. VehicleDAO.java
**Purpose:** Database operations for Vehicle entity  
**Responsibilities:**
- CRUD operations for vehicles
- Parking history management
- Search by vehicle number, owner, type, status
- Revenue calculations (today, week, month)
- Recent activity retrieval

**Key Methods:**
- `insertVehicle(vehicle)` - Add new vehicle
- `searchVehicle(vehicleNo)` - Find by registration number
- `searchByOwner(owner)` - Find by owner name
- `exitVehicle(vehicleNo, exitTime, fee)` - Process vehicle exit
- `insertParkingHistory(vehicle)` - Add to history
- `getParkingHistory(vehicleNo, owner, type, status)` - Filtered history
- `getParkingHistoryWithParked()` - Get currently parked vehicles
- `getRecentActivities(limit)` - Get recent records
- `getTotalVehiclesCount()` - Count all vehicles
- `getTodayRevenue()` - Calculate today's revenue
- `getWeeklyRevenue()` - Calculate week's revenue
- `getMonthlyRevenue()` - Calculate month's revenue
- `updateVehicleSlot(vehicleNo, newSlot)` - Update vehicle slot

---

#### 12. SettingsDAO.java
**Purpose:** Database operations for Settings entity  
**Responsibilities:**
- Load settings from database
- Save settings to database
- Provide default values

**Key Methods:**
- `loadSettings()` - Load settings into SettingsBean
- `saveSettings(settings)` - Persist settings to database

---

#### 13. DBConnection.java
**Purpose:** SQLite database connection management  
**Responsibilities:**
- Establish database connection
- Create database file if not exists
- Return connection instance

**Key Methods:**
- `getConnection()` - Get SQLite connection

---

#### 14. DBInitializer.java
**Purpose:** Database schema initialization and migration  
**Responsibilities:**
- Create database tables if not exist
- Migrate existing tables to new schema
- Insert default data (admin user, default settings)

**Key Methods:**
- `initialize()` - Main initialization entry point
- `migrateVehiclesTable(con)` - Add new columns to vehicles table
- `migrateSettingsTable(con)` - Add new settings columns
- `createLoginTable()` - Create login table
- `createVehiclesTable()` - Create vehicles table
- `createSettingsTable()` - Create settings table

**Tables Created:**
1. `login` - User credentials
2. `vehicles` - Vehicle records and parking history
3. `settings` - Application configuration

---

#### 15. SessionManager.java
**Purpose:** User session management  
**Responsibilities:**
- Track current logged-in user
- Manage login/logout state
- Store login timestamp

**Key Methods:**
- `login(username, password)` - Authenticate user
- `logout()` - Clear session
- `getCurrentUser()` - Get current username
- `isLoggedIn()` - Check login status

---

#### 16. PasswordUtils.java
**Purpose:** Password hashing and verification  
**Responsibilities:**
- Hash passwords using SHA-256
- Verify passwords against hashes

**Key Methods:**
- `hashPassword(password)` - Generate SHA-256 hash
- `verifyPassword(password, hash)` - Compare password with hash

---

### UI/Theme Files

#### 17. Theme.java
**Purpose:** Centralized UI styling system  
**Responsibilities:**
- Define color palette
- Define font families and sizes
- Create reusable UI components
- Custom rounded borders and buttons
- Vehicle icon rendering (PNG-based)

**Key Constants:**
- Colors: PRIMARY, ACCENT, SUCCESS, DANGER, WARNING, BG, CARD, etc.
- Fonts: FONT_TITLE, FONT_SECTION, FONT_LABEL, FONT_BODY, etc.
- Radii: RADIUS_SMALL(10), RADIUS_MEDIUM(14), RADIUS_LARGE(18)

**Key Classes:**
- `RoundedBorder` - Custom rounded border implementation
- `RoundedButton` - Custom rounded button with hover effects
- `VehicleIcon` - PNG-based vehicle icon for parking slots

**Key Methods:**
- `createButton(text)` - Create primary button
- `createAccentButton(text)` - Create accent-colored button
- `createDangerButton(text)` - Create danger button
- `createTextField()` - Create styled text field
- `createReadOnlyField()` - Create read-only text field
- `createComboBox(items)` - Create styled combo box
- `createLabel(text)` - Create styled label
- `createTitle(text)` - Create title label
- `createSectionTitle(text)` - Create section title
- `styleTable(table)` - Apply table styling
- `styleScrollPane(scrollPane)` - Apply scroll pane styling
- `getVehicleIcon(type, width, height)` - Get vehicle PNG icon

---

### Supporting Files

#### 18. ParkingFeeManager.java
**Purpose:** Calculate parking fees and durations  
**Responsibilities:**
- Calculate fee based on duration and vehicle type
- Format duration for display
- Load rates from settings

**Key Methods:**
- `calculateFee(vehicle, settings)` - Calculate total parking fee
- `calculateFeePreview(vehicle, settings)` - Calculate preview fee
- `calculateDurationMinutes(vehicle)` - Get duration in minutes
- `formatDuration(minutes)` - Format duration as HH:MM

---

#### 19. SettingsBean.java
**Purpose:** Settings data model  
**Responsibilities:**
- Store all application settings
- Provide getters/setters for all configuration options
- Calculate fees based on vehicle type and priority

**Key Fields:**
- Parking fees: car, bike, van, bus, SUV, truck, VIP
- Total capacity
- Bike slots per slot
- Allow bikes in car slots flag

**Key Methods:**
- `getRateForType(type)` - Get fee by vehicle type
- `getRateForVehicle(type, priority)` - Get fee by type and priority

---

#### 20. ReportExporter.java
**Purpose:** Export reports to PDF and CSV  
**Responsibilities:**
- Generate PDF revenue reports
- Generate CSV parking history exports

**Key Methods:**
- `exportRevenuePdf(file)` - Export revenue report to PDF
- `exportParkingHistoryCsv(file)` - Export history to CSV

---

#### 21. GreedyAllocator.java
**Purpose:** Alternative slot allocation using graph algorithms  
**Responsibilities:**
- Model parking lot as graph
- Find nearest empty slot using Dijkstra's algorithm
- Provide allocation order via DFS/BFS

**Key Methods:**
- `allocateBestSlot(vehicle)` - Find optimal slot using shortest path
- `findAllocateOrder()` - Get DFS allocation order
- `bfsAllocationOrder()` - Get BFS allocation order
- `getShortestDistancesFromEntrance()` - Get all distances from entrance

---

#### 22. AVLNode.java
**Purpose:** Node class for AVL tree data structure  
**Responsibilities:**
- Store vehicle reference
- Maintain left and right child pointers
- Track node height for balancing

**Key Fields:**
- `Vehicle vehicle` - Stored vehicle
- `AVLNode left` - Left child
- `AVLNode right` - Right child
- `int height` - Node height for balance factor

**Key Methods:**
- `AVLNode(Vehicle vehicle)` - Constructor initializing node

---

#### 23. CircularVehicleQueue.java
**Purpose:** Fixed-capacity circular queue for vehicle management  
**Responsibilities:**
- FIFO operations with circular array
- Efficient memory reuse via modular indexing
- Overwrite protection with full/empty checks

**Key Fields:**
- `Vehicle[] queue` - Array of vehicles
- `int front` - Front index
- `int rear` - Rear index
- `int size` - Current element count
- `int capacity` - Maximum capacity

**Key Methods:**
- `enqueue(Vehicle vehicle)` - Add vehicle to queue
- `dequeue()` - Remove and return front vehicle
- `peek()` - View front vehicle without removing
- `isEmpty()` - Check if queue is empty
- `isFull()` - Check if queue is full
- `getSize()` - Get current size
- `displayQueue()` - Print all vehicles

---

#### 24. DataStructureManager.java
**Purpose:** Central hub for initializing and managing all DSA structures  
**Responsibilities:**
- Initialize all data structures with settings from database
- Rebuild structures from database records
- Manage global static instances
- Handle capacity changes

**Key Fields:**
- `static ParkingQueue parkingQueue` - FIFO waiting queue
- `static VehicleStack vehicleStack` - LIFO recent operations
- `static VehicleLinkedList linkedList` - Singly linked list
- `static VehicleDoublyLinkedList doublyLinkedList` - Doubly linked list
- `static VehicleHashTable hashTable` - Hash table lookup
- `static VehicleAVLTree avlTree` - AVL tree sorted structure
- `static CircularVehicleQueue circularQueue` - Circular buffer
- `static ParkingMinHeap minHeap` - Priority queue
- `static VehicleTrie trie` - Prefix search
- `static DynamicArray<Vehicle> dynamicArray` - Dynamic array
- `static ParkingSlotManager slotManager` - Slot allocation
- `static java.util.Queue<Vehicle> waitingQueue` - Waiting queue
- `static int CAPACITY` - Current parking capacity

**Key Methods:**
- `initializeStructures()` - Create and configure all DSA instances
- `refreshFromDB()` - Rebuild structures from database
- `rebuildDSAStructures()` - Alias for refreshFromDB
- `reinitializeSlots(int newCapacity)` - Update capacity and rebuild

---

#### 25. DLLNode.java
**Purpose:** Node class for doubly linked list  
**Responsibilities:**
- Store vehicle reference
- Maintain bidirectional links (prev and next)

**Key Fields:**
- `Vehicle vehicle` - Stored vehicle
- `DLLNode prev` - Previous node
- `DLLNode next` - Next node

**Key Methods:**
- `DLLNode(Vehicle vehicle)` - Constructor initializing node

---

#### 26. DynamicArray.java
**Purpose:** Generic dynamic array with automatic resizing  
**Responsibilities:**
- Provide array-like storage with dynamic capacity
- Auto-resize when full or sparsely populated
- Support sorting via quick sort

**Key Fields:**
- `E[] data` - Internal array storage
- `int size` - Current number of elements
- `DEFAULT_CAPACITY = 10` - Initial array size

**Key Methods:**
- `add(E element)` - Add element to end (auto-resize)
- `insertAt(int index, E element)` - Insert at specific index
- `get(int index)` - Get element by index
- `remove(int index)` - Remove element at index
- `remove(E element)` - Remove first occurrence
- `indexOf(E element)` - Find index of element
- `contains(E element)` - Check if element exists
- `size()` - Get current size
- `isEmpty()` - Check if empty
- `clear()` - Remove all elements
- `toArray()` - Convert to array
- `quickSort(Comparator<E> comparator)` - Quick sort implementation
- `resize(int newCapacity)` - Internal resize method

---

#### 27. HashNode.java
**Purpose:** Node class for hash table separate chaining  
**Responsibilities:**
- Store vehicle reference
- Link to next node in collision chain

**Key Fields:**
- `Vehicle vehicle` - Stored vehicle
- `HashNode next` - Next node in chain

**Key Methods:**
- `HashNode(Vehicle vehicle)` - Constructor initializing node

---

#### 28. MinHeapNode.java
**Purpose:** Node class for min-heap priority queue  
**Responsibilities:**
- Store vehicle reference
- Store numeric priority value for heap ordering

**Key Fields:**
- `Vehicle vehicle` - Stored vehicle
- `int priority` - Numeric priority value

**Key Methods:**
- `MinHeapNode(Vehicle vehicle, int priority)` - Constructor initializing node

---

#### 29. ParkingGraph.java
**Purpose:** Weighted graph representing parking lot layout  
**Responsibilities:**
- Model parking slots as graph vertices
- Store distances between slots as edge weights
- Find shortest paths using Dijkstra's algorithm
- Support BFS, DFS, MST, and path enumeration

**Key Inner Classes:**
- `Edge` - Represents connection between two vertices with weight

**Key Fields:**
- `int vertices` - Number of slots
- `List<List<Edge>> adjacencyList` - Graph edges
- `Map<Integer, String> slotLabels` - Slot name mapping

**Key Methods:**
- `addEdge(int source, int destination, int weight)` - Add bidirectional edge
- `setSlotLabel(int slotNumber, String label)` - Name a slot
- `getSlotLabel(int slotNumber)` - Get slot name
- `getVertices()` - Get vertex count
- `getAdjacencyList(int vertex)` - Get edges for vertex
- `bfs(int startVertex)` - Breadth-first traversal
- `dfs(int startVertex)` - Depth-first traversal
- `dijkstraShortestPath(int startVertex)` - Shortest distances from source
- `getAllPaths(int source, int destination)` - Enumerate all paths
- `isConnected()` - Check if graph is fully connected
- `getMinimumSpanningTreeWeight()` - Calculate MST weight

---

#### 30. ParkingMinHeap.java
**Purpose:** Min-heap priority queue for vehicle priority management  
**Responsibilities:**
- Maintain vehicles in priority order (lower value = higher priority)
- O(log n) insertion and extraction
- Support priority mapping for vehicle types

**Priority Mapping:**
- VIP → 1, AMBULANCE → 2, FIRE BRIGADE → 3, POLICE → 4, NORMAL → 5

**Key Fields:**
- `MinHeapNode[] heap` - 1-indexed array for heap
- `int size` - Current number of elements
- `int capacity` - Maximum capacity

**Key Methods:**
- `insert(Vehicle vehicle)` - Insert vehicle by priority
- `extractMin()` - Remove and return highest priority vehicle
- `peek()` - View highest priority without removing
- `siftUp(int index)` - Bubble up to maintain heap property
- `siftDown(int index)` - Bubble down to maintain heap property
- `getPriorityValue(String priority)` - Convert priority string to numeric
- `displayHeap()` - Print heap contents
- `isEmpty()` - Check if heap is empty
- `getSize()` - Get current size

---

#### 31. ParkingQueue.java
**Purpose:** FIFO queue for managing waiting vehicles  
**Responsibilities:**
- Enqueue vehicles at rear
- Dequeue vehicles from front
- Track queue size

**Key Inner Classes:**
- `QueueNode` - Node storing vehicle and next pointer

**Key Fields:**
- `QueueNode front` - Front of queue
- `QueueNode rear` - Rear of queue
- `int Size` - Current queue size

**Key Methods:**
- `Addvehicle(Vehicle vehicle)` - Add vehicle to queue rear
- `removeVehicle()` - Remove vehicle from queue front
- `getFront()` - View front vehicle
- `isEmpty()` - Check if queue is empty
- `getSize()` - Get current size
- `displayQueue()` - Print all vehicles

---

#### 32. ParkingSlotManager.java
**Purpose:** Core parking slot allocation and management system  
**Responsibilities:**
- Zone-based slot allocation (bike zone and car zone)
- Priority-based slot replacement and relocation
- Track bike counts and slot occupancy
- Reserve and release slots

**Key Fields:**
- `List<Vehicle>[] slotVehicles` - Array of lists representing slots
- `int totalSlots` - Total parking capacity
- `int bikeZoneEnd` - Index marking end of bike zone
- `int bikesPerSlot` - Maximum bikes per bike slot
- `boolean allowBikeInCarSlots` - Configuration flag
- `Map<Integer, String> reservedFor` - Slot reservations

**Key Inner Classes:**
- `AllocationResult` - Encapsulates allocation outcome

**Key Methods:**
- `isBikeSlot(int slotNumber)` - Check if slot is in bike zone
- `isCarSlot(int slotNumber)` - Check if slot is in car zone
- `canParkBikeInSlot(int slotNumber)` - Validate bike parking permission
- `canParkCarInSlot(int slotNumber)` - Validate car parking permission
- `isBikeSlotFull(int slotNumber)` - Check bike slot capacity
- `getBikeCount(int slotNumber)` - Count bikes in slot
- `allocateSlot(String priority, String vehicleType)` - Auto-allocate slot
- `tryAssignSlot(int requestedSlot, Vehicle incoming)` - Attempt preferred slot
- `findNearestEmptySlot(Vehicle vehicle)` - Find closest available slot
- `relocateVehicle(Vehicle vehicle, int newSlot)` - Move vehicle to new slot
- `occupySlot(int slotNumber, Vehicle vehicle)` - Mark slot occupied
- `releaseSlot(int slotNumber)` - Free a slot
- `reserveSlot(int slotNumber, String reservedFor)` - Reserve a slot
- `addVehicleToSlot(int slotNumber, Vehicle vehicle)` - Add vehicle to slot
- `removeVehicleFromSlot(int slotNumber, Vehicle vehicle)` - Remove vehicle from slot
- `comparePriority(String p1, String p2)` - Compare priority levels
- `getOccupiedSlots()` - Count occupied slots
- `getOccupiedBikeSlots()` - Count occupied bike slots
- `getOccupiedCarSlots()` - Count occupied car slots
- `getTotalBikeCount()` - Total bikes across all slots
- `getOccupancyPercentage()` - Overall occupancy rate
- `getSlotStatuses()` - Get all slot occupancy states
- `getReservedStatuses()` - Get reservation info
- `displaySlots()` - Console display of all slots

---

#### 33. StackNode.java
**Purpose:** Node class for stack  
**Responsibilities:**
- Store vehicle reference
- Link to next node in stack

**Key Fields:**
- `Vehicle vehicle` - Stored vehicle
- `StackNode next` - Next node

**Key Methods:**
- `StackNode(Vehicle vehicle)` - Constructor initializing node

---

#### 34. TrieNode.java
**Purpose:** Node class for trie data structure  
**Responsibilities:**
- Store 36 children (A-Z, 0-9, special)
- Mark end of word
- Store complete vehicle prefix at leaf

**Key Fields:**
- `TrieNode[] children` - Array of 36 child nodes
- `boolean isEndOfWord` - End of registration flag
- `String vehiclePrefix` - Complete vehicle number at leaf

**Key Methods:**
- `TrieNode()` - Constructor initializing children array

---

#### 35. Vehicle.java
**Purpose:** Data model representing a parked or exited vehicle  
**Responsibilities:**
- Store vehicle attributes and parking state
- Provide getters and setters for all properties

**Key Fields:**
- `String vehicleNo` - Registration number
- `String ownerName` - Owner name
- `String vehicleType` - Vehicle type (Car, Bike, etc.)
- `String priority` - Priority level (VIP, NORMAL, etc.)
- `int slotNumber` - Assigned slot number (-1 if not parked)
- `LocalDateTime entryTime` - Entry timestamp
- `LocalDateTime exitTime` - Exit timestamp
- `double parkingFee` - Calculated fee
- `boolean parked` - Current parking status

**Key Methods:**
- `Vehicle(String vehicleNo, String ownerName, String vehicleType)` - Constructor
- Getters and setters for all fields
- `toString()` - String representation

---

#### 36. VehicleAVLTree.java
**Purpose:** Self-balancing BST for sorted vehicle operations  
**Responsibilities:**
- Maintain sorted order by registration number
- Guarantee O(log n) insert and search
- Support multiple traversals and range search

**Key Fields:**
- `AVLNode root` - Tree root

**Key Methods:**
- `insert(Vehicle vehicle)` - Insert while maintaining balance
- `search(String vehicleNo)` - Search by registration number
- `inOrder()` - In-order traversal (sorted)
- `preOrder()` - Pre-order traversal
- `postOrder()` - Post-order traversal
- `reverse()` - Reverse the tree
- `rangeSearch(String min, String max)` - Find vehicles in range
- `getHeight()` - Get tree height
- `getSize()` - Count total nodes
- `rightRotate(AVLNode y)` - Right rotation for balancing
- `leftRotate(AVLNode x)` - Left rotation for balancing
- `balanceFactor(AVLNode node)` - Calculate balance factor

---

#### 37. VehicleDoublyLinkedList.java
**Purpose:** Doubly linked list for bidirectional vehicle traversal  
**Responsibilities:**
- Forward and backward traversal
- O(1) insertion/deletion at both ends
- Efficient middle deletion with bidirectional pointers

**Key Fields:**
- `DLLNode head` - First node
- `DLLNode tail` - Last node
- `int size` - Number of vehicles

**Key Methods:**
- `addVehicle(Vehicle vehicle)` - Add to end
- `displayForward()` - Traverse head to tail
- `displayBackward()` - Traverse tail to head
- `searchVehicle(String vehicleNo)` - Linear search
- `deleteVehicle(String vehicleNo)` - Delete with pointer updates
- `updateOwnerName(String vehicleNo, String newOwner)` - Update owner
- `getFirst()` - Get head vehicle
- `getLast()` - Get tail vehicle
- `reverse()` - Reverse the list
- `getSize()` - Get list size

---

#### 38. VehicleHashTable.java
**Purpose:** Hash table for O(1) average vehicle lookup  
**Responsibilities:**
- Insert, search, and delete by registration number
- Handle collisions via separate chaining
- Case-insensitive vehicle number matching

**Key Fields:**
- `HashNode[] table` - Bucket array
- `int tableSize` - Number of buckets

**Key Methods:**
- `insertVehicle(Vehicle vehicle)` - Add vehicle to table
- `searchVehicle(String vehicleNO)` - Find by registration number
- `deleteVehicle(String vehicleNO)` - Remove by registration number
- `displayTable()` - Print all buckets

---

#### 39. VehicleLinkedList.java
**Purpose:** Singly linked list for sequential vehicle storage  
**Responsibilities:**
- Maintain insertion order
- Forward-only traversal
- Support reverse operation

**Key Fields:**
- `VehicleNode head` - First node
- `int size` - Number of vehicles

**Key Methods:**
- `addVehicle(Vehicle vehicle)` - Add with confirmation
- `addVehicleSilent(Vehicle vehicle)` - Add without message
- `displayVehicle()` - Print all vehicles
- `searchVehicle(String vehicleNo)` - Linear search
- `deleteVehicle(String vehicleNo)` - Remove by registration number
- `updateOwnerName(String vehicleNo, String newOwner)` - Update owner
- `reverse()` - Reverse the list
- `toArray()` - Convert to array
- `geSize()` / `getSize()` - Get list size

---

#### 40. VehicleNode.java
**Purpose:** Node class for singly linked list  
**Responsibilities:**
- Store vehicle reference
- Link to next node

**Key Fields:**
- `Vehicle vehicle` - Stored vehicle
- `VehicleNode next` - Next node

**Key Methods:**
- `VehicleNode(Vehicle vehicle)` - Constructor initializing node

---

#### 41. VehicleStack.java
**Purpose:** LIFO stack for tracking recent parking operations  
**Responsibilities:**
- Push vehicles onto stack
- Pop most recently added vehicle
- Peek at top without removing

**Key Fields:**
- `StackNode head` - Top of stack
- `StackNode tail` - Bottom of stack
- `int size` - Number of elements

**Key Methods:**
- `push(Vehicle vehicle)` - Add to top of stack
- `pop()` - Remove and return top vehicle
- `peek()` - View top vehicle without removing
- `isEmpty()` - Check if stack is empty
- `getSize()` - Get stack size
- `displayStack()` - Print all stack elements
- `toArray()` - Convert to array

---

#### 42. VehicleTrie.java
**Purpose:** Prefix tree for fast vehicle registration search and autocomplete  
**Responsibilities:**
- Insert, search, and delete registration numbers
- Provide prefix-based autocomplete suggestions
- Support case-insensitive operations

**Key Fields:**
- `TrieNode root` - Root of trie

**Key Methods:**
- `insert(String vehicleNo)` - Insert registration number
- `search(String vehicleNo)` - Exact match search
- `searchPrefix(String prefix)` - Find all registrations starting with prefix
- `delete(String vehicleNo)` - Remove registration from trie
- `collectAllWords(TrieNode node, List<String> results)` - Recursive word collection
- `hasNoChildren(TrieNode node)` - Check if node has no children

---

## Database Schema

### Table: login
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary key |
| username | TEXT | Username |
| password_hash | TEXT | SHA-256 hashed password |
| role | TEXT | User role |

### Table: vehicles
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary key (auto-increment) |
| vehicle_no | TEXT | Vehicle registration number |
| owner_name | TEXT | Owner name |
| vehicle_type | TEXT | Type (Car, Bike, Van, Bus, SUV, Truck) |
| slot_number | INTEGER | Assigned slot (-1 if not parked) |
| entry_time | TEXT | ISO format entry timestamp |
| exit_time | TEXT | ISO format exit timestamp |
| parking_fee | REAL | Calculated parking fee |
| parked | INTEGER | 1 if currently parked, 0 if exited |
| priority | TEXT | Priority level (VIP, AMBULANCE, etc.) |
| created_at | TEXT | Record creation timestamp |

### Table: settings
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary key (always 1) |
| parking_fee_car | REAL | Car parking fee per hour |
| parking_fee_bike | REAL | Bike parking fee per hour |
| parking_fee_van | REAL | Van parking fee per hour |
| parking_fee_bus | REAL | Bus parking fee per hour |
| parking_fee_suv | REAL | SUV parking fee per hour |
| parking_fee_truck | REAL | Truck parking fee per hour |
| parking_fee_vip | REAL | VIP vehicle fee per hour |
| total_capacity | INTEGER | Total parking slots |
| bike_slots_per_slot | INTEGER | Bike capacity per bike slot |
| allow_bike_in_car_slots | INTEGER | 1=yes, 0=no |
| hourly_rate | REAL | Legacy hourly rate |
| theme | TEXT | UI theme |
| long_parking_threshold_minutes | INTEGER | Threshold for long parking |
| updated_at | TEXT | Last update timestamp |

---

## Key Methods and Interfaces

### No Formal Interfaces
This project does not use formal Java interfaces. All data structures are implemented as concrete classes with public methods.

### Key Method Categories

#### Parking Operations
- `allocateSlot(priority, vehicleType)` - Auto-allocate parking slot
- `tryAssignSlot(requestedSlot, incoming)` - Attempt preferred slot
- `relocateVehicle(vehicle, newSlot)` - Move vehicle to new slot
- `exitVehicle(vehicleNo, exitTime, fee)` - Process vehicle exit
- `occupySlot(slotNumber, vehicle)` - Mark slot occupied
- `releaseSlot(slotNumber)` - Free parking slot

#### Data Structure Operations
- `insert(vehicle)` - Add to data structure
- `search(key)` - Find by key
- `delete(key)` - Remove by key
- `extractMin()` - Get highest priority from heap
- `push(vehicle)` - Add to stack
- `pop()` - Remove from stack
- `enqueue(vehicle)` - Add to queue
- `dequeue()` - Remove from queue
- `add(element)` - Add to dynamic array

#### Database Operations
- `getConnection()` - Get database connection
- `loadSettings()` - Load settings from DB
- `saveSettings(settings)` - Save settings to DB
- `insertVehicle(vehicle)` - Insert vehicle record
- `getAllVehicles()` - Retrieve all vehicles
- `getRecentActivities(limit)` - Get recent records
- `getTodayRevenue()` - Calculate today's revenue

#### UI Operations
- `setupFrame()` - Initialize UI components
- `refreshDashboard()` - Update dashboard display
- `renderSlots()` - Redraw parking grid
- `performSearch()` - Execute search query
- `exportToPDF()` - Generate PDF report
- `exportToCSV()` - Generate CSV export

---

## Project Workflow

### 1. Application Startup
```
main() → DBInitializer.initialize() → DataStructureManager() → EnhancedLoginFrame
```

### 2. User Login
```
Enter credentials → SessionManager.login() → Hash verification → EnhancedDashboard
```

### 3. Vehicle Entry
```
Fill form → Validate → Check duplicate → 
If preferred slot: tryAssignSlot() → Handle priority/replacement
If auto: allocateSlot() → 
Insert to DB + all DSA structures → Refresh UI
```

### 4. Vehicle Exit
```
Search vehicle → Calculate fee → 
Confirm exit → Update DB → 
Free slot → Refresh all structures
```

### 5. Priority-Based Replacement
```
Higher priority vehicle requests occupied slot →
Compare priorities →
If higher: relocate lower priority vehicle or remove if full →
Assign slot to higher priority vehicle
```

### 6. Data Structure Synchronization
```
After every parking operation:
1. Update ParkingSlotManager
2. Insert into ParkingMinHeap (priority)
3. Insert into VehicleHashTable (O(1) lookup)
4. Insert into VehicleAVLTree (sorted)
5. Insert into VehicleTrie (prefix search)
6. Add to VehicleLinkedList (sequential)
7. Add to VehicleDoublyLinkedList (bidirectional)
8. Push to VehicleStack (recent)
9. Enqueue to ParkingQueue (waiting)
10. Enqueue to CircularVehicleQueue (circular)
11. Add to DynamicArray (dynamic array)
```

---

## Data Structure Usage Summary

| Data Structure | Used For | Time Complexity (Avg) | Key Advantage |
|---------------|----------|----------------------|---------------|
| ParkingSlotManager | Slot allocation | O(1) direct access | Zone-based allocation with priority |
| VehicleHashTable | Vehicle lookup | O(1) | Fast exact match search |
| VehicleAVLTree | Sorted operations | O(log n) | Balanced tree with traversals |
| VehicleTrie | Prefix search | O(m) | Autocomplete functionality |
| VehicleLinkedList | Sequential storage | O(n) search | Simple, memory efficient |
| VehicleDoublyLinkedList | Bidirectional access | O(n) search | Forward/backward traversal |
| VehicleStack | Recent operations | O(1) push/pop | LIFO for recent vehicles |
| ParkingQueue | Waiting list | O(1) enqueue/dequeue | FIFO for waiting vehicles |
| CircularVehicleQueue | Fixed buffer | O(1) | Memory efficient circular buffer |
| ParkingMinHeap | Priority management | O(log n) insert/extract | Priority-based extraction |
| DynamicArray | Generic storage | O(1) random access | Dynamic resizing |
| ParkingGraph | Pathfinding | O(V+E) | Shortest path for slot allocation |

---

## Conclusion

This Smart Parking Management System demonstrates the practical application of various Data Structures and Algorithms in a real-world scenario. Each data structure is chosen for its specific strengths:

- **HashTable** for fast lookups
- **AVLTree** for sorted operations
- **Trie** for prefix searches
- **Linked Lists** for sequential data
- **Stack/Queue** for specific access patterns
- **MinHeap** for priority management
- **DynamicArray** for flexible arrays
- **Graph** for pathfinding

The modular architecture ensures that each component is independent yet integrated through the central `DataStructureManager`, making the system maintainable, extensible, and educational.
