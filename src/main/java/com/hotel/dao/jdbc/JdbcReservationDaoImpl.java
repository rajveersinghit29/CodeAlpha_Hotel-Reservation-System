package com.hotel.dao.jdbc;

import com.hotel.dao.ReservationDao;
import com.hotel.dao.RoomDao;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.model.room.Room;
import com.hotel.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ReservationDao.
 */
public class JdbcReservationDaoImpl implements ReservationDao {

    private final RoomDao roomDao;

    public JdbcReservationDaoImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public Reservation findById(String reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractReservationFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public void save(Reservation reservation) {
        String sql = "INSERT INTO reservations (reservation_id, user_id, user_name, user_email, user_phone, room_id, check_in_date, check_out_date, is_cancelled, total_amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT(reservation_id) DO UPDATE SET is_cancelled = EXCLUDED.is_cancelled";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservation.getReservationId());
            stmt.setString(2, reservation.getUser().getUserId());
            stmt.setString(3, reservation.getUser().getName());
            stmt.setString(4, reservation.getUser().getEmail());
            stmt.setString(5, reservation.getUser().getPhoneNumber());
            stmt.setString(6, reservation.getRoom().getRoomId());
            stmt.setDate(7, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(8, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setBoolean(9, reservation.isCancelled());
            stmt.setDouble(10, reservation.getTotalAmount());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reservationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        String reservationId = rs.getString("reservation_id");
        User user = new User(
                rs.getString("user_id"),
                rs.getString("user_name"),
                rs.getString("user_email"),
                rs.getString("user_phone")
        );
        String roomId = rs.getString("room_id");
        Room room = roomDao.findById(roomId);
        
        Reservation res = new Reservation(
                reservationId,
                user,
                room,
                rs.getDate("check_in_date").toLocalDate(),
                rs.getDate("check_out_date").toLocalDate(),
                rs.getDouble("total_amount")
        );
        res.setCancelled(rs.getBoolean("is_cancelled"));
        return res;
    }
}
