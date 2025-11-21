package Models.Singletons;

import Application.Game;
import Models.Buildings.ArmyCamp;
import Models.Troop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Army {
    private final static Army army = new Army();
    private Army() {}
    public static Army getInstance() {return army;}

    private List<ArmyCamp> camps = new ArrayList<>();
    private int maxSpace = 0;
    private int currSpace = 0;

    private Map<Integer, Integer> troopCounts; //troopID -> #troops
    private Map<Integer, List<Troop>> troops; //troopID -> list of troops

    public static void LoadArmy(){
        army.camps = new ArrayList<>();
        army.maxSpace = 0;
        army.currSpace = 0;

        army.troopCounts = Game.getInstance().GetDataService().GetPlayerArmy(Profile.GetID());
    }

    public void StartBattle(){
        troops = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : troopCounts.entrySet()){
            int troopID = e.getKey();
            troops.put(troopID, TroopFactoryHandler.getInstance().CreateTroops(troopID, e.getValue()));
        }
    }

    public void AddArmyCamp(ArmyCamp camp){
        camps.add(camp);
        UpdateSpace(camp.GetMaxSpace());
    }
    public void UpdateSpace(int oldSpace, int newSpace){
        UpdateSpace(newSpace - oldSpace);
    }
    public void UpdateSpace(int changeSpace){
        maxSpace += changeSpace;
        if (maxSpace < 0) maxSpace = 0;
    }
    public int GetSpace(){
        return maxSpace;
    }

    public void DeployTroops(int troopID){
        troopCounts.remove(troopID);
    }

    public String SeeArmy(){
        String a = "";
        for (Map.Entry<Integer, Integer> e : troopCounts.entrySet()){
            int troopID = e.getKey();
            a += String.format("(%d) %s: %d\n", troopID, TroopFactoryHandler.getInstance().GetTroopName(troopID), e.getValue());
        }

        if (a.isEmpty()) a = "There are no troops remaining.\n";

        return a.substring(0, a.length()-1);
    }
}
