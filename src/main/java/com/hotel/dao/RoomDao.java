package com.hotel.dao;

import com.hotel.model.room.Room;
import java.util.List;

/**
 * Data Access Object interface for Room.
 */
public interface RoomDao {
    /**
     * Finds a room by its ID.
     * @param roomId the room ID
     * @return the Room object if found, null otherwise
     */
    Room findById(String roomId);

    /**
     * Retrieves all rooms in the system.
     * @return List of all rooms
     */
    List<Room> findAll();

    /**
     * Saves a new room or updates an existing one.
     * @param room the room to save
     */
    void save(Room room);

    /**
     * Deletes a room by its ID.
     * @param roomId the room ID
     */
    void delete(String roomId);
}
