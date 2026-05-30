package com.hotel.model.room;

/**
 * Represents a standard room in the hotel.
 */
public class StandardRoom extends Room {

    public StandardRoom(String roomId, String roomNumber, double baseRate, boolean isAvailable) {
        super(roomId, roomNumber, baseRate, isAvailable);
    }

    /**
     * Standard rooms have no special multiplier on the base rate.
     */
    @Override
    public double calculateRate(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be greater than 0");
        }
        return getBaseRate() * days;
    }
    
    @Override
    public String toString() {
        return "StandardRoom{} " + super.toString();
    }
}
