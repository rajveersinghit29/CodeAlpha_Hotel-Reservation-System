package com.hotel.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.model.room.Room;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;
import com.hotel.service.payment.CreditCardPayment;
import com.hotel.service.payment.PaymentStrategy;
import com.hotel.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.UUID;

public class BookingHandler implements HttpHandler {

    private final ReservationService reservationService;
    private final RoomService roomService;

    public BookingHandler(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder buf = new StringBuilder();
                int b;
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }
                String payload = buf.toString();
                
                // Extremely simple manual JSON extraction
                String userName = JsonUtil.extractStringValue(payload, "userName");
                String userEmail = JsonUtil.extractStringValue(payload, "userEmail");
                String userPhone = JsonUtil.extractStringValue(payload, "userPhone");
                String roomId = JsonUtil.extractStringValue(payload, "roomId");
                String checkInStr = JsonUtil.extractStringValue(payload, "checkInDate");
                String checkOutStr = JsonUtil.extractStringValue(payload, "checkOutDate");
                
                User user = new User("U-" + UUID.randomUUID().toString().substring(0, 4), userName, userEmail, userPhone);
                Room room = roomService.getRoomById(roomId);
                
                if (room == null || !room.isAvailable()) {
                    sendError(exchange, 400, "Room is not available or invalid.");
                    return;
                }

                LocalDate checkIn = LocalDate.parse(checkInStr);
                LocalDate checkOut = LocalDate.parse(checkOutStr);
                
                // Using a mock credit card for all API requests for simplicity
                PaymentStrategy paymentStrategy = new CreditCardPayment("1111222233334444", userName, "123");
                
                Reservation res = reservationService.bookRoom(user, room, checkIn, checkOut, paymentStrategy);
                
                String jsonResponse = JsonUtil.reservationToJson(res);
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
                sendError(exchange, 500, "Internal Server Error: " + e.getMessage());
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
    
    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String json = "{\"error\": \"" + message + "\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }
}
