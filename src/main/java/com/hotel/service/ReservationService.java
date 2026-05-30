package com.hotel.service;

import com.hotel.dao.ReservationDao;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.model.room.Room;
import com.hotel.service.payment.PaymentStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Service layer for reservation-related operations.
 */
public class ReservationService {

    private final ReservationDao reservationDao;
    private final RoomService roomService;

    public ReservationService(ReservationDao reservationDao, RoomService roomService) {
        this.reservationDao = reservationDao;
        this.roomService = roomService;
    }

    /**
     * Handles the logic for booking a room.
     */
    public Reservation bookRoom(User user, Room room, LocalDate checkIn, LocalDate checkOut, PaymentStrategy paymentStrategy) {
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available for booking.");
        }
        
        int days = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) {
            throw new IllegalArgumentException("Check-out date must be at least one day after check-in.");
        }

        double totalAmount = room.calculateRate(days);
        
        // Process Payment
        boolean paymentSuccess = paymentStrategy.processPayment(totalAmount);
        if (!paymentSuccess) {
            throw new IllegalStateException("Payment failed. Reservation not created.");
        }

        // Generate Booking ID
        String reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Reservation reservation = new Reservation(reservationId, user, room, checkIn, checkOut, totalAmount);
        
        // Update Room Status and Save
        roomService.updateRoomStatus(room.getRoomId(), false);
        reservationDao.save(reservation);
        
        return reservation;
    }

    /**
     * Handles cancellation logic.
     */
    public boolean cancelReservation(String reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation != null && !reservation.isCancelled()) {
            reservation.setCancelled(true);
            reservationDao.save(reservation);
            
            // Make room available again
            roomService.updateRoomStatus(reservation.getRoom().getRoomId(), true);
            return true;
        }
        return false;
    }
}
