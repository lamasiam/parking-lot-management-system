\# Quick Reference - Parking Lot Project



\## 🚀 Quick Start Commands



\### Setup (First Time)

```bash

git clone https://github.com/lamasiam/parking-lot-management-system.git

cd parking-lot-management-system

```



\### Daily Workflow

```bash

\# 1. Start of day - get latest code

git pull origin main



\# 2. Work on your files...



\# 3. Test your code

cd src

javac -d ../bin models/\*\*/\*.java services/\*\*/\*.java TestMemberX.java

cd ../bin

java TestMemberX



\# 4. If tests pass, commit

cd ..

git add .

git commit -m "feat: your changes description"

git push origin main

```



\## 📁 File Locations



| What | Where |

|------|-------|

| Parking structure | `src/models/parking/` |

| Vehicle classes | `src/models/vehicle/` |

| Payment/Fines | `src/models/payment/` |

| Business logic | `src/services/` |

| GUI | `src/views/` |

| Tests | `src/TestMemberX.java` |



\## 🧪 Testing Your Module



Copy this template for your test file:

```java

public class TestMember2 {

&nbsp;   public static void main(String\[] args) {

&nbsp;       System.out.println("╔════════════════════════════════════════════════╗");

&nbsp;       System.out.println("║  MEMBER 2: YOUR MODULE TEST                    ║");

&nbsp;       System.out.println("╚════════════════════════════════════════════════╝\\n");

&nbsp;       

&nbsp;       // Your tests here

&nbsp;       

&nbsp;       System.out.println("\\n╔════════════════════════════════════════════════╗");

&nbsp;       System.out.println("║  ALL TESTS PASSED! ✓                          ║");

&nbsp;       System.out.println("╚════════════════════════════════════════════════╝");

&nbsp;   }

}

```



\## 💡 Common Tasks



\### Get available parking spots

```java

List<ParkingSpot> available = parkingLot.getAvailableSpots();

```



\### Park a vehicle

```java

spot.parkVehicle(vehicle);

vehicle.setEntryTime(LocalDateTime.now());

```



\### Calculate parking fee

```java

long hours = Duration.between(entryTime, exitTime).toHours();

if (hours == 0) hours = 1; // Minimum 1 hour

double fee = spot.getHourlyRate() \* hours;

```



\### Check spot type compatibility

```java

if (vehicle.canParkIn(spot.getType())) {

&nbsp;   // Can park here

}

```



\## 🎯 Module Responsibilities



| Module | Member | Key Classes |

|--------|--------|-------------|

| Parking Structure | Member 1 | ✅ ParkingLot, Floor, ParkingSpot |

| Vehicles | Member 2 | Vehicle, Motorcycle, Car, SUV |

| Payment/Fines | Member 3 | Payment, Fine, Receipt |

| Entry/Exit | Member 4 | EntryService, ExitService, Ticket |

| GUI | Member 5 | MainFrame, Panels, Forms |



\## 📞 Help



\- \*\*Can't compile?\*\* Make sure you're in `src/` directory

\- \*\*Import errors?\*\* Check package names match folder structure

\- \*\*Git conflicts?\*\* Always `git pull` before starting work

\- \*\*Test failing?\*\* Check JavaDoc and existing tests for examples



\## 🔗 Useful Links



\- GitHub Repo: https://github.com/lamasiam/parking-lot-management-system

\- Assignment Doc: Check `docs/` folder

\- Team Chat: \[Add your chat link]

