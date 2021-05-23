package App.services;
import App.Customer;

import java.util.*;
import java.util.regex.Pattern;

public class CustomerService {
    private final static LinkedList<Customer> allCustomer = new LinkedList<>();
    private static CustomerService INSTANCE;
    private CustomerService() {
    }
    public static CustomerService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CustomerService();
        }

        return INSTANCE;
    }
    public static int addCustomer(String fName, String lName, String email) {
        int randomInt = new SplittableRandom().nextInt(1000000, 9999999);
        if (CustomerService.getCustomer(email) != null) throw new IllegalArgumentException("Email already registered");
        allCustomer.add(new Customer(fName, lName, email, randomInt));
        return randomInt;
    }
    public static Customer getCustomer(String email) {
        for (Customer customer:allCustomer) {
            if (customer.getEmail().equalsIgnoreCase(email)) return customer;
        }
        return null;
    }
    public static LinkedList<Customer> getAllCustomer() {
        return allCustomer;
    }
    public static boolean checkEmail(String email) {
        return !Pattern.compile(Customer.regex).matcher(email).matches();
    }
}
