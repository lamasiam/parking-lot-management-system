package services;

import models.fine.*;

/**
 * Context class for the Strategy Pattern.
 * The Admin Panel will interact with this to change logic.
 */
public class FineCalculator {
    private static FineStrategy currentStrategy = new FixedFineStrategy(); // Default

    public static void setStrategy(FineStrategy strategy) {
        currentStrategy = strategy;
        System.out.println("✓ Fine Strategy changed to: " + strategy.getStrategyName());
    }

    public static FineStrategy getStrategy() {
        return currentStrategy;
    }

    public static double calculate(long durationHours) {
        return currentStrategy.calculateFine(durationHours);
    }
}