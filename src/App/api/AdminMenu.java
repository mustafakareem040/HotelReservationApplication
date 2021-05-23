package App.api;
import App.FreeRoom;
import App.Room;
import App.RoomType;
import App.services.ReservationService;

public class AdminMenu {
    public static final String MAIN_TEXT = """
            1_See all Customers
            2_See all Rooms
            3_See all Reservations
            4_Add a room
            5_Back to Main Menu""";
    public static String tempRoomID;
    public static RoomType tempRoomType;
    public static boolean checkRoom(String roomID) {
        return ReservationService.getARoom(roomID) != null;
    }
    public static RoomType getType(String value) {
        if (value.equalsIgnoreCase("SINGLE")) return RoomType.SINGLE;
        if (value.equalsIgnoreCase("DOUBLE")) return RoomType.DOUBLE;
        throw new IllegalArgumentException("Please enter a valid room size");
    }
    public static void makeARoom(String roomID, RoomType roomType, double price) {
        if (price == 0)  ReservationService.addRoom(new FreeRoom(roomID, roomType));
        else ReservationService.addRoom(new Room(roomID, roomType, price));
    }
}
