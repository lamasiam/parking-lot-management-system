package controllers;

import models.payment.Payment;
import models.payment.PaymentMethod;

/**
 * Payment Processor
 * Handles payment processing for parking fees
 * 
 * @author Member 3 - Exit & Payment Management Lead
 */
public class PaymentProcessor {
    
    /**
     * Process a payment transaction
     * 
     * @param licensePlate Vehicle license plate
     * @param parkingFee Parking fee amount
     * @param fineAmount Fine amount (if any)
     * @param paymentMethod Payment method (CASH or CARD)
     * @param ticketId Associated ticket ID
     * @return Payment object if successful, null if failed
     */
    public Payment processPayment(String licensePlate, double parkingFee, 
                                  double fineAmount, PaymentMethod paymentMethod,
                                  String ticketId) {
        // Validate inputs
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            System.err.println("✗ Invalid license plate");
            return null;
        }
        
        if (parkingFee < 0) {
            System.err.println("✗ Parking fee cannot be negative");
            return null;
        }
        
        if (fineAmount < 0) {
            System.err.println("✗ Fine amount cannot be negative");
            return null;
        }
        
        if (paymentMethod == null) {
            System.err.println("✗ Payment method is required");
            return null;
        }
        
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.err.println("✗ Ticket ID is required");
            return null;
        }
        
        try {
            // Create payment record
            Payment payment = new Payment(
                licensePlate.toUpperCase(),
                parkingFee,
                fineAmount,
                paymentMethod,
                ticketId
            );
            
            // Simulate payment processing
            if (paymentMethod == PaymentMethod.CASH) {
                System.out.println("✓ Cash payment received: RM " + 
                    String.format("%.2f", payment.getTotalAmount()));
            } else if (paymentMethod == PaymentMethod.CARD) {
                System.out.println("✓ Card payment processed: RM " + 
                    String.format("%.2f", payment.getTotalAmount()));
            }
            
            System.out.println("✓ Payment successful: " + payment.getPaymentId());
            
            return payment;
            
        } catch (Exception e) {
            System.err.println("✗ Payment processing failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Validate payment amount
     * 
     * @param amount Amount to validate
     * @return true if valid
     */
    public boolean validatePaymentAmount(double amount) {
        return amount >= 0;
    }
    
    /**
     * Calculate change for cash payments
     * 
     * @param totalDue Total amount due
     * @param amountPaid Amount paid by customer
     * @return Change to return
     */
    public double calculateChange(double totalDue, double amountPaid) {
        if (amountPaid < totalDue) {
            throw new IllegalArgumentException("Insufficient payment");
        }
        return amountPaid - totalDue;
    }
}
