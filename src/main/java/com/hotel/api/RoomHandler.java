package com.hotel.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.hotel.service.RoomService;
import com.hotel.util.JsonUtil;
import com.hotel.model.room.Room;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RoomHandler implements HttpHandler {

    private final RoomService roomService;

    public RoomHandler(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            List<Room> rooms;
            
            if (query != null && query.startsWith("category=")) {
                String category = query.split("=")[1];
                rooms = roomService.searchAvailableRooms(category);
            } else {
                // If no category specified, return all available rooms (using a hacky approach for this simple example)
                rooms = roomService.searchAvailableRooms("STANDARD");
                rooms.addAll(roomService.searchAvailableRooms("DELUXE"));
                rooms.addAll(roomService.searchAvailableRooms("SUITE"));
            }

            String jsonResponse = JsonUtil.roomListToJson(rooms);
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
