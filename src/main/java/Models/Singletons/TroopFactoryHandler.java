package Models.Singletons;

import Application.Game;
import Models.Troop;
import Models.TroopFactory;
import Service.DataService;
import Service.RequirementService;
import Util.Models.Upgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TroopFactoryHandler {
    private final static TroopFactoryHandler handler = new TroopFactoryHandler();
    private TroopFactoryHandler() {}
    public static TroopFactoryHandler getInstance() {return handler;}

    private Map<Integer, TroopFactory> factories = new HashMap<>();

    public static void GenerateFactories(){
        for (TroopFactory factory : Game.getInstance().GetRequirementService().GetTroopFactories()){
            handler.factories.put(factory.GetTroopID(), factory);
        }
    }
    public static void LoadTroopInfo(int profID){
        DataService dServ = Game.getInstance().GetDataService();
        RequirementService rServ = Game.getInstance().GetRequirementService();

        for (Map.Entry<Integer, Integer> e : dServ.GetUserTroopLevels(profID).entrySet()){
            int troopID = e.getKey();
            int troopLevel = e.getValue();

            handler.SetTroopInfo(troopID, troopLevel, rServ.GetTroopDamage(troopID, troopLevel),
                    rServ.GetTroopHP(troopID, troopLevel), rServ.GetUpgradeInfo(troopID, troopLevel+1));
        }
    }

    public void SetTroopInfo(int troopID, int newLevel, int newDmg, int newHP, Upgrade newUpgradeInfo){
        factories.get(troopID).SetTroopInfo(newLevel, newDmg, newHP, newUpgradeInfo);
    }
    public void UpgradeTroop(int troopID, int newLevel, int newDmg, int newHP, Upgrade newUpgradeInfo){
        factories.get(troopID).SetTroopInfo(newLevel, newDmg, newHP, newUpgradeInfo);
        Profile.getInstance().UpgradeTroop(troopID);
    }
    public void FinishUpgradingTroop(){
        Profile.getInstance().FinishUpgradingTroop();
    }
    public boolean GetTroopIsCurrentlyUpgrading(){
        return Profile.getInstance().GetTroopIsCurrentlyUpgrading();
    }
    public int GetUpgradingTroop(){ //unknown value if troop is actually not upgrading
        return Profile.getInstance().GetUpgradingTroop();
    }

    public List<Integer> GetAvailableTroops(int highestBarracksLevel){
        List<Integer> troops = new ArrayList<>();
        for (Map.Entry<Integer, TroopFactory> e : factories.entrySet()){
            if (e.getValue().TroopIsAvailable(highestBarracksLevel)) troops.add(e.getKey());
        }
        return troops;
    }

    public Troop CreateTroop(Integer troopID){
        return factories.get(troopID).CreateTroop();
    }
    public List<Troop> CreateTroops(Integer troopID, int numTroops){
        return factories.get(troopID).CreateTroops(numTroops);
    }

    public String GetTroopName(int troopID){
        return factories.get(troopID).GetTroopName();
    }
    public int GetResourceID(int troopID){
        return factories.get(troopID).GetResourceID();
    }

    public void Reset(){
        factories = new HashMap<>();
    }
}
