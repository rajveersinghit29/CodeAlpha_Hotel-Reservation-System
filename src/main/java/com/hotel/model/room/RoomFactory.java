package com.hotel.model.room;

/**
 * Factory pattern implementation for generating different room types.
 */
public class RoomFactory {

    /**
     * Creates a room of the specified type.
     *
     * @param type       the type of room ("STANDARD", "DELUXE", "SUITE")
     * @param roomId     unique identifier for the room
     * @param roomNumber room number
     * @param baseRate   base nightly rate
     * @return instantiated Room subclass
     * @throws IllegalArgumentException if the room type is unknown
     */
    public static Room createRoom(String type, String roomId, String roomNumber, double baseRate) {
        if (type == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }

        switch (type.toUpperCase()) {
            case "STANDARD":
                return new StandardRoom(roomId, roomNumber, baseRate, true);
            case "DELUXE":
                return new DeluxeRoom(roomId, roomNumber, baseRate, true);
            case "SUITE":
                return new SuiteRoom(roomId, roomNumber, baseRate, true);
            default:
                throw new IllegalArgumentException("Unknown room type: " + type);
        }
    }
}
