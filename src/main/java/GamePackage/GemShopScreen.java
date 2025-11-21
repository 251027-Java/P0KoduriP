package GamePackage;

import Models.PaymentAccount;
import Models.Singletons.GemShop;
import Models.Singletons.Profile;
import Util.Screen;
import Util.UserInput;

import java.util.*;

public class GemShopScreen {
    private final static GemShopScreen gemShop = new GemShopScreen();
    private GemShopScreen() {}
    public static GemShopScreen getInstance() {return gemShop;}

    public void OpenShop(){
        long input = -1;

        do {
            Screen.Clear();

            IO.println("--- Gem Shop ---");
            for (Map.Entry<Integer, Integer> e : GemShop.GetAllOptions().entrySet()) {
                IO.println(String.format("$%d -> %d gems", e.getKey(), e.getValue()));
            }

            Map<Long, PaymentAccount> accounts = Profile.getInstance().GetPaymentOptions();
            Set<Long> cardNos = accounts.keySet();
            IO.println("\nYour Payment Accounts:");
            for (Map.Entry<Long, PaymentAccount> e : accounts.entrySet()) {
                IO.println(String.format("#%d: $%d", e.getKey(), e.getValue().GetBalance()));
            }
            IO.println("\nYou have " + Profile.getInstance().GetGems() + " gems.");

            input = UserInput.GetUserLong("Enter a card number to use (or -1 to leave the shop)", false,
                    n -> n==-1 || cardNos.contains(n));
            if (input != -1) Purchase(accounts.get(input));
        } while (input != -1);
    }

    private void Purchase(PaymentAccount pa){
        Screen.Clear();

        Map<Integer, Integer> options = GemShop.GetAvailableOptions(pa.GetBalance());
        Set<Integer> prices = options.keySet();
        IO.println("--- Purchase Options ---");
        for (Map.Entry<Integer, Integer> e : options.entrySet()) {
            IO.println(String.format("$%d -> %d gems", e.getKey(), e.getValue()));
        }
        IO.println("-------");
        IO.println("Your balance (#" + pa.GetCardNumber() + ") is $" + pa.GetBalance() + ".");
        IO.println("You have " + Profile.getInstance().GetGems() + " gems.");

        int usd = UserInput.GetUserInt("Enter a price to buy (or -1 to go back)", true,
                n -> n==-1 || prices.contains(n));

        if (usd == -1) return;

        Profile.getInstance().ChangeGems(GemShop.GetGems(usd));
        pa.Withdraw(usd);

        IO.println("\nYour purchase was successful!");
        IO.println("Your new balance (#" + pa.GetCardNumber() + ") is $" + pa.GetBalance() + ".");
        IO.println("You now have " + Profile.getInstance().GetGems() + " gems.");

        UserInput.GetUserString("\nEnter any key to go back and continue", false);
    }
}
