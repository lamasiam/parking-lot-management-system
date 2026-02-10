\# Development Guide



\## Current Architecture Overview



\### Parking Structure (✅ Completed)

```

ParkingLot

├── Contains multiple Floors

│   └── Each Floor contains multiple ParkingSpots

│       ├── CompactSpot (RM 2/hour)

│       ├── RegularSpot (RM 5/hour)

│       ├── HandicappedSpot (RM 2/hour)

│       └── ReservedSpot (RM 10/hour)

```



\### Key Classes Implemented



\#### `ParkingLot.java`

\- Manages the entire parking facility

\- Methods:

&nbsp; - `addFloor(Floor floor)` - Add a new floor

&nbsp; - `getAvailableSpots()` - Get all available spots

&nbsp; - `findSpot(String spotId)` - Find specific spot

&nbsp; - `getOccupancyRate()` - Calculate occupancy percentage



\#### `Floor.java`

\- Represents a single floor

\- Methods:

&nbsp; - `addSpot(ParkingSpot spot)` - Add parking spot

&nbsp; - `getSpotsByType(String type)` - Filter spots by type

&nbsp; - `getAvailableCount()` - Count available spots



\#### `ParkingSpot.java` (Abstract)

\- Base class for all spot types

\- Subclasses: CompactSpot, RegularSpot, HandicappedSpot, ReservedSpot

\- Methods:

&nbsp; - `parkVehicle(Vehicle v)` - Occupy the spot

&nbsp; - `removeVehicle()` - Free the spot

&nbsp; - `getHourlyRate()` - Get pricing



\### How to Run Tests

```bash

\# From project root

cd src

javac -d ../bin models/vehicle/VehicleType.java models/vehicle/Vehicle.java models/parking/\*.java TestMember1.java

cd ../bin

java TestMember1

```



\## Next Steps for Team Members



\### For Vehicle Module Developer

1\. Review `models/vehicle/Vehicle.java` (base class exists)

2\. Create subclasses: Motorcycle, Car, SUV, HandicappedVehicle

3\. Implement `canParkIn(String spotType)` logic for each

4\. Add VehicleType enum if needed



\*\*Example:\*\*

```java

public class Motorcycle extends Vehicle {

&nbsp;   public boolean canParkIn(String spotType) {

&nbsp;       return spotType.equals("COMPACT");

&nbsp;   }

}

```



\### For Entry/Exit Module Developer

1\. Use `ParkingLot.getAvailableSpots()` to show options

2\. Use `ParkingSpot.parkVehicle(vehicle)` to assign

3\. Track entry time in Vehicle object

4\. Calculate duration on exit



\### For Payment Module Developer

1\. Calculate fees using `spot.getHourlyRate() \* hours`

2\. Implement Fine interface/strategy pattern

3\. Link fines to license plate numbers



\### For GUI Module Developer

1\. Display floors using `parkingLot.getFloors()`

2\. Show spots using `floor.getSpots()`

3\. Use color coding: Green (available), Red (occupied)

4\. Bind buttons to parking/exit logic



\## Code Standards



1\. \*\*Package Structure:\*\*

&nbsp;  - `models/parking/` - Parking structure classes

&nbsp;  - `models/vehicle/` - Vehicle classes

&nbsp;  - `models/payment/` - Payment and fine classes

&nbsp;  - `services/` - Business logic

&nbsp;  - `views/` - GUI components



2\. \*\*Naming Conventions:\*\*

&nbsp;  - Classes: PascalCase

&nbsp;  - Methods: camelCase

&nbsp;  - Constants: UPPER\_SNAKE\_CASE

&nbsp;  - Packages: lowercase



3\. \*\*Testing:\*\*

&nbsp;  - Create TestMemberX.java for your module

&nbsp;  - Follow the pattern in TestMember1.java

&nbsp;  - Test all edge cases



4\. \*\*Git Workflow:\*\*

```bash

&nbsp;  git pull origin main        # Before starting work

&nbsp;  # ... make changes ...

&nbsp;  git add .

&nbsp;  git commit -m "feat: description"

&nbsp;  git push origin main

```



\## Integration Points



\### When implementing other modules, integrate with parking structure:



\*\*Entry Service:\*\*

```java

// Get available spots for a vehicle type

List<ParkingSpot> spots = parkingLot.getAvailableSpotsByType("REGULAR");



// Park vehicle

spot.parkVehicle(vehicle);

vehicle.setEntryTime(LocalDateTime.now());

```



\*\*Exit Service:\*\*

```java

// Find vehicle's spot

ParkingSpot spot = parkingLot.findSpotByVehicle(licensePlate);



// Calculate fee

long hours = calculateHours(vehicle.getEntryTime(), LocalDateTime.now());

double fee = spot.getHourlyRate() \* hours;



// Release spot

spot.removeVehicle();

```



\*\*GUI Display:\*\*

```java

// Show occupancy

String status = String.format("%.1f%% occupied", 

&nbsp;   parkingLot.getOccupancyRate());



// List available spots

for (ParkingSpot spot : parkingLot.getAvailableSpots()) {

&nbsp;   System.out.println(spot.getSpotId() + " - " + spot.getType());

}

```



\## Questions?



If you need help understanding any part of the code:

1\. Read the JavaDoc comments

2\. Run the tests to see examples

3\. Ask in team chat

4\. Review this guide



\*\*Good luck with your modules!\*\* 🚀

