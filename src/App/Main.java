package App;

import App.api.MainMenu;
import App.services.ReservationService;

import java.time.DateTimeException;
import java.util.Scanner;

import static App.api.MainMenu.currentOption;

public class Main {
    static MainMenu menu = new MainMenu();
    static boolean error = false;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReservationService.addRoom(new Room("100", RoomType.DOUBLE, 20));
        ReservationService.addRoom(new FreeRoom("101", RoomType.SINGLE));
        while (true) {
            System.out.println(text());
            try {
                error = false;
                String input = scanner.nextLine();
                if (input.isBlank()) 
                    throw new IllegalArgumentException("Please enter a valid input");
                if (currentOption == null) {
                    menu.selectOption(input);
                    continue;
                }
                currentOption.accept(input);
                
            } catch (DateTimeException | IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                if (MainMenu.getCurrent() == 0) {
                    error = true;
                    continue;
                }
                System.out.println("""
                1_Retry
                2_Go back
                3_Back to Main Menu""");
                while (MainMenu.errorCase(scanner.nextLine())) {
                    System.out.println("Please enter a valid input");
                }
            }
        }
    }
    private static String text() {
        return MainMenu.getNextText();
    }
}
