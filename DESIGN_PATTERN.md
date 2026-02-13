# Design Patterns Documentation
## Parking Lot Management System

**Course:** CCP6224 - Object-Oriented Analysis and Design  
**Group:** Group 4 (TT6L)  
**Date:** February 2026

---

## Table of Contents
1. [Strategy Pattern for Fine Calculation](#strategy-pattern-for-fine-calculation)
2. [Factory Pattern for Vehicle Creation](#factory-pattern-for-vehicle-creation)
3. [Singleton Pattern for Database Management](#singleton-pattern-for-database-management)
4. [Benefits and Future Extensibility](#benefits-and-future-extensibility)

---

## 1. Strategy Pattern for Fine Calculation

### Why Strategy Pattern Was Chosen

The Strategy Pattern was selected for the fine calculation system because:

1. **Multiple Algorithms Required**: The assignment specifies three distinct fine calculation schemes:
   - **Fixed Fine**: RM 50 flat penalty
   - **Progressive Fine**: Tiered penalties (RM 50 → RM 150 → RM 300 → RM 500)
   - **Hourly Fine**: RM 20 per hour overstay

2. **Runtime Flexibility**: The admin needs to switch between schemes dynamically without system restart

3. **Open/Closed Principle**: New fine schemes should be addable without modifying existing code

4. **Separation of Concerns**: Fine calculation logic should be independent of the exit/billing workflow

### Pattern Structure

```
┌─────────────────────┐
│  FineCalculator     │
│  (Context)          │
│ ─────────────────── │
│ - strategy          │◄───┐
│ + setStrategy()     │    │ uses
│ + calculate()       │    │
└─────────────────────┘    │
                           │
              ┌────────────┴────────────┐
              │                         │
       ┌──────▼──────────┐              │
       │  <<interface>>  │              │
       │  FineStrategy   │              │
       │ ─────────────── │              │
       │ + calculateFine()│              │
       │ + getStrategyName()│            │
       └────────┬─────────┘              │
                │                        │
     ┌──────────┼──────────┐            │
     │          │          │            │
┌────▼───┐ ┌───▼────┐ ┌───▼────┐      │
│ Fixed  │ │Progress│ │ Hourly │      │
│ Fine   │ │ive Fine│ │  Fine  │      │
│Strategy│ │Strategy│ │Strategy│      │
└────────┘ └────────┘ └────────┘
```

### Implementation

#### Interface Definition
```java
package models.fine;

/**
 * Strategy Interface for Fine Calculation
 * Enables dynamic algorithm selection at runtime
 */
public interface FineStrategy {
    /**
     * Calculate fine based on overstay duration
     * @param durationHours Total hours parked (may exceed 24)
     * @return Fine amount in RM
     */
    double calculateFine(long durationHours);
    
    /**
     * Get human-readable name for this strategy
     * @return Strategy display name
     */
    String getStrategyName();
}
```

#### Concrete Strategies

**1. Fixed Fine Strategy (Option A)**
```java
public class FixedFineStrategy implements FineStrategy {
    private static final double FIXED_FINE = 50.0;
    
    @Override
    public double calculateFine(long durationHours) {
        return (durationHours > 24) ? FIXED_FINE : 0.0;
    }
    
    @Override
    public String getStrategyName() {
        return "Fixed Fine (RM 50)";
    }
}
```

**2. Progressive Fine Strategy (Option B)**
```java
public class ProgressiveFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;
        
        double fine = 50.0; // First 24 hours
        long overstay = durationHours - 24;
        
        if (overstay <= 24) {
            fine += 100.0; // Hours 24-48
        } else if (overstay <= 48) {
            fine += 100.0 + 150.0; // Hours 24-72
        } else {
            fine += 100.0 + 150.0 + 200.0; // 72+ hours
        }
        
        return fine;
    }
    
    @Override
    public String getStrategyName() {
        return "Progressive Fine (Tiered)";
    }
}
```

**3. Hourly Fine Strategy (Option C)**
```java
public class HourlyFineStrategy implements FineStrategy {
    private static final double RATE_PER_HOUR = 20.0;
    
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;
        
        long overstayHours = durationHours - 24;
        return overstayHours * RATE_PER_HOUR;
    }
    
    @Override
    public String getStrategyName() {
        return "Hourly Fine (RM 20/hr)";
    }
}
```

#### Context Class
```java
public class FineCalculator {
    private FineStrategy currentStrategy;
    
    public FineCalculator() {
        // Default strategy
        this.currentStrategy = new FixedFineStrategy();
    }
    
    /**
     * Change fine calculation algorithm at runtime
     */
    public void setFineStrategy(FineStrategy strategy) {
        this.currentStrategy = strategy;
    }
    
    /**
     * Calculate fine using current strategy
     */
    public double calculateFine(long durationHours) {
        return currentStrategy.calculateFine(durationHours);
    }
    
    public String getCurrentStrategyName() {
        return currentStrategy.getStrategyName();
    }
}
```

### How It Enhances the Design

#### 1. **Eliminates Conditional Complexity**

**WITHOUT Strategy Pattern** (Bad approach):
```java
public double calculateFine(long hours, String schemeType) {
    if (hours <= 24) return 0.0;
    
    // VIOLATES Open/Closed Principle
    if (schemeType.equals("FIXED")) {
        return 50.0;
    } else if (schemeType.equals("PROGRESSIVE")) {
        double fine = 50.0;
        long overstay = hours - 24;
        if (overstay <= 24) fine += 100.0;
        else if (overstay <= 48) fine += 250.0;
        else fine += 450.0;
        return fine;
    } else if (schemeType.equals("HOURLY")) {
        return (hours - 24) * 20.0;
    }
    
    // Adding new scheme requires modifying this method!
    // VIOLATES Single Responsibility Principle
    throw new IllegalArgumentException("Unknown scheme");
}
```

**Problems:**
- ❌ Violates Open/Closed Principle
- ❌ Hard to test (must test all branches)
- ❌ Hard to maintain (one method does everything)
- ❌ Hard to extend (must modify existing code)
- ❌ Tight coupling (ExitController knows all algorithms)

**WITH Strategy Pattern** (Good approach):
```java
// In ExitController
FineCalculator fineCalc = new FineCalculator();
// Admin changes scheme
fineCalc.setFineStrategy(new ProgressiveFineStrategy());
// Calculate fine
double fine = fineCalc.calculateFine(hours);
```

**Benefits:**
- ✅ Follows Open/Closed Principle
- ✅ Easy to test (each strategy tested independently)
- ✅ Easy to maintain (each algorithm in separate class)
- ✅ Easy to extend (add new class, no modification)
- ✅ Loose coupling (ExitController doesn't know algorithms)

#### 2. **Enables Runtime Algorithm Selection**

```java
// Admin Panel - Strategy Selection
JComboBox<String> schemeSelector = new JComboBox<>(new String[]{
    "Fixed Fine (RM 50)",
    "Progressive Fine (Tiered)",
    "Hourly Fine (RM 20/hr)"
});

schemeSelector.addActionListener(e -> {
    String selected = (String) schemeSelector.getSelectedItem();
    FineStrategy strategy;
    
    switch (selected) {
        case "Progressive Fine (Tiered)":
            strategy = new ProgressiveFineStrategy();
            break;
        case "Hourly Fine (RM 20/hr)":
            strategy = new HourlyFineStrategy();
            break;
        default:
            strategy = new FixedFineStrategy();
    }
    
    fineCalculator.setFineStrategy(strategy);
    // Future exits automatically use new strategy!
});
```

#### 3. **Improves Testability**

Each strategy can be tested independently:

```java
@Test
public void testFixedFineStrategy() {
    FineStrategy strategy = new FixedFineStrategy();
    
    assertEquals(0.0, strategy.calculateFine(24));  // No overstay
    assertEquals(50.0, strategy.calculateFine(25)); // 1 hour overstay
    assertEquals(50.0, strategy.calculateFine(48)); // 24 hour overstay
}

@Test
public void testProgressiveFineStrategy() {
    FineStrategy strategy = new ProgressiveFineStrategy();
    
    assertEquals(0.0, strategy.calculateFine(24));   // No fine
    assertEquals(50.0, strategy.calculateFine(25));  // First tier
    assertEquals(150.0, strategy.calculateFine(49)); // Second tier
    assertEquals(300.0, strategy.calculateFine(73)); // Third tier
}
```

### What Would Be Difficult Without the Pattern

#### Problem 1: Adding a New Fine Scheme

**Without Strategy Pattern:**
```java
// Must modify ExitController.calculateFine()
public double calculateFine(long hours, String scheme) {
    if (hours <= 24) return 0.0;
    
    if (scheme.equals("FIXED")) {
        return 50.0;
    } else if (scheme.equals("PROGRESSIVE")) {
        // ... existing code ...
    } else if (scheme.equals("HOURLY")) {
        // ... existing code ...
    } else if (scheme.equals("MAX_CAP")) {  // NEW SCHEME
        // Must modify this method!
        double fine = (hours - 24) * 20.0;
        return Math.min(fine, 500.0); // Cap at RM 500
    }
    
    // Risk of breaking existing schemes
    // Must retest ALL schemes
    // Violates Open/Closed Principle
}
```

**With Strategy Pattern:**
```java
// Just create new class - NO modification to existing code!
public class MaxCapFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;
        
        double fine = (durationHours - 24) * 20.0;
        return Math.min(fine, 500.0); // Cap at RM 500
    }
    
    @Override
    public String getStrategyName() {
        return "Max Cap Fine (RM 500 max)";
    }
}

// Add to dropdown - that's it!
// No risk of breaking existing schemes
// Only need to test NEW scheme
```

#### Problem 2: Testing Becomes a Nightmare

**Without Strategy Pattern:**
- Must test EVERY scheme in EVERY exit scenario
- One bug in one scheme affects all
- Can't test schemes in isolation
- 100+ test cases for 4 schemes

**With Strategy Pattern:**
- Test each scheme independently (25 test cases per scheme)
- Bug in one scheme doesn't affect others
- Can mock strategies for exit testing
- Total: ~30 test cases (much simpler)

#### Problem 3: Code Becomes Unmaintainable

**Without Strategy Pattern:**
- 200+ line calculateFine() method
- Nested if-else statements
- Hard to understand
- Fear of changing anything

**With Strategy Pattern:**
- Each strategy: ~20 lines
- Clean, focused classes
- Easy to understand
- Confident modifications

### How It Supports Future Expansion

#### Example 1: Weekend Discount Scheme
```java
public class WeekendDiscountStrategy implements FineStrategy {
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;
        
        LocalDateTime now = LocalDateTime.now();
        boolean isWeekend = now.getDayOfWeek() == DayOfWeek.SATURDAY ||
                           now.getDayOfWeek() == DayOfWeek.SUNDAY;
        
        double baseFine = (durationHours - 24) * 20.0;
        return isWeekend ? baseFine * 0.5 : baseFine; // 50% discount on weekends
    }
    
    @Override
    public String getStrategyName() {
        return "Weekend Discount (50% off Sat/Sun)";
    }
}
```

**Changes needed:**
1. Create new class (above) ✅
2. Add to dropdown in Admin Panel ✅
3. Write tests for new strategy ✅

**Changes NOT needed:**
- ❌ Modify ExitController
- ❌ Modify BillingCalculator
- ❌ Modify Database schema
- ❌ Modify any existing strategies
- ❌ Retest existing strategies

#### Example 2: Student Discount Scheme
```java
public class StudentDiscountStrategy implements FineStrategy {
    private static final double STUDENT_RATE = 10.0; // RM 10/hour instead of RM 20
    
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;
        
        long overstay = durationHours - 24;
        return overstay * STUDENT_RATE;
    }
    
    @Override
    public String getStrategyName() {
        return "Student Rate (RM 10/hr)";
    }
}
```

#### Example 3: Composite Strategy (Multiple Discounts)
```java
public class CompositeStrategy implements FineStrategy {
    private final FineStrategy baseStrategy;
    private final double discountPercentage;
    
    public CompositeStrategy(FineStrategy baseStrategy, double discount) {
        this.baseStrategy = baseStrategy;
        this.discountPercentage = discount;
    }
    
    @Override
    public double calculateFine(long durationHours) {
        double baseFine = baseStrategy.calculateFine(durationHours);
        return baseFine * (1.0 - discountPercentage);
    }
    
    @Override
    public String getStrategyName() {
        return baseStrategy.getStrategyName() + " with " + 
               (int)(discountPercentage * 100) + "% discount";
    }
}

// Usage:
FineStrategy studentWeekend = new CompositeStrategy(
    new HourlyFineStrategy(), 
    0.30  // 30% discount
);
```

---

## 2. Factory Pattern for Vehicle Creation

### Why Factory Pattern Was Chosen

The Factory Pattern centralizes vehicle object creation because:

1. **Multiple Vehicle Types**: 4 concrete vehicle classes (Motorcycle, Car, SUV, HandicappedVehicle)
2. **Complex Creation Logic**: Different constructors, validation rules per type
3. **Encapsulation**: Hide instantiation details from GUI and controllers
4. **Single Point of Change**: Modify vehicle creation in one place

### Implementation

```java
public class EntryController {
    public Vehicle createVehicle(String licensePlate, VehicleType type, 
                                boolean hasHandicappedCard) {
        // Centralized validation
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be empty");
        }
        
        licensePlate = licensePlate.trim().toUpperCase();
        
        // Check if vehicle already parked
        Vehicle existingVehicle = parkingLot.findVehicleByLicensePlate(licensePlate);
        if (existingVehicle != null) {
            throw new IllegalStateException("Vehicle already parked!");
        }
        
        // Factory Pattern: Create appropriate vehicle type
        Vehicle vehicle;
        switch (type) {
            case MOTORCYCLE:
                vehicle = new Motorcycle(licensePlate);
                break;
            case CAR:
                vehicle = new Car(licensePlate);
                break;
            case SUV:
                vehicle = new SUV(licensePlate);
                break;
            case HANDICAPPED:
                vehicle = new HandicappedVehicle(licensePlate, hasHandicappedCard);
                break;
            default:
                throw new IllegalArgumentException("Invalid vehicle type: " + type);
        }
        
        return vehicle;
    }
}
```

### Benefits

1. **Encapsulation**: GUI doesn't know about constructors
2. **Validation**: All vehicles validated consistently
3. **Easy Extension**: Add new vehicle type = add one case
4. **Single Responsibility**: Vehicle creation separate from parking logic

---

## 3. Singleton Pattern for Database Management

### Implementation

```java
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        // Private constructor prevents external instantiation
        initializeConnection();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initializeConnection() {
        try {
            String url = "jdbc:sqlite:parking_lot.db";
            connection = DriverManager.getConnection(url);
            initializeTables();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}
```

### Why Singleton?

1. **Single Database Connection**: Avoid multiple connection overhead
2. **Global Access Point**: All DAOs use same instance
3. **Resource Management**: Ensure proper connection cleanup
4. **Thread Safety**: Synchronized getInstance()

---

## 4. Benefits and Future Extensibility

### Overall Benefits of Our Design Pattern Choices

| Pattern | Benefit | Example |
|---------|---------|---------|
| **Strategy** | Algorithm flexibility | Switch fine schemes without restart |
| **Factory** | Centralized creation | Add Bus type in one place |
| **Singleton** | Resource efficiency | Single DB connection shared |

### Future Extensibility Examples

#### 1. Adding Electric Vehicle (EV) Support

**New Vehicle Type:**
```java
public class ElectricVehicle extends AbstractVehicle {
    private int batteryPercentage;
    
    public ElectricVehicle(String licensePlate, int battery) {
        super(licensePlate, VehicleType.EV);
        this.batteryPercentage = battery;
    }
    
    public boolean needsCharging() {
        return batteryPercentage < 20;
    }
}
```

**New Spot Type:**
```java
public class EVChargingSpot extends ParkingSpot {
    public EVChargingSpot(int floor, int row, int spot) {
        super(floor, row, spot, SpotType.EV_CHARGING, 8.0); // RM 8/hour
    }
    
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.EV ||
               vehicle.getType() == VehicleType.CAR ||
               vehicle.getType() == VehicleType.SUV;
    }
}
```

**Changes Required:**
1. Add `EV` to VehicleType enum ✅
2. Create ElectricVehicle class ✅
3. Add `EV_CHARGING` to SpotType enum ✅
4. Create EVChargingSpot class ✅
5. Add case in Factory: `case EV: vehicle = new ElectricVehicle(...);` ✅

**Changes NOT Required:**
- Entry/Exit controllers ❌
- Database schema (generic vehicle_type field) ❌
- Payment processing ❌
- Ticket generation ❌
- Fine calculation ❌

#### 2. Adding Reservation System

```java
public class Reservation {
    private String reservationId;
    private String licensePlate;
    private String spotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    // No changes to existing classes!
}

public class ReservationDAO {
    public void createReservation(Reservation reservation) {
        // Store in new table
    }
    
    public boolean isSpotReserved(String spotId, LocalDateTime time) {
        // Check reservations
    }
}

// Modify only ParkingSpot:
public boolean isAvailable() {
    return status == SpotStatus.AVAILABLE && 
           !reservationDAO.isSpotReserved(spotId, LocalDateTime.now());
}
```

**Impact Analysis:**
- ✅ Minimal changes to existing code
- ✅ No modification to Strategy pattern
- ✅ No modification to Factory pattern
- ✅ Add new DAO class
- ✅ Add new GUI panel

---

## Conclusion

Our design pattern choices demonstrate:

1. ✅ **Separation of Concerns**: Each pattern addresses specific responsibility
2. ✅ **Open/Closed Principle**: Open for extension, closed for modification
3. ✅ **Single Responsibility**: Each class has one reason to change
4. ✅ **Dependency Inversion**: Depend on abstractions (interfaces), not concretions
5. ✅ **Testability**: Each component can be tested independently

These patterns make our system:
- **Maintainable**: Easy to understand and modify
- **Extensible**: Easy to add new features
- **Robust**: Changes don't break existing functionality
- **Professional**: Industry-standard design practices

---

**Authors:** Group 4 - LAMA M. R. SIAM, EBA MOHAMED ABBAS AHMED, SITI ZULAIKHA BINTI ABDUL RAZIF, HONG JING JIE  
**Course:** CCP6224 - Object-Oriented Analysis and Design  
**Institution:** Multimedia University  
**Date:** February 2026
