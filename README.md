# 🚗 Parking Lot Management System

**A comprehensive parking facility management system built with Java Swing and SQLite**

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/Database-SQLite-blue.svg)](https://www.sqlite.org/)
[![License](https://img.shields.io/badge/License-Academic-green.svg)]()

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Requirements](#system-requirements)
- [Installation](#installation)
- [Quick Start Guide](#quick-start-guide)
- [User Manual](#user-manual)
- [Technical Documentation](#technical-documentation)
- [Team Members](#team-members)
- [License](#license)

---

## 🎯 Overview

The Parking Lot Management System is a standalone Java Swing application designed for managing multi-level parking facilities. It handles vehicle entry, spot allocation, payment processing, fine management, and administrative reporting with a focus on object-oriented design principles and design patterns.

**Course:** CCP6224 – Object-Oriented Analysis and Design  
**Institution:** Multimedia University  
**Semester:** Trimester 2530  
**Group:** Group 4 (TT6L)

---

## ✨ Features

### Core Functionality

✅ **Multi-Level Parking Structure**
- 5 floors with 18 spots per floor (90 spots total)
- 4 spot types: Compact, Regular, Handicapped, Reserved
- Real-time occupancy tracking

✅ **Vehicle Management**
- Support for 4 vehicle types: Motorcycle, Car, SUV, Handicapped
- Automatic spot compatibility checking
- License plate validation and duplicate prevention

✅ **Smart Entry System**
- Available spot search with filtering
- Visual spot selection interface
- Automatic ticket generation (Format: T-PLATE-TIMESTAMP)

✅ **Complete Exit & Billing Workflow**
- Ceiling rounding duration calculation
- Itemized bill with fee breakdown
- Cash and Card payment support
- Professional receipt generation

✅ **Flexible Fine Management**
- 3 fine calculation schemes (Strategy Pattern):
  - Fixed Fine: RM 50 flat penalty
  - Progressive Fine: Tiered penalties
  - Hourly Fine: RM 20 per hour
- Admin-configurable scheme selection
- Unpaid fine tracking across visits

✅ **Comprehensive Reporting**
- Real-time occupancy statistics
- Revenue tracking (parking + fines)
- Vehicle list with entry times
- Outstanding fines report

### Technical Highlights

🔧 **Design Patterns Implemented:**
- Strategy Pattern (Fine calculation)
- Factory Pattern (Vehicle creation)
- Singleton Pattern (Database management)
- DAO Pattern (Data persistence)

🔧 **Database:**
- SQLite for lightweight persistence
- 6 tables: parking_spots, vehicles, tickets, payments, fines, floors
- ACID-compliant transactions

---

## 💻 System Requirements

### Software Requirements

| Component | Requirement |
|-----------|-------------|
| **Java JDK** | Version 11 or higher |
| **Operating System** | Windows 10/11, macOS 10.14+, Linux (Ubuntu 18.04+) |
| **Display Resolution** | 1280×720 minimum (1920×1080 recommended) |
| **RAM** | 512 MB minimum (1 GB recommended) |
| **Disk Space** | 50 MB for application + database |

### Included Libraries

All required libraries are included in the `lib/` directory:
- `sqlite-jdbc-3.45.0.0.jar` - SQLite database driver
- `slf4j-api-2.0.9.jar` - Logging framework API
- `slf4j-simple-2.0.9.jar` - Simple logging implementation

---

## 📦 Installation

### Method 1: Clone from GitHub (Recommended)

```bash
# Clone the repository
git clone https://github.com/lamasiam/parking-lot-management-system.git

# Navigate to project directory
cd parking-lot-management-system

# Compile the project
cd src
javac -d ../bin -cp "../lib/*" models/**/*.java controllers/*.java database/*.java views/*.java services/*.java Main.java

# Run the application
cd ../bin
java -cp ".;../lib/*" Main
```

**Note for macOS/Linux:** Use colon `:` instead of semicolon `;` in classpath:
```bash
java -cp ".:../lib/*" Main
```

### Method 2: Download Release

1. Download the latest release ZIP from GitHub
2. Extract to desired location
3. Follow compilation steps above

---

## 🚀 Quick Start Guide

### First Time Setup

1. **Launch the Application:**
   ```bash
   cd bin
   java -cp ".;../lib/*" Main  # Windows
   java -cp ".:../lib/*" Main  # macOS/Linux
   ```

2. **System Initialization:**
   - On first run, the system automatically creates:
     - SQLite database (`parking_lot.db`)
     - 5 floors with 90 parking spots
     - Default fine scheme (Fixed Fine)

3. **Verify Setup:**
   - Click on **Admin Panel** tab
   - Confirm: Total Spots = 90, Occupancy = 0%

### Basic Workflow

```
┌─────────────────────────────────────────────────────────┐
│  CUSTOMER ENTERS                                        │
│  1. Go to "Vehicle Entry" tab                          │
│  2. Enter license plate & select vehicle type          │
│  3. Click "Search Available Spots"                     │
│  4. Select desired spot from table                     │
│  5. Click "Park Vehicle"                               │
│  6. Receive & save parking ticket                      │
└─────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│  CUSTOMER EXITS                                         │
│  1. Go to "Vehicle Exit" tab                           │
│  2. Enter license plate                                │
│  3. Click "Search Vehicle"                             │
│  4. Click "Calculate Bill"                             │
│  5. Review charges (parking fee + fines if any)        │
│  6. Select payment method (Cash/Card)                  │
│  7. Click "Process Exit & Payment"                     │
│  8. Receive exit receipt                               │
└─────────────────────────────────────────────────────────┘
```

---

## 📖 User Manual

### 1. Admin Panel

**Purpose:** Monitor parking lot status and configure system settings

#### Viewing Statistics

1. Click **Admin Panel** tab
2. View summary cards:
   - **Total Spots:** 90 (all spots)
   - **Occupied:** Number of vehicles currently parked
   - **Available:** Empty spots ready for parking
   - **Occupancy Rate:** Percentage (Occupied / Total × 100%)

#### Floor Details

1. Select **Floors Overview** tab
2. View per-floor statistics in table format
3. Select specific floor from dropdown to see:
   - All 18 spots on that floor
   - Spot ID, Type, Status, Rate, Current Vehicle

#### Spot Management

1. Select **Spot Details** tab
2. Choose floor from "Select Floor" dropdown
3. View detailed spot information:
   - Spot ID (e.g., F1-R1-S1)
   - Type (Compact/Regular/Handicapped/Reserved)
   - Status (Available/Occupied)
   - Hourly Rate (RM 2.00 - RM 10.00)
   - Current Vehicle (if occupied)

#### Configuring Fine Scheme

1. Select **Settings** tab
2. Under "Fine Strategy Configuration":
   - **Fixed Fine (RM 50):** Flat penalty for overstaying
   - **Progressive Scheme:** Tiered penalties (RM 50 → RM 150 → RM 300 → RM 500)
   - **Hourly Fine (RM 20/hr):** Per-hour overstay charge
3. Select desired scheme from dropdown
4. **Important:** New scheme applies to future vehicle entries only

#### Testing with Time Travel Simulation

1. Under "Simulation & Testing Tools":
2. Enter target vehicle license plate
3. Enter hours to simulate (e.g., 45 for overstay testing)
4. Click **Simulate Overstay Entry**
5. System backdates entry time for testing fines

---

### 2. Vehicle Entry

**Purpose:** Register incoming vehicles and assign parking spots

#### Step-by-Step Entry Process

**Step 1: Enter Vehicle Information**
1. Click **Vehicle Entry** tab
2. Enter **License Plate** (3-10 alphanumeric characters)
   - Examples: `ABC123`, `WXY789`, `CAR0001`
   - System auto-converts to uppercase
3. Select **Vehicle Type** from dropdown:
   - **Motorcycle:** Can park in Compact spots only
   - **Car:** Can park in Compact or Regular spots
   - **SUV:** Can park in Regular spots only
   - **Handicapped Vehicle:** Can park in any spot type

**Step 2: Handicapped Card (if applicable)**
- If vehicle type is "Handicapped Vehicle":
  - ✅ Check "Has Handicapped Card" if card holder
  - Effect: Parking rate = RM 2/hour (regardless of spot type)
  - Without card: Standard spot rate applies

**Step 3: Search Available Spots**
1. Click **🔍 Search Available Spots** button
2. System displays compatible spots in table:
   - **Select:** Radio button for selection
   - **Spot ID:** Location identifier (F1-R2-S5)
   - **Type:** Spot category
   - **Rate (RM/Hr):** Hourly parking rate
   - **Floor:** Floor number (1-5)

**Step 4: Select Parking Spot**
1. Click checkbox next to desired spot
2. Selected row highlights in blue
3. Note the Spot ID and Rate for reference

**Step 5: Park Vehicle**
1. Click **🚗 Park Vehicle** button
2. System performs:
   - Creates vehicle record
   - Assigns vehicle to selected spot
   - Updates spot status to OCCUPIED
   - Records entry time
   - Generates ticket

**Step 6: Receive Ticket**
- Ticket displays in "Generated Ticket" panel
- **Ticket Format:**
  ```
  ═════════════════════════════
  PARKING TICKET
  ═════════════════════════════
  Ticket ID    : T-WXY789-20260211154649
  License Plate: WXY789
  Vehicle Type : Car
  Spot ID      : F2-R1-S5
  Spot Type    : Compact
  Hourly Rate  : RM 2.00/hour
  Entry Time   : 2026-02-11 15:46:49
  
  Please keep this ticket for exit.
  ═════════════════════════════
  ```
- 📝 **Important:** Note the Ticket ID for reference

**Step 7: Clear Form**
1. Click **🔄 Clear Form** to reset for next vehicle

#### Parking Rules Reference

| Vehicle Type | Compatible Spots | Hourly Rate |
|-------------|------------------|-------------|
| Motorcycle | Compact only | RM 2.00 |
| Car | Compact or Regular | RM 2.00 or RM 5.00 |
| SUV | Regular only | RM 5.00 |
| Handicapped (with card) | Any spot type | RM 2.00 (discounted) |
| Handicapped (no card) | Any spot type | Standard spot rate |

#### Common Issues & Solutions

**Problem:** "Invalid license plate format"
- **Solution:** Ensure 3-10 alphanumeric characters (no spaces or special chars)

**Problem:** "Vehicle ABC123 is already parked"
- **Solution:** This vehicle hasn't exited yet. Use Exit tab to check status.

**Problem:** "No available spots found"
- **Solution:** 
  - All compatible spots are occupied
  - Try different vehicle type or wait for exits
  - Check Admin Panel for occupancy status

---

### 3. Vehicle Exit

**Purpose:** Process vehicle exits, calculate fees, and handle payments

#### Step-by-Step Exit Process

**Step 1: Search for Vehicle**
1. Click **Vehicle Exit** tab
2. Enter **License Plate** in text field
3. Click **🔍 Search Vehicle** button
4. System displays vehicle information:
   ```
   ✓ Vehicle Found!
   
   License Plate: WXY 9
   Parking Spot : F1-R1-S1
   Spot Type    : Compact
   Entry Time   : 2026-02-11 21:46:17
   ```

**Step 2: Calculate Bill**
1. Click **💰 Calculate Bill** button
2. System performs calculations:
   - **Entry Time:** Retrieved from database
   - **Exit Time:** Current time
   - **Duration:** Ceiling rounded (1h 1min = 2 hours)
   - **Parking Fee:** Duration × Hourly Rate
   - **Unpaid Fines:** Check previous violations
   - **Total Due:** Parking Fee + Fines

3. Bill displays in "Bill Details" panel:
   ```
   ━━━━━━━ PARKING BILL ━━━━━━━
   
   License Plate: WXY 9
   Parking Spot : F1-R1-S1
   
   Entry Time   : 2026-02-11 21:46:17
   Exit Time    : 2026-02-11 22:03:47
   Duration     : 1 hour
   
   Hourly Rate  : RM 2.00/hour
   Parking Fee  : RM 2.00
   
   TOTAL DUE    : RM 2.00
   ━━━━━━━━━━━━━━━━━━━━━━━━━━━
   ```

#### Understanding Duration Calculation

The system uses **ceiling rounding** to calculate parking duration:

| Entry Time | Exit Time | Actual Duration | Charged Duration |
|-----------|-----------|-----------------|------------------|
| 10:00 AM | 10:30 AM | 30 minutes | 1 hour |
| 10:00 AM | 11:01 AM | 1 hour 1 min | 2 hours |
| 10:00 AM | 12:00 PM | 2 hours | 2 hours |
| 10:00 AM | 12:59 PM | 2h 59min | 3 hours |

**Formula:** `Charged Hours = CEILING(Actual Duration in hours)`

**Step 3: Review Charges**

**Scenario A: Normal Exit (No Fines)**
```
Parking Fee  : RM 5.00  (1 hour @ RM 5.00/hr)
Fines        : RM 0.00
──────────────────────
TOTAL DUE    : RM 5.00
```

**Scenario B: Exit with Unpaid Fine**
```
Parking Fee  : RM 15.00  (3 hours @ RM 5.00/hr)
Unpaid Fines : RM 50.00  (Previous violation)
──────────────────────
TOTAL DUE    : RM 65.00
```

**Scenario C: Overstay Fine (24+ hours)**
```
Parking Fee  : RM 130.00  (26 hours @ RM 5.00/hr)
Overstay Fine: RM 50.00   (Fixed Fine Scheme)
──────────────────────
TOTAL DUE    : RM 180.00
```

**Step 4: Select Payment Method**
1. Choose payment type:
   - 💵 **Cash:** Physical currency
   - 💳 **Card:** Credit/Debit card
2. Selection affects receipt display only

**Step 5: Process Payment**
1. Click **✅ Process Exit & Payment** button
2. Confirmation dialog appears:
   ```
   ┌─────────────────────────────┐
   │ Confirm Payment             │
   ├─────────────────────────────┤
   │ Process payment of RM 2.00  │
   │ via Cash?                   │
   │                             │
   │ This will complete the exit │
   │ process.                    │
   │                             │
   │   [ Yes ]      [ No ]       │
   └─────────────────────────────┘
   ```
3. Click **Yes** to confirm

**Step 6: Receive Receipt**

System generates and displays exit receipt:

```
═══════════════════════════════════════
        PARKING EXIT RECEIPT
═══════════════════════════════════════
Receipt ID  : R-WXY-20260211220350100
Ticket ID   : T-WXY9-20260211214617
License Plate: WXY 9
Parking Spot: F1-R1-S1 (Compact)

Entry Time  : 2026-02-11 21:46:17
Exit Time   : 2026-02-11 22:03:47
Duration    : 1 hour

Parking Fee : RM 2.00 (1 × 2.00)
Fines       : RM 0.00

Total Paid  : RM 2.00
Payment Method: Cash

Thank you for parking with us!
═══════════════════════════════════════
```

**Step 7: Automatic Cleanup**
System performs:
- ✅ Saves payment record to database
- ✅ Updates spot status to AVAILABLE
- ✅ Removes vehicle record
- ✅ Deletes ticket record
- ✅ Marks fines as PAID (if any)

**Step 8: Clear for Next Exit**
1. Click **🔄 Clear** button
2. Form resets for next vehicle

#### Fine Calculation Examples

**Fixed Fine Scheme (RM 50 flat):**
```
Duration: 25 hours → Fine: RM 50
Duration: 48 hours → Fine: RM 50
Duration: 100 hours → Fine: RM 50
```

**Progressive Fine Scheme:**
```
Duration: 25 hours (1hr overstay) → Fine: RM 50
Duration: 30 hours (6hr overstay) → Fine: RM 50
Duration: 50 hours (26hr overstay) → Fine: RM 150 (RM 50 + RM 100)
Duration: 75 hours (51hr overstay) → Fine: RM 300 (RM 50 + RM 100 + RM 150)
Duration: 100 hours (76hr overstay) → Fine: RM 500 (RM 50 + RM 100 + RM 150 + RM 200)
```

**Hourly Fine Scheme (RM 20/hour):**
```
Duration: 25 hours (1hr overstay) → Fine: RM 20
Duration: 30 hours (6hr overstay) → Fine: RM 120 (6 × RM 20)
Duration: 48 hours (24hr overstay) → Fine: RM 480 (24 × RM 20)
```

---

### 4. Reports

**Purpose:** View system analytics and transaction history

#### Dashboard Summary

1. Click **Reports** tab
2. Select **Dashboard Summary** tab
3. View KPIs:
   - **Total Revenue:** RM amount from parking + fines
   - **Current Occupancy:** X / 90 vehicles
   - **Active Vehicles:** Count of parked vehicles

4. Visual charts:
   - **Occupancy Rate:** Pie chart showing used vs available
   - **Revenue Breakdown:** Bar chart (parking vs fines)

#### Live Vehicle List

1. Select **Live Vehicle List** tab
2. View table of currently parked vehicles:
   
   | License Plate | Vehicle Type | Spot ID | Entry Time |
   |--------------|--------------|---------|------------|
   | ABC123 | Car | F1-R2-S5 | 2026-02-11 10:30:15 |
   | XYZ789 | SUV | F2-R3-S10 | 2026-02-11 11:45:22 |

3. Use for:
   - Quick vehicle lookup
   - Occupancy verification
   - Entry time reference

#### Outstanding Fines

1. Select **Outstanding Fines** tab
2. View unpaid fines report:
   
   | License Plate | Fine Amount | Violation | Date |
   |--------------|-------------|-----------|------|
   | TEST | RM 50.00 | 45 hours | 2026-02-10 19:14:01 |
   | ABC1234 | RM 50.00 | 25 hours | 2026-02-10 15:14:33 |

3. Fines automatically added to next exit bill

#### Refreshing Data

- Click **Refresh Data** button (top right)
- Updates all reports with latest database data
- Recommended after busy periods

---

## 🛠 Technical Documentation

### Project Structure

```
parking-lot-management-system/
├── src/                          # Source code
│   ├── models/                   # Domain models
│   │   ├── parking/             # Parking infrastructure
│   │   │   ├── ParkingLot.java
│   │   │   ├── Floor.java
│   │   │   ├── ParkingSpot.java (abstract)
│   │   │   ├── CompactSpot.java
│   │   │   ├── RegularSpot.java
│   │   │   ├── HandicappedSpot.java
│   │   │   ├── ReservedSpot.java
│   │   │   ├── SpotType.java (enum)
│   │   │   └── SpotStatus.java (enum)
│   │   ├── vehicle/             # Vehicle hierarchy
│   │   │   ├── Vehicle.java (interface)
│   │   │   ├── AbstractVehicle.java
│   │   │   ├── Motorcycle.java
│   │   │   ├── Car.java
│   │   │   ├── SUV.java
│   │   │   ├── HandicappedVehicle.java
│   │   │   ├── VehicleType.java (enum)
│   │   │   └── Ticket.java
│   │   ├── payment/             # Payment processing
│   │   │   ├── Payment.java
│   │   │   ├── PaymentMethod.java (enum)
│   │   │   └── Receipt.java
│   │   └── fine/                # Fine strategies (Strategy Pattern)
│   │       ├── FineStrategy.java (interface)
│   │       ├── FixedFineStrategy.java
│   │       ├── ProgressiveFineStrategy.java
│   │       ├── HourlyFineStrategy.java
│   │       └── Fine.java
│   ├── controllers/             # Business logic
│   │   ├── EntryController.java (Factory Pattern)
│   │   ├── ExitController.java
│   │   ├── BillingCalculator.java
│   │   ├── PaymentProcessor.java
│   │   └── ReportController.java
│   ├── views/                   # GUI (Java Swing)
│   │   ├── AdminPanel.java
│   │   ├── EntryPanel.java
│   │   ├── ExitPanel.java
│   │   └── ReportPanel.java
│   ├── database/                # Data Access Layer (DAO Pattern)
│   │   ├── DatabaseManager.java (Singleton)
│   │   ├── ParkingSpotsDAO.java
│   │   ├── VehiclesDAO.java
│   │   ├── TicketsDAO.java
│   │   ├── PaymentsDAO.java
│   │   └── FinesDAO.java
│   ├── services/                # Utility services
│   │   └── FineCalculator.java
│   └── Main.java                # Application entry point
├── database/                     # Database files
│   └── schema.sql               # Database schema
├── lib/                         # External libraries
│   ├── sqlite-jdbc-3.45.0.0.jar
│   ├── slf4j-api-2.0.9.jar
│   └── slf4j-simple-2.0.9.jar
├── docs/                        # Documentation
│   ├── DESIGN_PATTERN.md       # Design pattern explanations
│   └── UML/                     # UML diagrams
├── bin/                         # Compiled classes (generated)
├── README.md                    # This file
└── .gitignore
```

### Database Schema

```sql
-- Parking Spots Table
CREATE TABLE parking_spots (
    spot_id VARCHAR(20) PRIMARY KEY,
    floor_number INT NOT NULL,
    spot_type VARCHAR(20) NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL,
    is_occupied BOOLEAN DEFAULT 0
);

-- Vehicles Table
CREATE TABLE vehicles (
    license_plate VARCHAR(20) PRIMARY KEY,
    vehicle_type VARCHAR(20) NOT NULL,
    has_handicapped_card BOOLEAN DEFAULT 0,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    spot_id VARCHAR(20),
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id)
);

-- Tickets Table
CREATE TABLE tickets (
    ticket_id VARCHAR(50) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    spot_id VARCHAR(20) NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate),
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id)
);

-- Payments Table
CREATE TABLE payments (
    payment_id VARCHAR(50) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    parking_fee DECIMAL(10,2) NOT NULL,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(10) NOT NULL
);

-- Fines Table
CREATE TABLE fines (
    fine_id INTEGER PRIMARY KEY AUTOINCREMENT,
    license_plate VARCHAR(20) NOT NULL,
    fine_date TIMESTAMP NOT NULL,
    fine_reason VARCHAR(100) NOT NULL,
    fine_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'UNPAID'
);

-- Floors Table
CREATE TABLE floors (
    floor_number INT PRIMARY KEY,
    total_spots INT NOT NULL,
    occupied_spots INT DEFAULT 0
);
```

### Design Patterns

For detailed design pattern documentation, see [DESIGN_PATTERN.md](DESIGN_PATTERN.md)

**Summary:**
1. **Strategy Pattern:** Fine calculation algorithms (3 schemes)
2. **Factory Pattern:** Vehicle object creation
3. **Singleton Pattern:** Database connection management
4. **DAO Pattern:** Data access abstraction

### API Quick Reference

#### Entry Controller
```java
// Find available spots
List<ParkingSpot> findAvailableSpots(VehicleType type);

// Create vehicle (Factory Pattern)
Vehicle createVehicle(String plate, VehicleType type, boolean hasCard);

// Park vehicle
boolean parkVehicle(Vehicle vehicle, String spotId);
```

#### Exit Controller
```java
// Search vehicle by license plate
Vehicle searchVehicle(String licensePlate);

// Calculate bill
double[] calculateBill(String licensePlate);
// Returns: [duration, parkingFee, fine, total]

// Process exit
Receipt processExit(String plate, PaymentMethod method);
```

#### Fine Calculator
```java
// Set fine strategy (Strategy Pattern)
void setFineStrategy(FineStrategy strategy);

// Calculate fine
double calculateFine(long durationHours);
```

---

## 👥 Team Members

**Group 4 - Tutorial TT6L**

| No. | Student ID | Name | Role | Contribution |
|-----|-----------|------|------|--------------|
| 1 | 242UC243B4 | LAMA M. R. SIAM | **Team Leader** | Parking Infrastructure (25%) |
| 2 | 242UC243BE | EBA MOHAMED ABBAS AHMED | Member | Vehicle & Entry Management (25%) |
| 3 | 242UC243TL | SITI ZULAIKHA BINTI ABDUL RAZIF | Member | Exit & Payment Processing (25%) |
| 4 | 243UC246VU | HONG JING JIE | Member | Fine Management & Reporting (25%) |

### Module Responsibilities

#### Member 1: Parking Infrastructure (LAMA M. R. SIAM)
- ParkingLot, Floor, ParkingSpot hierarchy (Composite Pattern)
- Admin Panel GUI with real-time statistics
- ParkingSpotsDAO for database persistence
- Integration testing and GitHub setup

#### Member 2: Vehicle & Entry Management (EBA MOHAMED ABBAS AHMED)
- Vehicle hierarchy with Factory Pattern
- Entry Panel GUI with 6-step workflow
- License plate validation and duplicate prevention
- VehiclesDAO and TicketsDAO

#### Member 3: Exit & Payment Processing (SITI ZULAIKHA BINTI ABDUL RAZIF)
- Transaction management (Payment, Receipt classes)
- Ceiling rounding algorithm for duration
- Exit Panel GUI with bill calculation
- PaymentsDAO for payment persistence

#### Member 4: Fine Management & Reporting (HONG JING JIE)
- Strategy Pattern for fine calculation
- Time Travel simulation tool
- ReportPanel dashboard with KPIs
- SQL aggregation queries for reports

---

## 📄 License

This project is developed for academic purposes as part of CCP6224 - Object-Oriented Analysis and Design course at Multimedia University.

**Copyright © 2026 Group 4 (TT6L)**

---

## 📞 Support

For questions or issues:

1. **Check Documentation:**
   - README.md (this file)
   - DESIGN_PATTERN.md
   - Report PDF

2. **Common Issues:**
   - See "Common Issues & Solutions" in User Manual sections

3. **Contact:**
   - Raise an issue on [GitHub](https://github.com/lamasiam/parking-lot-management-system/issues)
   - Contact team leader: LAMA M. R. SIAM

---

## 🎓 Academic Information

**Course Code:** CCP6224  
**Course Name:** Object-Oriented Analysis and Design  
**Trimester:** 2530  
**Lecture Session:** TC2L  
**Tutorial Session:** TT6L  
**Group:** Group 4  
**Institution:** Multimedia University  
**Submission Date:** 16 February 2026

---

## 📊 Project Statistics

- **Total Java Files:** 48
- **Total Lines of Code:** ~5,000
- **Database Tables:** 6
- **Design Patterns:** 4
- **GUI Panels:** 4
- **Test Files:** 4
- **Documentation Pages:** 28

---

**Built with ❤️ by Group 4 (TT6L) | Multimedia University | 2026**
