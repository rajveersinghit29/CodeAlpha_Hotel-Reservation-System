package com.hotel.service;

import com.hotel.dao.RoomDao;
import com.hotel.model.room.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for room-related operations.
 */
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    /**
     * Searches for available rooms by category.
     * @param category the category (e.g., "STANDARD", "DELUXE")
     * @return list of available rooms in the given category
     */
    public List<Room> searchAvailableRooms(String category) {
        List<Room> allRooms = roomDao.findAll();
        List<Room> availableRooms = new ArrayList<>();
        
        for (Room room : allRooms) {
            String roomType = room.getClass().getSimpleName().replace("Room", "").toUpperCase();
            if (room.isAvailable() && roomType.equalsIgnoreCase(category)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Room getRoomById(String roomId) {
        return roomDao.findById(roomId);
    }
    
    public void updateRoomStatus(String roomId, boolean isAvailable) {
        Room room = roomDao.findById(roomId);
        if (room != null) {
            room.setAvailable(isAvailable);
            roomDao.save(room);
        } else {
            throw new IllegalArgumentException("Room not found: " + roomId);
        }
    }
}
