package GamePackage;

import Application.Game;
import Service.DataService;
import Util.UserInput;

import java.util.List;

public class PaymentScreen {
    private static DataService serv;

    public static void StartPaymentScreen(){
        serv = Game.getInstance().GetDataService();

        int input;

        do {
            input = UserInput.GetUserInt("\n0: Back\n1: Add Payment Account\n2: Remove Payment Account\n" +
                    "3: Add Game Accounts to Payment Account\n4: Remove Game Accounts from Payment Account\n" +
                    "5: Deposit Money\n6: Withdraw Money\n7: View Balance\nEnter which # you want to do",false,
                    n -> (0 <= n && n <= 7));
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

        if (serv.GetPaymentAccountExists(cardno)){
            IO.println("That account number already exists!");
            return;
        }

        serv.AddNewCard(cardno, expmo, expyr, pin);
        IO.println("Account added!");
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

        if (!serv.GetPaymentAccountExists(cardno, expmo, expyr, pin)){
            IO.println("That payment account doesn't exist!");
            return;
        }

        serv.DeleteCard(cardno, expmo, expyr, pin);
        IO.println("Account deleted!");
    }
    private static void AttachGameAccounts(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        if (!serv.GetPaymentAccountExists(cardno)){
            IO.println("That account number doesn't exist!");
            return;
        }

        List<Integer> profIDs = serv.GetGameAccountIDsToAddToCard(cardno);
        if (profIDs.isEmpty()){
            IO.println("Every game account is already attached to this payment account!");
            return;
        }

        IO.println("\nGame Accounts with IDs to add to this payment account:");
        for (String s : serv.DisplayGameAccountsToAddToCard(cardno)){
            IO.println(s);
        }

        int profID = UserInput.GetUserInt("Enter an ID to attach to this account (or 0 to go back)", true,
                n -> n==0 || profIDs.contains(n));

        if (profID == 0) return;

        serv.AddGameAccountToCard(cardno, profID);
        IO.println("Game Account is now attached to the Payment Account!");
    }
    private static void DetachGameAccounts(){
        long cardno = getUserCardNumber();
        if (cardno == -1) return;

        if (!serv.GetPaymentAccountExists(cardno)){
            IO.println("That account number doesn't exist!");
            return;
        }

        List<Integer> profIDs = serv.GetGameAccountsIDsToRemoveFromCard(cardno);
        if (profIDs.isEmpty()){
            IO.println("This payment account is not attached to any game accounts!");
            return;
        }

        IO.println("\nGame Accounts with IDs to remove this payment account:");
        for (String s : serv.DisplayGameAccountsToRemoveFromCard(cardno)){
            IO.println(s);
        }

        int profID = UserInput.GetUserInt("Enter an ID to detach from this account (or 0 to go back)", true,
                n -> n==0 || profIDs.contains(n));

        if (profID == 0) return;

        serv.RemoveGameAccountFromCard(cardno, profID);
        IO.println("Game Account is now detached from the Payment Account!");
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

        if (!serv.GetPaymentAccountExists(cardno, expmo, expyr, pin)){
            IO.println("That payment account doesn't exist!");
            return;
        }

        IO.println("\nCurrent Balance: $" + serv.GetBalance(cardno, expmo, expyr, pin));

        int deposit = UserInput.GetUserInt("Enter how many dollars (>0) you want to deposit (or 0 to go back)",
                true, n -> n >= 0);

        if (deposit == 0) return;

        serv.MakeDeposit(deposit, cardno, expmo, expyr, pin);
        IO.println("New Balance: $" + serv.GetBalance(cardno, expmo, expyr, pin));
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

        if (!serv.GetPaymentAccountExists(cardno, expmo, expyr, pin)){
            IO.println("That payment account doesn't exist!");
            return;
        }

        int balance = serv.GetBalance(cardno, expmo, expyr, pin);

        IO.println("\nCurrent Balance: $" + balance);

        int withdrawal = UserInput.GetUserInt("Enter how many dollars (>0) you want to withdraw without going negative (or 0 to go back)",
                true, n -> n >= 0 && n <= balance);

        if (withdrawal == 0) return;

        serv.MakeWithdrawal(withdrawal, cardno, expmo, expyr, pin);
        IO.println("New Balance: $" + serv.GetBalance(cardno, expmo, expyr, pin));
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

        if (!serv.GetPaymentAccountExists(cardno, expmo, expyr, pin)){
            IO.println("That payment account doesn't exist!");
            return;
        }

        IO.println("\nCurrent Balance: $" + serv.GetBalance(cardno, expmo, expyr, pin));
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
