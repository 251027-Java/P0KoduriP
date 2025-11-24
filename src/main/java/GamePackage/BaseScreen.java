package GamePackage;

import Application.Game;
import Models.Abstracts.Building;
import Models.Singletons.*;
import Models.TroopFactory;
import Service.DataService;
import Util.Screen;
import Util.Time;
import Util.UserInput;

public class BaseScreen {
    private static DataService serv;

    public static void ShowBaseScreen(){
        serv = Game.getInstance().GetDataService();

        int input;

        do {
            Screen.Clear();

            input = UserInput.GetUserInt("0: Back\n1: Display Profile Info\n2: Gem Shop\n" +
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

        UserInput.GetUserString("\nType any key and hit enter to go back and continue", false);
    }

    private static void DisplayCurrentUpgrades(){
        IO.println();
        long troopTime = Profile.getInstance().GetTroopUpgradeTimeRemainingSeconds();
        if (troopTime == 0) IO.println("No troop is currently upgrading.");
        else {
            TroopFactoryHandler handler = TroopFactoryHandler.getInstance();
            IO.println(String.format("%s: %s", handler.GetTroopName(handler.GetUpgradingTroop()), Time.FormatTime(troopTime)));
        }
        IO.println();

        boolean atLeastOne = false;
        for (Building b : BuildingHandler.getInstance().GetBuildings()){
            long buildingTime = b.GetBuildingUpgradeTimeRemainingSeconds(Profile.GetID());
            if (buildingTime > 0){
                IO.println(String.format("(%d) %s: %s", b.GetBuildID(), b.GetName(), Time.FormatTime(buildingTime)));
                atLeastOne = true;
            }
        }
        if (!atLeastOne) IO.println("No building is currently upgrading.");

        UserInput.GetUserString("\nType any key and hit enter to go back and continue", false);
    }
}
