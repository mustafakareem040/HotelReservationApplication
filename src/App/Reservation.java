package App;

import java.util.Date;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkIn;
    private Date checkOut;
    public Reservation(Customer customer, IRoom room, Date checkIn, Date checkOut){
        this.customer = customer;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
    public Customer getCustomer() {return customer;}
    public IRoom getRoom() {return room;}
    public Date getCheckIn() {return checkIn;}
    public Date getCheckOut() {return checkOut;}
    @Override
    public String toString() {
        return "Customer{" + customer +
                "}, room{" + room +
                "}, checkIn{" + checkIn +
                "}, checkOut{" + checkOut + "}";
    }
}
