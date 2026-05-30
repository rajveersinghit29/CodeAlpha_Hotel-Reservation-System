package com.hotel.dao.file;

import com.hotel.dao.ReservationDao;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.model.room.Room;
import com.hotel.dao.RoomDao;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of ReservationDao using CSV storage.
 */
public class FileReservationDaoImpl implements ReservationDao {
    private static final String FILE_PATH = "src/main/resources/data/reservations.csv";
    private final RoomDao roomDao;

    // We pass RoomDao to resolve Room references
    public FileReservationDaoImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating reservations file: " + e.getMessage());
        }
    }

    @Override
    public Reservation findById(String reservationId) {
        List<Reservation> reservations = findAll();
        for (Reservation res : reservations) {
            if (res.getReservationId().equals(reservationId)) {
                return res;
            }
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming CSV format: reservationId,userId,userName,userEmail,userPhone,roomId,checkIn,checkOut,isCancelled,totalAmount
                String[] data = line.split(",");
                if (data.length == 10) {
                    String reservationId = data[0];
                    User user = new User(data[1], data[2], data[3], data[4]);
                    
                    String roomId = data[5];
                    Room room = roomDao.findById(roomId);
                    
                    LocalDate checkIn = LocalDate.parse(data[6]);
                    LocalDate checkOut = LocalDate.parse(data[7]);
                    boolean isCancelled = Boolean.parseBoolean(data[8]);
                    double totalAmount = Double.parseDouble(data[9]);
                    
                    Reservation res = new Reservation(reservationId, user, room, checkIn, checkOut, totalAmount);
                    res.setCancelled(isCancelled);
                    reservations.add(res);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading reservations from file: " + e.getMessage());
        }
        return reservations;
    }

    @Override
    public void save(Reservation reservation) {
        List<Reservation> reservations = findAll();
        boolean updated = false;
        
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservation.getReservationId())) {
                reservations.set(i, reservation);
                updated = true;
                break;
            }
        }
        
        if (!updated) {
            reservations.add(reservation);
        }
        
        saveAll(reservations);
    }

    @Override
    public void delete(String reservationId) {
        List<Reservation> reservations = findAll();
        reservations.removeIf(res -> res.getReservationId().equals(reservationId));
        saveAll(reservations);
    }

    private void saveAll(List<Reservation> reservations) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Reservation res : reservations) {
                User u = res.getUser();
                pw.println(res.getReservationId() + "," + 
                           u.getUserId() + "," + u.getName() + "," + u.getEmail() + "," + u.getPhoneNumber() + "," +
                           res.getRoom().getRoomId() + "," +
                           res.getCheckInDate() + "," + res.getCheckOutDate() + "," +
                           res.isCancelled() + "," + res.getTotalAmount());
            }
        } catch (IOException e) {
            System.err.println("Error writing reservations to file: " + e.getMessage());
        }
    }
}
