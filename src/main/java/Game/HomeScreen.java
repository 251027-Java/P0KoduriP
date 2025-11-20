package Game;

import Util.UserInput;

public class HomeScreen {
    public static void StartHomeScreen(){
        int input;

        do {
            input = UserInput.GetUserInt("Where would you like to go (0: Quit, 1: Payment Account Screen, 2: Game Screen)",
                    false, n -> (0 <= n && n <= 2));
            switch (input) {
                case 0:
                    System.exit(0);
                case 1:
                    PaymentScreen.StartPaymentScreen();
                    break;
                case 2:
                    break;
            }
        } while (input != 0);
    }
}
