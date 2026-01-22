cat > README.md << 'EOF'

# Parking Lot Management System

**Course:** Object-Oriented Analysis and Design  
**Assignment:** Final Project (40%)  
**Submission Deadline:** 16 Feb 2026, 11:59 PM

## Team Information

**Group Name:** [Group 4]  
**Tutorial Section:** [TT6L]

### Team Members

1. **[LAMA M. R. SIAM]** - Member 1 (Parking Structure Lead) - Team Leader
2. **[EBA MOAHMED ABBAD AHMED]** - Member 2 (Vehicle & Entry Lead)
3. **[HONG JING JIE]** - Member 3 (Exit & Payment Lead)
4. **[SITI ZULAIKHA BINTI ABDUL RAZIF]** - Member 4 (Fine & Reports Lead)

## Project Description

A Java Swing-based parking lot management system for a multi-level building that handles:

- Vehicle parking and spot allocation
- Payment processing
- Fine management
- Administrative reporting

## How to Compile and Run

### Prerequisites

- Java JDK 11 or higher
- SQLite JDBC driver (included in lib/)

### Compilation

```bash
cd src
javac -d ../bin -cp "../lib/*" models/**/*.java controllers/*.java views/*.java database/*.java Main.java
```

### Run

```bash
cd bin
java -cp ".;../lib/*" Main
```

## Project Structure

```
parking-lot-management-system/
├── src/
│   ├── models/
│   │   ├── parking/      (Member 1: ParkingLot, Floor, Spots)
│   │   ├── vehicle/      (Member 2: Vehicle hierarchy)
│   │   ├── payment/      (Member 3: Payment classes)
│   │   └── fine/         (Member 4: Fine strategies)
│   ├── controllers/      (All controllers)
│   ├── views/           (All GUI panels)
│   ├── database/        (DAO classes)
│   └── Main.java
├── docs/                (UML diagrams, documentation)
├── lib/                 (External libraries)
└── resources/           (Images, config files)
```

## Module Responsibilities

### Member 1: Parking Infrastructure (Status: In Progress)

- ✅ ParkingLot class
- ✅ Floor class
- ✅ ParkingSpot hierarchy (4 types)
- ✅ Admin Panel GUI
- ⏳ Database integration

### Member 2: Vehicle & Entry Management (Status: Pending)

- ⏳ Vehicle hierarchy
- ⏳ Entry system
- ⏳ Entry Panel GUI

### Member 3: Exit & Payment (Status: Pending)

- ⏳ Payment processing
- ⏳ Exit system
- ⏳ Exit Panel GUI

### Member 4: Fine & Reports (Status: Pending)

- ⏳ Fine strategies
- ⏳ Report generation
- ⏳ Admin reporting GUI

## Design Pattern

**Pattern Used:** Strategy Pattern  
**Implementation:** Fine calculation system (Member 4)

## Features Implemented

- [ ] Multi-level parking lot structure
- [ ] Vehicle entry process
- [ ] Vehicle exit and billing
- [ ] Fine management
- [ ] Administrative reports
