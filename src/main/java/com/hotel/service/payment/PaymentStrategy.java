package com.hotel.service.payment;

/**
 * Strategy pattern interface for payment processing.
 */
public interface PaymentStrategy {
    /**
     * Processes a payment for the given amount.
     * @param amount the amount to process
     * @return true if payment is successful, false otherwise
     */
    boolean processPayment(double amount);
}
