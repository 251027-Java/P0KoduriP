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
        String userString = scan.nextLine();

        while (true) {
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
        String userString = scan.nextLine();
        int userInt;

        while (true) {
            try {
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

    public static boolean GetUserConfirmation(){
        return GetUserString("Are you sure what you entered is okay? (0: No, 1: Yes)", false,
                s -> s.equals("0") || s.equals("1")).equals("1");
    }

    public static void CloseScanner(){
        scan.close();
    }
}
