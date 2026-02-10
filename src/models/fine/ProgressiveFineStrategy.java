package models.fine;

public class ProgressiveFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;

        double fine = 0.0;
        long overstay = durationHours - 24;

        // First 24 hours overstay (Hours 25-48)
        fine += 50.0;

        // Second 24 hours overstay (Hours 49-72)
        if (overstay > 24) {
            fine += 100.0;
        }

        // Third 24 hours overstay (Hours 73+)
        if (overstay > 48) {
            fine += 150.0;
        }
        
        // Capping logic (Optional based on requirement interpretation)
        // Requirement says "Above 72 hours: Additional RM 200". 
        // This implies specific tiers.
        if (overstay > 72) {
            fine += 200.0;
        }

        return fine;
    }

    @Override
    public String getStrategyName() {
        return "Progressive Scheme (Tiered)";
    }
}