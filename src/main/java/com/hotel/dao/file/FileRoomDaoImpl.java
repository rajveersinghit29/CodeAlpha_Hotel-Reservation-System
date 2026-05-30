package com.hotel.dao.file;

import com.hotel.dao.RoomDao;
import com.hotel.model.room.Room;
import com.hotel.model.room.RoomFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of RoomDao using CSV storage.
 */
public class FileRoomDaoImpl implements RoomDao {
    private static final String FILE_PATH = "src/main/resources/data/rooms.csv";

    public FileRoomDaoImpl() {
        // Ensure directory and file exist
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating rooms file: " + e.getMessage());
        }
    }

    @Override
    public Room findById(String roomId) {
        List<Room> rooms = findAll();
        for (Room room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming CSV format: roomId,roomNumber,baseRate,isAvailable,type
                String[] data = line.split(",");
                if (data.length == 5) {
                    String roomId = data[0];
                    String roomNumber = data[1];
                    double baseRate = Double.parseDouble(data[2]);
                    boolean isAvailable = Boolean.parseBoolean(data[3]);
                    String type = data[4];
                    
                    Room room = RoomFactory.createRoom(type, roomId, roomNumber, baseRate);
                    room.setAvailable(isAvailable);
                    rooms.add(room);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading rooms from file: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public void save(Room room) {
        List<Room> rooms = findAll();
        boolean updated = false;
        
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId().equals(room.getRoomId())) {
                rooms.set(i, room);
                updated = true;
                break;
            }
        }
        
        if (!updated) {
            rooms.add(room);
        }
        
        saveAll(rooms);
    }

    @Override
    public void delete(String roomId) {
        List<Room> rooms = findAll();
        rooms.removeIf(room -> room.getRoomId().equals(roomId));
        saveAll(rooms);
    }

    private void saveAll(List<Room> rooms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Room room : rooms) {
                // We extract the type by class name (e.g., StandardRoom -> STANDARD)
                String type = room.getClass().getSimpleName().replace("Room", "").toUpperCase();
                pw.println(room.getRoomId() + "," + room.getRoomNumber() + "," + room.getBaseRate() + "," + room.isAvailable() + "," + type);
            }
        } catch (IOException e) {
            System.err.println("Error writing rooms to file: " + e.getMessage());
        }
    }
}
