package models.fine;

/**
 * Concrete Strategy: Fixed Fine
 * Charges a flat RM 50 if duration > 24 hours
 */
public class FixedFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours > 24) {
            return 50.0;
        }
        return 0.0;
    }

    @Override
    public String getStrategyName() {
        return "Fixed Fine (RM 50 flat)";
    }
}