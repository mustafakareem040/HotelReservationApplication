package App.services;

import App.Customer;
import App.IRoom;
import App.Reservation;
import java.util.*;

public class ReservationService {
    private static final Map<IRoom, List<Reservation>> ROOMS = new HashMap<>();
    public static final List<IRoom> availableRooms = new ArrayList<>();
    private static ReservationService INSTANCE;
    private ReservationService() {
    }
    public static ReservationService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ReservationService();
        }
        return INSTANCE;
    }
    public static void addRoom(IRoom room) {
        if (ROOMS.containsKey(room)) throw new IllegalArgumentException("Room already exists");
        ROOMS.put(room, null);
        availableRooms.add(room);
    }
    public static IRoom getARoom(String roomID) {
        for (IRoom room: ROOMS.keySet()) {
            if (room.getRoomNumber().equals(roomID)) return room;
        }
        return null;
    }
    public static IRoom getRandomRoom() {
        if (availableRooms.isEmpty()) return null;
        return availableRooms.get(new Random().nextInt(ROOMS.size()-2));
    }

    public static void reserveARoom(Customer customer, IRoom room, Date checkIn, Date checkOut) {
        if (!ROOMS.containsKey(room)) throw new IllegalArgumentException("The room is not exists");
        if (ROOMS.keySet().contains(room)) {
            ROOMS.forEach((value, reservations) -> {
                if (value == room && reservations != null && reservations.stream().anyMatch(key -> 
                checkIn.compareTo(key.getCheckOut()) < 0 && 
                (checkIn.compareTo(key.getCheckIn()) > 0 || checkOut.compareTo(key.getCheckIn()) > 0)))
                throw new IllegalArgumentException("This room is already reserved in this range");
            });
        }
        Reservation reserve = new Reservation(customer, room, checkIn, checkOut);
        ROOMS.put(room, List.of(reserve));
        availableRooms.remove(room);
    }
    public static List<Reservation> getCustomersReservations(Customer customer) {
        List<Reservation> list = new LinkedList<>();
        for (List<Reservation> a : ROOMS.values()) {
            if (a != null)
            for (Reservation c : a)
            if (c.getCustomer() == customer) list.add(c);
        }
        return list;
    }
    public static List<Reservation> printAllReservations() {
        List<Reservation> result = new ArrayList<>();
        for (List<Reservation> reservation: ROOMS.values()) {
            if (reservation != null)
            result.addAll(reservation);
        }
        return result;
    }
    public static List<IRoom> printAllRooms() {
        return List.copyOf(ROOMS.keySet());
    }
    public static Map<IRoom, List<Reservation>> getCopy() {
        return new HashMap<>(ROOMS);
    }
}
