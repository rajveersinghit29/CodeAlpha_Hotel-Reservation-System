package com.hotel.dao.jdbc;

import com.hotel.dao.RoomDao;
import com.hotel.model.room.Room;
import com.hotel.model.room.RoomFactory;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of RoomDao.
 */
public class JdbcRoomDaoImpl implements RoomDao {

    @Override
    public Room findById(String roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public void save(Room room) {
        String sql = "INSERT INTO rooms (room_id, room_number, base_rate, is_available, type) VALUES (?, ?, ?, ?, ?) " +
                     "ON CONFLICT(room_id) DO UPDATE SET room_number = EXCLUDED.room_number, " +
                     "base_rate = EXCLUDED.base_rate, is_available = EXCLUDED.is_available, type = EXCLUDED.type";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getRoomId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setDouble(3, room.getBaseRate());
            stmt.setBoolean(4, room.isAvailable());
            stmt.setString(5, room.getClass().getSimpleName().replace("Room", "").toUpperCase());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Room extractRoomFromResultSet(ResultSet rs) throws SQLException {
        String roomId = rs.getString("room_id");
        String roomNumber = rs.getString("room_number");
        double baseRate = rs.getDouble("base_rate");
        boolean isAvailable = rs.getBoolean("is_available");
        String type = rs.getString("type");
        
        Room room = RoomFactory.createRoom(type, roomId, roomNumber, baseRate);
        room.setAvailable(isAvailable);
        return room;
    }
}
