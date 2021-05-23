package App.api;

import App.IRoom;
import App.Reservation;
import java.util.*;
import java.time.DateTimeException;
import java.util.function.Consumer;

public class MainMenu {
    private static String emailTemp;
    private static String nameTemp;
    private static Double tempStartPrice;
    public static Date tempCheckOut;
    public static boolean isBook = false;
    private static Date tempCheckIn;
    private static final String OPTION1_TITLE = "Find and reserve a room";
    public static final String NUM_QUESTION = "Please enter a number from 1 to ";
    private static final String MAIN_TITLE = "Welcome to Hotel Reservation Application";
    private static String title = MAIN_TITLE;
    public static String MAIN_TEXT = """
            1_Find and reserve a room
            2_See my reservations
            3_Login Page
            4_Admin
            5_Exit""";
    public static final String OPTION1_TEXT = """
            1_List all available rooms
            2_Search a room
            3_Reserve a room
            4_Back to main menu""";
    public static final String OPTION3_TEXT = "Enter your email";
    public static final String OPTION4_TEXT = AdminMenu.MAIN_TEXT;
    private static String currentText = MAIN_TEXT;
    private static byte current = 0;
    public static Consumer<String> currentOption = null;
    public static String getNextText() {
        if (currentOption == null) {
            return onEach(true) + currentText + onEach(false) + NUM_QUESTION + range();
        }
        return currentText;
    }
    public static String onEach(boolean with) {
        if (with) return String.format("-------------%s-------------", title) + "\n";
        return ("\n--------------------------" + "-".repeat(title.length()) + "\n");
    }

    private static void option1(String value) {
        switch (current) {
            case 0 -> {
                switch (value) {
                    case "1" -> System.out.println(HotelResource.getAllAvaliableRooms());
                    case "2" -> {
                        current = 2;
                         textAbv("""
                        1_by roomID
                        2_by Price Range
                        3_by Room Size
                        4_Go Back""");
                    }
                    case "3" -> {
                        if (AdminResource.getAllRooms().isEmpty())
                            throw new IllegalArgumentException("Sorry currently there is no available rooms" +
                                    ", please come back again");
                        current = 3;
                        currentText = "Enter checkIn date format YYYY/MM/DD";
                    }
                    case "4" -> {
                        currentOption = null;
                        title = MAIN_TITLE;
                        currentText = MAIN_TEXT;
                    }
                    default -> System.out.println("Please enter a valid input");
                    
                }
            }
            case 2 -> {
                switch (value) {
                    case "1" -> {
                        current = 6;
                        currentText = "Enter room ID: ";
                    }
                    case "2" -> {
                        current = 7;
                        currentText = "Enter startPrice: ";
                    }
                    case "3" -> {
                        current = 9;
                        currentText = "Enter room size (SINGLE or DOUBLE)";
                    }
                    case "4" -> {
                        title = OPTION1_TITLE;
                        textAbv(OPTION1_TEXT);
                        current = 0;
                    }
                    default -> System.out.println("Please enter a valid input");
                    
                }
            }
            case 3 -> {
                 tempCheckIn = checkDate(value);
                        if (HotelResource.getCustomerReservation(HotelResource.getCurrentAccount()).stream().anyMatch(it ->
                        tempCheckIn.compareTo(it.getCheckOut()) < 0 && tempCheckIn.compareTo(it.getCheckIn()) >= 0))
                    throw new IllegalArgumentException("You already reserved a room in this checkIn date");
                 if (new Date().compareTo(tempCheckIn) >= 0) throw new DateTimeException("checkIn date must be in present");
                 current++;
                 currentText = "Enter checkOut in date format YYYY/MM/DD";
            }
            case 4 -> {
                tempCheckOut = checkDate(value);
                if (HotelResource.getCustomerReservation(HotelResource.getCurrentAccount()).stream().anyMatch(it -> 
                (tempCheckOut.compareTo(it.getCheckOut()) < 0 && tempCheckIn.compareTo(it.getCheckIn()) > 0)||
                tempCheckIn.compareTo(it.getCheckIn()) < 0 && tempCheckOut.compareTo(it.getCheckIn()) > 0))
                    throw new IllegalArgumentException("You already reserved a room in checkIn-checkOut range");
                if(tempCheckOut.compareTo(tempCheckIn) <= 0)
                    throw new DateTimeException("checkOut date must be after checkIn date");
                LinkedList<IRoom> tempRoom = HotelResource.findRooms(tempCheckIn, tempCheckOut);
                if (tempRoom.isEmpty()) {
                    System.out.println("Sorry there is no avaliable rooms at your date range");
                    Calendar a = Calendar.getInstance();
                    Calendar b = Calendar.getInstance();
                    a.setTime(tempCheckIn);
                    a.add(Calendar.DATE, 7);
                    b.setTime(tempCheckOut);
                    b.add(Calendar.DATE, 7);
                    tempRoom = HotelResource.findRooms(a.getTime(), b.getTime());
                    if (tempRoom.isEmpty()) {
                        Date available = HotelResource.getAvaliablity(HotelResource.lastChanceRoom());
                        System.out.println("We recommend you this room:\n" + HotelResource.lastChanceRoom().toString());
                        System.out.println("Will avaliable after "+ available);
                        a.setTime(tempCheckIn);
                        b.setTime(tempCheckOut);
                        Calendar between = checkDate(b.get(Calendar.YEAR)-a.get(Calendar.YEAR), b.get(Calendar.MONTH)-
                        a.get(Calendar.MONTH), b.get(Calendar.DAY_OF_MONTH) - a.get(Calendar.DAY_OF_MONTH));
                        a.setTime(available);
                        b.setTime(available);
                        b.add(Calendar.DATE, between.get(Calendar.DATE));
                        tempCheckIn = a.getTime();
                        tempCheckOut = b.getTime();
                        currentText = "Select room ID or type BACK to Back to Main Menu";
                        current++;
                        tempRoom = null;
                        return;
                    }
                    else {
                        System.out.println("Here is our recommendation avaliable after 7days of your date: " + tempRoom.getFirst().toString());
                        currentText = "Select room ID or type BACK to Back to Main Menu";
                        tempCheckIn = a.getTime();
                        tempCheckOut = b.getTime();
                        current++;
                        tempRoom = null;
                        return;
                    }
                }
                System.out.println("Here are avaliable rooms for your range");
                for (IRoom room: tempRoom) {
                    System.out.println(room);
                }
                System.out.println("Here's our recommendation");
                System.out.println(tempRoom.getFirst());
                currentText = "Select room ID or type BACK to Back to Main Menu";
                current++;
                tempRoom = null;
            }
            case 5 -> {
                if (value.equals("BACK")) {
                    currentText = MAIN_TEXT;
                    title = MAIN_TITLE;
                    currentOption = null;
                    current = 0;
                }
                IRoom temp = HotelResource.getRoom(value);
                if (temp == null) throw new IllegalArgumentException("Couldn't find a room with this number");
                HotelResource.bookARoom(HotelResource.getCurrentAccount(), temp, tempCheckIn, tempCheckOut);
                System.out.println("Booked!");
                title = OPTION1_TITLE;
                textAbv(OPTION1_TEXT);
                current = 0;
            }
            case 6 -> {
                IRoom temp = HotelResource.getRoom(value);
                title = OPTION1_TITLE;
                textAbv(OPTION1_TEXT);
                current = 0;
                if (temp == null) System.out.println("Couldn't find a room with this number");
                else System.out.println("["+ temp + ", Reserved: " + HotelResource.isReserved(temp) + "]");
            }
            case 7 -> {
                try {
                    tempStartPrice = Double.parseDouble(value);
                    if (Math.abs(tempStartPrice) != tempStartPrice)
                        throw new IllegalArgumentException("Price can't be a nagitive number");
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid number format");
                }
                currentText = "Enter endPrice: ";
                current++;
            }
            case 8 -> {
                try {
                    double tempEndPrice = Double.parseDouble(value);
                    if (Math.abs(tempEndPrice) != tempEndPrice)
                        throw new IllegalArgumentException("Price can't be a nagitive number");
                    if (tempEndPrice < tempStartPrice)
                        throw new IllegalArgumentException("End price can't be smaller than start price");
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid number format");
                }
                List<IRoom> temp = HotelResource.getByPrice(tempStartPrice, Double.parseDouble(value));
                if (temp.isEmpty()) {
                    System.out.println("Sorry, there is no rooms at this price range");
                }
                for (IRoom room: temp) {
                    System.out.println(room.toString() + ", isReserved: " + HotelResource.isReserved(room));
                }
                current = 0;
                temp = null;
                textAbv(OPTION1_TEXT);
            }
            case 9 -> {
                if (value.equalsIgnoreCase("SINGLE") || value.equalsIgnoreCase("DOUBLE")) {
                    List<IRoom> temp = HotelResource.displayBySize(value);
                    if (temp.isEmpty()) {
                        System.out.println("Sorry, there no rooms at this size");
                    }
                    for (IRoom room: temp) {
                        System.out.println(room.toString() + ", isReserved: " + HotelResource.isReserved(room));
                    }
                }
                else throw new IllegalArgumentException("Input must be SINGLE or DOUBLE");
                current = 0;
                textAbv(OPTION1_TEXT);
            }
        }
    }

    private static void option2(){

        Collection<Reservation> temp = HotelResource.getCustomerReservation(HotelResource.getCurrentAccount());
        if (temp.isEmpty()) {
            System.out.println("You don't have any reservations yet");
            return;
        }
        temp.forEach(System.out::println);
    }

    private static void option3(String value) {
        switch (current) {
            case 0 -> {
                switch (value) {
                    case "1" -> {
                        currentText = "Enter your email: ";
                        current++;
                    }
                    case "2" -> {
                        currentText = "Enter your email";
                        current = 3;
                    }
                    case "3" -> {
                        title = MAIN_TITLE;
                        currentText = MAIN_TEXT;
                        currentOption = null;
                    }
                    default -> System.out.println("Please enter a valid input");
                }
            }
            case 1 -> {
                if (HotelResource.checkEmail(value)) 
                  throw new IllegalArgumentException("Invalid email format");
                emailTemp = value;
                currentText = "Enter your password: ";
                current++;
            }
            case 2 -> {
                try {
                    Integer.parseInt(value);
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid password format");
                }
                if (!HotelResource.signIn(emailTemp, Integer.parseInt(value))) {
                  currentText = "Enter your email: ";
                  current = 1;
                  throw new IllegalArgumentException("Invalid email or password");
                }
                System.out.println("Signed in!");
                MAIN_TEXT = MAIN_TEXT.replaceAll("Login Page", "Logout");
                currentText = MAIN_TEXT;
                title = MAIN_TITLE;
                currentOption = null;
                emailTemp = null;
                current = 0;
            }
            case 3 -> {
                if (HotelResource.checkEmail(value))
                    throw new IllegalArgumentException("Invalid email format");
                if (HotelResource.getCustomer(value))
                    throw new IllegalArgumentException("Email already registered");
                emailTemp = value;
                currentText = "Enter your first name";
                current++;
            }
            case 4 -> {
                if (value.split(" ").length > 1 || value.isBlank()) 
                    throw new IllegalArgumentException("Please enter a valid name");
                currentText = "Enter your last name";
                current++;
                nameTemp = value;
            }
            case 5 -> {
                if (value.split(" ").length > 1 || value.isBlank()) throw new IllegalArgumentException("Please enter a valid name");
                System.out.println("Done!, Your password is "+HotelResource.createACustomerAccount(emailTemp, nameTemp, value)
                + "\nPlease save it for later time");
                MAIN_TEXT = MAIN_TEXT.replaceAll("Login Page", "Logout");
                currentText = MAIN_TEXT;
                title = MAIN_TITLE;
                emailTemp = null;
                nameTemp = null;
                currentOption = null;
                current = 0;
            }
            default -> System.out.println("Please enter a valid input");
        }
    }

    private static void option4(String value) {
        switch (current) {
            case 0 -> {
                switch (value) {
                    case "1" -> AdminResource.getAllCustomers().forEach(System.out::println);
                    case "2" -> AdminResource.getAllRooms().forEach(System.out::println);
                    case "3" -> AdminResource.displayAllReservations().forEach(System.out::println);
                    case "4" -> {
                        currentText = "Enter room id";
                        current++;
                    }
                    case "5" -> {
                        currentOption = null;
                        title = MAIN_TITLE;
                        currentText = MAIN_TEXT;
                    }
                    default -> System.out.println("Please enter a valid input");
                }
            }
            case 1 -> {
                if (AdminMenu.checkRoom(value)) throw new IllegalArgumentException("Room already exists");
                AdminMenu.tempRoomID = value;
                current++;
                currentText = "Enter room size";
            }
            case 2 -> {
                AdminMenu.tempRoomType = AdminMenu.getType(value);
                current++;
                currentText = "Enter room price";
            }
            case 3 -> {
                AdminMenu.makeARoom(AdminMenu.tempRoomID, AdminMenu.tempRoomType, Double.parseDouble(value));
                System.out.println("Done!");
                AdminMenu.tempRoomID = null;
                AdminMenu.tempRoomType = null;
                current = 0;
                textAbv(AdminMenu.MAIN_TEXT);
            }
            default -> System.out.println("Please enter a valid input");

        }
    }

    public void selectOption(String option) {
        switch (option) {
            case "1" -> {
                if (HotelResource.getCurrentAccount() == null) {
                    System.out.println("You must sign in or create a new account to find and reserve a room");
                    return;
                }
                currentOption = MainMenu::option1;
                title = OPTION1_TITLE;
                textAbv(OPTION1_TEXT);
            }
            case "2" -> option2();
            case "3" -> {
                if (HotelResource.getCurrentAccount() != null) {
                    HotelResource.signOut();
                    System.out.println("Signed out!");
                    MAIN_TEXT = MAIN_TEXT.replaceAll("Logout", "Login Page");
                    title = MAIN_TITLE;
                    currentText = MAIN_TEXT;
                    currentOption = null;
                    return;
                }
                currentOption = MainMenu::option3;
                title = "Login Page";
                textAbv("""
                1_Sign In
                2_Create a new account
                3_Back to Main Menu""");
            }
            case "4" -> {
                currentOption = MainMenu::option4;
                title = "Admin";
                textAbv(OPTION4_TEXT);
            }
            case "5" -> System.exit(1);
            default -> System.out.println("Please enter a valid input");
        }
    }
    public static byte range() {
        return (byte)currentText.lines().count();
    }
    public static Date checkDate(String date) {
        String[] d = date.split("([-/,])");
        if (d.length != 3 || d[0].length() != 4 || d[1].length() != 2 || d[2].length() != 2)
            throw new DateTimeException("Invalid date format");
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR, Integer.parseInt(d[0]));
        time.set(Calendar.MONTH, Integer.parseInt(d[1])+1);
        time.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d[2]));
        time.set(Calendar.HOUR, 0);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.MILLISECOND, 0);
        time.set(Calendar.SECOND, 0);
        return time.getTime();
    }
    public static Calendar checkDate(int year, int month, int day) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month);
        time.set(Calendar.DAY_OF_MONTH, day);
        time.set(Calendar.HOUR, 0);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.MILLISECOND, 0);
        time.set(Calendar.SECOND, 0);
        return time;
    }
    public static void textAbv(String text) {
        currentText = onEach(true) + text + onEach(false);
        currentText += NUM_QUESTION + (range()-2);
    }
    public static byte getCurrent() {
        return current;
    }
    public static boolean errorCase(String input) {
        switch (input) {
            case "1" -> {
                return false;
            }
            case "2" -> {
                switch (title) {
                    case MAIN_TITLE -> currentText = MAIN_TEXT;
                    case OPTION1_TITLE -> textAbv(OPTION1_TEXT);
                    case "Login Page" ->
                        textAbv("""
                        1_Sign In
                        2_Create a new account
                        3_Back to Main Menu""");
                
                    case "Admin" -> textAbv(AdminMenu.MAIN_TEXT);
                    default -> {
                        title = MAIN_TITLE;
                        currentText = MAIN_TEXT;
                        currentOption = null;
                    }
                }
            current = 0;
            return false;
            }
        case "3" -> {
            title = MAIN_TITLE;
            currentText = MAIN_TEXT;
            currentOption = null;
            current = 0;
            return false;}
        default -> {return true;}
        }
    }
}

