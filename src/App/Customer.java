package App;
import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int password;
    public static final String regex = "(.+)@(.+).(.net|.org|.com)$"; //NOT Private because using it's immutable in class so final better
    public Customer(String firstName, String lastName, String email, int password) {
        if (!Pattern.compile(regex).matcher(email).matches()) throw new IllegalArgumentException("Invalid Email");
        else {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }
    }
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}
    public boolean checkPassword(int password) {return password == this.password;}
    @Override
    public String toString() {
        return "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + "'" ;
    }

    @Override
    public boolean equals(Object o) {
        if (this.email == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
