package App;

import App.api.HotelResource;
import App.api.MainMenu;
import App.services.CustomerService;
import App.services.ReservationService;
import java.util.SplittableRandom;
public class Tester {
    public static void main(String[] args) {
        Customer customer = new Customer("first", "second","email@gmail.com", new SplittableRandom().nextInt(1000000, 9999999));
        CustomerService.addCustomer("first", "s", "email@gmail.com");
        ReservationService.addRoom(new Room("100", RoomType.DOUBLE, 20.0));
        ReservationService.addRoom(new FreeRoom("101", RoomType.SINGLE));
        ReservationService.addRoom(new Room("102", RoomType.SINGLE, 1));
        ReservationService.reserveARoom(customer, ReservationService.getARoom("100"), 
        MainMenu.checkDate("2021-09-03"), MainMenu.checkDate("2021-09-25"));
        System.out.println(ReservationService.printAllReservations().get(0).getCheckIn());
        ReservationService.reserveARoom(customer, ReservationService.getARoom("101"), 
        MainMenu.checkDate("2021-09-05"), MainMenu.checkDate("2021-09-24"));
        ReservationService.reserveARoom(customer, ReservationService.getARoom("102"), 
        MainMenu.checkDate("2021-09-24"), MainMenu.checkDate("2021-09-30"));
        System.out.println(HotelResource.findRooms(MainMenu.checkDate("2021-09-16"), MainMenu.checkDate("2021-09-30")));
        System.out.println(ReservationService.printAllReservations().get(0).getCheckIn());
        System.out.println(HotelResource.getCustomerReservation(customer));
        System.out.println(HotelResource.lastChanceRoom());
    }
}

