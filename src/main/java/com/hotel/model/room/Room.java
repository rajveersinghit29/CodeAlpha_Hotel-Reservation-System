package com.hotel.model.room;

/**
 * Abstract base class representing a Room in the hotel.
 * This class enforces strong encapsulation and input validation.
 */
public abstract class Room {
    private String roomId;
    private String roomNumber;
    private double baseRate;
    private boolean isAvailable;

    public Room(String roomId, String roomNumber, double baseRate, boolean isAvailable) {
        setRoomId(roomId);
        setRoomNumber(roomNumber);
        setBaseRate(baseRate);
        setAvailable(isAvailable);
    }

    /**
     * Polymorphic method to calculate the rate for a given number of days.
     * Overridden by subclasses.
     *
     * @param days number of days stayed
     * @return total calculated rate
     */
    public abstract double calculateRate(int days);

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty.");
        }
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be null or empty.");
        }
        this.roomNumber = roomNumber;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        if (baseRate < 0) {
            throw new IllegalArgumentException("Base rate cannot be negative.");
        }
        this.baseRate = baseRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", baseRate=" + baseRate +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
