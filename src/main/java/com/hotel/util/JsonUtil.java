package com.hotel.util;

import com.hotel.model.Reservation;
import com.hotel.model.room.Room;

import java.util.List;

/**
 * A highly simplified, dependency-free utility to build JSON strings 
 * and extract string values from simple JSON payloads.
 */
public class JsonUtil {

    // Helper to format a Room to a JSON string
    public static String roomToJson(Room room) {
        String type = room.getClass().getSimpleName().replace("Room", "").toUpperCase();
        return String.format(
            "{\"roomId\": \"%s\", \"roomNumber\": \"%s\", \"baseRate\": %f, \"isAvailable\": %b, \"type\": \"%s\"}",
            room.getRoomId(), room.getRoomNumber(), room.getBaseRate(), room.isAvailable(), type
        );
    }

    // Helper to format a List of Rooms to a JSON array string
    public static String roomListToJson(List<Room> rooms) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rooms.size(); i++) {
            sb.append(roomToJson(rooms.get(i)));
            if (i < rooms.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Helper to format a Reservation to a JSON string
    public static String reservationToJson(Reservation res) {
        return String.format(
            "{\"reservationId\": \"%s\", \"totalAmount\": %f}",
            res.getReservationId(), res.getTotalAmount()
        );
    }
    
    // Simplistic method to extract a string value from a flat JSON string
    // e.g. extractStringValue("{\"name\":\"John\"}", "name") -> "John"
    public static String extractStringValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;
        
        startIndex += searchKey.length();
        // find opening quote of the value
        int quoteStart = json.indexOf("\"", startIndex);
        if (quoteStart == -1) return null;
        
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        if (quoteEnd == -1) return null;
        
        return json.substring(quoteStart + 1, quoteEnd);
    }
}
