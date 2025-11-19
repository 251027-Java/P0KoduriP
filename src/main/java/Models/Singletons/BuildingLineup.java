package Models.Singletons;

import Application.Game;
import Models.Abstracts.Building;
import Service.DataService;
import Service.RequirementService;

import java.util.Map;

public class BuildingLineup {
    private final static BuildingLineup buildingLineup = new BuildingLineup();
    private BuildingLineup() {}
    public static BuildingLineup getInstance() {return buildingLineup;}

    private static final int maxBuildings = 12;
    private Building[] lineup = new Building[maxBuildings];

    public static void LoadBuildings(int profID){
        buildingLineup.Reset();

        DataService dServ = Game.getInstance().GetDataService();
        RequirementService rServ = Game.getInstance().GetRequirementService();

        for (Map.Entry<Integer, Integer> e : dServ.GetUserTroopLevels(profID).entrySet()){
            int troopID = e.getKey();
            int troopLevel = e.getValue();

            handler.SetTroopInfo(troopID, troopLevel, rServ.GetTroopDamage(troopID, troopLevel),
                    rServ.GetTroopHP(troopID, troopLevel), rServ.GetUpgradeInfo(troopID, troopLevel+1));
        }
    }
    public void Reset(){
        lineup = new Building[maxBuildings];
    }
    public void AddBuilding(Building building, int pos){
        lineup[pos] = building;
    }
    public Building GetBuilding(int pos){
        return lineup[pos];
    }
    public Building[] GetLineup(){
        return lineup.clone();
    }
}
