package Util;

import java.util.function.Predicate;
import java.util.Scanner;

public class UserInput {
    private static Scanner scan;

    static {
        scan = new Scanner(System.in);
    }

    public static String GetUserString(String prompt, boolean getConfirmation, Predicate<String> validString){
        prompt += ": ";

        IO.print(prompt);
        String userString;

        while (true) {
            userString = scan.nextLine();
            if (validString.test(userString)){
                if (!getConfirmation || GetUserConfirmation()) return userString;
            }
            else IO.println("Invalid input.");

            IO.print("\n" + prompt);
        }
    }
    public static String GetUserString(String prompt, boolean getConfirmation) {
        return GetUserString(prompt, getConfirmation, s -> true);
    }

    public static int GetUserInt(String prompt, boolean getConfirmation, Predicate<Integer> validInt){
        prompt += ": ";

        IO.print(prompt);
        String userString;
        int userInt;

        while (true) {
            try {
                userString = scan.nextLine();
                userInt = Integer.parseInt(userString);

                if (validInt.test(userInt)){
                    if (!getConfirmation || GetUserConfirmation()) return userInt;
                }
                else IO.println("Invalid integer input.");

                IO.print("\n" + prompt);
            } catch (NumberFormatException e){
                IO.println("Enter an integer (that satisfies the prompt).\n");
                IO.print(prompt);
            }
        }
    }
    public static int GetUserInt(String prompt, boolean getConfirmation) {
        return GetUserInt(prompt, getConfirmation, n -> true);
    }

    public static long GetUserLong(String prompt, boolean getConfirmation, Predicate<Long> validLong){
        prompt += ": ";

        IO.print(prompt);
        String userString;
        long userLong;

        while (true) {
            try {
                userString = scan.nextLine();
                userLong = Long.parseLong(userString);

                if (validLong.test(userLong)){
                    if (!getConfirmation || GetUserConfirmation()) return userLong;
                }
                else IO.println("Invalid integer input.");

                IO.print("\n" + prompt);
            } catch (NumberFormatException e){
                IO.println("Enter an integer (that satisfies the prompt).\n");
                IO.print(prompt);
            }
        }
    }
    public static long GetUserLong(String prompt, boolean getConfirmation) {
        return GetUserLong(prompt, getConfirmation, n -> true);
    }

    public static boolean GetUserConfirmation(){
        return GetUserString("Are you sure what you entered is okay? (0: No, 1: Yes)", false,
                s -> s.equals("0") || s.equals("1")).equals("1");
    }
    public static void UserPressAnyKeyToContinue(){
        GetUserString("\nType any key and hit enter to continue.", false);
    }

    public static void CloseScanner(){
        scan.close();
    }
}
