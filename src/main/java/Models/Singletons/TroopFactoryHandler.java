package Models.Singletons;

import Models.Troop;
import Models.TroopFactory;
import Util.Models.Upgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TroopFactoryHandler {
    private final static TroopFactoryHandler handler = new TroopFactoryHandler();
    private TroopFactoryHandler() {}
    public static TroopFactoryHandler getInstance() {return handler;}

    private Map<String, TroopFactory> factories = new HashMap<>();

    public void AddFactory(String troopName, TroopFactory factory){
        factories.put(troopName, factory);
    }

    public void UpgradeTroop(String troop, int newLevel, int newDmg, int newHP, Upgrade newUpgradeInfo){
        factories.get(troop).UpgradeTroop(newLevel, newDmg, newHP, newUpgradeInfo);
    }

    public List<String> GetAvailableTroops(int highestBarracksLevel){
        List<String> troops = new ArrayList<>();
        for (Map.Entry<String, TroopFactory> e : factories.entrySet()){
            if (e.getValue().troopIsAvailable(highestBarracksLevel)) troops.add(e.getKey());
        }
        return troops;
    }

    public Troop CreateTroop(String troop){
        return factories.get(troop).CreateTroop();
    }
    public List<Troop> CreateTroops(String troop, int numTroops){
        return factories.get(troop).CreateTroops(numTroops);
    }

    public void Reset(){
        factories = new HashMap<>();
    }
}
