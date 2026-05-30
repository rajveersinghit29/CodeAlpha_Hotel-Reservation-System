package com.hotel.dao;

import com.hotel.model.Reservation;
import java.util.List;

/**
 * Data Access Object interface for Reservation.
 */
public interface ReservationDao {
    /**
     * Finds a reservation by its ID.
     * @param reservationId the reservation ID
     * @return the Reservation object if found, null otherwise
     */
    Reservation findById(String reservationId);

    /**
     * Retrieves all reservations.
     * @return List of all reservations
     */
    List<Reservation> findAll();

    /**
     * Saves a new reservation or updates an existing one.
     * @param reservation the reservation to save
     */
    void save(Reservation reservation);

    /**
     * Deletes a reservation by its ID.
     * @param reservationId the reservation ID
     */
    void delete(String reservationId);
}
