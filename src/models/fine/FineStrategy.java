package models.fine;

/**
 * Strategy Interface for Fine Calculation
 * Implements the Strategy Design Pattern
 */
public interface FineStrategy {
    double calculateFine(long durationHours);
    String getStrategyName();
}