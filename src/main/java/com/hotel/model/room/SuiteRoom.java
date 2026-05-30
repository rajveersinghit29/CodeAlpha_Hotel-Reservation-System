package com.hotel.model.room;

/**
 * Represents a suite room in the hotel.
 */
public class SuiteRoom extends Room {

    // Suite rooms have a high premium multiplier (e.g., 50% more)
    private static final double SUITE_MULTIPLIER = 1.50;
    // Flat fee for suite services per stay
    private static final double SERVICE_FEE = 100.00;

    public SuiteRoom(String roomId, String roomNumber, double baseRate, boolean isAvailable) {
        super(roomId, roomNumber, baseRate, isAvailable);
    }

    /**
     * Suite rooms cost (baseRate * SUITE_MULTIPLIER * days) + flat service fee.
     */
    @Override
    public double calculateRate(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be greater than 0");
        }
        return (getBaseRate() * SUITE_MULTIPLIER * days) + SERVICE_FEE;
    }

    @Override
    public String toString() {
        return "SuiteRoom{} " + super.toString();
    }
}
