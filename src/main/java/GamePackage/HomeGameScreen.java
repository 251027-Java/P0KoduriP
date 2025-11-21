package GamePackage;

import Application.Game;
import Service.DataService;
import Util.Screen;
import Util.UserInput;

import java.util.List;

public class HomeGameScreen {
    private static final int failedProfileCreation = -1;
    private static DataService serv;

    public static void StartHomeGameScreen(){
        serv = Game.getInstance().GetDataService();

        Screen.Clear();
        IO.println("Game Accounts:");
        for (String s : serv.DisplayGameAccounts()){
            IO.println(s);
        }
        List<Integer> profIDs = serv.GetGameAccountIDs();


        int profID = UserInput.GetUserInt("Enter an ID to load that game, 0 to create a new profile, or -1 to go back", true,
                n -> n==-1 || n==0 || profIDs.contains(n));

        switch (profID) {
            case -1:
                break;
            case 0:
                int newProfID = CreateAccount();
                if (newProfID != failedProfileCreation) LoadProfile(newProfID);
                break;
            default:
                LoadProfile(profID);
                break;
        }
        IO.println();
    }

    private static int CreateAccount(){
        String name = UserInput.GetUserString("Enter a name for your profile (max 50 chars) or leave it empty to go back",
                true,s -> s.length() <= 50);

        int newID = Game.NumProfiles + 1;
        if (name.isEmpty() || !serv.CreateProfile(newID, name)){
            IO.println("New profile was not created.");
            return failedProfileCreation;
        }

        Game.NumProfiles += 1;

        IO.println(String.format("A new profile (ID = %d) for %s was created!", newID, name));
        return newID;
    }

    private static void LoadProfile(int profID){

    }
}
