package Game;

import Util.UserInput;

public class PaymentScreen {
    public static void StartPaymentScreen(){
        int input;

        do {
            input = UserInput.GetUserInt("\n0: Back\n1: Add Payment Account\n2: Remove Payment Account\n" +
                    "3: Add Game Accounts to Payment Account\n4: Remove Game Accounts from Payment Account\n" +
                    "5: Deposit Money\n6: Withdraw Money\n7: View Balance\nWhat would you like to do?",false,
                    n -> (0 <= n && n <= 6));
            switch (input) {
                case 1:
                    AddPaymentAccount();
                    break;
                case 2:
                    RemovePaymentAccount();
                    break;
                case 3:
                    AttachGameAccounts();
                    break;
                case 4:
                    DetachGameAccounts();
                    break;
                case 5:
                    MakeDeposit();
                    break;
                case 6:
                    MakeWithdrawal();
                    break;
                case 7:
                    ViewBalance();
                    break;
            }
        } while (input != 0);
    }

    private static void AddPaymentAccount(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        int expmo = getUserExpirationMonth();
        if (expmo == 0) return;

        int expyr = getUserExpirationYear();
        if (expyr == 0) return;

        int pin = getUserPIN();
        if (pin == -1) return;

        if (!UserInput.GetUserConfirmation()) return;


    }
    private static void RemovePaymentAccount(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        int expmo = getUserExpirationMonth();
        if (expmo == 0) return;

        int expyr = getUserExpirationYear();
        if (expyr == 0) return;

        int pin = getUserPIN();
        if (pin == -1) return;

        if (!UserInput.GetUserConfirmation()) return;


    }
    private static void AttachGameAccounts(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;


    }
    private static void DetachGameAccounts(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;


    }
    private static void MakeDeposit(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        int expmo = getUserExpirationMonth();
        if (expmo == 0) return;

        int expyr = getUserExpirationYear();
        if (expyr == 0) return;

        int pin = getUserPIN();
        if (pin == -1) return;

        if (!UserInput.GetUserConfirmation()) return;


    }
    private static void MakeWithdrawal(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        int expmo = getUserExpirationMonth();
        if (expmo == 0) return;

        int expyr = getUserExpirationYear();
        if (expyr == 0) return;

        int pin = getUserPIN();
        if (pin == -1) return;

        if (!UserInput.GetUserConfirmation()) return;


    }
    private static void ViewBalance(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        int expmo = getUserExpirationMonth();
        if (expmo == 0) return;

        int expyr = getUserExpirationYear();
        if (expyr == 0) return;

        int pin = getUserPIN();
        if (pin == -1) return;

        if (!UserInput.GetUserConfirmation()) return;


    }

    private static Long getUserCardNumber(){
        return UserInput.GetUserLong("Enter the card number (0 - 9999 9999 9999 9999 or -1 to go back)", false,
                n -> -1 <= n && n <= 9999999999999999L);
    }
    private static int getUserExpirationMonth(){
        return UserInput.GetUserInt("Enter the expiration month (1-12 or 0 to go back)", false, n->0<=n&&n<=12);
    }
    private static int getUserExpirationYear(){
        return UserInput.GetUserInt("Enter the expiration year (or 0 to go back)", false);
    }
    private static int getUserPIN(){
        return UserInput.GetUserInt("Enter the PIN (0-999 or -1 to go back)", false, n->-1<=n&&n<=999);
    }
}
