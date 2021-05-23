package App.api;
import App.Customer;
import App.IRoom;
import App.Reservation;
import App.RoomType;
import App.services.CustomerService;
import App.services.ReservationService;
import java.util.*;
import java.util.stream.Collectors;

public class HotelResource {
    private static Customer currentAccount;

    public static boolean getCustomer(String email){
        return CustomerService.getCustomer(email) != null;
    }

    public static int createACustomerAccount(String email, String firstName, String lastName){
        int account = CustomerService.addCustomer(firstName, lastName, email);
        currentAccount = CustomerService.getAllCustomer().getLast();
        return account;
    }

    public static IRoom getRoom(String roomNumber){
        return ReservationService.getARoom(roomNumber);
    }

    public static void bookARoom(Customer customer, IRoom room, Date checkIn, Date checkOut){
        ReservationService.reserveARoom(customer, room, checkIn, checkOut);
    }

    public static Collection<Reservation> getCustomerReservation(Customer customer){
        if (customer == null) return new LinkedList<>();
        return ReservationService.getCustomersReservations(customer);
    }

    public static LinkedList<IRoom> findRooms(Date checkIn, Date checkOut) {
        LinkedList<IRoom> result = new LinkedList<>();
        ReservationService.getCopy().forEach((value, reservations) -> {
        if (reservations == null || reservations.stream().noneMatch(key -> checkIn.compareTo(key.getCheckOut()) < 0 && 
        checkOut.compareTo(key.getCheckIn()) > 0)) {result.add(value);}
    });
        return result;
    }
    public static boolean signIn(String email, int password) {
        Customer customer = CustomerService.getCustomer(email);
        if (customer != null && customer == currentAccount) throw new IllegalArgumentException("Already Signed In");
        if (customer != null && customer.checkPassword(password)) {
            currentAccount = customer;
            return true;
        }
        return false;
    }

    public static void signOut() {currentAccount = null;}

    public static Customer getCurrentAccount() {return currentAccount;}
    public static boolean checkDate(Customer customer, Date checkIn, Date checkOut) {
        return getCustomerReservation(customer).stream().anyMatch(it ->
        it.getCheckIn().compareTo(checkOut) < 0 && 
        it.getCheckOut().compareTo(checkIn) > 0);
    }
    public static List<IRoom> getAllReservedRooms() {
        List<IRoom> result = new ArrayList<>();
        for (Reservation reservation: ReservationService.printAllReservations()) {
            result.add(reservation.getRoom());
        }
        return result;
    }
    public static List<IRoom> getAllAvaliableRooms() {
        return ReservationService.availableRooms;
    }

    public static boolean checkEmail(String email) {
        return CustomerService.checkEmail(email);
    }
    public static List<IRoom> getByPrice(double startPrice, double endPrice) {
        return ReservationService.printAllRooms().stream().filter(it ->
        it.getRoomPrice() >= startPrice && it.getRoomPrice() <= endPrice).collect(Collectors.toList());
    }
    public static boolean isReserved(IRoom room) {
        return !ReservationService.availableRooms.contains(room);
    }
    public static IRoom lastChanceRoom() {
        Reservation reservation = null;
        for (Reservation R: ReservationService.printAllReservations()) {
            if (reservation == null) {
                reservation = R;
                continue;
            }
            if (R.getCheckOut().compareTo(reservation.getCheckOut()) < 0) reservation = R;
        }
        if (reservation == null) return null;
        return reservation.getRoom();
    }
    public static List<IRoom> displayBySize(String value) {
        RoomType roomSize;
        if (value.equalsIgnoreCase("SINGLE")) roomSize = RoomType.SINGLE;
        else if (value.equalsIgnoreCase("DOUBLE")) roomSize = RoomType.DOUBLE;
        else throw new IllegalArgumentException("Invalid input");
        return ReservationService.printAllRooms().stream().filter(it ->
        it.getRoomType() == roomSize).collect(Collectors.toList());
    }
    public static Date getAvaliablity(IRoom room) {
        Date max_Date = null;
        for (Reservation reservation: ReservationService.getCopy().get(room)) {
            if (max_Date == null || reservation.getCheckOut().compareTo(max_Date) > 0) {
                max_Date = reservation.getCheckOut();
            }
        }
        return max_Date;
    }
}
