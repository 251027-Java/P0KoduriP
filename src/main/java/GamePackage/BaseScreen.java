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

            input = UserInput.GetUserInt("\n0: Back\n1: Display Profile Info\n2: Gem Shop\n" +
                            "Enter which # you want to do",false,n -> (0 <= n && n <= 2));
            switch (input) {
                case 1:
                    DisplayInfo();
                    break;
                case 2:
                    GemShopScreen.getInstance().OpenShop();
                    break;
            }
        } while (input != 0);
    }

    private static void DisplayInfo(){
        IO.println(String.format("\n%s (ID = %d)", Profile.getInstance().GetName(), Profile.GetID()));
        IO.println("Account created on " + serv.GetCreationDate(Profile.GetID()));
        IO.println("Town Hall Level " + BuildingHandler.getInstance().GetBuilding(0).GetLevel());
        IO.println(Profile.getInstance().GetTrophies() + " Trophies");
        for (int i : ResourceManager.GetResourceIDs()){
            IO.println(ResourceManager.getInstance().GetResources(i) + " " + ResourceManager.GetResourceName(i));
        }
        IO.println(Profile.getInstance().GetGems() + " Gems");

        UserInput.GetUserString("\nEnter any key and hit enter to go back and continue", false);
    }
}
