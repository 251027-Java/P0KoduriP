package Models.Singletons;

import Application.Game;
import Service.RequirementService;

public class GemValues {
    private GemValues(){}

    private static int time; //minutes per gem
    private static int resource; //resource amount per gem

    public static void GenerateValues(){
        RequirementService serv = Game.getInstance().GetRequirementService();
        time = serv.GetValuePerGem("time");
        resource = serv.GetValuePerGem("resource");
    }

    public static int GetTime(){
        return time;
    }
    public static int GetResource(){
        return resource;
    }
    public static int TimeToGems(int days, int hours, int minutes, int seconds){
        int totalMins = days*24*60 + hours*60 + minutes;
        if (totalMins <= 0) return 0;
        return (totalMins-1)/time + 1;
    }
    public static int ResourceToGems(int resourceAmount){
        if (resourceAmount == 0) return 0;
        return (resourceAmount-1)/resource + 1;
    }
}
