package App;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType Type) {
        super(roomNumber, Type, 0);
    }
    @Override
    public String toString() {
        return "" +
                "roomNumber='" + roomNumber + '\'' +
                ", Type=" + Type +
                ", price=" + "FREE" +
                "";
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof IRoom) return ((IRoom) o).getRoomNumber().equals(this.roomNumber);
        return false;
    }
}
