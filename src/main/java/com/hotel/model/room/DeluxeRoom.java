package com.hotel.model.room;

/**
 * Represents a deluxe room in the hotel.
 */
public class DeluxeRoom extends Room {

    // Deluxe rooms might have a premium multiplier (e.g., 20% more)
    private static final double PREMIUM_MULTIPLIER = 1.20;

    public DeluxeRoom(String roomId, String roomNumber, double baseRate, boolean isAvailable) {
        super(roomId, roomNumber, baseRate, isAvailable);
    }

    /**
     * Deluxe rooms cost baseRate * PREMIUM_MULTIPLIER per day.
     */
    @Override
    public double calculateRate(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be greater than 0");
        }
        return getBaseRate() * PREMIUM_MULTIPLIER * days;
    }
    
    @Override
    public String toString() {
        return "DeluxeRoom{} " + super.toString();
    }
}
