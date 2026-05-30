package com.hotel.service.payment;

/**
 * Concrete strategy for UPI payments.
 */
public class UPIPayment implements PaymentStrategy {
    private String upiId;

    public UPIPayment(String upiId) {
        if (upiId == null || !upiId.contains("@")) {
            throw new IllegalArgumentException("Invalid UPI ID.");
        }
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing UPI payment of $" + amount + " for UPI ID: " + upiId);
        // Simulate UPI payment gateway logic here
        return true;
    }
}
