package com.hotel;

import com.hotel.dao.file.FileReservationDaoImpl;
import com.hotel.dao.file.FileRoomDaoImpl;
import com.hotel.model.room.RoomFactory;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;
import com.hotel.api.ApiServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Hotel Reservation System Backend starting... ---");
        
        // 1. Initialize DAOs
        FileRoomDaoImpl roomDao = new FileRoomDaoImpl();
        FileReservationDaoImpl reservationDao = new FileReservationDaoImpl(roomDao);
        
        // 2. Initialize Services
        RoomService roomService = new RoomService(roomDao);
        ReservationService reservationService = new ReservationService(reservationDao, roomService);
        
        // 3. Seed some dummy data if file is empty
        if (roomDao.findAll().isEmpty()) {
            System.out.println("Seeding initial room data...");
            // Standard Rooms
            roomDao.save(RoomFactory.createRoom("STANDARD", "R-101", "101", 100.0));
            roomDao.save(RoomFactory.createRoom("STANDARD", "R-102", "102", 100.0));
            roomDao.save(RoomFactory.createRoom("STANDARD", "R-103", "103", 100.0));
            roomDao.save(RoomFactory.createRoom("STANDARD", "R-104", "104", 105.0));
            roomDao.save(RoomFactory.createRoom("STANDARD", "R-105", "105", 105.0));
            // Deluxe Rooms
            roomDao.save(RoomFactory.createRoom("DELUXE", "R-201", "201", 150.0));
            roomDao.save(RoomFactory.createRoom("DELUXE", "R-202", "202", 150.0));
            roomDao.save(RoomFactory.createRoom("DELUXE", "R-203", "203", 160.0));
            roomDao.save(RoomFactory.createRoom("DELUXE", "R-204", "204", 160.0));
            // Suite Rooms
            roomDao.save(RoomFactory.createRoom("SUITE", "R-301", "301", 300.0));
            roomDao.save(RoomFactory.createRoom("SUITE", "R-302", "302", 320.0));
            roomDao.save(RoomFactory.createRoom("SUITE", "R-303", "Penthouse", 500.0));
        }

        // 4. Start the API Server
        try {
            ApiServer server = new ApiServer(8081, roomService, reservationService);
            server.start();
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
