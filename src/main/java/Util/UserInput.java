package Util;

import java.util.function.Predicate;
import java.util.Scanner;

public class UserInput {
    private static Scanner scan;

    static {
        scan = new Scanner(System.in);
    }

    public static String GetUserString(String prompt, Predicate<String> validString){
        IO.println(prompt);
        String userString = scan.nextLine();

        while (!validString.test(userString)) {
            IO.println("Invalid string input.\n");
            IO.println(prompt);
            userString = scan.nextLine();
        }

        return userString;
    }
    public static String GetUserString(String prompt) { return GetUserString(prompt, s -> true); }

    public static int GetUserInt(String prompt, Predicate<Integer> validInt){
        IO.println(prompt);
        String userString = scan.nextLine();
        int userInt;

        while (true) {
            try {
                userInt = Integer.parseInt(userString);

                if (validInt.test(userInt)) return userInt;

                IO.println("Invalid integer input.\n");
            } catch (NumberFormatException e){
                IO.println("Enter an integer that satisfies the prompt.\n");
            }
        }
    }

    public static void CloseScanner(){
        scan.close();
    }
}
