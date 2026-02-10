package models.fine;

public class HourlyFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(long durationHours) {
        if (durationHours <= 24) return 0.0;

        // Option C: RM 20 per hour for every hour over 24
        long overstay = durationHours - 24;
        return overstay * 20.0;
    }

    @Override
    public String getStrategyName() {
        return "Hourly Scheme (RM 20/hr)";
    }
}