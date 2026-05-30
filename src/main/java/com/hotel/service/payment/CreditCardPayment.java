package com.hotel.service.payment;

/**
 * Concrete strategy for Credit Card payments.
 */
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cardHolderName, String cvv) {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid credit card number.");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Credit Card payment of $" + amount + " for " + cardHolderName);
        // Simulate payment gateway logic here
        return true;
    }
}
