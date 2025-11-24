package GamePackage;

import Application.Game;
import Service.DataService;
import Util.Screen;
import Util.UserInput;

public class UpgradeScreen {
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
}
