package Util;

import java.util.function.Predicate;
import java.util.Scanner;

public class UserInput {
    private static Scanner scan;

    static {
        scan = new Scanner(System.in);
    }

    public static String GetUserString(String prompt, Predicate<String> validString){
        prompt += ": ";

        IO.print(prompt);
        String userString = scan.nextLine();

        while (!validString.test(userString)) {
            IO.println("Invalid string input.\n");
            IO.print(prompt);
            userString = scan.nextLine();
        }

        return userString;
    }
    public static String GetUserString(String prompt) { return GetUserString(prompt, s -> true); }

    public static int GetUserInt(String prompt, Predicate<Integer> validInt){
        prompt += ": ";

        IO.print(prompt);
        String userString = scan.nextLine();
        int userInt;

        while (true) {
            try {
                userInt = Integer.parseInt(userString);

                if (validInt.test(userInt)) return userInt;

                IO.println("Invalid integer input.\n");
                IO.print(prompt);
            } catch (NumberFormatException e){
                IO.println("Enter an integer (that satisfies the prompt).\n");
                IO.print(prompt);
            }
        }
    }

    public static void CloseScanner(){
        scan.close();
    }
}
