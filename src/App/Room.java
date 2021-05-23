package App;

public class Room implements IRoom {
    final String roomNumber; //NOT Private to allow FreeRoom super it
    final RoomType Type;
    final double price;
    public Room(String roomNumber, RoomType Type, double price) {
        this.roomNumber = roomNumber;
        this.Type = Type;
        this.price = price;
    }
    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return Type;
    }

    @Override
    public boolean isFree() {
        return price == 0;
    }

    @Override
    public String toString() {
        return "" +
                "roomNumber='" + roomNumber + '\'' +
                ", Type=" + Type +
                ", price=" + price +
                "";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IRoom) return ((IRoom) o).getRoomNumber().equals(this.roomNumber);
        return false;
    }

    @Override
    public int hashCode() {
        return roomNumber.hashCode();
    }
}
