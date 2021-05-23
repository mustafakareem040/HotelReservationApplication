package App.api;
import App.Customer;
import App.IRoom;
import App.Reservation;
import App.services.CustomerService;
import App.services.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

public class AdminResource {
    public static Customer getCustomer(String email) {
        return CustomerService.getCustomer(email);
    }
    public static void addRooms(List<IRoom> rooms) {
        for (IRoom room: rooms) {
            ReservationService.addRoom(room);
        }
    }
    public static List<IRoom> getAllRooms(){
        return ReservationService.printAllRooms().stream().collect(Collectors.toList());
    }
    public static List<Customer> getAllCustomers(){
        return CustomerService.getAllCustomer();
    }
    public static List<Reservation> displayAllReservations() {
        return ReservationService.printAllReservations();
    }
}
