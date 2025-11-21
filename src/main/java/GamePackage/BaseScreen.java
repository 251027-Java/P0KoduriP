package GamePackage;

import Application.Game;
import Models.Singletons.BuildingHandler;
import Models.Singletons.GemShop;
import Models.Singletons.Profile;
import Models.Singletons.ResourceManager;
import Service.DataService;
import Util.Screen;
import Util.UserInput;

public class BaseScreen {
    private static DataService serv;

    public static void ShowBaseScreen(){
        serv = Game.getInstance().GetDataService();

        int input;

        do {
            Screen.Clear();

            input = UserInput.GetUserInt("\n0: Back\n1: Display Info\n2: Gem Shop\n" +
                            "3: Add Game Accounts to Payment Account\n4: Remove Game Accounts from Payment Account\n" +
                            "5: Deposit Money\n6: Withdraw Money\n7: View Balance\nEnter which # you want to do",false,
                    n -> (0 <= n && n <= 7));
            switch (input) {
                case 1:
                    DisplayInfo();
                    break;
                case 2:
                    GemShopScreen.getInstance().OpenShop();
                    break;
                /*case 3:
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
                    break;*/
            }
        } while (input != 0);
    }

    private static void DisplayInfo(){
        IO.println(String.format("%s (ID = %d)", Profile.getInstance().GetName(), Profile.GetID()));
        IO.println("Account created on " + serv.GetCreationDate(Profile.GetID()));
        IO.println("Town Hall Level " + BuildingHandler.getInstance().GetBuilding(0).GetLevel());
        IO.println(Profile.getInstance().GetTrophies() + " Trophies");
        for (int i : ResourceManager.GetResourceIDs()){
            IO.println(ResourceManager.getInstance().GetResources(i) + " " + ResourceManager.GetResourceName(i));
        }
        IO.println(Profile.getInstance().GetGems() + " Gems");

        UserInput.GetUserString("\nEnter any key to go back and continue", false);
    }
}
