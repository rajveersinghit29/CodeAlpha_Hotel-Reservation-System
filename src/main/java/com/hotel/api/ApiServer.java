package com.hotel.api;

import com.sun.net.httpserver.HttpServer;
import com.hotel.service.RoomService;
import com.hotel.service.ReservationService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {
    private final int port;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private HttpServer server;

    public ApiServer(int port, RoomService roomService, ReservationService reservationService) {
        this.port = port;
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Define endpoints
        server.createContext("/api/rooms", new RoomHandler(roomService));
        server.createContext("/api/book", new BookingHandler(reservationService, roomService));
        
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("REST API Server started on port " + port);
        System.out.println("Available endpoints:");
        System.out.println("  GET  /api/rooms");
        System.out.println("  POST /api/book");
    }
    
    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped.");
        }
    }
}
